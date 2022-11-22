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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShoppingList2Activity extends AppCompatActivity implements AddItemDialogFragment.AddItemDialogListener {

    public static final String DEBUG_TAG = "ReviewJobLeadsActi";

    private RecyclerView recyclerView;
    private ItemRecyclerAdapter recyclerAdapter;

    private List<Item> itemList;

    private FirebaseDatabase database;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        Log.d( DEBUG_TAG, "onCreate()" );

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_shopping_list2 );

        recyclerView = findViewById(R.id.recyclerView);

        FloatingActionButton floatingButton = findViewById(R.id.floatingActionButton);
        floatingButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new AddItemDialogFragment();
                newFragment.show( getSupportFragmentManager(), null);
            }
        });

        // initialize the Itemlist
        itemList = new ArrayList<Item>();

        // use a linear layout manager for the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // the recycler adapter with job leads is empty at first; it will be updated later
        recyclerAdapter = new ItemRecyclerAdapter(itemList, ShoppingList2Activity.this );
        recyclerView.setAdapter( recyclerAdapter );

        // get a Firebase DB instance reference
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("shopping-list");

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
                itemList.clear(); // clear the current content; this is inefficient!
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    Item item = postSnapshot.getValue(Item.class);
                    item.setKey( postSnapshot.getKey() );
                    itemList.add(item);
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

    // this is our own callback for a AddJobLeadDialogFragment which adds a new job lead.
    public void addItem(Item item) {
        // add the new job lead
        // Add a new element (JobLead) to the list of job leads in Firebase.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("shopping-list");

        // First, a call to push() appends a new node to the existing list (one is created
        // if this is done for the first time).  Then, we set the value in the newly created
        // list node to store the new job lead.
        // This listener will be invoked asynchronously, as no need for an AsyncTask, as in
        // the previous apps to maintain job leads.
        myRef.push().setValue(item)
                .addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // Reposition the RecyclerView to show the JobLead most recently added (as the last item on the list).
                        // Use of the post method is needed to wait until the RecyclerView is rendered, and only then
                        // reposition the item into view (show the last item on the list).
                        // the post method adds the argument (Runnable) to the message queue to be executed
                        // by Android on the main UI thread.  It will be done *after* the setAdapter call
                        // updates the list items, so the repositioning to the last item will take place
                        // on the complete list of items.
                        recyclerView.post( new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.smoothScrollToPosition(itemList.size()-1 );
                            }
                        } );

                        //Log.d( DEBUG_TAG, "Job lead saved: " + jobLead );
                        // Show a quick confirmation
                        Toast.makeText(getApplicationContext(), "Item created",
                                Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure( @NonNull Exception e ) {
                        Toast.makeText( getApplicationContext(), "Failed to create item",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // This is our own callback for a DialogFragment which edits an existing JobLead.
    // The edit may be an update or a deletion of this JobLead.
    // It is called from the EditJobLeadDialogFragment.
    public void updateItem(int position, Item item, int action ) {
        if( action == EditItemDialogFragment.SAVE ) {
            //Log.d( DEBUG_TAG, "Updating job lead at: " + position + "(" + jobLead.getCompanyName() + ")" );

            // Update the recycler view to show the changes in the updated job lead in that view
            recyclerAdapter.notifyItemChanged( position );

            // Update this job lead in Firebase
            // Note that we are using a specific key (one child in the list)
            DatabaseReference ref = database
                    .getReference()
                    .child( "items" )
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
                            Toast.makeText(getApplicationContext(), "Item updated",
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
        else if( action == EditItemDialogFragment.DELETE ) {
            //Log.d( DEBUG_TAG, "Deleting job lead at: " + position + "(" + jobLead.getCompanyName() + ")" );

            // remove the deleted job lead from the list (internal list in the App)
            itemList.remove( position );

            // Update the recycler view to remove the deleted job lead from that view
            recyclerAdapter.notifyItemRemoved( position );

            // Delete this job lead in Firebase.
            // Note that we are using a specific key (one child in the list)
            DatabaseReference ref = database
                    .getReference()
                    .child( "items" )
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
                            Toast.makeText(getApplicationContext(), "Item deleted for",
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