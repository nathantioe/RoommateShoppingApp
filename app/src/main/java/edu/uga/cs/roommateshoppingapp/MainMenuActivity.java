package edu.uga.cs.roommateshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Button recentBuys = findViewById(R.id.buttonRecentBuys);
        recentBuys.setOnClickListener(new ButtonClickListener());

        Button shoppingList = findViewById(R.id.buttonShoppingList);
        shoppingList.setOnClickListener(new ButtonClickListener());

        Button logout = findViewById(R.id.buttonLogout);
        logout.setOnClickListener(new ButtonClickListener());

    }

    private class ButtonClickListener implements
            View.OnClickListener
    {
        /**
         *
         * @param view View in which the buttons are
         */
        @Override
        public void onClick( View view ) {
            if (view.getId() == R.id.buttonRecentBuys ) {
//                Intent intent = new
//                        Intent(view.getContext(),
//                        RecentBuysActivity.class);
//                String message = "";
//                intent.putExtra("MESSAGE", message);
//                view.getContext().startActivity(intent);
            } else if (view.getId() == R.id.buttonShoppingList ) {
                Intent intent = new
                        Intent(view.getContext(),
                        ShoppingList2Activity.class);
                String message = "";
                intent.putExtra("MESSAGE", message);
                view.getContext().startActivity(intent);
            } else if (view.getId() == R.id.buttonLogout) {
                finish();
            }

        }
    }
}