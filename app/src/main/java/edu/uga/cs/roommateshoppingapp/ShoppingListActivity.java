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

/**
 * Page that contains all the items on the shopping list
 */
public class ShoppingListActivity extends AppCompatActivity
        implements AddItemDialogFragment.AddItemDialogListener,
        EditItemDialogFragment.EditItemDialogListener,
        PurchaseItemDialogFragment.PurchaseItemDialogListener
{

    public static final String DEBUG_TAG = "ShoppingListActivity";

    private RecyclerView recyclerView;
    private ItemRecyclerAdapter recyclerAdapter;

    private List<Item> itemList;

    private FirebaseDatabase database;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        Log.d( DEBUG_TAG, "onCreate()" );

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_shopping_list);

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

        recyclerAdapter = new ItemRecyclerAdapter(itemList, ShoppingListActivity.this );
        recyclerView.setAdapter( recyclerAdapter );

        // get a Firebase DB instance reference
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("shopping-list");

        myRef.addValueEventListener( new ValueEventListener() {

            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {
                itemList.clear();
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

    /**
     * Function to add item to the shopping list
     * @param item
     */
    public void addItem(Item item) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("shopping-list");
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

    /**
     * Function to update item on the shopping list
     * @param position
     * @param item
     * @param action
     */
    public void updateItem(int position, Item item, int action ) {
        if( action == EditItemDialogFragment.SAVE ) {
            recyclerAdapter.notifyItemChanged( position );

            DatabaseReference ref = database
                    .getReference()
                    .child( "shopping-list" )
                    .child( item.getKey() );

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

            itemList.remove( position );

            recyclerAdapter.notifyItemRemoved( position );

            DatabaseReference ref = database
                    .getReference()
                    .child( "shopping-list" )
                    .child( item.getKey() );

            ref.addListenerForSingleValueEvent( new ValueEventListener() {
                @Override
                public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {
                    dataSnapshot.getRef().removeValue().addOnSuccessListener( new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Item deleted",
                                    Toast.LENGTH_SHORT).show();                        }
                    });
                }

                @Override
                public void onCancelled( @NonNull DatabaseError databaseError ) {
                    Toast.makeText(getApplicationContext(), "Failed to delete ",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Function to purchase item from the shopping list
     * @param position
     * @param item
     */
    public void purchaseItem(int position, Item item) {
        // delete from shopping list
        updateItem(position, item, EditItemDialogFragment.DELETE);

        // add item to recent purchase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("recent-purchases");
        myRef.push().setValue(item)
                .addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        recyclerView.post( new Runnable() {
                            @Override
                            public void run() {
                                if (itemList.size() != 0) {
                                    recyclerView.smoothScrollToPosition(itemList.size() - 1);
                                }
                            }
                        } );
                        Toast.makeText(getApplicationContext(), "Item created in recent purchases",
                                Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure( @NonNull Exception e ) {
                        Toast.makeText( getApplicationContext(), "Failed to create item in recent purchases",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }
}