package edu.unice.messenger.messageriembds.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.unice.messenger.messageriembds.Model.Message;
import edu.unice.messenger.messageriembds.R;

public class ReceivedMessageHolder extends RecyclerView.ViewHolder {
    TextView messageText, nameText, timeText;

    ReceivedMessageHolder(View itemView) {
        super(itemView);
        messageText = (TextView) itemView.findViewById(R.id.text_message_body);
        nameText = (TextView) itemView.findViewById(R.id.text_message_name);
        timeText = (TextView) itemView.findViewById(R.id.text_message_time);
    }

    void bind(Message message) {
        messageText.setText(message.getMessage());
        nameText.setText(message.getSender().getUsername());
        timeText.setText(message.getDateCreated().toString());
    }
}