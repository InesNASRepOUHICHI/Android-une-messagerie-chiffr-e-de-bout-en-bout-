package org.mbds.messagerie.mbds_messagerie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Authentification extends Activity  {
    Button buttonLogin,buttonFermer;
    EditText editTextUsername,editTextPassword;

    TextView tx1;
    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);

        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        editTextUsername = (EditText)findViewById(R.id.editTextUsername);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);

        buttonFermer = (Button)findViewById(R.id.buttonFermer);
        tx1 = (TextView)findViewById(R.id.textView3);
        tx1.setVisibility(View.GONE);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextUsername.getText().toString().equals("admin") &&
                        editTextPassword.getText().toString().equals("admin")) {
                    Toast.makeText(getApplicationContext(),
                            "Redirecting...",Toast.LENGTH_SHORT).show();

                    Intent adminPage = new Intent(Authentification.this, AdminActivity.class);
                    startActivity(adminPage);
                } else if (editTextUsername.getText().toString().equals("user") &&
                        editTextPassword.getText().toString().equals("user")) {
                    Toast.makeText(getApplicationContext(),
                            "Redirecting...", Toast.LENGTH_SHORT).show();

                    Intent userPage = new Intent(Authentification.this, UserActivity.class);
                    startActivity(userPage);
                } else{
                    Toast.makeText(getApplicationContext(), "Wrong  Credentials",Toast.LENGTH_SHORT).show();

                            tx1.setVisibility(View.VISIBLE);
                    tx1.setBackgroundColor(Color.RED);
                    counter--;
                    tx1.setText(Integer.toString(counter));

                    if (counter == 0) {
                        buttonLogin.setEnabled(false);
                    }
                }
            }
        });

        buttonFermer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}