package pt.pleiria.estg.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    TextView backToRegister;
    Button login;
    Switch switchKeepMeLoggedInLogin;
    SharedPreferences sp;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //see if user was logged in before
        sp = getSharedPreferences("login", MODE_PRIVATE);
        String loggedInUser = sp.getString("userId","");
        if(loggedInUser.isEmpty()){
            System.out.println("logged user is empty");
        }else {//our user was laready logged in and is supposed to be kept logged in
            System.out.println("user already logged in");

            firebaseFirestore.collection("Users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(loggedInUser.equals(document.getId())) {
                                        Dashboard.loggedUser = new User(document.getString("firstName"),
                                                document.getString("lastName"), document.getString("email"),
                                                document.getString("password"), document.getString("phoneNumber"),
                                                document.getString("licensePlate"), document.getString("balance"), document.getString("deletedAt"));
                                        Dashboard.currentUserId = document.getId();

                                        Intent i = new Intent(LoginActivity.this, Dashboard.class);
                                        startActivity(i);
                                    }
                                }
                            }
                        }
                    });
        }

        email = findViewById(R.id.editTextEmailLogin);
        password = findViewById(R.id.editTextPasswordLogin);
        backToRegister = findViewById(R.id.LoginBackToRegister);
        login = findViewById(R.id.buttonLogin);
        switchKeepMeLoggedInLogin = findViewById(R.id.switchKeepMeLoggedInLogin);

    }

    public void loginClick(View v) {
        //TG1 AT2



        if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            ErrorHandling.setErrorMessage(email, "Deve preencher o email");
            ErrorHandling.setErrorMessage(password, "Deve preencher a password");

            return;
        }

        firebaseFirestore.collection("Users")
                .whereEqualTo("deletedAt","")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean email_fault = true;
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String emailUsed = document.getString("email");
                                String passwordUsed = document.getString("password");
                                String firstName = document.getString("firstName");
                                String lastName = document.getString("lastName");
                                String phoneNumber = document.getString("phoneNumber");
                                String licensePlate = document.getString("licensePlate");
                                String  balance = document.getString("balance");
                                String delete = document.getString("deletedAt");

                                if(emailUsed.equals(email.getText().toString()) && delete.equals("")) {
                                    if (passwordUsed.equals(password.getText().toString())) {
                                        Dashboard.loggedUser = new User(firstName, lastName, emailUsed, passwordUsed, phoneNumber, licensePlate, balance, "");
                                        Dashboard.currentUserId = document.getId();
                                        if(switchKeepMeLoggedInLogin.isChecked()){
                                            sp.edit().putString("userId",document.getId()).apply(); //activate the KeepMeLoggedIn
                                        }
                                        Intent i = new Intent(LoginActivity.this, Dashboard.class);
                                        startActivity(i);
                                        email_fault = false;
                                        break;
                                    } else {
                                        System.out.println("password Invalido");
                                        ErrorHandling.setErrorMessage(password, "password Inválida");
                                        email_fault = false;
                                        break;
                                    }
                                }
                            }
                            if(email_fault){
                            ErrorHandling.setErrorMessage(email, "Não existe conta com este email");
                            }

                        }
                    }
                });

    }

    //TG1 AT4
    public void backToRegisterClick(View v){
        Intent i = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(i);
    }

}
