package edu.unice.messenger.messageriembds.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.unice.messenger.messageriembds.R;
import edu.unice.messenger.messageriembds.helper.ContactUtils;
import edu.unice.messenger.messageriembds.helper.SQLiteHandler;

public class ListeContactsActivity extends Activity {

    private SQLiteHandler db;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_contacts);


        context = this;
        ListView lstview = (ListView) findViewById(R.id.listv);
        lstview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "An item of the ListView is clicked.", Toast.LENGTH_LONG).show();
            }
        });

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        List<String> contacts = db.getContacts();

        LstViewAdapter adapter = new LstViewAdapter(this, R.layout.list_item, R.id.txt, contacts.toArray(new String[0]));
        // Bind data to the ListView
        lstview.setAdapter(adapter);


    }

    public void clickMe(View view) {
        Button bt = (Button) view;
        Toast.makeText(this, "Messages de " + bt.getText().toString(), Toast.LENGTH_LONG).show();

        ContactUtils contactUtils = new ContactUtils();
        contactUtils.setContactNameToDisplayHisMessages(bt.getText().toString());

        // Launching the login activity
        Intent intent = new Intent(ListeContactsActivity.this, MessageListActivity.class);
        startActivity(intent);
        finish();
    }

}

