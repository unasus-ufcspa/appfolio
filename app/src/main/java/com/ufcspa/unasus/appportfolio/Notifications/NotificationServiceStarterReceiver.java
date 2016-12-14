package com.ufcspa.unasus.appportfolio.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ufcspa.unasus.appportfolio.Model.Singleton;

/**
 * Created by Steffano on 14/12/2016.
 */

public class NotificationServiceStarterReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationEventReceiver.setupAlarm(context, Singleton.getInstance().interval);
    }
}
