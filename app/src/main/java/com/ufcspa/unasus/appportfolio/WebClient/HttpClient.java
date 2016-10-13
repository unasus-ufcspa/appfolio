package com.ufcspa.unasus.appportfolio.WebClient;

import android.content.Context;

/**
 * Created by icaromsc on 29/03/2016.
 */
public class HttpClient {
    //public static final String ip = "folio.unasus.ufcspa.edu.br"; //PRODUÇÃO
    public static final String ip = "192.168.0.9"; //TESTE
    protected static final String URL = "http://" + ip + "/webfolio/app_dev.php/";
    protected String tag="JSON";
    private Context context;

    public HttpClient(Context context) {
        this.context = context;
    }
}
