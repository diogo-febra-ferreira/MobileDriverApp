package pt.pleiria.estg.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText passwordConfirm;
    Button buttonRegister;
    TextView fn_tv;
    TextView ln_tv;
    TextView em_tv;
    TextView p_tv;
    TextView cp_tv;
    EditText phoneNumber;
    EditText licensePlate;
    boolean isEmailValid;
    private boolean isPhoneValid;
    private boolean isLicensePlateValid;
    protected String balance;

    SharedPreferences sp;   //object used for keep me logged in
    Switch switchKeepMeLoggedInRegister;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //see if user was logged in before
        sp = getSharedPreferences("login", MODE_PRIVATE);
        String loggedInUser = sp.getString("userId","");
        if(loggedInUser.isEmpty()){
            System.out.println("logged user is empty");
        }else {//our user was laready logged in and is supposed to be kept logged in

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

                                        Intent i = new Intent(MainActivity.this, Dashboard.class);
                                        startActivity(i);
                                    }
                                }
                            }
                        }
                    });

        }

        switchKeepMeLoggedInRegister = findViewById(R.id.switchKeepMeLoggedInRegister);
        firstName = findViewById(R.id.editTextFirstName);
        lastName = findViewById(R.id.editTextLastName);
        email = findViewById(R.id.editTextEmailAddress);
        password = findViewById(R.id.editTextPassword);
        passwordConfirm = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        fn_tv = findViewById(R.id.textViewFirstName);
        ln_tv = findViewById(R.id.textViewLastName);
        em_tv = findViewById(R.id.textViewEmail);
        p_tv = findViewById(R.id.textViewPassword);
        cp_tv = findViewById(R.id.textViewConfirmPassword);
        phoneNumber = findViewById(R.id.editTextPhone);
        licensePlate = findViewById(R.id.editTextLicensePlate);
        isEmailValid = true;
        isPhoneValid = true;
        isLicensePlateValid = true;


    }

    public void registerClick(View v) {
        if (firstName.getText().toString().isEmpty() && lastName.getText().toString().isEmpty() && email.getText().toString().isEmpty() && password.getText().toString().isEmpty() && passwordConfirm.getText().toString().isEmpty() && isEmpty(phoneNumber) && isEmpty(licensePlate)) {
            ErrorHandling.setErrorMessage(firstName, "Preencha o seu nome");
            ErrorHandling.setErrorMessage(lastName, "Preencha o seu último nome");
            ErrorHandling.setErrorMessage(email, "preencha com o seu email");
            ErrorHandling.setErrorMessage(password, "Preencha com a sua password");
            ErrorHandling.setErrorMessage(passwordConfirm, "Confirme a sua password");
            ErrorHandling.setErrorMessage(licensePlate, "Preencha com a sua matrícula");
            ErrorHandling.setErrorMessage(phoneNumber, "Preencha com o seu número de telefone");

        }else if (firstName.getText().toString().isEmpty()) {
            ErrorHandling.setErrorMessage(firstName, "Preencha o seu nome");
            return;
        } else if (lastName.getText().toString().isEmpty()) {
            ErrorHandling.setErrorMessage(lastName, "Preencha o seu último nome");
            return;
        } else if (email.getText().toString().isEmpty()) {
            ErrorHandling.setErrorMessage(email, "Preencha com o seu email");
            return;
        } else if (password.getText().toString().isEmpty()) {
            ErrorHandling.setErrorMessage(password, "Preencha com a sua password");
            return;
        } else if (passwordConfirm.getText().toString().isEmpty()) {
            ErrorHandling.setErrorMessage(passwordConfirm, "Confirme a sua password");
            return;
        } else if (licensePlate.getText().toString().isEmpty()) {
            ErrorHandling.setErrorMessage(licensePlate, "Preencha com a sua matrícula");
            return;
        } else if (phoneNumber.getText().toString().isEmpty()) {
            ErrorHandling.setErrorMessage(phoneNumber, "Preencha com o seu número de telefone");
            return;
        }
        else {
            if (!isEmailValid(email.getText().toString())) {
                ErrorHandling.setErrorMessage(email, "Email inválido");

                return;
            }else if(!isNameValid(firstName.getText().toString())){
                ErrorHandling.setErrorMessage(firstName, "Nome Inválido");

                return;
            }else if(!isNameValid(lastName.getText().toString())){
                ErrorHandling.setErrorMessage(lastName, "Último Nome Inválido");

                return;
            }
            else if (!passwordConfirm.getText().toString().equals(password.getText().toString())) {
                ErrorHandling.setErrorMessage(passwordConfirm, "Passwords não coincidem");

                return;
            }else if (!isLicensePlateValid(licensePlate.getText().toString())) {
                ErrorHandling.setErrorMessage(licensePlate, "Formato Inválido E.g: A1-3B-2C");
                return;
            }
        }
        firebaseFirestore.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String userId = null;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String emailUsed = document.getString("email");
                                String phoneUsed = document.getString("phoneNumber");
                                balance = "0";
                                String licensePlateUsed = document.getString("licensePlate");
                                userId = document.getId();
                                if(emailUsed.equals(email.getText().toString())) {
                                    ErrorHandling.setErrorMessage(email, "Já existe um user com este email");

                                    isEmailValid = false;
                                }
                                if(phoneUsed.equals(phoneNumber.getText().toString())){
                                    ErrorHandling.setErrorMessage(phoneNumber, "Já existe um user com este número de telefone");
                                    isPhoneValid = false;
                                }
                                if(licensePlateUsed.equals(licensePlate.getText().toString())){
                                    ErrorHandling.setErrorMessage(licensePlate, "Já existe um user com esta matrícula");
                                    isLicensePlateValid = false;

                                }
                            }

                            if(isEmailValid && isPhoneValid && isLicensePlateValid){
                                User newUser = new User(firstName.getText().toString(), lastName.getText().toString(), email.getText().toString(), password.getText().toString(), phoneNumber.getText().toString(), licensePlate.getText().toString(), balance, "");
                                createUser(newUser);
                                Dashboard.loggedUser = newUser;
                                Dashboard.currentUserId = userId;

                                if(switchKeepMeLoggedInRegister.isChecked()){
                                    sp.edit().putString("userId",userId).apply(); //activate the KeepMeLoggedIn
                                }

                                Intent i = new Intent(MainActivity.this, Dashboard.class);
                                startActivity(i);
                            }else {
                                isEmailValid = true;
                                isPhoneValid = true;
                                isLicensePlateValid = true;
                            }
                        }
                    }
                });




    }

    void createUser(User user){
        firebaseFirestore.collection("Users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public boolean isPasswordValid(String password, String passwordConfirm) {
        if (!passwordConfirm.equals(password)) {
            return false;
        }
        return true;
    }

    public static boolean isEmailValid(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public static boolean isNameValid(String name) {
        String regEx = "\b([A-ZÀ-ÿ][-,a-z. ']+[ ]*)+";
        Pattern pattern = Pattern.compile(regEx, Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher((CharSequence) name);

        if (!matcher.matches()) {
            return true;
        }
        return false;
    }

    public static boolean isLicensePlateValid(String plate) {
        String regEx = "^[A-Z0-9]{2}-[A-Z0-9]{2}-[A-Z0-9]{2}$";
        Pattern pattern = Pattern.compile(regEx, Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher((CharSequence) plate);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }


    public void signInClick(View view) {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }
}