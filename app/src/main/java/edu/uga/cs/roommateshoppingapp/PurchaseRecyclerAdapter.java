package edu.uga.cs.roommateshoppingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;

public class PurchaseRecyclerAdapter extends RecyclerView.Adapter<PurchaseRecyclerAdapter.ItemHolder>{

    private List<Item> itemList;
    private Context context;

    public PurchaseRecyclerAdapter(List<Item> itemList, Context context ) {
        this.itemList = itemList;
        this.context = context;
    }

    // The adapter must have a ViewHolder class to "hold" one item to show.
    class ItemHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView price;
        TextView purchaser;

        public ItemHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.purchaseName);
            price = itemView.findViewById(R.id.purchasePrice);
            purchaser = itemView.findViewById(R.id.purchaser);
        }
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.purchase, parent, false );
        return new ItemHolder( view );
    }

    // This method fills in the values of the Views to show a JobLead
    @Override
    public void onBindViewHolder(ItemHolder holder, int position ) {
        Item item = itemList.get( position );

        String key = item.getKey();
        String name = item.getItemName();
        String price = item.getPrice().toString();
        String purchaser = item.getPurchaser();

        holder.name.setText( item.getItemName());
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        holder.price.setText(formatter.format(item.getPrice()));
        holder.purchaser.setText(item.getPurchaser());

        // We can attach an OnClickListener to the itemView of the holder;
        // itemView is a public field in the Holder class.
        // It will be called when the user taps/clicks on the whole item, i.e., one of
        // the job leads shown.
        // This will indicate that the user wishes to edit (modify or delete) this item.
        // We create and show an EditJobLeadDialogFragment.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditPurchaseDialogFragment editPurchaseFragment =
                        EditPurchaseDialogFragment.newInstance( holder.getAdapterPosition(), key, name, price, purchaser);
                editPurchaseFragment.show( ((AppCompatActivity)context).getSupportFragmentManager(), null);
            }
        });

//        holder.itemView.findViewById(R.id.purchaseButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PurchaseItemDialogFragment purchaseItemFragment =
//                        PurchaseItemDialogFragment.newInstance( holder.getAdapterPosition(), key, name);
//                purchaseItemFragment.show( ((AppCompatActivity)context).getSupportFragmentManager(), null);
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}