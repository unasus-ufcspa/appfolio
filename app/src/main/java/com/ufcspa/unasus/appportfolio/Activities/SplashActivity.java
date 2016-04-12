package com.ufcspa.unasus.appportfolio.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

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

        DataBaseAdapter adapter = DataBaseAdapter.getInstance(this);
        int statusApp = adapter.getStatus();

        Intent intent = new Intent(this, LoginActivity2.class);
        if (statusApp == 1) {
            Singleton singleton = Singleton.getInstance();
            singleton.user = adapter.getUser();
            intent = new Intent(this, MainActivity.class);
        } else if (statusApp == 0) {
            intent.putExtra("dont_have_basic_data", true);
        }

        startActivity(intent);
        finish();
    }
}
