package com.ufcspa.unasus.appportfolio.WebClient;

import android.content.Context;

import com.ufcspa.unasus.appportfolio.Model.Sync;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by Arthur Zettler on 13/04/2016.
 */
public class SyncData {
    private LinkedList<Sync> syncs;
    private Context context;

    public SyncData(Context context) {
        this.context = context;
        syncs = new LinkedList<>();
    }

    public static JSONObject toJSON(String idDevice) {
        JSONObject json = new JSONObject();
        try {
            json.put("sendSync_request", new JSONObject().put("tb_sync", new JSONObject().put("id_device", idDevice)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public void addSync(Sync sync) {
        syncs.add(sync);
    }

    public LinkedList<Sync> getSyncs() {
        return syncs;
    }

    public synchronized void insertDataIntoSQLITE() {
        DataBaseAdapter data = DataBaseAdapter.getInstance(context);
        data.insertTBSync(syncs);
    }
}
