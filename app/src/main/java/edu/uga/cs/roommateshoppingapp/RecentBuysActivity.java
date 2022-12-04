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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Page that displays all recent purchases
 */
public class RecentBuysActivity extends AppCompatActivity
    implements EditPurchaseDialogFragment.EditPurchaseDialogListener
{

    public static final String DEBUG_TAG = "RecentBuysActivity";

    private RecyclerView recyclerView;
    private PurchaseRecyclerAdapter recyclerAdapter;
    private List<Item> purchaseList;
    private List<User> userList;
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
                settle();
            }
        });

        purchaseList = new ArrayList<>();
        userList = new ArrayList<>();

        // use a linear layout manager for the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // the recycler adapter with job leads is empty at first; it will be updated later
        recyclerAdapter = new PurchaseRecyclerAdapter(purchaseList, RecentBuysActivity.this );
        recyclerView.setAdapter( recyclerAdapter );

        // get a Firebase DB instance reference
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("recent-purchases");

        myRef.addValueEventListener( new ValueEventListener() {

            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {
                purchaseList.clear();
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

        getAllUsers();
    }

    /**
     * Function to update purchase made by user
     * @param position
     * @param item
     * @param action
     */
    public void updatePurchase(int position, Item item, int action ) {
        if( action == EditPurchaseDialogFragment.SAVE ) {
            recyclerAdapter.notifyItemChanged( position );

            DatabaseReference ref = database
                    .getReference()
                    .child( "recent-purchases" )
                    .child( item.getKey() );

            ref.addListenerForSingleValueEvent( new ValueEventListener() {
                @Override
                public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {
                    dataSnapshot.getRef().setValue(item).addOnSuccessListener( new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Purchase updated",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled( @NonNull DatabaseError databaseError ) {
                    Toast.makeText(getApplicationContext(), "Failed to update ",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if( action == EditPurchaseDialogFragment.DELETE ) {

            purchaseList.remove( position );
            recyclerAdapter.notifyItemRemoved( position );

            DatabaseReference ref = database
                    .getReference()
                    .child( "recent-purchases" )
                    .child( item.getKey() );

            ref.addListenerForSingleValueEvent( new ValueEventListener() {
                @Override
                public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {
                    dataSnapshot.getRef().removeValue().addOnSuccessListener( new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Purchase deleted",
                                    Toast.LENGTH_SHORT).show();                        }
                    });
                }

                @Override
                public void onCancelled( @NonNull DatabaseError databaseError ) {
                    Toast.makeText(getApplicationContext(), "Failed to delete ",
                            Toast.LENGTH_SHORT).show();
                }
            });

            item.setKey("");
            item.setPrice(-1.0);
            item.setPurchaser("");
            addItemBackToShoppingList(item);
        }
    }

    /**
     * Function to add item back to the shopping list when deleted from recent buys
     * @param item
     */
    private void addItemBackToShoppingList(Item item) {
        DatabaseReference myRef = database.getReference("shopping-list");
        myRef.push().setValue(item)
                .addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Item returned to shopping list",
                                Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure( @NonNull Exception e ) {
                        Toast.makeText( getApplicationContext(), "Failed to move item back to shopping list",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Function to compute calculations when users settle cost
     */
    public void settle() {
        double totalCost = 0.0;
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        HashMap<String, Double> userToAmount = new HashMap<>();
        for (User user : userList) {
            if (!userToAmount.containsKey(user.getEmail())) {
                userToAmount.put(user.getEmail(), user.getAmountOwed());
            }
        }

        for (Item item : purchaseList) {
            totalCost = totalCost + item.getPrice();
            userToAmount.put(item.getPurchaser(), userToAmount.get(item.getPurchaser()) + item.getPrice());
        }

        double averageCost = totalCost / userList.size();

        String paymentForEachUser = "";
        for (String key : userToAmount.keySet()) {
            paymentForEachUser = paymentForEachUser + key + ": " + formatter.format(userToAmount.get(key)) + "\n";
        }
        paymentForEachUser = paymentForEachUser.substring(0, paymentForEachUser.length() - 1);
        DialogFragment newFragment = SettleDialogFragment.newInstance(formatter.format(totalCost), formatter.format(averageCost), paymentForEachUser);
        clearRecentPurchases();
        newFragment.show( getSupportFragmentManager(), null);

    }

    /**
     * Clear items in recent buys
     */
    private void clearRecentPurchases() {
        DatabaseReference ref = database
                .getReference()
                .child( "recent-purchases" );

        ref.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {
                dataSnapshot.getRef().removeValue().addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Cleared recent purchases",
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

    /**
     * Retrieve all users from DB
     */
    private void getAllUsers() {
        DatabaseReference ref = database
                .getReference()
                .child( "users" );

        ref.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {
                userList.clear(); // clear the current content; this is inefficient!
                for( DataSnapshot postSnapshot: dataSnapshot.getChildren() ) {
                    User user = postSnapshot.getValue(User.class);
                    user.setKey( postSnapshot.getKey() );
                    userList.add(user);
                }
            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {
                Toast.makeText(getApplicationContext(), "Failed to retrieve users",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}