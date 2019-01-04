package edu.unice.messenger.messageriembds.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.unice.messenger.messageriembds.Model.Message;
import edu.unice.messenger.messageriembds.Model.User;
import edu.unice.messenger.messageriembds.R;
import edu.unice.messenger.messageriembds.helper.MessageListAdapter;

public class MessageListActivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        List<Message> messageList = new ArrayList<Message>();
        messageList.add(new Message("Hello From ADMIN", new User("admin", "admin", "admin")));

        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);
    }
}
