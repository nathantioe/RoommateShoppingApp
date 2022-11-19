package edu.uga.cs.roommateshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class RecentBuysActivity extends AppCompatActivity {

    private ListView listView;
    private Button buttonSettle;
    private RecentBuy[] recentBuys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_buys);

        listView = findViewById(R.id.listViewRecentBuys);
        buttonSettle = findViewById(R.id.buttonSettle);

        listView.setChoiceMode(ListView.CHOICE_MODE_NONE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        initListViewData();



    }

    private void initListViewData(){
        //ShoppingListItem paper = new ShoppingListItem("3 items bought by: Bob  \nTot: 12.34");
        ShoppingListItem paper = new ShoppingListItem("paper", 3.12);
        ShoppingListItem water = new ShoppingListItem("water", 1.2);
        ShoppingListItem monster = new ShoppingListItem("monster", 2.45);
        ShoppingListItem[] buy1List = new ShoppingListItem[]{paper,water, monster, paper,water, monster, paper,water, monster, paper,water, monster};
        RecentBuy buy1 = new RecentBuy("Joe", buy1List);

        ShoppingListItem paper1 = new ShoppingListItem("paper1", 5.76);
        ShoppingListItem water1 = new ShoppingListItem("water1", 3.43);
        //ShoppingListItem monster1 = new ShoppingListItem("monster1");
        ShoppingListItem[] buy2List = new ShoppingListItem[]{paper1,water1};
        RecentBuy buy2 = new RecentBuy("Joe", buy2List);

        recentBuys = new RecentBuy[]{buy1, buy2};

        ArrayAdapter<RecentBuy> arrayAdapter
                = new ArrayAdapter<RecentBuy>(this, android.R.layout.simple_list_item_1 , recentBuys);

        listView.setAdapter(arrayAdapter);

    }
}