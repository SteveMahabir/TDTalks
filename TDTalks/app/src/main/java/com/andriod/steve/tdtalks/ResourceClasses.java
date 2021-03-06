package com.andriod.steve.tdtalks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Steve on 10/21/15.
 * This is a place to stuff a bunch of the extras, mostly Listeners and things that we could take out of other code areas
 */

final class Resources {

    private Resources(){
    }


    public static String FormattedDate(long timeMillis)
    {
        Date date = new Date(timeMillis);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        return format.format(date);
    }
}

//Class added by Steve
// Touch listener class for the Press and hold picture event in chat area
class touchListener_Image implements View.OnTouchListener{

    // Data Members
    private TextView textview;
    private TextMessage textMessage;
    private TDTalksApplication globals;
    private Context context;

    public touchListener_Image( TextView tv, TextMessage tm, Context con)
    {
        textview = tv;
        textMessage = tm;
        globals = MainActivity.globals;
        context = con;

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean retVal = false;

        // Assume all messages are encrypted then
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                v.setPressed(true);
                // Start Showing the Message
                textview.setVisibility(View.VISIBLE);
                retVal = true;
                break;
            case MotionEvent.ACTION_CANCEL:
                v.setPressed(false);
                // Stop action ...
                textview.setText("");
                textview.setVisibility(View.INVISIBLE);
                retVal = true;
                break;
            case  MotionEvent.ACTION_UP:
                v.setPressed(false);
                // Stop action ...
                textview.setText("");
                textview.setVisibility(View.INVISIBLE);
                retVal = true;
                break;
        }
        return retVal;
    }
}

//Class added by Kevin
// Touch listener class for the Press and hold picture event in chat area
class touchListener_Conversation implements AdapterView.OnItemClickListener{

    // Data Members
    private Context context;
    private String phoneNumber;
    ArrayList<Conversation> smsConversationList;

    public touchListener_Conversation( ArrayList<Conversation> _smsConversationList, String phoneno, Context con)
    {
        smsConversationList = _smsConversationList;
        context = con;
        phoneNumber = phoneno;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        //String phoneNo = numbersOnly.get(position).getNumber();
        String phoneNo = smsConversationList.get(position).getPhoneNumber();
        int threadid = smsConversationList.get(position).getThreadId();
        //show what number was selected
        Toast.makeText(context, phoneNo, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("phoneNo", phoneNo);
        intent.putExtra("MyPhoneno", phoneNumber);
        intent.putExtra("threadid", threadid);
        //intent.putExtra("UserPrivateKey" , encryption.privateKey);
        //DataWrapper dw = new DataWrapper(chatMessageList);
        //intent.putExtra("data", dw);
        context.startActivity(intent);

    }

}

class touchListener_Contact implements AdapterView.OnTouchListener {

    // Data Members
    public Context context;
    private String name;
    private String friendsNumber;
    private int threadid;
    private String myPhoneNumber;
    private boolean openConversation;


    public touchListener_Contact(String _name, String phoneno, int threadno, String myPhoneNo, Context con) {
        name = _name;
        context = con;
        threadid = threadno;
        friendsNumber = phoneno;
        openConversation = true;
        myPhoneNumber = myPhoneNo;
    }

    final Handler handler = new Handler();

    Runnable openContactManager = new Runnable() {
        public void run() {
            openConversation = false;
            Intent intent = new Intent(context, ContactsActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("phoneno", friendsNumber);
            intent.putExtra("threadid", threadid);
            //intent.putExtra("UserPrivateKey" , encryption.privateKey);
            context.startActivity(intent);
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean retVal = false;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            // Wait one second
            case MotionEvent.ACTION_DOWN:
                handler.postDelayed(openContactManager, 1000);
                openConversation = true;
                retVal = true;
                break;

            // Action Cancelled and Action Move
            case MotionEvent.ACTION_CANCEL:
                handler.removeCallbacks(openContactManager);
                openConversation = false;
                retVal = true;
                break;

            // Stop take some time to think,
            // Gotta make annn importanntt decisionnnn!
            case MotionEvent.ACTION_UP:
                handler.removeCallbacks(openContactManager);
                if (openConversation) {
                    //show what number was selected
                    Toast.makeText(context, friendsNumber, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("phoneNo", friendsNumber);
                    intent.putExtra("MyPhoneno", myPhoneNumber);
                    intent.putExtra("threadid", threadid);
                    //intent.putExtra("UserPrivateKey" , encryption.privateKey);
                    //DataWrapper dw = new DataWrapper(chatMessageList);
                    //intent.putExtra("data", dw);
                    context.startActivity(intent);
                    retVal = true;
                }
                break;
        }
        return retVal;
    }
}