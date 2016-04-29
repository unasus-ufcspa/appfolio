package com.ufcspa.unasus.appportfolio.WebClient;

import android.content.Context;
import android.util.Log;

import com.ufcspa.unasus.appportfolio.Model.Comentario;
import com.ufcspa.unasus.appportfolio.Model.Sync;
import com.ufcspa.unasus.appportfolio.Model.VersionActivity;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by icaromsc on 19/04/2016.
 */
public class SendData {
    protected LinkedHashMap<String, LinkedList<HolderIDS>> dadosResponse;
    private Context context;
    private LinkedList<Integer> idSync;
    private ArrayList<Sync> sincronias;
    private LinkedHashMap<String,LinkedList<Integer>> dadosAgrupados;
    private LinkedList<Comentario> comentarios;
    private LinkedList<VersionActivity> versions_list;
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
        versions_list = new LinkedList<>();
        comentarios = new LinkedList<>();
        idSync = new LinkedList<>();
    }

    public int getSyncs(){
        Log.d("json send full data ","obtendo lista de sincronizações");
        sincronias = data.getSyncs();
        Log.d("json send full data ","numero de sincronias:"+sincronias.size());
        //sincronias.size();
        dadosAgrupados = new LinkedHashMap<>();
        for (Sync s:sincronias) {
            if(dadosAgrupados.get((s.getNm_table()))== null){
                Log.d("json send full data ","encontrou tabela a ser sincronizada");
                LinkedList l= new LinkedList();
                dadosAgrupados.put(s.getNm_table(),l);
            }
            idSync.add(s.getId_sync());
            LinkedList l = dadosAgrupados.get(s.getNm_table());
            l.add(s.getCo_id_table());
            dadosAgrupados.put(s.getNm_table(),l);
        }
        return sincronias.size();
    }

    public void getDataFromTables(){
        Log.d("json send full data ","obtendo dados das tabelas a serem sincronizadas ");
        // String tbComm="tb_comment";
        if (dadosAgrupados.get(tbComm) != null) {
            comentarios = (LinkedList) data.getCommentsByIDs(dadosAgrupados.get(tbComm));
      }
      if(dadosAgrupados.get(tbVers)!=null){
          versions_list = (LinkedList) data.getVersionActivitiesByIDs(dadosAgrupados.get(tbVers));
      }
    }

    public void insertDataOnResponse(){
        // insert data by json response sendfulldata
        if(dadosResponse.get(tbComm)!=null){
            Log.d("json send full data ","atualizando tabela "+tbComm+"...");
            data.updateCommentBySendFullData(dadosResponse.get(tbComm));
            data.deleteSync(idSync);
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
        JSONObject jsonVersion = new JSONObject();
        JSONArray jsonArrayVersions = new JSONArray();
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
            if(versions_list !=null){
                for (VersionActivity v : versions_list){
                    jsonVersion.put("id_version_activity",v.getId_version_activity());
                    jsonVersion.put("id_activity_student",v.getId_activity_student());
                    jsonVersion.put("tx_activity",v.getTx_activity());
                    jsonVersion.put("dt_last_access",v.getDt_last_access());
                    jsonVersion.put("dt_submission",v.getDt_submission());
                    jsonArrayVersions.put(jsonVersion);
                }
            }

            //mount device
            device.put("id_device",idDevice);


            //mount pseudo final
            jsonPseudoFinal.put("device",device);
            jsonPseudoFinal.put("comment", new JSONObject().put("tb_comment", jsonComments));
            jsonPseudoFinal.put("version", new JSONObject().put("tb_version_activity", jsonArrayVersions));

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
