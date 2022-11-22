//package edu.uga.cs.roommateshoppingapp;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.Dialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.SparseBooleanArray;
//import android.view.View;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.CheckedTextView;
//import android.widget.EditText;
//import android.widget.ListView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ShoppingListActivity extends AppCompatActivity {
//
//    private ListView listView;
//    //List<ShoppingListItem> items;
//    Item[] items;
//    public static Item[] itemsToBuy;
//    Button buttonBuy;
//    Button buttonDelete;
//    Button buttonadditem;
//    boolean firstTimeLoad = true;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_shopping_list);
//
//        buttonBuy = findViewById(R.id.buttonBuy);
//        buttonDelete = findViewById(R.id.buttonDelete);
//        buttonadditem = findViewById(R.id.buttonAddItem);
//
//        buttonadditem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showCustomDialog();
//            }
//        });
//
//        buttonBuy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                buySelectedItems();
//                deleteSelectedItems();
//                Intent intent = new
//                        Intent(view.getContext(),
//                        EnterPriceActivity.class);
//                String message = "";
//                intent.putExtra("MESSAGE", message);
//                view.getContext().startActivity(intent);
//            }
//        });
//
//
//
//        buttonDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                deleteSelectedItems();
//            }
//        });
//
//
//
//        this.listView = findViewById(R.id.list_view_1);
//
//        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                CheckedTextView v = (CheckedTextView) view;
//                boolean currentCheck = v.isChecked();
//                Item listItem = (Item) listView.getItemAtPosition(i);
//                //listItem.setActive(!currentCheck);
//            }
//        });
//
//        this.initListViewData();
//
//    }
//
//    public void showCustomDialog() {
//        final Dialog dialog = new Dialog(ShoppingListActivity.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(true);
//        dialog.setContentView(R.layout.add_item_dialog);
//
//        EditText itemName = dialog.findViewById(R.id.name);
//        //Button submit = dialog.findViewById(R.id.buttonSubmit);
//
////        submit.setOnClickListener((v) -> {
////            String name = itemName.getText().toString();
////            addItem(name);
////            dialog.dismiss();
////
////            final ShoppingListItem item = new ShoppingListItem(name, true);
////
////            // Add a new element (JobLead) to the list of job leads in Firebase.
////            FirebaseDatabase database = FirebaseDatabase.getInstance();
////            DatabaseReference myRef = database.getReference("items");
////
////            // First, a call to push() appends a new node to the existing list (one is created
////            // if this is done for the first time).  Then, we set the value in the newly created
////            // list node to store the new job lead.
////            // This listener will be invoked asynchronously, as no need for an AsyncTask, as in
////            // the previous apps to maintain job leads.
////            myRef.push().setValue( item )
////                    .addOnSuccessListener( new OnSuccessListener<Void>() {
////                        @Override
////                        public void onSuccess(Void aVoid) {
////                            // Show a quick confirmation
////                            Toast.makeText(getApplicationContext(), "Item created for ",
////                                    Toast.LENGTH_SHORT).show();
////
////                            // Clear the EditTexts for next use.
////
////                        }
////                    })
////                    .addOnFailureListener( new OnFailureListener() {
////                        @Override
////                        public void onFailure( @NonNull Exception e ) {
////                            Toast.makeText( getApplicationContext(), "Failed to create a item for ",
////                                    Toast.LENGTH_SHORT).show();
////                        }
////                    });
////        });
//
//        dialog.show();
//    }
//
//    public void deleteSelectedItems(){
//        SparseBooleanArray sp = listView.getCheckedItemPositions();
//
//        List<Item> arraylist = new ArrayList<>();
//
//        for(int i=0;i<sp.size();i++){
//            if(sp.valueAt(i) == false){
//                Item item= (Item) listView.getItemAtPosition(i);
//                arraylist.add(item);
//
//            }
//        }
//        Item[] newItems = new Item[arraylist.size()];
//        for (int i=0; i<arraylist.size();i++){
//            newItems[i] = arraylist.get(i);
//        }
//        items = newItems;
//        initListViewData();
//    }
//
//    public void buySelectedItems(){
//        SparseBooleanArray sp = listView.getCheckedItemPositions();
//
//        List<Item> arraylist = new ArrayList<>();
//
//        for(int i=0;i<sp.size();i++){
//            if(sp.valueAt(i) == true){
//                Item item= (Item) listView.getItemAtPosition(i);
//                arraylist.add(item);
//
//            }
//        }
//        Item[] newItems = new Item[arraylist.size()];
//        for (int i=0; i<arraylist.size();i++){
//            newItems[i] = arraylist.get(i);
//        }
//        itemsToBuy = newItems;
//
//    }
//
//
//    public void addItem(String namee){
//        Item sli = new Item(namee);
//
//        SparseBooleanArray sp = listView.getCheckedItemPositions();
//
//        List<Item> arraylist = new ArrayList<>();
//
//        for(int i=0;i<sp.size();i++){
//            Item item= (Item) listView.getItemAtPosition(i);
//            arraylist.add(item);
//        }
//        arraylist.add(sli);
//        Item[] newItems = new Item[arraylist.size()];
//        for (int i=0; i<arraylist.size();i++){
//            newItems[i] = arraylist.get(i);
//        }
//        items = newItems;
//        initListViewData();
//    }
//
//    private void initData(){
//        Item paper = new Item("3 items bought by: Bob  \nTot: 12.34");
//        Item water = new Item("water");
//        Item monster = new Item("monster");
//        Item paper1 = new Item("paper1");
//        Item water1 = new Item("water1");
//        Item monster1 = new Item("monster1");
//        Item paper2 = new Item("paper2");
//        Item water2 = new Item("water2");
//        Item monster2 = new Item("monster2");
//        Item paper3 = new Item("paper3");
//        Item water3 = new Item("water3");
//        Item monster3 = new Item("monster3");
//        Item paper4 = new Item("paper4");
//        Item water4 = new Item("water4");
//        Item monster4 = new Item("monster4");
//
//
//        items = new Item[]{paper,water, monster,paper1,water1, monster1,paper2,water2, monster2,paper3,water3, monster3, paper4,water4, monster4};
//    }
//
//
//    private void initListViewData()  {
//
//        if (firstTimeLoad){
//            initData();
//            firstTimeLoad = false;
//        }
//
//        // android.R.layout.simple_list_item_checked:
//        // ListItem is very simple (Only one CheckedTextView).
//        ArrayAdapter<Item> arrayAdapter
//                = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_multiple_choice , items);
//
//        this.listView.setAdapter(arrayAdapter);
//
//        for(int i=0;i< items.length; i++ )  {
//            //this.listView.setItemChecked(i,items[i].isActive());
//        }
//
//    }
//}
