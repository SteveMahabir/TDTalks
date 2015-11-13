package com.andriod.steve.tdtalks;

/**
 * Created by Kevin on 9/28/2015.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<TextMessage> {

    private SkullColour skullour;
    private SkullType skullType;

    private final Context context;
    private final ArrayList<TextMessage> msgArrayList;
    private final String phoneNo;

    public MyAdapter(Context context, ArrayList<TextMessage> msgArrayList , String phoneNumber) {

        super(context, R.layout.row, msgArrayList);
        this.phoneNo = phoneNumber;
        this.context = context;
        this.msgArrayList = msgArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row, parent, false);

        TextView msgView = null;

        ImageView imgViewIn = (ImageView) rowView.findViewById(R.id.incomingLogo);
        ImageView imgViewOut = (ImageView) rowView.findViewById(R.id.outgoingLogo);

        //get the skullour and skull type from the saved prefferences
        SharedPreferences sharedpref = context.getSharedPreferences("state", Context.MODE_PRIVATE);
        //So enums are classes in java, not ints, so have to do some stupd string crap to get this to work
        skullour =  SkullColour.valueOf( sharedpref.getString("skullour", SkullColour.Pueple.toString() ));
        skullType = SkullType.valueOf( sharedpref.getString("skullType", SkullType.Glow.toString() ));
        //set the image to the right skullour and type
        Bitmap bmp = Resources.GetSkullFromEnum(skullour, skullType, context);
        imgViewIn.setImageBitmap( bmp );
        imgViewOut.setImageBitmap( bmp );

        if(msgArrayList.get(position).getIsIncoming())
        {
            //incoming
            // 3. Get the text view from the rowView
            imgViewOut.setVisibility(View.GONE);

            msgView = (TextView) rowView.findViewById(R.id.incoming);
            // 4. Set the text for textView

            msgView.setText(msgArrayList.get(position).getText());
            msgView.setVisibility(View.VISIBLE);


        }
        else
        {
            //outgoing
            imgViewIn.setVisibility(View.GONE);
            // 3. Get the text view from the rowView
            msgView = (TextView) rowView.findViewById(R.id.outgoing);

            // 4. Set the text for textView
            msgView.setText(msgArrayList.get(position).getText());
            msgView.setVisibility(View.VISIBLE);


            //create a listening object and giving it the message view. this is to show on a press and hold
                imgViewOut.setOnTouchListener(new touchListener_Image(msgView, msgArrayList.get(position), context));
            //msgView.setVisibility(View.INVISIBLE);

        }


        if(msgView != null) {
            //set incoming or outgoing
            msgView.setGravity(msgArrayList.get(position).getIsIncoming() ? Gravity.LEFT : Gravity.RIGHT);
        }
        // 5. return rowView
        return rowView;
    }
}