package pt.pleiria.estg.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserPage extends AppCompatActivity {
    TextView name;
    TextView money;
    TextView email;
    TextView telefone;
    TextView licensePlate;
    Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta_pessoal);

        email = findViewById(R.id.NovoTextoEmail1);
        money = findViewById(R.id.TextoValor);
        name = findViewById(R.id.TextoNome);
        telefone = findViewById(R.id.TextoNumero);
        licensePlate = findViewById(R.id.TextoLicensa);
        delete = findViewById(R.id.buttonDelete);

        email.setText(Dashboard.loggedUser.email);
        //money.setText(Index.loggedUser.valor); falta o user ter dinheiro na conta; A java class do user precisa de um campo de dinheiro penso eu
        money.setText(Dashboard.loggedUser.balance

                + "€");

        name.setText(Dashboard.loggedUser.firstName + " "+ Dashboard.loggedUser.lastName);
        telefone.setText(Dashboard.loggedUser.phoneNumber);
        licensePlate.setText(Dashboard.loggedUser.licensePlate);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //quero fazer tudo mas so qd carrega no pop up OK

                AlertDialog.Builder builder = new AlertDialog.Builder(UserPage.this);
                builder.setCancelable(true);
                builder.setTitle("Are you sure you want to delete your account?");
                builder.setMessage("If you delete your account you will have to contact an administrator to get it back.");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
                        sp.edit().putString("userId","").apply(); //disable the KeepMeLoggedIn

                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        FirebaseFirestore firebaseFirestoreWrite = FirebaseFirestore.getInstance();
                        firebaseFirestoreWrite.collection("Users")
                                .document(Dashboard.currentUserId)
                                .update("deletedAt",now);
                        finish();
                        System.out.println("OK");
                        Intent j = new Intent(UserPage.this, MainActivity.class);
                        startActivity(j);
                    }
                });
                builder.show();
            }

        });
    }
    public void accountClickHome(View view){
        Intent i = new Intent(UserPage.this, Dashboard.class);
        startActivity(i);
    }

    public void accountClickStatistics(View view){
        Intent i = new Intent(UserPage.this, Statistics.class);
        startActivity(i);
    }

    public void logoutClick(View view) {
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        sp.edit().putString("userId","").apply(); //disable the KeepMeLoggedIn

        Dashboard.loggedUser = new User();
        Intent i = new Intent(UserPage.this, LoginActivity.class);
        startActivity(i);
    }
}
