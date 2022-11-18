package edu.uga.cs.roommateshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Button recentBuys = findViewById(R.id.buttonRecentBuys);
        recentBuys.setOnClickListener(new ButtonClickListener2());

        Button shoppingList = findViewById(R.id.buttonShoppingList);
        shoppingList.setOnClickListener(new ButtonClickListener2());

    }
}

class ButtonClickListener2 implements
        View.OnClickListener
{
    /**
     *
     * @param view View in which the buttons are
     */
    @Override
    public void onClick( View view ) {
        if (view.getId() == R.id.buttonRecentBuys ) {
            Intent intent = new
                    Intent(view.getContext(),
                    RecentBuysActivity.class);
            String message = "";
            intent.putExtra("MESSAGE", message);
            view.getContext().startActivity(intent);
        } if (view.getId() == R.id.buttonShoppingList ) {
            Intent intent = new
                    Intent(view.getContext(),
                    ShoppingListActivity.class);
            String message = "";
            intent.putExtra("MESSAGE", message);
            view.getContext().startActivity(intent);
        }

    }
}