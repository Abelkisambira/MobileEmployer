package com.innovation.mobileemployer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.innovation.mobileemployer.ChatMessage;
import com.innovation.mobileemployer.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> chatMessages;
    private String employerID;

    public ChatAdapter(List<ChatMessage> chatMessages, String employerID) {
        this.chatMessages = chatMessages;
        this.employerID = employerID;
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
//        holder.timestampTextView.setText(ChatViewHolder.formatTimestamp(chatMessage.getTimestamp()));
//        holder.timestampTextView.setText(formatTimestamp(chatMessage.getTimestamp()));
        if (!chatMessage.isSentByUser()) {
            // Add any other customization as needed
            holder.senderMessageTextView.setVisibility(View.GONE);
            holder.receiverMessageTextView.setVisibility(View.VISIBLE);
            holder.receiverTextView.setVisibility(View.VISIBLE);
            // Customize the appearance of the professional's message
            holder.receiverTextView.setText(chatMessage.getReceiverName());
            holder.receiverMessageTextView.setText(chatMessage.getMessage());
//            holder.receiverMessageTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.cyan));

        } else {
            // Add any other customization as needed
            holder.receiverMessageTextView.setVisibility(View.GONE);
            holder.senderMessageTextView.setVisibility(View.VISIBLE);
            holder.receiverTextView.setVisibility(View.GONE);

            // Customize the appearance of the employer's message
            holder.senderMessageTextView.setText(chatMessage.getMessage());
//            holder.senderMessageTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.lightgrey));

        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView senderTextView;
        private TextView receiverTextView;
        private TextView timestampTextView;
        private TextView senderMessageTextView;
        private TextView receiverMessageTextView;
//        private LinearLayout senderMessageContainer;
//        private LinearLayout receiverMessageContainer;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            receiverTextView = itemView.findViewById(R.id.receiverTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
            senderMessageTextView = itemView.findViewById(R.id.senderMessageTextView);
            receiverMessageTextView = itemView.findViewById(R.id.receiverMessageTextView);
//            senderMessageContainer = itemView.findViewById(R.id.senderMessageContainer);
//            receiverMessageContainer = itemView.findViewById(R.id.receiverMessageContainer);
        }



//        public void bind(ChatMessage chatMessage) {
//            // Set sender's name (you can modify this based on your implementation)
//
//            // Set receiver's name
//            receiverTextView.setText(chatMessage.getReceiverName());
//
//            // Set sender's message text
//            senderMessageTextView.setText(chatMessage.getMessage());
//
//            // Set receiver's message text
//            receiverMessageTextView.setText(chatMessage.getMessage());
//            // Get the current user's ID from Firebase Authentication
//            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//
//
//            // Set visibility based on the sender
//            if (chatMessage.getSenderID().equals(currentUser.getUid())) {
//                senderMessageContainer.setVisibility(View.VISIBLE);
//                receiverMessageContainer.setVisibility(View.GONE);
//            } else {
//                senderMessageContainer.setVisibility(View.GONE);
//                receiverMessageContainer.setVisibility(View.VISIBLE);
//            }
//        }
        }
//    private String formatTimestamp(Date timestamp) {
//        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
//        return sdf.format(timestamp);
//    }
    }
