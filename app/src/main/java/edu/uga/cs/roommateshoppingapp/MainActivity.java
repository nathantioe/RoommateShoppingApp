package edu.uga.cs.roommateshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button login = findViewById(R.id.buttonLogin);
        login.setOnClickListener(new ButtonClickListener());

        Button registrer = findViewById(R.id.buttonRegistrer);
        registrer.setOnClickListener(new ButtonClickListener());

    }
}

class ButtonClickListener implements
        View.OnClickListener
{
    /**
     *
     * @param view View in which the buttons are
     */
    @Override
    public void onClick( View view ) {
        if (view.getId() == R.id.buttonRegistrer ) {
            Intent intent = new
                    Intent(view.getContext(),
                    RegistrerActivity.class);
            String message = "";
            intent.putExtra("MESSAGE", message);
            view.getContext().startActivity(intent);
        } if (view.getId() == R.id.buttonLogin ) {
            Intent intent = new
                    Intent(view.getContext(),
                    MainMenuActivity.class);
            String message = "";
            intent.putExtra("MESSAGE", message);
            view.getContext().startActivity(intent);
        }

    }
}