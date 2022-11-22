//package edu.uga.cs.roommateshoppingapp;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//
//public class RecentBuysActivity extends AppCompatActivity {
//
//    private ListView listView;
//    private Button buttonSettle;
//    private RecentBuy[] recentBuys;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_recent_buys);
//
//        listView = findViewById(R.id.listViewRecentBuys);
//        buttonSettle = findViewById(R.id.buttonSettle);
//
//        listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//        });
//
//        initListViewData();
//
//
//
//    }
//
//    private void initListViewData(){
//        //ShoppingListItem paper = new ShoppingListItem("3 items bought by: Bob  \nTot: 12.34");
//        Item paper = new Item("paper", 3.12);
//        Item water = new Item("water", 1.2);
//        Item monster = new Item("monster", 2.45);
//        Item[] buy1List = new Item[]{paper,water, monster, paper,water, monster, paper,water, monster, paper,water, monster};
//        RecentBuy buy1 = new RecentBuy("Joe", buy1List);
//
//        Item paper1 = new Item("paper1", 5.76);
//        Item water1 = new Item("water1", 3.43);
//        //ShoppingListItem monster1 = new ShoppingListItem("monster1");
//        Item[] buy2List = new Item[]{paper1,water1};
//        RecentBuy buy2 = new RecentBuy("Joe", buy2List);
//
//        recentBuys = new RecentBuy[]{buy1, buy2};
//
//        ArrayAdapter<RecentBuy> arrayAdapter
//                = new ArrayAdapter<RecentBuy>(this, android.R.layout.simple_list_item_1 , recentBuys);
//
//        listView.setAdapter(arrayAdapter);
//
//    }
//}