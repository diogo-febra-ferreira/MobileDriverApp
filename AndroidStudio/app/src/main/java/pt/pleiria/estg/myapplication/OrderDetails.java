package pt.pleiria.estg.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import pt.pleiria.estg.myapplication.directionHelper.FetchURL;
import pt.pleiria.estg.myapplication.directionHelper.TaskLoadedCallback;

public class OrderDetails extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    TextView nome;
    TextView montante;
    LinearLayout itens;
    TextView morada;
    String customerId;
    private GoogleMap myMap;
    MarkerOptions markerOptionsRestaurante,markerOptionsDestino;
    private Polyline currentPolyline;
    public LatLng coordsDestino, coordsRestaurante;
    float[] results = new float[1];
    float distancia;
    TextView currOrder;
    TextView quantidade;
    Button home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myGoogleMap);
        mapFragment.getMapAsync(this);

        nome = findViewById(R.id.NameEdit);
        montante = findViewById(R.id.MontanteEdit);
        itens = findViewById(R.id.LinearLayoutItens);
        morada = findViewById(R.id.AddressEdit);
        currOrder = findViewById(R.id.DistanciaEdit); //isto pode tar a dar erro
        quantidade = findViewById(R.id.ItensText);
        home = findViewById(R.id.ButtonHomeO);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Orders").document(Dashboard.currOrder).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();

                            if (document.exists()) {

                                TextView availableOrders = new TextView(OrderDetails.this);

                                List<String> produtos = (List<String>) document.get("itens");
                                String produto = produtos.toString();
                                produto = produtos.toString().replace("["," ");

                                produto = produto.toString().replace("]","");

                                produto = produto.toString().replace(",","\n");
                                availableOrders.setText(produto);
                                availableOrders.setTextSize(16);

                                customerId = document.getString("customer_id");
                                quantidade.setText("Order: \nQuantity(x" + produtos.toArray().length + ")");

                                FirebaseFirestore firebaseFirestoreClient = FirebaseFirestore.getInstance();

                                firebaseFirestoreClient.collection("Clients").document(customerId).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        String name = document.getString("Name");
                                                        nome.setText(name);
                                                        nome.setTextSize(16);
                                                    }
                                                }
                                            }
                                        });
                                String orderLatitude = document.getString("latitude"); //ir buscar field latitude da order
                                String orderLongitude = document.getString("longitude"); //ir buscar field longitude da order
                                coordsDestino = new LatLng(Double.parseDouble(orderLatitude), Double.parseDouble(orderLongitude)); //LatLng coordenadas da order
                                coordsRestaurante = Dashboard.restaurant;
                                Location.distanceBetween(coordsDestino.latitude, coordsDestino.longitude, coordsRestaurante.latitude, coordsDestino.longitude, results);
                                distancia = results[0]/1000;
                                currOrder.setText(String.format("%.2f",distancia) + " Km");
                                currOrder.setTextSize(16);

                                String i = document.get("total_price").toString();
                                if(i == null){
                                    montante.setText("0€");
                                }else {
                                    montante.setText(i +"€");

                                }
                                morada.setText(document.getString("address"));
                                montante.setTextSize(16);
                                morada.setTextSize(16);
                                itens.addView(availableOrders);
                            }
                        }
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

        markerOptionsDestino.title("Your local");
        markerOptionsRestaurante.title("FasTuga");

        System.out.println("Restaurante: "+markerOptionsRestaurante.getPosition() + "\nDestino: "+markerOptionsDestino.getPosition());
        String url = getUrl(markerOptionsRestaurante.getPosition(), markerOptionsDestino.getPosition(), "driving");
        new FetchURL(OrderDetails.this).execute(url, "driving");
        myMap.addMarker(markerOptionsRestaurante);
        myMap.addMarker(markerOptionsDestino);

        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Dashboard.restaurant, 12));

    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode){
        System.out.println("dentro do getURL");
        String str_origin = "origin="+ origin.latitude+","+origin.longitude;
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        String mode = "mode="+directionMode;
        String parameters = str_origin+"&"+str_dest+"&"+mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters+"&key=AIzaSyATsmlGDvg3-21QbvxyxfFJrwOUEEns2bk";
        return url;
    }


    @Override
    public void onTaskDone(Object... values) {
        if(currentPolyline != null){
            currentPolyline.remove();
        }
        currentPolyline = myMap.addPolyline((PolylineOptions) values[0]);
    }

    public void logoutClick(View view) {

        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        sp.edit().putString("userId","").apply(); //disable the KeepMeLoggedIn

        Dashboard.loggedUser = new User();
        Intent i = new Intent(OrderDetails.this, LoginActivity.class);
        startActivity(i);
    }

    public void homeClick(View view){
        Intent i = new Intent(OrderDetails.this, Dashboard.class);
        startActivity(i);
    }

}
