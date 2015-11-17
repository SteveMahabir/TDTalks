package com.andriod.steve.tdtalks;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CompanyActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    // Database Object
    public DBAdapter db;
    ArrayList<String> ar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        ar = new ArrayList<String>();
        db = new DBAdapter(this);
        Cursor c;
        db.open();
        c = db.getAllCompanies();

        if (c.moveToFirst())
        {
            do {
                if(c.getString(0) != "") {
                    String temp = c.getString(0);
                    if(temp != null)
                        ar.add(c.getString(0));
                    //Toast.makeText(this, "Company: " + c.getString(0) + "\n", Toast.LENGTH_SHORT).show();
                }
            } while (c.moveToNext());
        }
        db.close();

        if(ar.isEmpty()) {
            ar.add("Sorry, no companies found");
            Button b = (Button) findViewById(R.id.buttonChat);
            b.setVisibility(View.INVISIBLE);
        }


        else{


            ListView lv = (ListView) findViewById(R.id.listCompany);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    getBaseContext(),
                    android.R.layout.simple_list_item_1,
                    ar );

            lv.setAdapter(arrayAdapter);
            lv.setOnItemClickListener(this);
        }




    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String myphoneNumber;
        if(!tm.getLine1Number().isEmpty())
            myphoneNumber = (String)tm.getLine1Number();
        else
            myphoneNumber = "5195202520";

        String company = ((TextView)view).getText().toString();

        Intent intent = new Intent(CompanyActivity.this, ChatActivity.class);
        intent.putExtra("phoneNo", "");
        intent.putExtra("MyPhoneno", "5555555555");
        intent.putExtra("threadid", 0);
        intent.putExtra("company", company);
        startActivity(intent);
    }
}

