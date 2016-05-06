package com.ufcspa.unasus.appportfolio.WebClient;

import android.content.Context;
import android.util.Log;

import com.ufcspa.unasus.appportfolio.Model.Comentario;
import com.ufcspa.unasus.appportfolio.Model.Sync;
import com.ufcspa.unasus.appportfolio.Model.User;
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
    private LinkedHashMap<Integer, LinkedList<Comentario>> commentsByVersions;
    private LinkedList<Comentario> comentarios;
    private LinkedList<User> users;
    private LinkedList<Comentario> commentByVersion;
    private LinkedList<VersionActivity> versions;
    private DataBaseAdapter data;
    private String tbComm="tb_comment";
    private String tbVers="tb_version_activity";
    private String tbUser = "tb_user";
    //ids to response
    //-2 is default value
    private int id=-2;
    private int idserver=-2;


    public SendData(Context context) {
        this.context = context;
        sincronias= new ArrayList<>();
        data= DataBaseAdapter.getInstance(context);
        versions = new LinkedList<VersionActivity>();
        comentarios = new LinkedList<>();
        commentsByVersions = new LinkedHashMap<>();
        commentByVersion = new LinkedList<>();
        idSync = new LinkedList<>();
    }

    public int getSyncs(){
        Log.d("json send full data ","obtendo lista de sincronizações");
        sincronias = data.getSyncs();
        Log.d("json send full data ","numero de sincronias:"+sincronias.size());
        //sincronias.size();
        dadosAgrupados = new LinkedHashMap<>();
        idSync = new LinkedList<>();
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
        if (dadosAgrupados.get(tbVers) != null) {
            versions = (LinkedList) data.getVersionActivitiesByIDs(dadosAgrupados.get(tbVers));
            for (VersionActivity v : versions) {
                commentsByVersions.put(v.getId_version_activity(), data.getCommentVersion(v.getId_version_activity()));
            }
        }
        if (dadosAgrupados.get(tbUser) != null) {
            users = (LinkedList) data.getUsersByIDs(dadosAgrupados.get(tbUser));
        }
    }

    public void insertDataOnResponse(){
        // insert data by json response sendfulldata
        if(dadosResponse.get(tbComm)!=null){
            Log.d("json send full data ", "atualizando tabela " + tbComm + "...");
            data.updateCommentBySendFullData(dadosResponse.get(tbComm));
//            data.deleteSync(idSync);
            Log.d("json send full data ", "conseguiu atualizar com sucesso id server");
        }
        if (dadosResponse.get(tbVers) != null) {
            Log.d("json send full data ", "atualizando tabela " + tbVers + "...");
            data.updateVersionsBySendFullData(dadosResponse.get(tbVers));
//            data.deleteSync(idSync);
            Log.d("json send full data ", "conseguiu atualizar com sucesso id server");
        }
        data.deleteSync(idSync);
    }



    public JSONObject GenerateJSON(String idDevice){
        getSyncs();// obtem lista de sincronizações
        getDataFromTables(); // obtem listas das tabelas a serem enviadas

        //gera json a partir das listas de tabelas

        JSONObject jsonFinal= new JSONObject();
        JSONObject jsonPseudoFinal= new JSONObject();
        JSONObject reference = new JSONObject();
        JSONObject device = new JSONObject();

        JSONArray jsonArrayVersions = new JSONArray();
        JSONArray jsonComments = new JSONArray();

        JSONObject jsonUser = new JSONObject();

        try {
            // mount JSON comment
            if(comentarios!=null) {
                for (Comentario comment : comentarios) {
                    JSONObject jsonComment = new JSONObject();
                    jsonComment.put("id_comment", comment.getIdComment());
                    jsonComment.put("id_activity_student", comment.getIdActivityStudent());
                    jsonComment.put("id_author", comment.getIdAuthor());
                    jsonComment.put("tx_comment", comment.getTxtComment());
                    jsonComment.put("tx_reference", comment.getTxtReference());
                    jsonComment.put("tp_comment", comment.getTypeComment());
                    jsonComment.put("dt_comment", comment.getDateComment());
                    jsonComment.put("nu_comment_activity", comment.getIdNote());
                    jsonComments.put(jsonComment);
                }
            }
            if (versions != null) {
                for (VersionActivity v : versions) {
                    JSONObject jsonVersion = new JSONObject();
                    jsonVersion.put("id_version_activity", v.getId_version_activity());
                    jsonVersion.put("id_activity_student", v.getId_activity_student());
                    jsonVersion.put("tx_activity", v.getTx_activity());
                    jsonVersion.put("dt_last_access", v.getDt_last_access());
                    jsonArrayVersions.put(jsonVersion);
                    if (commentsByVersions.containsKey(v.getId_version_activity())) {
                        commentByVersion = commentsByVersions.get(v.getId_version_activity());
                        JSONArray jsonCommentsByVersion = new JSONArray();
                        for (Comentario c : commentByVersion) {
                            JSONObject jComment = new JSONObject();
                            jComment.put("id_comment", c.getIdComment());
                            jComment.put("id_activity_student", c.getIdActivityStudent());
                            jComment.put("id_author", c.getIdAuthor());
                            jComment.put("tx_comment", c.getTxtComment());
                            jComment.put("tx_reference", c.getTxtReference());
                            jComment.put("tp_comment", c.getTypeComment());
                            jComment.put("dt_comment", c.getDateComment());
                            jComment.put("nu_comment_activity", c.getIdNote());

                            jsonCommentsByVersion.put(jComment);
                        }
                        JSONObject jTb_comment = new JSONObject();
                        jTb_comment.put("tb_comment", jsonCommentsByVersion);
                        jsonVersion.put("comment", jTb_comment);
                    }
                }
            }

            if (users != null && users.size() > 0) {
                jsonUser.put("id_user", users.getFirst().getIdUser());
                jsonUser.put("nm_user", users.getFirst().getName());
                jsonUser.put("nu_identification", users.getFirst().getIdCode());
                jsonUser.put("ds_email", users.getFirst().getEmail());
                jsonUser.put("nu_cellphone", users.getFirst().getCellphone());
                jsonUser.put("im_photo", users.getFirst().getPhoto());
            }

            //mount device
            device.put("id_device",idDevice);

            //mount pseudo final
            jsonPseudoFinal.put("device",device);
            jsonPseudoFinal.put("comment", new JSONObject().put("tb_comment", jsonComments));
            jsonPseudoFinal.put("version", new JSONObject().put("tb_version_activity", jsonArrayVersions));
            jsonPseudoFinal.put("user", jsonUser);

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
