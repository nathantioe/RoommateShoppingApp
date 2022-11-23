package edu.uga.cs.roommateshoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecentBuysActivity extends AppCompatActivity
    implements EditPurchaseDialogFragment.EditPurchaseDialogListener
{

    public static final String DEBUG_TAG = "RecentBuysActivity";

    private RecyclerView recyclerView;
    //private ItemRecyclerAdapter recyclerAdapter;
    private PurchaseRecyclerAdapter recyclerAdapter;

    private List<Item> purchaseList;

    private FirebaseDatabase database;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        Log.d( DEBUG_TAG, "onCreate()" );

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_recent_buys);

        recyclerView = findViewById(R.id.purchases);

        FloatingActionButton settleButton = findViewById(R.id.settle);
        settleButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DialogFragment newFragment = new AddItemDialogFragment();
//                newFragment.show( getSupportFragmentManager(), null);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(getApplicationContext(), "User: " + user.getEmail(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // initialize the Itemlist
        purchaseList = new ArrayList<Item>();

        // use a linear layout manager for the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // the recycler adapter with job leads is empty at first; it will be updated later
        recyclerAdapter = new PurchaseRecyclerAdapter(purchaseList, RecentBuysActivity.this );
        recyclerView.setAdapter( recyclerAdapter );

        // get a Firebase DB instance reference
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("recent-purchases");

        // Set up a listener (event handler) to receive a value for the database reference.
        // This type of listener is called by Firebase once by immediately executing its onDataChange method
        // and then each time the value at Firebase changes.
        //
        // This listener will be invoked asynchronously, hence no need for an AsyncTask class, as in the previous apps
        // to maintain job leads.
        myRef.addValueEventListener( new ValueEventListener() {

            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, we need to iterate over the elements and place them on our job lead list.
                purchaseList.clear(); // clear the current content; this is inefficient!
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    Item item = postSnapshot.getValue(Item.class);
                    item.setKey( postSnapshot.getKey() );
                    purchaseList.add(item);
                }

                Log.d( DEBUG_TAG, "ValueEventListener: notifying recyclerAdapter" );
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {
                System.out.println( "ValueEventListener: reading failed: " + databaseError.getMessage() );
            }
        } );
    }

    // This is our own callback for a DialogFragment which edits an existing JobLead.
    // The edit may be an update or a deletion of this JobLead.
    // It is called from the EditJobLeadDialogFragment.
    public void updatePurchase(int position, Item item, int action ) {
        if( action == EditPurchaseDialogFragment.SAVE ) {
            //Log.d( DEBUG_TAG, "Updating job lead at: " + position + "(" + jobLead.getCompanyName() + ")" );

            // Update the recycler view to show the changes in the updated job lead in that view
            recyclerAdapter.notifyItemChanged( position );

            // Update this job lead in Firebase
            // Note that we are using a specific key (one child in the list)
            DatabaseReference ref = database
                    .getReference()
                    .child( "recent-purchases" )
                    .child( item.getKey() );

            // This listener will be invoked asynchronously, hence no need for an AsyncTask class, as in the previous apps
            // to maintain job leads.
            ref.addListenerForSingleValueEvent( new ValueEventListener() {
                @Override
                public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {
                    dataSnapshot.getRef().setValue(item).addOnSuccessListener( new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Log.d( DEBUG_TAG, "updated job lead at: " + position + "(" + jobLead.getCompanyName() + ")" );
                            Toast.makeText(getApplicationContext(), "Purchase updated",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled( @NonNull DatabaseError databaseError ) {
                    //Log.d( DEBUG_TAG, "failed to update job lead at: " + position + "(" + jobLead.getCompanyName() + ")" );
                    Toast.makeText(getApplicationContext(), "Failed to update ",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if( action == EditPurchaseDialogFragment.DELETE ) {

            // remove the deleted job lead from the list (internal list in the App)
            purchaseList.remove( position );

            // Update the recycler view to remove the deleted job lead from that view
            recyclerAdapter.notifyItemRemoved( position );

            // Delete this job lead in Firebase.
            // Note that we are using a specific key (one child in the list)
            DatabaseReference ref = database
                    .getReference()
                    .child( "recent-purchases" )
                    .child( item.getKey() );

            // This listener will be invoked asynchronously, hence no need for an AsyncTask class, as in the previous apps
            // to maintain job leads.
            ref.addListenerForSingleValueEvent( new ValueEventListener() {
                @Override
                public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {
                    dataSnapshot.getRef().removeValue().addOnSuccessListener( new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Log.d( DEBUG_TAG, "deleted job lead at: " + position + "(" + jobLead.getCompanyName() + ")" );
                            Toast.makeText(getApplicationContext(), "Purchase deleted",
                                    Toast.LENGTH_SHORT).show();                        }
                    });
                }

                @Override
                public void onCancelled( @NonNull DatabaseError databaseError ) {
                    //Log.d( DEBUG_TAG, "failed to delete job lead at: " + position + "(" + jobLead.getCompanyName() + ")" );
                    Toast.makeText(getApplicationContext(), "Failed to delete ",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}