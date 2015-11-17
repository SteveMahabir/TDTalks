package com.andriod.steve.tdtalks;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactsActivity extends Activity {

    // Contact Info
    String phoneNumber;
    String name;
    String company;
    int Threadid;

    // Database Object
    public DBAdapter db;

    // Edit Text View
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        db = new DBAdapter(this);

        // Get Contact Name
        name = String.valueOf(getIntent().getExtras().getString("name"));
        if(name == null)
            name = "";

        Threadid = Integer.valueOf(getIntent().getExtras().getInt("threadid"));


        // Get Contact Phone Number
        phoneNumber = String.valueOf(getIntent().getExtras().getString("phoneno"));
        if(phoneNumber == null)
            phoneNumber = "";
        else
        {
            db.open();
            Cursor c = db.getContactByPhoneNumber(phoneNumber);
            if (c.moveToFirst())
                company = c.getString(4);
            else
                company = "";

            db.close();
        }

        // Set Contact Name
        et = (EditText)findViewById(R.id.editTextName);
        if(name == "null") {
            et.setText("");
            et.setHint("Enter Name Here");
        }
        else
            et.setText(name);

        // Set Contact Phone Number
        et = (EditText)findViewById(R.id.editTextPhone);
        et.setText(phoneNumber);

        // If friend, set false
        if(!phoneNumber.equals(""))
            et.setEnabled(false);

        if(phoneNumber == "null") {
            et.setHint("Enter Phone Number Here");
            et.setText("");
            et.setEnabled(true);
            Button b = (Button) findViewById(R.id.buttonUpdate);
            b.setVisibility(View.INVISIBLE);
        }

        // Set Contact Company
        et = (EditText)findViewById(R.id.editTextCompany);
        et.setText(company);

    }


    public void onClickEvent(View view) {
        switch(view.getId())
        {
            case(R.id.buttonUpdate):
                if(infoValidated()) {
                    db.open();
                    if(db.updateContact(phoneNumber, name, null, company)) {
                        db.close();
                        Toast.makeText(this, "Success, " + name +  " Updated!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else {
                        db.close();
                        Toast.makeText(this, "Failed to update contact", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(this, "Please fill in Name and Number", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent mainmenuint = new Intent(ContactsActivity.this, MainActivity.class);

                startActivity(mainmenuint);


                break;
            case(R.id.buttonChat):
                TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                String myphoneNumber;
                if(!tm.getLine1Number().isEmpty())
                    myphoneNumber = (String)tm.getLine1Number();
                else
                    myphoneNumber = "5195202520";

                EditText et = (EditText) findViewById(R.id.editTextPhone);
                if(et.getText().toString() == "" ||
                        et.getText().toString() == "null" ||
                        et.getText().length() == 0)
                    break;
                else
                    phoneNumber = et.getText().toString();

                Intent intent = new Intent(ContactsActivity.this, ChatActivity.class);
                intent.putExtra("phoneNo", phoneNumber);
                intent.putExtra("MyPhoneno", myphoneNumber);
                intent.putExtra("threadid", Threadid);
                intent.putExtra("company", company);
                startActivity(intent);
                break;
        }
    }

    private boolean infoValidated() {
        boolean retVal;

        et = (EditText)findViewById(R.id.editTextName);
        name = et.getText().toString();
        if(name.equals("") || name == null)
            return false;

        et = (EditText)findViewById(R.id.editTextPhone);
        phoneNumber = et.getText().toString();
        if(name.equals("") || name == null)
            return false;

        et = (EditText)findViewById(R.id.editTextCompany);
        company = et.getText().toString();

        return true;
    }
}
