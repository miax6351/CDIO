package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class LandingPage extends AppCompatActivity {

    Button switchToDetector;
    Button popUpButton;
    Button loadGameButton;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        switchToDetector = findViewById(R.id.LandingEnterButton);
        loadGameButton = findViewById(R.id.LandingLoadButton);

        loadGameButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                switchActivities(true);
            }
        });

        switchToDetector.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                CreateRulesDialog();
                //switchActivities();
            }
        });

    }


    private void switchActivities(boolean load){
        Intent switchActivityIntent = new Intent(this, DetectorActivity.class);
        switchActivityIntent.putExtra("load", load);
        startActivity(switchActivityIntent);
    }



    private void CreateRulesDialog(){

        dialogBuilder = new AlertDialog.Builder(this);
        final View rulesPopUpView = getLayoutInflater().inflate(R.layout.popup_rules_window,null);

        popUpButton = (Button) rulesPopUpView.findViewById(R.id.popUpButton);

        dialogBuilder.setView(rulesPopUpView);
        dialog = dialogBuilder.create();
        dialog.show();

        popUpButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                dialog.dismiss();
                switchActivities(false);
            }
        });

    }


}