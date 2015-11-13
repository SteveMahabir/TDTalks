package com.andriod.steve.tdtalks;


import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Postma29 on 11/12/2015.
 */
public class TDTalksService extends IntentService {

    private static final String TAG = "com.andriod.steve.tdtalks";
    public TDTalksService() {
        super("");
    }
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public TDTalksService(String name) {
        super(name);
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
            /*Toast.makeText(getApplicationContext(), "Service Created",Toast.LENGTH_SHORT).show();*/
        super.onCreate();
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
            /*Toast.makeText(getApplicationContext(), "Service Destroy",Toast.LENGTH_SHORT).show();*/
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
            /*Toast.makeText(getApplicationContext(), "Service Working",Toast.LENGTH_SHORT).show();*/
        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
}
