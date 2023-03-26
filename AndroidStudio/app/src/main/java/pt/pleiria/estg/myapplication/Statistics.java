package pt.pleiria.estg.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;

public class Statistics extends AppCompatActivity {
    public int totalDeliveredOrders;
    public int totalCanceledOrders;

    TextView textViewStatsCanceled,textViewStatsDelivered,textViewStatsTotalTime,
            textViewStatsAverageTime, textViewStatsTotalEarned, textViewStatsDistinctCustomers;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        totalCanceledOrders =0;
        totalDeliveredOrders =0;

        textViewStatsCanceled = findViewById(R.id.textViewStatsCanceled);
        textViewStatsDelivered = findViewById(R.id.textViewStatsDelivered);
        textViewStatsTotalTime = findViewById(R.id.textViewStatsTotalTime);
        textViewStatsAverageTime = findViewById(R.id.textViewStatsAverageTime);
        textViewStatsTotalEarned = findViewById(R.id.textViewStatsTotalEarned);
        textViewStatsDistinctCustomers = findViewById(R.id.textViewStatsDistinctCustomers);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        //count all the delivered orders associated with this user in the database
        firebaseFirestore.collection("Orders")
                .whereEqualTo("status", "D")
                .whereEqualTo("userId",Dashboard.currentUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            totalDeliveredOrders=task.getResult().size();
                            textViewStatsDelivered.setText("Total orders delivered: "+ totalDeliveredOrders);

                            //get time stats for every delivery
                            long totalDeliveryMiliseconds = 0;
                            LinkedList<String> customers = new LinkedList<>();
                            String auxCustomerId;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                totalDeliveryMiliseconds+=(document.getLong("time_finished") - document.getLong("time_started"));

                                //make a list of every different customer served
                                auxCustomerId = document.getString("customer_id");
                                if(!customers.contains(auxCustomerId)) {
                                    customers.add(auxCustomerId);
                                }
                            }

                            textViewStatsDistinctCustomers.setText("number of distinct customers served: "+ customers.size());

                            //calculate times
                            long s = totalDeliveryMiliseconds/1000;
                            long h = s/ 3600;
                            s = s % 3600;
                            long m = s/60;
                            s = s % 60;

                            textViewStatsTotalTime.setText("Total time spent doing deliveries: "+ h+"h "+m+"m "+s+"s");

                            if(totalDeliveredOrders >=2){
                                s = totalDeliveryMiliseconds/1000/totalDeliveredOrders;
                                h = s/ 3600;
                                s = s % 3600;
                                m = s/60;
                                s = s % 60;
                            }
                            textViewStatsAverageTime.setText("Average time spent doing deliveries: "+ h+"h "+m+"m "+s+"s");
                        }
                    }
                });

        //count all the canceled orders associated with this user in the database
        firebaseFirestore.collection("Orders")
                .whereEqualTo("status", "C")
                .whereEqualTo("userId", Dashboard.currentUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            totalCanceledOrders =task.getResult().size();
                            textViewStatsCanceled.setText("Total orders canceled: "+ totalCanceledOrders);
                        }
                    }
                });

        //get total amount earned
        firebaseFirestore.collection("Orders")
                .whereEqualTo("status", "D")
                .whereEqualTo("userId",Dashboard.currentUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            long balance=0;
                            double distance;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                distance = document.getDouble("distance");
                                if(distance < 3.0){
                                    balance += 2.0;
                                }else if(distance >= 3.0 && distance < 10.0){
                                    balance += 3.0;
                                }else{
                                    balance += 4.0;
                                }
                            }
                            textViewStatsTotalEarned.setText("Total amount earned: "+balance);
                        }

                    }
                });

    }

    public void backToAccountClick(View view){
        Intent i = new Intent(Statistics.this, UserPage.class);
        startActivity(i);
    }

    public void logout(View view){
        Dashboard.loggedUser = new User();

        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        sp.edit().putString("userId","").apply(); //disable the KeepMeLoggedIn

        Intent i = new Intent(Statistics.this, LoginActivity.class);
        startActivity(i);
    }
}