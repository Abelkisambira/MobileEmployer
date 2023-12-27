package com.innovation.mobileemployer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.innovation.mobileemployer.ChatMessage;
import com.innovation.mobileemployer.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> chatMessages;
    private String senderId;

    public ChatAdapter(List<ChatMessage> chatMessages, String senderId) {
        this.chatMessages = chatMessages;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        holder.bind(chatMessage);
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView senderTextView;
        private TextView receiverTextView;
        private TextView senderMessageTextView;
        private TextView receiverMessageTextView;
        private LinearLayout senderMessageContainer;
        private LinearLayout receiverMessageContainer;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            receiverTextView=itemView.findViewById(R.id.receiverTextView);
            senderMessageTextView = itemView.findViewById(R.id.senderMessageTextView);
            receiverMessageTextView = itemView.findViewById(R.id.receiverMessageTextView);
            senderMessageContainer = itemView.findViewById(R.id.senderMessageContainer);
            receiverMessageContainer = itemView.findViewById(R.id.receiverMessageContainer);
        }

        public void bind(ChatMessage chatMessage) {
            // Set sender's name (you can modify this based on your implementation)

            // Set receiver's name
            receiverTextView.setText(chatMessage.getReceiverName());

            // Set sender's message text
            senderMessageTextView.setText(chatMessage.getMessage());

            // Set receiver's message text
            receiverMessageTextView.setText(chatMessage.getMessage());
            // Get the current user's ID from Firebase Authentication
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


            // Set visibility based on the sender
            if (chatMessage.getSenderID().equals(currentUser.getUid())) {
                senderMessageContainer.setVisibility(View.VISIBLE);
                receiverMessageContainer.setVisibility(View.GONE);
            } else {
                senderMessageContainer.setVisibility(View.GONE);
                receiverMessageContainer.setVisibility(View.VISIBLE);
            }
        }
    }
}
