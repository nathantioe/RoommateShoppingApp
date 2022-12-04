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

import java.util.List;

/**
 * Represents RecyclerAdapter for the items in the shopping list
 */
public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemHolder>{

    private List<Item> itemList;
    private Context context;

    public ItemRecyclerAdapter(List<Item> itemList, Context context ) {
        this.itemList = itemList;
        this.context = context;
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        TextView name;

        public ItemHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById( R.id.name );
        }
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.item, parent, false );
        return new ItemHolder( view );
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position ) {
        Item item = itemList.get( position );

        String key = item.getKey();
        String name = item.getItemName();

        holder.name.setText( item.getItemName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditItemDialogFragment editItemFragment =
                        EditItemDialogFragment.newInstance( holder.getAdapterPosition(), key, name);
                editItemFragment.show( ((AppCompatActivity)context).getSupportFragmentManager(), null);
            }
        });

        holder.itemView.findViewById(R.id.purchaseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PurchaseItemDialogFragment purchaseItemFragment =
                        PurchaseItemDialogFragment.newInstance( holder.getAdapterPosition(), key, name);
                purchaseItemFragment.show( ((AppCompatActivity)context).getSupportFragmentManager(), null);
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
