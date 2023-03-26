package pt.pleiria.estg.myapplication;

import static pt.pleiria.estg.myapplication.Dashboard.currentUserId;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import pt.pleiria.estg.myapplication.directionHelper.DataParser;
import pt.pleiria.estg.myapplication.directionHelper.FetchURL;
import pt.pleiria.estg.myapplication.directionHelper.TaskLoadedCallback;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    public static String currOrder;
    private Marker marker;
    public static String orderLat;
    public static String orderLng;
    public static String directions;
    public static ArrayList<Direction> directionsRoute = new ArrayList<>();
    LinearLayout linearLayoutInfo, linearLayoutBtns;
    Button btn_Concluir, btn_Cancelar, btn_detalhes, btn_directions;
    public LatLng coordsDestino, coordsRestaurante;
    private Polyline currentPolyline;
    private GoogleMap myMap;
    float[] results = new float[1];
    MarkerOptions markerOptionsRestaurante, markerOptionsDestino;
    float distancia, updatedBalance;
    TextView textViewDirecoes;
    public static LinkedList<String> directionsText = new LinkedList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println(Dashboard.currOrder + "aaaaaaaaaaaa");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myGoogleMap);
        mapFragment.getMapAsync(this);

        linearLayoutInfo = findViewById(R.id.linearLayoutInfo);
        linearLayoutBtns = findViewById(R.id.linearLayoutBotoes);
        btn_Concluir = findViewById(R.id.ButtonConcluir);
        btn_Concluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore firebaseFirestoreWrite = FirebaseFirestore.getInstance();
                firebaseFirestoreWrite.collection("Orders")
                        .document(Dashboard.currOrder)
                        .update("status", "D","time_finished",System.currentTimeMillis());
                finish();

                if(distancia < 3.0){
                    updatedBalance = Float.parseFloat(Dashboard.loggedUser.balance);
                    updatedBalance += 2.0;
                    System.out.println("UPDATED BALANCE: " + updatedBalance);
                } else if (distancia >= 3.0 && distancia < 10.0) {
                    updatedBalance = Float.parseFloat(Dashboard.loggedUser.balance);
                    updatedBalance += 3.0;
                    System.out.println("UPDATED BALANCE: " + updatedBalance);
                } else {
                    updatedBalance = Float.parseFloat(Dashboard.loggedUser.balance);
                    updatedBalance += 4.0;
                    System.out.println("UPDATED BALANCE: " + updatedBalance);
                }

                FirebaseFirestore firebaseFirestoreWriteBalance = FirebaseFirestore.getInstance();
                firebaseFirestoreWriteBalance.collection("Users")
                        .document(currentUserId)
                        .update("balance", Float.toString(updatedBalance));
                finish();

                Dashboard.loggedUser.balance = String.valueOf(updatedBalance);
                Dashboard.currOrder = "";
                Intent i = new Intent(MapaActivity.this, Dashboard.class);
                startActivity(i);
            }
        });


        btn_Cancelar = findViewById(R.id.ButtonCancelar);
        btn_Cancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                EditText editText = new EditText(MapaActivity.this);
                AlertDialog dialog = new AlertDialog.Builder(MapaActivity.this)
                        .setTitle("Motivo por cancelar: ")
                        .setView(editText)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String editTextInput = editText.getText().toString();
                                Log.d("onclick","editext value is: "+ editTextInput);
                                FirebaseFirestore firebaseFirestoreWrite = FirebaseFirestore.getInstance();
                                firebaseFirestoreWrite.collection("Orders")
                                        .document(Dashboard.currOrder)
                                        .update("status", "C","time_finished",System.currentTimeMillis());
                                finish();
                                Dashboard.currOrder = "";
                                Intent j = new Intent(MapaActivity.this, Dashboard.class);
                                startActivity(j);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });

        btn_detalhes = findViewById(R.id.ButtonDetalhes);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Orders")
                .whereEqualTo("userId", currentUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                btn_detalhes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        currOrder = document.getId();
                                        orderLat = document.getString("latitude");
                                        orderLng = document.getString("longitude");
                                        Intent i = new Intent(MapaActivity.this, OrderDetails.class);
                                        startActivity(i);
                                    }
                                });
                            }
                        }
                    }

                });


        TextView currOrder = new TextView(MapaActivity.this);
        FirebaseFirestore firebaseFirestoreDetalhes = FirebaseFirestore.getInstance();
        firebaseFirestoreDetalhes.collection("Orders").document(Dashboard.currOrder).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                linearLayoutInfo.removeAllViews();
                                String orderLatitude = document.getString("latitude"); //ir buscar field latitude da order
                                String orderLongitude = document.getString("longitude"); //ir buscar field longitude da order
                                coordsDestino = new LatLng(Double.parseDouble(orderLatitude), Double.parseDouble(orderLongitude)); //LatLng coordenadas da order
                                coordsRestaurante = Dashboard.restaurant;
                                Location.distanceBetween(coordsDestino.latitude, coordsDestino.longitude, coordsRestaurante.latitude, coordsDestino.longitude, results);
                                distancia = results[0] / 1000;
                                currOrder.setText("Morada: " + document.get("address").toString() + "\nDistância: " + String.format("%.2f", distancia) + " Km");
                                currOrder.setTextSize(16);
                                linearLayoutInfo.addView(currOrder);
                            } else {
                                linearLayoutInfo.removeAllViews();
                                currOrder.setText("Error. Try again");
                                currOrder.setTextSize(16);
                                linearLayoutInfo.addView(currOrder);
                            }
                        }
                    }
                });
        textViewDirecoes = new TextView(MapaActivity.this);
        for (Direction d :
                directionsRoute) {
            showDirections(d.getHtml_instructions().toString(), d.getDistance().toString());
        }

        btn_directions = findViewById(R.id.ButtonDirections);
        btn_directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MapaActivity.this, DirectionsActivity.class);
                startActivity(i); //Alterar para DirectionsActivity
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        myMap = googleMap;
        markerOptionsRestaurante = new MarkerOptions();
        markerOptionsDestino = new MarkerOptions();

        markerOptionsRestaurante.position(Dashboard.restaurant);
        LatLng destCoords = new LatLng(Double.parseDouble(Dashboard.orderLat), Double.parseDouble(Dashboard.orderLng));
        markerOptionsDestino.position(destCoords);

        markerOptionsDestino.title("Your destination");
        markerOptionsRestaurante.title("FasTuga");
        String url = getUrl(markerOptionsRestaurante.getPosition(), markerOptionsDestino.getPosition(), "driving");
        new FetchURL(MapaActivity.this).execute(url, "driving");
        myMap.addMarker(markerOptionsRestaurante);
        myMap.addMarker(markerOptionsDestino);

        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Dashboard.restaurant, 16));

    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        String mode = "mode=" + directionMode;
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyATsmlGDvg3-21QbvxyxfFJrwOUEEns2bk";
        return url;
    }


    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null) {
            currentPolyline.remove();
        }

        currentPolyline = myMap.addPolyline((PolylineOptions) values[0]);
        directions = String.valueOf(Html.fromHtml(DataParser.jDirections.toString()));

        for (Direction d :
                directionsRoute) {
            simulateDriver(d.getHtml_instructions().toString(), d.getDistance().toString(), Double.parseDouble(d.getLat().toString()), Double.parseDouble(d.getLng().toString()));
            showDirections(d.getHtml_instructions().toString(), d.getDistance().toString());
        }
    }


    public void simulateDriver(String html_instructions, String distance, Double lat, Double lng) {

        MarkerOptions markerDriver = new MarkerOptions();
        markerDriver.position(new LatLng(lat, lng));
        markerDriver.title(Html.fromHtml(html_instructions).toString());
        marker = myMap.addMarker(markerDriver);
        marker.setPosition(new LatLng(lat, lng));
    }

    public void showDirections(String html_instructions, String distance) {
        directionsText.add("In " + distance + " " + html_instructions );
    }
}