package pt.pleiria.estg.myapplication;

import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DirectionsActivity extends AppCompatActivity {
    TextView textViewDir;
    LinearLayout linearLayoutDirections;
    ScrollView scrollviewDirections;
    TextView[] t;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        linearLayoutDirections = findViewById(R.id.linearLayoutDirections);
        scrollviewDirections = findViewById(R.id.scrollViewDirections);
        t = new TextView[200];
        int i = 0;
        for (String s:
             MapaActivity.directionsText) {

            t[i] = new TextView(this);
            t[i].setText((i+1)+" - " + Html.fromHtml(s) + "\n");
            t[i].setTextSize(16);
            linearLayoutDirections.addView(t[i]);
            i++;
        }
    }
}
