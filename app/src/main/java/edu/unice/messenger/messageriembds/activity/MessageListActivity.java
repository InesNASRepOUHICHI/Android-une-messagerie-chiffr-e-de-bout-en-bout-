package edu.unice.messenger.messageriembds.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.unice.messenger.messageriembds.Model.Message;
import edu.unice.messenger.messageriembds.Model.User;
import edu.unice.messenger.messageriembds.R;
import edu.unice.messenger.messageriembds.app.AppConfig;
import edu.unice.messenger.messageriembds.app.AppController;
import edu.unice.messenger.messageriembds.helper.MessageListAdapter;
import edu.unice.messenger.messageriembds.helper.RestClient;
import edu.unice.messenger.messageriembds.helper.SQLiteHandler;

public class MessageListActivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private SQLiteHandler db;
    List<Message> messageList;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        messageList = new ArrayList<Message>();
        messageList.add(new Message("Hello From ADMIN", new User("admin", "admin", "admin")));


        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());


        // Tag used to cancel the request
        String tag_json_req = "req_fetchMessage";

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer "+db.getUserDetails().getAccess_token());

        JsonArrayRequest request = new RestClient().createJsonArrayRequestWithHeaders(Request.Method.GET, AppConfig.URL_FETCH_MESSAGE, null, new Response.Listener<JSONArray >() {

            public void onResponse(JSONArray  response) {
                try {
                    for(int i=0; i<response.length(); i++){
                        // Get current json object
                        JSONObject message = response.getJSONObject(i);

                        // Get the current student (json object) data
                        String id = message.getString("id");
                        messageList.add(new Message(message.getString("msg"), new User(message.getString("author"), null,null)));
                    }

                    mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
                    mMessageAdapter = new MessageListAdapter(MessageListActivity.this, messageList);
                    mMessageRecycler.setLayoutManager(new LinearLayoutManager(MessageListActivity.this));
                    mMessageRecycler.setAdapter(mMessageAdapter);
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Erreur de décodage des messages", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "Erreur de réception des messages: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, headers);



        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request, tag_json_req);




    }
}
