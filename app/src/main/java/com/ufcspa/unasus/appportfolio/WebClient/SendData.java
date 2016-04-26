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
    private String tbVers="tb_version_activity";
    //ids to response
    //-2 is default value
    private int id=-2;
    private int idserver=-2;


    public SendData(Context context) {
        this.context = context;
        sincronias= new ArrayList<>();
        data= DataBaseAdapter.getInstance(context);
        comentarios= new LinkedList<Comentario>();

    }

    public int getSyncs(){
        Log.d("json send full data ","obtendo lista de sincronizações");
        sincronias=data.getSyncs();
        Log.d("json send full data ","numero de sincronias:"+sincronias.size());
        //sincronias.size();
        dadosAgrupados= new LinkedHashMap<>();
        for (Sync s:sincronias) {
            Log.d("json send full data ","syncs:"+s.toString());
            if(dadosAgrupados.get((s.getNm_table()))== null){
                Log.d("json send full data ","encontrou tabela a ser sincronizada");
                LinkedList l= new LinkedList();
                dadosAgrupados.put(s.getNm_table(),l);
            }
            LinkedList l =dadosAgrupados.get(s.getNm_table());
                    l.add(s.getCo_id_table());
            dadosAgrupados.put(s.getNm_table(),l);
        }
        return sincronias.size();
    }

    public void getDataFromTables(){
        Log.d("json send full data ","obtendo dados das tabelas a serem sincronizadas " +dadosAgrupados.size());
        // String tbComm="tb_comment";
        Log.d("json send full data ","hashmap dados agrupados " +dadosAgrupados.toString());
      if(dadosAgrupados.get(tbComm)!=null){
          comentarios=(LinkedList)data.getCommentsByIDs(dadosAgrupados.get(tbComm));
          Log.d("json send full data ","encontrou tb comentarios");
          Log.d("json send full data ","comentarios:"+comentarios.toString());
      }
      if(dadosAgrupados.get(tbVers)!=null){

      }


    }

    public void insertDataOnResponse(){
        // insert data by json response sendfulldata

        if(dadosResponse.get(tbComm)!=null){
            Log.d("json send full data ","atualizando tabela "+tbComm+"...");
            data.updateCommentBySendFullData(dadosResponse.get(tbComm));
            Log.d("json send full data ", "conseguiu atualizar com sucesso id server");

        }
    }



    public JSONObject GenerateJSON(String idDevice){
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
            if(comentarios!=null) {
                for (Comentario c : comentarios) {
                    jsonComment.put("id_comment", c.getIdComment());
                    jsonComment.put("id_activity_student", c.getIdActivityStudent());
                    jsonComment.put("id_author", c.getIdAuthor());
                    jsonComment.put("tx_comment", c.getTxtComment());
                    jsonComment.put("tx_reference", c.getTxtReference());
                    jsonComment.put("tp_comment", c.getTypeComment());
                    jsonComment.put("dt_comment", c.getDateComment());
                    jsonComment.put("nu_comment_activity", c.getIdNote());
                    jsonComments.put(jsonComment);
                }
            }

            //mount device
            device.put("id_device",idDevice);


            //mount pseudo final
            jsonPseudoFinal.put("device",device);
            jsonPseudoFinal.put("comment",jsonComments);

            jsonFinal.put("fullDataDevSrv_request", jsonPseudoFinal);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("json send full data ",jsonFinal.toString().replaceAll("\\{", "\n{"));
        return jsonFinal;
    }

    private void clear(){
        id=-2;
        idserver=-2;
    }

}
