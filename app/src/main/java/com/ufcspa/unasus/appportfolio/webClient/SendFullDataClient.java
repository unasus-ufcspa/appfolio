package com.ufcspa.unasus.appportfolio.webClient;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ufcspa.unasus.appportfolio.activities.MainActivity;
import com.ufcspa.unasus.appportfolio.database.DataBase;
import com.ufcspa.unasus.appportfolio.model.Attachment;
import com.ufcspa.unasus.appportfolio.model.basicData.ActivityStudent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * Created by icaromsc on 22/04/2016.
 */
public class SendFullDataClient extends HttpClient{
    private String method = "fullDataDevSrv";
    private Context context;
    private SendData sendData;


    public SendFullDataClient(Context context,SendData send) {
        super(context);
        this.context=context;
        this.sendData=send;
    }

    public void postJson(JSONObject jsonSendFullData) {
        Log.d(tag, "URL: " + URL + method);
        System.out.println("json send fulldata: " + jsonSendFullData.toString());
        JsonObjectRequest jsObjReq = new JsonObjectRequest(Request.Method.POST, URL + method, jsonSendFullData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(tag, "Retornou do request");
                try {
                    Log.d(tag, "JSON RESPONSE: " + response.toString().replaceAll("\\{", "\n{"));
                    if (response.has("error")) {
                        Log.e(tag, "sincronizacao de dados full falhou");
                        MainActivity.sendResponseNotReceived = false;
                    } else if (response.has("fullDataDevSrv_response")) {
                        sendData.dadosResponse = new LinkedHashMap<>();
                        LinkedList<HolderIDS> holdersComments = new LinkedList<>();

                        JSONObject resp = response.getJSONObject("fullDataDevSrv_response");
                        Log.d(tag, "JSON POST existe Send Full Data response");
                        if (resp.has("comment")) {
                            JSONObject comment = resp.getJSONObject("comment");

                            if (comment.has("tb_comment")) {
                                JSONArray tb_comment = comment.getJSONArray("tb_comment");
                                for (int i = 0; i < tb_comment.length(); i++) {
                                    HolderIDS holder = new HolderIDS();
                                    JSONObject temp = tb_comment.getJSONObject(i);
                                    int id_comment = temp.getInt("id_comment");
                                    int id_comment_srv = temp.getInt("id_comment_srv");
                                    String dt_comment_srv=null;

                                    if(temp.has("id_comment_version_srv")){
                                        holder.idcvSrv=(temp.getInt("id_comment_version_srv"));
                                    }
                                    if(temp.has("dt_comment_srv")){
                                        dt_comment_srv = temp.getString("dt_comment_srv");
                                    }else{
                                        Log.d("json","não encontrou campo json dt_comment_srv na response sendfulldata");
                                    }

                                    holder.id = id_comment;
                                    holder.idSrv = id_comment_srv;
                                    holder.date = dt_comment_srv;
                                    holdersComments.add(holder);
                                }
                                sendData.dadosResponse.put("tb_comment", holdersComments);
                            }
                        }
                        if (resp.has("version")) {
                            JSONObject version = resp.getJSONObject("version");

                            LinkedList<HolderIDS> holdersVersion = new LinkedList<>();
                            LinkedList<HolderIDS> holdersCommentVersion = new LinkedList<>();

                            if (version.has("tb_version_activity")) {


                                // ANTIGO
//                                if (version.get("tb_version_activity") instanceof JSONObject) {
//                                    Log.d("json","tem tb_version_activity");
//                                    JSONObject tb_version_activity = version.getJSONObject("tb_version_activity");
//                                    for (int i = 0; i < tb_version_activity.length(); i++) {
//                                        HolderIDS holder = new HolderIDS();
//                                        JSONObject temp = tb_version_activity;
//                                        //if(yn)
//                                        int id_version_activity = temp.getInt("id_version_activity");
//                                        int id_version_activity_srv = temp.getInt("id_version_activity_srv");
//                                        String dt_submission = temp.getString("dt_submission");
//                                        holder.id = id_version_activity;
//                                        holder.idSrv = id_version_activity_srv;
//                                        holder.date = dt_submission;
//                                        holders.add(holder);
//
//
//                                    }
//                                    sendData.dadosResponse.put("tb_version_activity", holders);
//                                }

                                if (version.get("tb_version_activity") instanceof JSONArray) {
                                    Log.d("json","tem tb_version_activity array");
                                    JSONArray jsonTbVersionArray = version.getJSONArray("tb_version_activity");
                                    for (int i = 0; i < jsonTbVersionArray.length(); i++) {
                                        HolderIDS holderV = new HolderIDS();
                                        HolderIDS holderCV = new HolderIDS();
                                        JSONObject temp = jsonTbVersionArray.getJSONObject(i);


                                        if(temp.has("id_version_activity_srv")){
                                            int id_version_activity_srv = temp.getInt("id_version_activity_srv");
                                            holderV.idSrv = id_version_activity_srv;
                                            if(temp.has("id_version_activity")){
                                                int id_version_activity = temp.getInt("id_version_activity");
                                                holderV.id = id_version_activity;
                                            }

                                            if(temp.has("dt_submission")){
                                                String dt_submission = temp.getString("dt_submission");
                                                holderV.date = dt_submission;
                                            }
                                            holdersVersion.add(holderV);
                                        }

                                        if(temp.has("id_comment_version_srv")){
                                            int id_comment_version_srv = temp.getInt("id_comment_version_srv");
                                            holderCV.idSrv=id_comment_version_srv;
                                            if(temp.has("id_comment_version_mobile")){
                                                int id_comment_version_mobile = temp.getInt("id_comment_version_mobile");
                                                holderCV.id=id_comment_version_mobile;
                                            }
                                            holdersCommentVersion.add(holderCV);
                                        }
                                    }
                                    sendData.dadosResponse.put("tb_version_activity", holdersVersion);
                                    sendData.dadosResponse.put("tb_comment_version",holdersCommentVersion);
                                }

                            }else{
                                Log.d("json","não entrou em version_activity json");
                            }
                        }else{
                            Log.d("json","não entrou em version");
                        }

                        if (resp.has("activityStudent")) {
                            JSONObject activityStudent = resp.getJSONObject("activityStudent");
                            if (activityStudent.has("tb_activity_student")) {
                                JSONArray tb_activity_student = activityStudent.getJSONArray("tb_activity_student");
                                for (int i = 0; i < tb_activity_student.length(); i++) {
                                    JSONObject temp = tb_activity_student.getJSONObject(i);
                                    ActivityStudent as = new ActivityStudent();
                                    int id_activity_student = temp.getInt("id_activity_student");
                                    String dt_conclusion = temp.getString("dt_conclusion");
                                    as = DataBase.getInstance(context).getActivityStudentById(id_activity_student);
                                    as.setDt_conclusion(dt_conclusion);
                                    DataBase.getInstance(context).updateTBActivityStudent(as);
                                    Log.d("activityStudent",as.toString());
                                }
                            }
                        }

                        if (resp.has("attachment")) {
                            JSONObject attach = resp.getJSONObject("attachment");
                            if (attach.has("tb_attachment")) {
                                JSONArray tb_attach_temp = attach.getJSONArray("tb_attachment");
                                JSONArray tb_attach = tb_attach_temp.getJSONArray(0);
                                for (int i = 0; i < tb_attach.length(); i++) {
                                    JSONObject temp = tb_attach.getJSONObject(i);
                                    Attachment a = new Attachment();
                                    int id_attachment = temp.getInt("id_attachment");
                                    int id_attachment_srv = temp.getInt("id_attachment_srv");
                                    a = DataBase.getInstance(context).getAttachmentByID(id_attachment);
                                    DataBase.getInstance(context).updateIdAttachmentSrvById(id_attachment,id_attachment_srv);
                                    Log.d("anexo",a.toString());
                                }

                            }

                        }

                        if (resp.has("annotation")) {
                            JSONObject annotations = resp.getJSONObject("annotation");
                            if (annotations.has("tb_annotation")) {
                                JSONArray tb_annotation = annotations.getJSONArray("tb_annotation");
                                LinkedList<HolderIDS> holdersAnnotation = new LinkedList<>();
                                for (int i = 0; i < tb_annotation.length(); i++) {
                                    HolderIDS holderA = new HolderIDS();
                                    JSONObject temp = tb_annotation.getJSONObject(i);

                                    holderA.setId(temp.getInt("id_annotation"));
                                    holderA.setIdSrv(temp.getInt("id_annotation_srv"));

                                    holdersAnnotation.add(holderA);
                                }
                                DataBase.getInstance(context).updateAnnotationsBySendFullData(holdersAnnotation);
                            }
                        }

                        //atualiza dados recebidos via json no sqlite
                        sendData.insertDataOnResponse();
                        MainActivity.shouldSend = false;
                    } else {
                        DataBase.getInstance(context).deleteTBUserFromSync();
                    }
                    DataBase.getInstance(context).deleteAllNotificationsFromSync();
                } catch (Exception v) {
                    //MainActivity.isFullSyncNotSucessful = true;
                    v.printStackTrace();
                }finally {
                    Log.d(tag, "Fim  da request");
                    //EXECUTE INSERTS IN SQLITE
                    //MainActivity.isFullDataSucessful = true;
                    MainActivity.sendResponseNotReceived = false;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //MainActivity.isFullSyncNotSucessful = true;
                Log.e(tag, "Erro  na request");
                Log.e(tag, "erro=" + volleyError.getMessage());
                volleyError.printStackTrace();
                MainActivity.sendResponseNotReceived = false;
            }
        });

        jsObjReq.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjReq);
    }



}
