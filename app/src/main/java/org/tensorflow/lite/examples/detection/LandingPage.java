package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LandingPage extends AppCompatActivity {

    Button switchToDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        switchToDetector = findViewById(R.id.LandingEnterButton);

        switchToDetector.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                switchActivities();
            }
        });

    }


    private void switchActivities(){
        Intent switchActivityIntent = new Intent(this, DetectorActivity.class);
        startActivity(switchActivityIntent);


    }
}