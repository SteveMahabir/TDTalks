package com.andriod.steve.tdtalks;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class MessageReceiver extends BroadcastReceiver {
    //MainActivity m;

    private DBAdapter db;

    @Override
    public void onReceive(Context context, Intent intent) {
        /*// Create service Intent
        Intent serviceIntent = new Intent(context, TDTalksService.class);
        // Start service
        context.startService(serviceIntent);

        //Start App On Boot Start Up
        Intent App = new Intent(context, MainActivity.class);
        App.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(App);*/

        Bundle bundle = intent.getExtras();
        SmsMessage[] recievedMsgs = null;
        String str = "";
        String nmbr = "";
        if (bundle != null)
        {

            Object[] pdus = (Object[]) bundle.get("pdus");
            recievedMsgs = new SmsMessage[pdus.length];
            TextMessage newMessage = null;
            String phoneNumber = "";
            String msgDate = "";
            int threadId = -1;

            String msg = "";
            for(int i = 0 ; i < pdus.length; i++)
            {
                recievedMsgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                if (i == 0)
                {
                    phoneNumber = recievedMsgs[i].getOriginatingAddress();
                    msgDate = Resources.FormattedDate(recievedMsgs[i].getTimestampMillis());
                    if(ChatActivity.GetConversationPhoneNumber() != null && ChatActivity.GetConversationPhoneNumber().replaceAll("[()+-]", "").equalsIgnoreCase(phoneNumber.replaceAll("[()+-]", "")))
                    {
                        threadId = ChatActivity.GetThreadId();
                    }
                }
                msg += recievedMsgs[i].getMessageBody();

            }
            if (!msg.equals("")) {

                // Check the message to see if it is hidden data
                newMessage = new TextMessage(true,
                        msg,
                        phoneNumber,
                        msgDate,
                        threadId,
                        -1
                );

            }

            if (newMessage != null) {
                if(threadId >= 0) {
                    //MainActivity.chatMessageList.add(new TextMessage(true, str, nmbr,"", 0, 0));
                    ChatActivity.chatMsgs.add(newMessage);
                    ChatActivity.update();
                }
                showNotification(context, newMessage);

                //Toast.makeText(context, str, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void showNotification(Context context, TextMessage newMessage){

        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0

        String msgText = newMessage.getText().length() > 30 ? newMessage.getText().substring(0, 30) + "..." : newMessage.getText();
        Notification mNotification = new Notification.Builder(context)

                .setContentTitle(newMessage.getNumber())
                .setContentText(msgText)
                .setSmallIcon(R.drawable.whiteskul)
                        //.setVibrate(temp)
                //.setColor(Color.BLACK)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon))
                .setContentIntent(pIntent)
                .setSound(soundUri)
                .addAction(0, "View", pIntent)
                .addAction(0, "Remind", pIntent)

                .build();


        int mId = 1;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        // If you want to hide the notification after it was selected, do the code below
        // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        // mId allows you to update the notification later on.
        notificationManager.notify(mId, mNotification);
        //notificationManager.notify(0, mNotification);
    }

}
