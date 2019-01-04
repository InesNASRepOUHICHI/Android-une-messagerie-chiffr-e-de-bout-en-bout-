package edu.unice.messenger.messageriembds.activity;

import edu.unice.messenger.messageriembds.Model.User;
import edu.unice.messenger.messageriembds.helper.SQLiteHandler;
import edu.unice.messenger.messageriembds.helper.SessionManager;

import edu.unice.messenger.messageriembds.R;
 
import java.util.HashMap;
 
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
 
    private EditText txtPassword;
    private EditText txtUsername;
    private Button btnLogout;
    private Button btnListeMessages;
    private Button btnListeContacts;
    private Button btnAjouterContact;
 
    private SQLiteHandler db;
    private SessionManager session;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        txtUsername = (EditText) findViewById(R.id.username);
        txtPassword = (EditText) findViewById(R.id.password);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnListeMessages = (Button) findViewById(R.id.btnListeMessages);
        btnListeContacts = (Button) findViewById(R.id.btnListeContacts);
        btnAjouterContact = (Button) findViewById(R.id.btnAjouterContact);

 
        // session manager
        session = new SessionManager(getApplicationContext());
 
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Fetching user details from sqlite
        User user = db.getUserDetails();
 
        String password = user.getPassword();
        String username = user.getUsername();
        String access_token = user.getAccess_token();
 
        // Displaying the user details on the screen
        txtPassword.setText(password);
        txtUsername.setText(username);

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
        btnListeMessages.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ListeMessages();
            }
        });
        btnListeContacts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ListeContacts();
            }
        });
        btnAjouterContact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AjouterContact();
            }
        });
    }
 
    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);
 
        db.deleteUsers();
 
        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void ListeMessages() {
        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, MessageListActivity.class);
        startActivity(intent);
        finish();
    }

    private void ListeContacts() {
        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, ListeContactsActivity.class);
        startActivity(intent);
        finish();
    }

    private void AjouterContact() {
        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, AjouterContactActivity.class);
        startActivity(intent);
        finish();
    }

}