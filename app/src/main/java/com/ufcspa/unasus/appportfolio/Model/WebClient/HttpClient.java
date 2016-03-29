package com.ufcspa.unasus.appportfolio.Model.WebClient;

import android.content.Context;

/**
 * Created by icaromsc on 29/03/2016.
 */
public class HttpClient {
    private static final String ip = "192.168.0.25";
    protected static final String URL = "http://" + ip + "/webfolio/app_dev.php/";
    protected String tag="JSON";
    //private static final String URL = "http://" + ip + "/portfolio/volley.php";
    private Context context;

    public HttpClient(Context context) {
        this.context = context;
    }
}
