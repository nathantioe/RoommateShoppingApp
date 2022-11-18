package edu.uga.cs.roommateshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

    private ListView listView;
    //List<ShoppingListItem> items;
    ShoppingListItem[] items;
    public static ShoppingListItem[] itemsToBuy;
    Button buttonBuy;
    Button buttonDelete;
    Button buttonadditem;
    boolean firstTimeLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        buttonBuy = findViewById(R.id.buttonBuy);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonadditem = findViewById(R.id.buttonAddItem);

        buttonadditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });

        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buySelectedItems();
                deleteSelectedItems();
                Intent intent = new
                        Intent(view.getContext(),
                        EnterPriceActivity.class);
                String message = "";
                intent.putExtra("MESSAGE", message);
                view.getContext().startActivity(intent);
            }
        });



        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSelectedItems();
            }
        });



        this.listView = findViewById(R.id.list_view_1);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedTextView v = (CheckedTextView) view;
                boolean currentCheck = v.isChecked();
                ShoppingListItem listItem = (ShoppingListItem) listView.getItemAtPosition(i);
                listItem.setActive(!currentCheck);
            }
        });

        this.initListViewData();

    }

    public void showCustomDialog() {
        final Dialog dialog = new Dialog(ShoppingListActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_item_dialog);

        EditText itemName = dialog.findViewById(R.id.itemName);
        Button submit = dialog.findViewById(R.id.buttonSubmit);

        submit.setOnClickListener((v) -> {
            String name = itemName.getText().toString();
            addItem(name);
            dialog.dismiss();
        });

        dialog.show();
    }

    public void deleteSelectedItems(){
        SparseBooleanArray sp = listView.getCheckedItemPositions();

        List<ShoppingListItem> arraylist = new ArrayList<>();

        for(int i=0;i<sp.size();i++){
            if(sp.valueAt(i) == false){
                ShoppingListItem item= (ShoppingListItem) listView.getItemAtPosition(i);
                arraylist.add(item);

            }
        }
        ShoppingListItem[] newItems = new ShoppingListItem[arraylist.size()];
        for (int i=0; i<arraylist.size();i++){
            newItems[i] = arraylist.get(i);
        }
        items = newItems;
        initListViewData();
    }

    public void buySelectedItems(){
        SparseBooleanArray sp = listView.getCheckedItemPositions();

        List<ShoppingListItem> arraylist = new ArrayList<>();

        for(int i=0;i<sp.size();i++){
            if(sp.valueAt(i) == true){
                ShoppingListItem item= (ShoppingListItem) listView.getItemAtPosition(i);
                arraylist.add(item);

            }
        }
        ShoppingListItem[] newItems = new ShoppingListItem[arraylist.size()];
        for (int i=0; i<arraylist.size();i++){
            newItems[i] = arraylist.get(i);
        }
        itemsToBuy = newItems;

    }


    public void addItem(String namee){
        ShoppingListItem sli = new ShoppingListItem(namee);

        SparseBooleanArray sp = listView.getCheckedItemPositions();

        List<ShoppingListItem> arraylist = new ArrayList<>();

        for(int i=0;i<sp.size();i++){
            ShoppingListItem item= (ShoppingListItem) listView.getItemAtPosition(i);
            arraylist.add(item);
        }
        arraylist.add(sli);
        ShoppingListItem[] newItems = new ShoppingListItem[arraylist.size()];
        for (int i=0; i<arraylist.size();i++){
            newItems[i] = arraylist.get(i);
        }
        items = newItems;
        initListViewData();
    }

    private void initData(){
        ShoppingListItem paper = new ShoppingListItem("paper");
        ShoppingListItem water = new ShoppingListItem("water");
        ShoppingListItem monster = new ShoppingListItem("monster");
        ShoppingListItem paper1 = new ShoppingListItem("paper1");
        ShoppingListItem water1 = new ShoppingListItem("water1");
        ShoppingListItem monster1 = new ShoppingListItem("monster1");
        ShoppingListItem paper2 = new ShoppingListItem("paper2");
        ShoppingListItem water2 = new ShoppingListItem("water2");
        ShoppingListItem monster2 = new ShoppingListItem("monster2");
        ShoppingListItem paper3 = new ShoppingListItem("paper3");
        ShoppingListItem water3 = new ShoppingListItem("water3");
        ShoppingListItem monster3 = new ShoppingListItem("monster3");
        ShoppingListItem paper4 = new ShoppingListItem("paper4");
        ShoppingListItem water4 = new ShoppingListItem("water4");
        ShoppingListItem monster4 = new ShoppingListItem("monster4");


        items = new ShoppingListItem[]{paper,water, monster,paper1,water1, monster1,paper2,water2, monster2,paper3,water3, monster3, paper4,water4, monster4};
    }


    private void initListViewData()  {

        if (firstTimeLoad){
            initData();
            firstTimeLoad = false;
        }

        // android.R.layout.simple_list_item_checked:
        // ListItem is very simple (Only one CheckedTextView).
        ArrayAdapter<ShoppingListItem> arrayAdapter
                = new ArrayAdapter<ShoppingListItem>(this, android.R.layout.simple_list_item_multiple_choice , items);

        this.listView.setAdapter(arrayAdapter);

        for(int i=0;i< items.length; i++ )  {
            this.listView.setItemChecked(i,items[i].isActive());
        }

    }
}
