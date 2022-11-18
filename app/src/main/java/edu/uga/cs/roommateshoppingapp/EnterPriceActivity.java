package edu.uga.cs.roommateshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class EnterPriceActivity extends AppCompatActivity {

    private ListView listView;
    private Button buttonDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_price);

        listView = findViewById(R.id.list_view_2);
        buttonDone = findViewById(R.id.buttonDone);

        listView.setChoiceMode(ListView.CHOICE_MODE_NONE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                showCustomDialog(position);

            }
        });

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new
                        Intent(view.getContext(),
                        MainMenuActivity.class);
                String message = "";
                intent.putExtra("MESSAGE", message);
                view.getContext().startActivity(intent);
            }
        });

        initListViewData();

    }

    private void initListViewData() {
        ShoppingListItem[] list = ShoppingListActivity.itemsToBuy;

        ArrayAdapter<ShoppingListItem> arrayAdapter
                = new ArrayAdapter<ShoppingListItem>(this, android.R.layout.simple_list_item_1 , list);

        this.listView.setAdapter(arrayAdapter);

        //for(int i=0;i< list.length; i++ )  {
        //    this.listView.setItemChecked(i,list[i].isActive());
       // }
    }

    private void showCustomDialog(int position) {
        final Dialog dialog = new Dialog(EnterPriceActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_price_dialog);

        EditText itemPrice = dialog.findViewById(R.id.editTextPrice);
        Button submit = dialog.findViewById(R.id.buttonDonePrice);

        submit.setOnClickListener((v) -> {
            Double price = Double.parseDouble(itemPrice.getText().toString());
            ShoppingListActivity.itemsToBuy[position].setPrice(price);
            ShoppingListActivity.itemsToBuy[position].setItemName(ShoppingListActivity.itemsToBuy[position].getItemName() + " (" + itemPrice.getText().toString() + " $)" );
            initListViewData();
            dialog.dismiss();
        });

        dialog.show();
    }
}


