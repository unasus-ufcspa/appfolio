package com.ufcspa.unasus.appportfolio.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.ufcspa.unasus.appportfolio.Model.Device;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.List;

/**
 * Created by Zago on 17/12/2015.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* Log.d("ANDROID ID", "getting android ID...");
        android_id = Secure.getString(this.getContentResolver(),Secure.ANDROID_ID);
        Log.d("ANDROID ID","Android ID:"+android_id);
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String id =telephonyManager.getDeviceId();
        Log.d("ANDROID ID", "getting android ID BY IMEI...");
        Log.d("ANDROID ID","Android ID IMEI:"+id);*/

        Stetho.initializeWithDefaults(this);

        DataBaseAdapter adapter = DataBaseAdapter.getInstance(this);
        Singleton singleton = Singleton.getInstance();

        List<Integer> d = adapter.listSpecificComments(13);

        Log.d("splash", "comments:" + d.toString());

        Device device = adapter.getDevice();

        singleton.device = device;
        singleton.user = adapter.getUser();

        int statusApp = adapter.getStatus(device);
        Log.d("SplashActivity", "status device = " + statusApp);

        Intent intent = new Intent(this, LoginActivity.class);
            if (statusApp == 1) {
                intent = new Intent(this, MainActivity.class);
            } else if (statusApp == 0) {
                intent.putExtra("dont_have_basic_data", true);
            }

            startActivity(intent);
            finish();
    }
}
