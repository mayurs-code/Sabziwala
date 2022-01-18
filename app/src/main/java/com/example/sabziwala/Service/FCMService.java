package com.example.sabziwala.Service;


import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        System.out.println("MESSAGE_RECIEVED " + remoteMessage.getNotification());
        System.out.println("MESSAGE_RECIEVED " + remoteMessage.getNotification().getTitle());
        System.out.println("MESSAGE_RECIEVED " + remoteMessage.getNotification().getBody());

    }
}
