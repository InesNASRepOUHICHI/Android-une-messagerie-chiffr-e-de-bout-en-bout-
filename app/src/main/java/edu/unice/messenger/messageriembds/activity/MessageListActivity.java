package edu.unice.messenger.messageriembds.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import edu.unice.messenger.messageriembds.Model.Message;
import edu.unice.messenger.messageriembds.Model.User;
import edu.unice.messenger.messageriembds.R;
import edu.unice.messenger.messageriembds.app.AppConfig;
import edu.unice.messenger.messageriembds.app.AppController;
import edu.unice.messenger.messageriembds.helper.ContactUtils;
import edu.unice.messenger.messageriembds.helper.MessageListAdapter;
import edu.unice.messenger.messageriembds.helper.RestClient;
import edu.unice.messenger.messageriembds.helper.SQLiteHandler;

public class MessageListActivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private SQLiteHandler db;
    List<Message> messageList;
    private Button button_chatbox_send;
    private EditText edittext_chatbox;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        edittext_chatbox = (EditText) findViewById(R.id.edittext_chatbox);
        button_chatbox_send = (Button) findViewById(R.id.button_chatbox_send);

        ContactUtils contactUtils = new ContactUtils();
        final String contactNameToDisplayHisMessages = contactUtils.getContactNameToDisplayHisMessages();

        button_chatbox_send.setOnClickListener(new View.OnClickListener() {



            public void onClick(View view) {
                String messageBody = edittext_chatbox.getText().toString();

                // Check for empty data in the form
                if (!messageBody.isEmpty() ) {

                    String tag_json_req = "req_send_message";

                    JSONObject params = new JSONObject();
                    try {
                        params.put("receiver", contactNameToDisplayHisMessages);
                        params.put("message", messageBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "Bearer "+db.getUserDetails().getAccess_token());

                    JsonRequest<JSONObject> request = new RestClient().createJsonRequestWithHeaders(Request.Method.POST, AppConfig.URL_SEND_MESSAGE, params, new Response.Listener<JSONObject>() {

                        public void onResponse(JSONObject response) {
                            try {

                                String author = response.getString("author");

                            } catch (JSONException e) {
                                // JSON error
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Erreur d'envoi du message", Toast.LENGTH_LONG).show();
                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),
                                    "Erreur d'envoi du message: "+error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }, headers);



                    // Adding request to request queue
                    AppController.getInstance().addToRequestQueue(request, tag_json_req);


                    db.saveMessage(messageBody, contactNameToDisplayHisMessages);

                    Intent intent = new Intent(MessageListActivity.this, MessageListActivity.class);
                    startActivity(intent);
                    finish();


                } else {
                    Toast.makeText(getApplicationContext(),
                            "Veuillez rentrez un message SVP!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        messageList = new ArrayList<Message>();
        //messageList.add(new Message("Hello From ADMIN", new User("admin", "admin", "admin")));


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
                        if ((contactNameToDisplayHisMessages.equals("ALL")) ||(contactNameToDisplayHisMessages.equals(message.getString("author")))){
                            Timestamp timestamp = null;
                            try {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                                Date parsedDate = dateFormat.parse(message.getString("dateCreated"));
                                parsedDate = cvtFromGmt(parsedDate);
                                timestamp = new java.sql.Timestamp(parsedDate.getTime());
                            } catch(Exception e) { //this generic but you can control another types of exception
                                // look the origin of excption
                            }
                            messageList.add(new Message(message.getString("msg"), new User(message.getString("author"), null,null), db.getUserDetails(), timestamp ));
                        }
                    }

                    messageList.addAll(db.getMessages());
                    Collections.sort(messageList);

                    messageList.add(new Message("", new User("", "", ""), new User("", "", ""), null));

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


    private  Date cvtFromGmt( Date date ){
        TimeZone tz = TimeZone.getDefault();
        Date ret = new Date( date.getTime() + tz.getRawOffset() );
        // if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
        if ( tz.inDaylightTime( ret )){
            Date dstDate = new Date( ret.getTime() + tz.getDSTSavings() );

            // check to make sure we have not crossed back into standard time
            // this happens when we are on the cusp of DST (7pm the day before the change for PDT)
            if ( tz.inDaylightTime( dstDate )){
                ret = dstDate;
            }
        }
        return ret;
    }
}
