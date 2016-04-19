package com.ufcspa.unasus.appportfolio.WebClient;

import android.content.Context;

import com.ufcspa.unasus.appportfolio.Model.Comentario;
import com.ufcspa.unasus.appportfolio.Model.Sync;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by icaromsc on 19/04/2016.
 */
public class SendData {
    private Context context;
    private ArrayList<Sync> sincronias;
    private LinkedHashMap<String,LinkedList<Integer>> dadosAgrupados;
    private LinkedList<Comentario> comentarios;
    DataBaseAdapter data;

    public SendData(Context context) {
        this.context = context;
        sincronias= new ArrayList<>();
        data= DataBaseAdapter.getInstance(context);
    }

    public void getSyncs(){

        sincronias=data.getSyncs();
        dadosAgrupados= new LinkedHashMap<>();
        for (Sync s:sincronias) {
            if(dadosAgrupados.get((s.getNm_table()))== null){
                LinkedList l= new LinkedList();
                dadosAgrupados.put(s.getNm_table(),l);
            }
            LinkedList l =dadosAgrupados.get(s.getNm_table());
                    l.add(s.getCo_id_table());
            dadosAgrupados.put(s.getNm_table(),l);
        }
    }

    public void getDataFromTables(){
        String tbComm="tb_comment";
      if(dadosAgrupados.get(tbComm)!=null){
          
      }
    }
}
