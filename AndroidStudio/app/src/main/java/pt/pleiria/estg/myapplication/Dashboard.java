package pt.pleiria.estg.myapplication;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static User loggedUser;
    public static String currOrder;

    public static List<String> notificaçoes;

    public static LatLng restaurant;
    public static String orderLat;
    public static String orderLng;
    //public static String userID;
    public float distancia;
    private String lat,lng;
    public static String currentUserId;
    float[] results = new float[1];

    TextView textWelcome, available_orders;              //AO->Available Orders - MO->My Offers
    ScrollView scrollView, scrollViewMO;
    LinearLayout linearLayoutAO, linearLayoutMO;
    Button btn_home, btn_account;
    public LatLng coordsDestino, coordsRestaurante;
    Spinner spinnerAO, spinnerMO;
    String[] options = {"Closest Orders", "Farthest Orders"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        textWelcome = findViewById(R.id.textWelcome);
        available_orders = findViewById(R.id.textAvailableOrders);
        scrollView = findViewById(R.id.scrollViewAvailableOrders);
        linearLayoutAO = findViewById(R.id.linearLayoutAvailableOrders);
        scrollViewMO = findViewById(R.id.scrollViewMyOrders);
        linearLayoutMO = findViewById(R.id.linearLayoutMyOrders);
        btn_account = findViewById(R.id.buttonAccount);
        spinnerAO = findViewById(R.id.spinnerAO);
        spinnerAO.setOnItemSelectedListener(this);
        spinnerMO = findViewById(R.id.spinnerMO);
        spinnerMO.setOnItemSelectedListener(this);
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item,options);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_item);


        notificaçoes = new ArrayList<>();


        spinnerAO.setAdapter(ad);
        spinnerMO.setAdapter(ad);


        String welcome = "Welcome " + loggedUser.firstName + " " + loggedUser.lastName;
        textWelcome.setText(welcome);
        textWelcome.setTextSize(16);
        restaurant = new com.google.android.gms.maps.model.LatLng(39.743000,  -8.813704);





        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Orders")
                .whereIn("status",Arrays.asList("P","R"))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<DocumentChange> documentChangesList = value.getDocumentChanges();
                        if(value.getMetadata().isFromCache()) return;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                            NotificationChannel channel = new NotificationChannel("Order","Order",NotificationManager.IMPORTANCE_DEFAULT);
                            NotificationManager manager = getSystemService(NotificationManager.class);
                            manager.createNotificationChannel(channel);
                        }

                        for (DocumentChange documentChange : documentChangesList){
                            String id = documentChange.getDocument().getId();
                            FirebaseFirestore fdb = FirebaseFirestore.getInstance();
                            fdb.collection("Orders").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot valueF, @Nullable FirebaseFirestoreException error) {
                                        String stat = valueF.getString("status");
                                        String driver = valueF.getString("userId");
                                        String addressName = valueF.getString("address");

                                        if (stat.equals("R") && driver.equals("") && !notificaçoes.contains("New order(s) are avaiable in " + addressName + ",")){
                                            System.out.println("dentro do if r equals stats e blah blah");
                                            if(valueF.getMetadata().isFromCache()) return;
                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(Dashboard.this, "Order");
                                            builder.setContentTitle("Order");
                                            builder.setContentText("New order(s) are avaiable in " + addressName);
                                            builder.setSmallIcon(android.R.drawable.ic_dialog_email);
                                            builder.setAutoCancel(true);
                                            String a = "New order(s) are avaiable in " + addressName + ",";
                                            notificaçoes.add(a);
                                            notificaçoes.add("-------------------------------------,");
                                            // Create an Intent for the activity you want to start
                                            Intent resultIntent = new Intent(Dashboard.this, NotificationActivity.class);
                                            // Create the TaskStackBuilder and add the intent, which inflates the back stack
                                            resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            String message = "this a experience";
                                            resultIntent.putExtra("message",message);
                                            // Get the PendingIntent containing the entire back stack
                                            PendingIntent resultPendingIntent = PendingIntent
                                                    .getActivity(Dashboard.this,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                                            builder.setContentIntent(resultPendingIntent);

                                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Dashboard.this);
                                            notificationManager.notify(0, builder.build());


                                        }
                                }
                            });

                        }
                    }
                });

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Orders")
                .whereIn("status", Arrays.asList("P","R"))
                .whereEqualTo("userId", "")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            linearLayoutAO.removeAllViews();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                lat =  document.getString("latitude");
                                lng =  document.getString("longitude");
                                if(document.getMetadata().isFromCache()) return;


                                String address = document.getString("address");
                                String id = document.getId();
                                Map<String, Object> data = document.getData();
                                TextView availableOrders = new TextView(Dashboard.this);
                                Button btn_Assign = new Button(Dashboard.this);
                                btn_Assign.setWidth(50);
                                btn_Assign.setHeight(20);
                                btn_Assign.setText("Assign");
                                //btn_Assign.setLayoutParams();
                                coordsDestino = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)); //LatLng coordenadas da order
                                coordsRestaurante = Dashboard.restaurant;
                                Location.distanceBetween(coordsDestino.latitude, coordsDestino.longitude, coordsRestaurante.latitude, coordsDestino.longitude, results);
                                distancia = results[0]/1000;
                                firebaseFirestore.collection("Orders")
                                                .document(document.getId())
                                                        .update("distance", distancia);
                                availableOrders.setText("\nAddress: " + address + "\nDistance: "+String.format("%.2f",distancia)+" Km");
                                availableOrders.setTextSize(16);
                                availableOrders.setTypeface(null, Typeface.BOLD);
                                linearLayoutAO.setOrientation(LinearLayout.VERTICAL);
                                linearLayoutAO.addView(availableOrders);
                                linearLayoutAO.addView(btn_Assign);
                                btn_Assign.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        FirebaseFirestore firebaseFirestoreWrite = FirebaseFirestore.getInstance();
                                        firebaseFirestoreWrite.collection("Orders")
                                                .document(id)
                                                .update("userId", currentUserId, "status", "R");
                                        finish();
                                        startActivity(getIntent());
                                    }
                                });
                                Button btn_details = new Button(Dashboard.this);
                                    btn_details.setBackgroundColor(0xff000000);
                                    btn_details.setWidth(50);
                                    btn_details.setHeight(20);
                                    btn_details.setText("details");
                                    btn_details.setTextColor(0xffffffff);
                                linearLayoutAO.addView(btn_details);
                                btn_details.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        currOrder = document.getId();
                                        orderLat = document.getString("latitude");
                                        orderLng = document.getString("longitude");
                                        FirebaseFirestore firebaseFirestoreWrite = FirebaseFirestore.getInstance();
                                        firebaseFirestoreWrite.collection("Orders")
                                                .document(id);
                                        finish();
                                        Intent i = new Intent(Dashboard.this, OrderDetails.class);
                                        startActivity(i);
                                    }
                                });
                            }
                        }

                        firebaseFirestore.collection("Orders")
                                .whereEqualTo("status", "R")
                                .whereEqualTo("userId",currentUserId)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            linearLayoutMO.removeAllViews();
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                lat =  document.getString("latitude");
                                                lng =  document.getString("longitude");
                                                String address = document.getString("address");
                                                String id = document.getId();
                                                TextView myOrders = new TextView(Dashboard.this);
                                                Button btn_Start = new Button(Dashboard.this);
                                                btn_Start.setText("Start");
                                                coordsDestino = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)); //LatLng coordenadas da order
                                                coordsRestaurante = Dashboard.restaurant;
                                                Location.distanceBetween(coordsDestino.latitude, coordsDestino.longitude, coordsRestaurante.latitude, coordsDestino.longitude, results);
                                                distancia = results[0]/1000;
                                                myOrders.setText("\nAddress: " + address + "\nDistance: "+String.format("%.2f",distancia)+" Km");
                                                myOrders.setTypeface(null, Typeface.BOLD);
                                                myOrders.setTextSize(16);
                                                linearLayoutMO.setOrientation(LinearLayout.VERTICAL);
                                                linearLayoutMO.addView(myOrders);
                                                linearLayoutMO.addView(btn_Start);
                                                btn_Start.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {

                                                        //update start time on the database
                                                        FirebaseFirestore firebaseFirestoreWrite = FirebaseFirestore.getInstance();
                                                        firebaseFirestoreWrite.collection("Orders")
                                                                .document(document.getId())
                                                                .update("userId", currentUserId, "time_started", System.currentTimeMillis());
                                                        finish();

                                                        currOrder = document.getId();
                                                        orderLat = document.getString("latitude");
                                                        orderLng = document.getString("longitude");
                                                        Intent i = new Intent(Dashboard.this, MapaActivity.class);
                                                        startActivity(i);
                                                    }
                                                });
                                                Button btn_details = new Button(Dashboard.this);
                                                btn_details.setBackgroundColor(0xff000000);
                                                btn_details.setWidth(50);
                                                btn_details.setHeight(20);
                                                btn_details.setText("details");
                                                btn_details.setTextColor(0xffffffff);
                                                linearLayoutMO.addView(btn_details);
                                                btn_details.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        currOrder = document.getId();
                                                        orderLat = document.getString("latitude");
                                                        orderLng = document.getString("longitude");
                                                        FirebaseFirestore firebaseFirestoreWrite = FirebaseFirestore.getInstance();
                                                        firebaseFirestoreWrite.collection("Orders")
                                                                .document(id);
                                                        finish();
                                                        Intent i = new Intent(Dashboard.this, OrderDetails.class);
                                                        startActivity(i);
                                                    }
                                                });
                                                Button btn_unassign = new Button(Dashboard.this);
                                                btn_unassign.setWidth(50);
                                                btn_unassign.setHeight(20);
                                                btn_unassign.setText("unassign");
                                                btn_unassign.setTextColor(0xffffffff);
                                                linearLayoutMO.addView(btn_unassign);
                                                btn_unassign.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        FirebaseFirestore firebaseFirestoreWrite = FirebaseFirestore.getInstance();
                                                        firebaseFirestoreWrite.collection("Orders")
                                                                .document(id)
                                                                .update("userId", "", "status", "R");
                                                        finish();
                                                        startActivity(getIntent());
                                                    }
                                                });
                                            }
                                        }
                                    }

                                });
                    }
                });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My notification","My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        //isto imcompleto, penso que as notificaçoes ja existem pois aparecem na definiços->aplicaçao->fastuga, no entanto nao aparece na barra de cima

        /*FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Orders")
                .whereEqualTo("status", "P")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        for (QueryDocumentSnapshot documentSnapshot : value){
                            if (documentSnapshot.getString("status") == "R"){
                                System.out.println("dentro do if status R");
                                //talvez fazer um if se isto mandar bues notificaçoes
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(Dashboard.this,"My Notification");
                                builder.setContentTitle("My title");
                                builder.setContentText("New orders are avaiable");
                                builder.setSmallIcon(R.drawable.ic_launcher_background);
                                builder.setAutoCancel(true);

                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Dashboard.this);
                                notificationManager.notify(1,builder.build());
                            }
                        }
                    }
                });*/
    }

    public void logoutClick(View view) {

        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        sp.edit().putString("userId","").apply(); //disable the KeepMeLoggedIn

        loggedUser = new User();
        Intent i = new Intent(Dashboard.this, LoginActivity.class);
        startActivity(i);
    }

    public void accountClick(View view){
        Intent i = new Intent(Dashboard.this, UserPage.class);
        startActivity(i);
        }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.spinnerAO) {
            if (i == 0) {
                System.out.println("Closest Location");
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("Orders")
                        .whereIn("status", Arrays.asList("P", "R"))
                        .whereEqualTo("userId", "")
                        .orderBy("distance", Query.Direction.ASCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    linearLayoutAO.removeAllViews();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        lat = document.getString("latitude");
                                        lng = document.getString("longitude");
                                        String id = document.getId();
                                        String address = document.getString("address");

                                        TextView availableOrders = new TextView(Dashboard.this);
                                        Button btn_Assign = new Button(Dashboard.this);
                                        btn_Assign.setWidth(50);
                                        btn_Assign.setHeight(20);
                                        btn_Assign.setText("Assign");

                                        Button btn_details = new Button(Dashboard.this);
                                        btn_details.setBackgroundColor(0xff000000);
                                        btn_details.setWidth(50);
                                        btn_details.setHeight(20);
                                        btn_details.setText("details");
                                        btn_details.setTextColor(0xffffffff);

                                        btn_details.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                currOrder = document.getId();
                                                orderLat = document.getString("latitude");
                                                orderLng = document.getString("longitude");
                                                FirebaseFirestore firebaseFirestoreWrite = FirebaseFirestore.getInstance();
                                                firebaseFirestoreWrite.collection("Orders")
                                                        .document(id);
                                                finish();
                                                Intent i = new Intent(Dashboard.this, OrderDetails.class);
                                                startActivity(i);
                                            }
                                        });

                                        coordsDestino = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)); //LatLng coordenadas da order
                                        coordsRestaurante = Dashboard.restaurant;
                                        Location.distanceBetween(coordsDestino.latitude, coordsDestino.longitude, coordsRestaurante.latitude, coordsDestino.longitude, results);
                                        distancia = results[0] / 1000;
                                        availableOrders.setText("\nAddress: " + address + "\nDistance: " + String.format("%.2f", distancia) + " Km");
                                        availableOrders.setTextSize(16);
                                        availableOrders.setTypeface(null, Typeface.BOLD);
                                        linearLayoutAO.setOrientation(LinearLayout.VERTICAL);
                                        linearLayoutAO.addView(availableOrders);
                                        linearLayoutAO.addView(btn_Assign);
                                        linearLayoutAO.addView(btn_details);

                                        btn_Assign.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                FirebaseFirestore firebaseFirestoreWrite = FirebaseFirestore.getInstance();
                                                firebaseFirestoreWrite.collection("Orders")
                                                        .document(id)
                                                        .update("userId", currentUserId, "status", "R");
                                                finish();
                                                startActivity(getIntent());
                                            }
                                        });


                                    }
                                }
                            }

                        });

            } else if (i == 1) {
                System.out.println("Farthest Location");
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("Orders")
                        .whereIn("status", Arrays.asList("P", "R"))
                        .whereEqualTo("userId", "")
                        .orderBy("distance", Query.Direction.DESCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    linearLayoutAO.removeAllViews();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        lat = document.getString("latitude");
                                        lng = document.getString("longitude");
                                        String id = document.getId();
                                        String address = document.getString("address");
                                        System.out.println("AQUI: " + address);
                                        TextView availableOrders = new TextView(Dashboard.this);
                                        Button btn_Assign = new Button(Dashboard.this);
                                        btn_Assign.setWidth(50);
                                        btn_Assign.setHeight(20);
                                        btn_Assign.setText("Assign");

                                        Button btn_details = new Button(Dashboard.this);
                                        btn_details.setBackgroundColor(0xff000000);
                                        btn_details.setWidth(50);
                                        btn_details.setHeight(20);
                                        btn_details.setText("details");
                                        btn_details.setTextColor(0xffffffff);

                                        btn_details.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                currOrder = document.getId();
                                                orderLat = document.getString("latitude");
                                                orderLng = document.getString("longitude");
                                                FirebaseFirestore firebaseFirestoreWrite = FirebaseFirestore.getInstance();
                                                firebaseFirestoreWrite.collection("Orders")
                                                        .document(id);
                                                finish();
                                                Intent i = new Intent(Dashboard.this, OrderDetails.class);
                                                startActivity(i);
                                            }
                                        });

                                        coordsDestino = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)); //LatLng coordenadas da order
                                        coordsRestaurante = Dashboard.restaurant;
                                        Location.distanceBetween(coordsDestino.latitude, coordsDestino.longitude, coordsRestaurante.latitude, coordsDestino.longitude, results);
                                        distancia = results[0] / 1000;
                                        availableOrders.setText("\nAddress: " + address + "\nDistance: " + String.format("%.2f", distancia) + " Km");
                                        availableOrders.setTextSize(16);
                                        availableOrders.setTypeface(null, Typeface.BOLD);
                                        linearLayoutAO.setOrientation(LinearLayout.VERTICAL);
                                        linearLayoutAO.addView(availableOrders);
                                        linearLayoutAO.addView(btn_Assign);
                                        linearLayoutAO.addView(btn_details);
                                        btn_Assign.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                FirebaseFirestore firebaseFirestoreWrite = FirebaseFirestore.getInstance();
                                                firebaseFirestoreWrite.collection("Orders")
                                                        .document(id)
                                                        .update("userId", currentUserId, "status", "R");
                                                finish();
                                                startActivity(getIntent());
                                            }
                                        });

                                    }
                                }
                            }
                        });
            }
        }else if(adapterView.getId() == R.id.spinnerMO){
            if(i == 0){
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("Orders")
                        .whereEqualTo("status", "R")
                        .whereEqualTo("userId",currentUserId)
                        .orderBy("distance", Query.Direction.ASCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    linearLayoutMO.removeAllViews();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String address = document.getString("address");
                                        System.out.println("AQUI: "+address);
                                        String id = document.getId();

                                        lat =  document.getString("latitude");
                                        lng =  document.getString("longitude");
                                        TextView myOrders = new TextView(Dashboard.this);
                                        Button btn_Start = new Button(Dashboard.this);
                                        btn_Start.setText("Start");
                                        Button btn_details = new Button(Dashboard.this);
                                        btn_details.setBackgroundColor(0xff000000);
                                        btn_details.setWidth(50);
                                        btn_details.setHeight(20);
                                        btn_details.setText("details");
                                        btn_details.setTextColor(0xffffffff);

                                        btn_details.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                currOrder = document.getId();
                                                orderLat = document.getString("latitude");
                                                orderLng = document.getString("longitude");
                                                FirebaseFirestore firebaseFirestoreWrite = FirebaseFirestore.getInstance();
                                                firebaseFirestoreWrite.collection("Orders")
                                                        .document(id);
                                                finish();
                                                Intent i = new Intent(Dashboard.this, OrderDetails.class);
                                                startActivity(i);
                                            }
                                        });



                                        coordsDestino = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)); //LatLng coordenadas da order
                                        coordsRestaurante = Dashboard.restaurant;
                                        Location.distanceBetween(coordsDestino.latitude, coordsDestino.longitude, coordsRestaurante.latitude, coordsDestino.longitude, results);
                                        distancia = results[0]/1000;
                                        myOrders.setText("\nAddress: " + address + "\nDistance: "+String.format("%.2f",distancia)+" Km");
                                        myOrders.setTypeface(null, Typeface.BOLD);
                                        myOrders.setTextSize(16);
                                        linearLayoutMO.setOrientation(LinearLayout.VERTICAL);
                                        linearLayoutMO.addView(myOrders);
                                        linearLayoutMO.addView(btn_Start);
                                        linearLayoutMO.addView(btn_details);
                                        btn_Start.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                currOrder = document.getId();
                                                orderLat = document.getString("latitude");
                                                orderLng = document.getString("longitude");
                                                Intent i = new Intent(Dashboard.this, MapaActivity.class);
                                                startActivity(i);
                                            }
                                        });
                                        Button btn_unassign = new Button(Dashboard.this);
                                        btn_unassign.setWidth(50);
                                        btn_unassign.setHeight(20);
                                        btn_unassign.setText("unassign");
                                        btn_unassign.setTextColor(0xffffffff);
                                        linearLayoutMO.addView(btn_unassign);
                                        btn_unassign.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                FirebaseFirestore firebaseFirestoreWrite = FirebaseFirestore.getInstance();
                                                firebaseFirestoreWrite.collection("Orders")
                                                        .document(id)
                                                        .update("userId", "", "status", "R");
                                                finish();
                                                startActivity(getIntent());
                                            }
                                        });
                                    }
                                }
                            }


                        });

            }else if(i == 1){
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("Orders")
                        .whereEqualTo("status", "R")
                        .whereEqualTo("userId",currentUserId)
                        .orderBy("distance", Query.Direction.DESCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    linearLayoutMO.removeAllViews();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String address = document.getString("address");
                                        lat =  document.getString("latitude");
                                        lng =  document.getString("longitude");
                                        String id = document.getId();
                                        TextView myOrders = new TextView(Dashboard.this);
                                        Button btn_Start = new Button(Dashboard.this);
                                        btn_Start.setText("Start");
                                        Button btn_details = new Button(Dashboard.this);
                                        btn_details.setBackgroundColor(0xff000000);
                                        btn_details.setWidth(50);
                                        btn_details.setHeight(20);
                                        btn_details.setText("details");
                                        btn_details.setTextColor(0xffffffff);

                                        btn_details.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                currOrder = document.getId();
                                                orderLat = document.getString("latitude");
                                                orderLng = document.getString("longitude");
                                                FirebaseFirestore firebaseFirestoreWrite = FirebaseFirestore.getInstance();
                                                firebaseFirestoreWrite.collection("Orders")
                                                        .document(id);
                                                finish();
                                                Intent i = new Intent(Dashboard.this, OrderDetails.class);
                                                startActivity(i);

                                            }
                                        });

                                        coordsDestino = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)); //LatLng coordenadas da order
                                        coordsRestaurante = Dashboard.restaurant;
                                        Location.distanceBetween(coordsDestino.latitude, coordsDestino.longitude, coordsRestaurante.latitude, coordsDestino.longitude, results);
                                        distancia = results[0]/1000;
                                        myOrders.setText("\nAddress: " + address + "\nDistance: "+String.format("%.2f",distancia)+" Km");
                                        myOrders.setTypeface(null, Typeface.BOLD);
                                        myOrders.setTextSize(16);
                                        linearLayoutMO.setOrientation(LinearLayout.VERTICAL);
                                        linearLayoutMO.addView(myOrders);
                                        linearLayoutMO.addView(btn_Start);
                                        linearLayoutMO.addView(btn_details);
                                        btn_Start.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                currOrder = document.getId();
                                                orderLat = document.getString("latitude");
                                                orderLng = document.getString("longitude");
                                                Intent i = new Intent(Dashboard.this, MapaActivity.class);
                                                startActivity(i);
                                            }
                                        });
                                        Button btn_unassign = new Button(Dashboard.this);
                                        btn_unassign.setWidth(50);
                                        btn_unassign.setHeight(20);
                                        btn_unassign.setText("unassign");
                                        btn_unassign.setTextColor(0xffffffff);
                                        linearLayoutMO.addView(btn_unassign);
                                        btn_unassign.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                FirebaseFirestore firebaseFirestoreWrite = FirebaseFirestore.getInstance();
                                                firebaseFirestoreWrite.collection("Orders")
                                                        .document(id)
                                                        .update("userId", "", "status", "R");
                                                finish();
                                                startActivity(getIntent());
                                            }
                                        });
                                    }
                                }
                            }

                        });
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void notificationButton(View view){
        Intent i = new Intent(Dashboard.this, NotificationActivity.class);
        startActivity(i);
    }
}
