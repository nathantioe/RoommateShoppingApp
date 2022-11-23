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

// This is a DialogFragment to handle edits to a JobLead.
// The edits are: updates and deletions of existing JobLeads.
public class EditPurchaseDialogFragment extends DialogFragment {

    // indicate the type of an edit
    public static final int SAVE = 1;   // update an existing job lead
    public static final int DELETE = 2; // delete an existing job lead

    private EditText priceEditText;

    private int position;     // the position of the edited JobLead on the list of job leads
    private String key;
    private String itemName;
    private String price;
    private String purchaser;

    // A callback listener interface to finish up the editing of a JobLead.
    // ReviewJobLeadsActivity implements this listener interface, as it will
    // need to update the list of JobLeads and also update the RecyclerAdapter to reflect the
    // changes.
    public interface EditPurchaseDialogListener {
        void updatePurchase(int position, Item item, int action);
    }

    public static EditPurchaseDialogFragment newInstance(int position, String key, String itemName, String price, String purchaser) {
        EditPurchaseDialogFragment dialog = new EditPurchaseDialogFragment();

        // Supply job lead values as an argument.
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

        // Pre-fill the edit texts with the current values for this job lead.
        // The user will be able to modify them.
        //priceEditText.setText(String.format("%.2f",price));

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
            // add the new job lead
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