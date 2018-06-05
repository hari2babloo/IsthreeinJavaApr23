package com.a3x3conect.mobile.isthreeinjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.hari.isthreeinjava.R;

public class Feedback extends AppCompatActivity {

    Button cancel;
    ViewGroup transitionsContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        cancel = (Button)findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(Feedback.this, "CLicked cancel", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
