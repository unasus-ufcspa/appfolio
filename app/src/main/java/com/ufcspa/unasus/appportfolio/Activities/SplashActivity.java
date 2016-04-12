package com.ufcspa.unasus.appportfolio.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.Model.StatusApp;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

/**
 * Created by Zago on 17/12/2015.
 */

public class SplashActivity extends AppCompatActivity {
    private String android_id;
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

        DataBaseAdapter adapter = DataBaseAdapter.getInstance(this);
        StatusApp statusApp = adapter.getStatus();

        Intent intent = new Intent(this, LoginActivity2.class);
        if (statusApp.isBasic_data_sync()) {
            Singleton singleton = Singleton.getInstance();
            singleton.user = adapter.getUser();
            intent = new Intent(this, MainActivity.class);
        } else if (statusApp.isFirst_sync()) {
            intent.putExtra("dont_have_basic_data", true);
        }

        startActivity(intent);
        finish();
    }
}
