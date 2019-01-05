package edu.unice.messenger.messageriembds.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.unice.messenger.messageriembds.Model.User;
import edu.unice.messenger.messageriembds.R;
import edu.unice.messenger.messageriembds.app.AppConfig;
import edu.unice.messenger.messageriembds.app.AppController;
import edu.unice.messenger.messageriembds.helper.KeysGenerator;
import edu.unice.messenger.messageriembds.helper.RestClient;
import edu.unice.messenger.messageriembds.helper.SQLiteHandler;

public class AjouterContactActivity extends AppCompatActivity {

    private EditText inputUsername;
    private Button btnAjouterContact;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_contact);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        inputUsername = (EditText) findViewById(R.id.username);
        btnAjouterContact = (Button) findViewById(R.id.btnAjouterContact);

        btnAjouterContact.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String username = inputUsername.getText().toString().trim();

                // Check for empty data in the form
                if (!username.isEmpty()) {
                    // login user
                    EnvoyerInvitationContact(username);
                    db.addContact(username);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Veuillez rentrez un username SVP!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

    }


    private void EnvoyerInvitationContact(final String username) {
        String AlicePublicKey = new KeysGenerator().getPublicAsymmetricAlgorithmRSA();
        String messageInvitation = "Alice[|]PING[|]" + AlicePublicKey;

        // Tag used to cancel the request
        String tag_json_req = "req_create_contact";

        JSONObject params = new JSONObject();
        try {
            params.put("receiver", username);
            params.put("message", messageInvitation);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer "+db.getUserDetails().getAccess_token());

        JsonRequest<JSONObject> request = new RestClient().createJsonRequestWithHeaders(Request.Method.POST, AppConfig.URL_SEND_MESSAGE, params, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject response) {
                try {

                    String id = response.getString("id");

                    Toast.makeText(getApplicationContext(), "Invitation au nv contact bien envoyée", Toast.LENGTH_LONG).show();
                    // Launch main activity
                    Intent intent = new Intent(AjouterContactActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Erreur d'envoi de l'invitation au nv contact", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "Erreur d'envoi de l'invitation au nv contact: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, headers);



        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request, tag_json_req);
    }
}
