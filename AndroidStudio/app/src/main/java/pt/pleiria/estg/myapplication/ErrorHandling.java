package pt.pleiria.estg.myapplication;

import android.widget.EditText;

public class ErrorHandling {
    public static void setErrorMessage(EditText textField, String message){
        textField.setError(message);
        textField.setContentDescription(message);
    }
}
