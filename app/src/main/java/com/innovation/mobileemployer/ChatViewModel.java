package com.innovation.mobileemployer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ChatViewModel extends ViewModel {
    private final MutableLiveData<List<ChatMessage>> chatMessagesLiveData = new MutableLiveData<>();

    public MutableLiveData<List<ChatMessage>> getChatMessagesLiveData() {
        return chatMessagesLiveData;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        chatMessagesLiveData.setValue(chatMessages);
    }
}
