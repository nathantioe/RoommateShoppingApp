package edu.uga.cs.roommateshoppingapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

/**
 * DialogFragment that displays info after settling cost
 */
public class SettleDialogFragment extends DialogFragment {

    private TextView totalCostTextView;
    private TextView averageCostTextView;
    private TextView costPerUserTextView;
    private String totalCost;
    private String averageCost;
    private String costPerUser;

    public static SettleDialogFragment newInstance(String totalCost, String averageCost, String costPerUser) {
        SettleDialogFragment dialog = new SettleDialogFragment();

        Bundle args = new Bundle();
        args.putString( "totalCost", totalCost);
        args.putString( "averageCost", averageCost);
        args.putString( "costPerUser", costPerUser);

        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        totalCost = getArguments().getString( "totalCost" );
        averageCost = getArguments().getString( "averageCost" );
        costPerUser = getArguments().getString( "costPerUser" );

        // Create the AlertDialog view
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.settle_dialog,
                getActivity().findViewById(R.id.scrollView));

        // get the view objects in the AlertDialog
        totalCostTextView = layout.findViewById(R.id.totalCost);
        averageCostTextView = layout.findViewById(R.id.averageCost);
        costPerUserTextView = layout.findViewById(R.id.costPerUser);

        totalCostTextView.setText("Total cost: " + totalCost);
        averageCostTextView.setText("Average cost per roommate: " + averageCost);
        costPerUserTextView.setText("Cost per roommate: \n" + costPerUser);
        // create a new AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set its view (inflated above).
        builder.setView(layout);

        // Set the title of the AlertDialog
        builder.setTitle( "Cost Breakdown" );
        // Provide the negative button listener
        builder.setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // close the dialog
                dialog.dismiss();
            }
        });

        // Create the AlertDialog and show it
        return builder.create();
    }
}