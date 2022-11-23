package edu.uga.cs.roommateshoppingapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class PurchaseItemDialogFragment extends DialogFragment {

    private EditText itemPriceView;
    private int position;
    private String key;
    private String itemName;

    public interface PurchaseItemDialogListener {
        void purchaseItem(int position, Item item);
    }

    public static PurchaseItemDialogFragment newInstance(int position, String key, String itemName) {
        PurchaseItemDialogFragment dialog = new PurchaseItemDialogFragment();

        Bundle args = new Bundle();
        args.putString( "key", key );
        args.putInt( "position", position );
        args.putString("itemName", itemName);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        key = getArguments().getString( "key" );
        position = getArguments().getInt( "position" );
        itemName = getArguments().getString( "itemName" );

        // Create the AlertDialog view
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.add_price_dialog,
                getActivity().findViewById(R.id.linearLayout));

        // get the view objects in the AlertDialog
        itemPriceView = layout.findViewById(R.id.editTextPrice);

        // create a new AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set its view (inflated above).
        builder.setView(layout);

        // Set the title of the AlertDialog
        builder.setTitle( "Enter cost" );
        // Provide the negative button listener
        builder.setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // close the dialog
                dialog.dismiss();
            }
        });
        // Provide the positive button listener
        builder.setPositiveButton( android.R.string.ok, new PurchaseItemListener());

        // Create the AlertDialog and show it
        return builder.create();
    }

    private class PurchaseItemListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Double price = Double.parseDouble(itemPriceView.getText().toString());

            Item item = new Item(itemName, price);
            item.setKey(key);

            // get the Activity's listener to add the new job lead
            PurchaseItemDialogListener listener = (PurchaseItemDialogListener) getActivity();

            // add the new job lead
            listener.purchaseItem(position, item);

        }
    }
}

