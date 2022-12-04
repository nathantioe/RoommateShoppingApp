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
 * DialogFragment to handle edits to an item
 */
public class EditItemDialogFragment extends DialogFragment {

    // indicate the type of an edit
    public static final int SAVE = 1;
    public static final int DELETE = 2;

    private EditText itemNameView;

    private int position;
    private String key;
    private String itemName;

    public interface EditItemDialogListener {
        void updateItem(int position, Item item, int action);
    }

    public static EditItemDialogFragment newInstance(int position, String key, String itemName) {
        EditItemDialogFragment dialog = new EditItemDialogFragment();

        Bundle args = new Bundle();
        args.putString( "key", key );
        args.putInt( "position", position );
        args.putString("itemName", itemName);
        dialog.setArguments(args);

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ) {

        key = getArguments().getString( "key" );
        position = getArguments().getInt( "position" );
        itemName = getArguments().getString( "itemName" );

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.add_item_dialog, getActivity().findViewById( R.id.root ) );

        itemNameView = layout.findViewById( R.id.editText1 );

        itemNameView.setText(itemName);

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity());
        builder.setView(layout);

        // Set the title of the AlertDialog
        builder.setTitle( "Edit Item" );

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
            String name = itemNameView.getText().toString();
            Item item = new Item(name);
            item.setKey(key);

            EditItemDialogListener listener = (EditItemDialogFragment.EditItemDialogListener) getActivity();
            listener.updateItem( position, item, SAVE );
            dismiss();
        }
    }

    private class DeleteButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick( DialogInterface dialog, int which ) {
            Item item = new Item(itemName);
            item.setKey(key);

            EditItemDialogFragment.EditItemDialogListener listener = (EditItemDialogFragment.EditItemDialogListener) getActivity();            // add the new job lead
            listener.updateItem( position, item, DELETE );
            dismiss();
        }
    }
}