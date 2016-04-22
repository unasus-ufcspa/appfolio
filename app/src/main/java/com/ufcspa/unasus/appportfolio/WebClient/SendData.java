package com.ufcspa.unasus.appportfolio.WebClient;

import android.content.Context;
import android.util.Log;

import com.ufcspa.unasus.appportfolio.Model.Comentario;
import com.ufcspa.unasus.appportfolio.Model.Sync;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    protected LinkedHashMap<String,HolderIDS> dadosResponse;
    private LinkedList<Comentario> comentarios;
    private DataBaseAdapter data;
    private String tbComm="tb_comment";
    //ids to response
    //-2 is default value
    private int id=-2;
    private int idserver=-2;


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
       // String tbComm="tb_comment";
        String tbVers="tb_version_activity";
      if(dadosAgrupados.get(tbComm)!=null){
          comentarios=(LinkedList)data.getCommentsByIDs(dadosAgrupados.get(tbComm));
      }
      if(dadosAgrupados.get(tbVers)!=null){

      }


    }

    public void insertDataOnResponse(){
        // insert data by json response sendfulldata

        if(dadosResponse.get(tbComm)!=null){
            data.updateCommentBySendFullData(dadosResponse.get(tbComm));
        }
    }



    public JSONObject GenerateJSON(int idDevice){
        getSyncs();// obtem lista de sincronizações
        getDataFromTables(); // obtem listas das tabelas a serem enviadas

        //gera json a partir das listas de tabelas

        JSONObject jsonFinal= new JSONObject();
        JSONObject jsonPseudoFinal= new JSONObject();
        JSONObject reference = new JSONObject();
        JSONObject device = new JSONObject();
        JSONObject version = new JSONObject();
        JSONArray jsonComments = new JSONArray();
        JSONObject jsonComment= new JSONObject();
        try {

            // mount JSON comment
            for (Comentario c: comentarios) {
                jsonComment.put("id_comment",c.getIdComment());
                jsonComment.put("id_activity_student",c.getIdActivityStudent());
                jsonComment.put("id_author",c.getIdAuthor());
                jsonComment.put("tx_comment",c.getTxtComment());
                jsonComment.put("tx_reference",c.getTxtReference());
                jsonComment.put("tp_comment",c.getTypeComment());
                jsonComment.put("dt_comment",c.getDateComment());
                jsonComment.put("nu_comment_activity",c.getIdNote());
                jsonComments.put(jsonComment);
            }

            //mount device
            device.put("id_device",idDevice);



            //mount pseudo final
            jsonPseudoFinal.put("device",device);
            jsonPseudoFinal.put("comment",jsonComments);

            jsonFinal.put("fullDataDevSrv_request",jsonPseudoFinal);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("json send full data ",jsonFinal.toString());
        return jsonFinal;
    }

    private void clear(){
        id=-2;
        idserver=-2;
    }

}
