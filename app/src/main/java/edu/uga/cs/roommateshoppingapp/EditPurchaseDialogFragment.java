package edu.uga.cs.roommateshoppingapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

/**
 * DialogFragment to handle edit on purchase
 */
public class EditPurchaseDialogFragment extends DialogFragment {

    // indicate the type of an edit
    public static final int SAVE = 1;
    public static final int DELETE = 2;

    private EditText priceEditText;

    private int position;
    private String key;
    private String itemName;
    private String price;
    private String purchaser;

    public interface EditPurchaseDialogListener {
        void updatePurchase(int position, Item item, int action);
    }

    public static EditPurchaseDialogFragment newInstance(int position, String key, String itemName, String price, String purchaser) {
        EditPurchaseDialogFragment dialog = new EditPurchaseDialogFragment();

        Bundle args = new Bundle();
        args.putString( "key", key );
        args.putInt( "position", position );
        args.putString("itemName", itemName);
        args.putString("price", price);
        args.putString("purchaser", purchaser);
        dialog.setArguments(args);

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ) {

        key = getArguments().getString( "key" );
        position = getArguments().getInt( "position" );
        itemName = getArguments().getString( "itemName" );
        price = getArguments().getString("price");
        purchaser = getArguments().getString("purchaser");

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.edit_purchase_dialog, getActivity().findViewById( R.id.constraintLayout ) );

        priceEditText = layout.findViewById( R.id.priceEditText );

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity());
        builder.setView(layout);

        // Set the title of the AlertDialog
        builder.setTitle( "Edit Purchase" );

        // The Cancel button handler
        builder.setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // close the dialog
                dialog.dismiss();
            }
        });

        // The Save button handler
        builder.setPositiveButton( "SAVE", new SaveButtonClickListener() );

        // The Delete button handler
        builder.setNeutralButton( "DELETE", new DeleteButtonClickListener() );

        // Create the AlertDialog and show it
        return builder.create();
    }

    private class SaveButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String price = priceEditText.getText().toString();
            Item item = new Item(itemName, Double.parseDouble(price), purchaser);
            item.setKey(key);

            // get the Activity's listener to add the new job lead
            EditPurchaseDialogListener listener = (EditPurchaseDialogFragment.EditPurchaseDialogListener) getActivity();
            listener.updatePurchase( position, item, SAVE );

            // close the dialog
            dismiss();
        }
    }

    private class DeleteButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick( DialogInterface dialog, int which ) {
            Item item = new Item(itemName);
            item.setKey(key);

            // get the Activity's listener to add the new job lead
            EditPurchaseDialogFragment.EditPurchaseDialogListener listener = (EditPurchaseDialogFragment.EditPurchaseDialogListener) getActivity();            // add the new job lead
            listener.updatePurchase( position, item, DELETE );
            // close the dialog
            dismiss();
        }
    }
}