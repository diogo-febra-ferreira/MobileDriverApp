package pt.pleiria.estg.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationActivity extends AppCompatActivity {
    LinearLayout nots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacao);

        nots = findViewById(R.id.notis);


            String notificacao;
            notificacao = Dashboard.notificaçoes.toString().replace(",,", "\n").replace("[","").replace(",]"," ").replace("]", " ");

            TextView availableOrders = new TextView(NotificationActivity.this);

            availableOrders.setText(notificacao);
            availableOrders.setTextSize(16);

            nots.addView(availableOrders);

    }

    public void Home(View view){
        Intent resultIntent = new Intent(NotificationActivity.this, Dashboard.class);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
        startActivity(resultIntent);
    }
    public void logoutClick(View view) {

        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        sp.edit().putString("userId","").apply(); //disable the KeepMeLoggedIn

        Dashboard.loggedUser = new User();
        Intent i = new Intent(NotificationActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
