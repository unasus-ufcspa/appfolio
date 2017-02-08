package com.ufcspa.unasus.appportfolio.WebClient;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.Comentario;
import com.ufcspa.unasus.appportfolio.Model.Notification;
import com.ufcspa.unasus.appportfolio.Model.Observation;
import com.ufcspa.unasus.appportfolio.Model.Reference;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.Model.VersionActivity;
import com.ufcspa.unasus.appportfolio.Model.basicData.ActivityStudent;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Arthur Zettler on 18/04/2016.
 */
public class FullDataClient extends HttpClient {
    private String method = "fullDataSrvDev";
    private Context context;
    private FullData fullData;

    public FullDataClient(Context context) {
        super(context);
        this.context = context;
    }

    public void postJson(JSONObject jsonFirstRequest) {
        Log.d(tag, "URL: " + URL + method);
        JsonObjectRequest jsObjReq = new JsonObjectRequest(Request.Method.POST, URL + method, jsonFirstRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(tag, "Retornou do request");
                try {
                    Log.d(tag, "JSON RESPONSE: " + response.toString().replaceAll("\\{", "\n{"));
                    System.out.print(response.toString().replaceAll("\\{", "\n{"));
                    if (response.has("error")) {
                        MainActivity.isFullSyncNotSucessful = true;
                        // Log.e(tag, "sincronizacao de dados full falhou");
                    } else {
                        if (response.has("fullDataSrvDev_response")) {
                            Log.d(tag, "JSON POST existe Full Data response");
                            fullData = new FullData(context);
                            JSONObject resp = new JSONObject();
                            try {
                                resp = response.getJSONObject("fullDataSrvDev_response");

                                if (resp.has("notice")) {
                                    JSONObject notice = resp.getJSONObject("notice");
                                    if (notice.has("tb_notice")) {
                                        if (notice.get("tb_notice") instanceof JSONArray) {
                                            JSONArray tb_notice = notice.getJSONArray("tb_notice");

                                            for (int i = 0; i < tb_notice.length(); i++) {
                                                JSONObject temp = tb_notice.getJSONObject(i);
                                                Notification notification = new Notification();

                                                int id_notice = temp.getInt("id_notice");
                                                int id_author = temp.getInt("id_author");
                                                int id_destination = temp.getInt("id_destination");
                                                int id_activity_student = temp.getInt("id_activity_student");
                                                String nm_table = temp.getString("nm_table");
                                                int co_id_table_srv = temp.getInt("co_id_table_srv");
                                                String dt_notice = temp.getString("dt_notice");
                                                //                                        String dt_read = temp.getString("dt_read");

                                                notification.setId_notice(id_notice);
                                                notification.setId_author(id_author);
                                                notification.setId_destination(id_destination);
                                                notification.setId_activity_student(id_activity_student);
                                                notification.setNm_table(nm_table);
                                                notification.setCo_id_table_srv(co_id_table_srv);
                                                notification.setDt_notice(dt_notice);
                                                //                                        notification.setDt_read(dt_read);

                                                fullData.addNotification(notification);
                                            }
                                        }
                                    }
                                }

                                if (resp.has("data")) {
                                    JSONObject data = new JSONObject();
                                    try {
                                        data = resp.getJSONObject("data");
                                    } catch (Exception e) {
                                    }
                                    if (data.has("comment")) {
                                        JSONObject comment = data.getJSONObject("comment");
                                    /* VERSÃO ANTIGA ATE 07/06/2016
                                    if (comment.has("tb_comment")) {
                                        JSONArray tb_comment = comment.getJSONArray("tb_comment");
                                        for (int i = 0; i < tb_comment.length(); i++) {
                                            JSONObject temp = tb_comment.getJSONObject(i);
                                            Comentario comentario = new Comentario();

                                            if (!temp.has("error")) {
                                                int id_activity_student = temp.getInt("id_activity_student");
                                                int id_author = temp.getInt("id_author");
                                                String tx_comment = temp.getString("tx_comment");
                                                String tx_reference = temp.getString("tx_reference");
                                                String tp_comment = temp.getString("tp_comment");
                                                String dt_comment = temp.getString("dt_comment");
                                                String dt_send=dt_comment;
                                                if(temp.has("dt_send"))
                                                    dt_send=temp.getString("dt_send");
                                                int nu_comment_activity = -1;
                                                if (!temp.isNull("nu_comment_activity"))
                                                    nu_comment_activity = temp.getInt("nu_comment_activity");
                                                int id_comment_srv = temp.getInt("id_comment");

                                                comentario.setIdActivityStudent(id_activity_student);
                                                comentario.setIdAuthor(id_author);
                                                comentario.setTxtComment(tx_comment);
                                                comentario.setTxtReference(tx_reference);
                                                comentario.setTypeComment(tp_comment);
                                                comentario.setDateComment(dt_comment);
                                                comentario.setDateSend(dt_send);
                                                comentario.setIdNote(nu_comment_activity);
                                                comentario.setIdCommentSrv(id_comment_srv);

                                                JSONObject attachments = new JSONObject();
                                                try {
                                                    attachments = temp.getJSONObject("attachment");
                                                } catch (Exception e) {
                                                }

                                                if (attachments.has("id_attachment")) {
                                                    Attachment attachment = new Attachment();

                                                    String tp_attachment = attachments.getString("tp_attachment");
                                                    String nm_file = attachments.getString("nm_file");
                                                    String nm_system = attachments.getString("nm_system");
                                                    int id_attachment_srv = attachments.getInt("id_attachment");

                                                    attachment.setTpAttachment(tp_attachment);
                                                    attachment.setNmFile(nm_file);
                                                    attachment.setNmSystem(nm_system);
                                                    attachment.setIdAttachmentSrv(id_attachment_srv);

                                                    fullData.addCommentAttachment(new Tuple<Comentario, Attachment>(comentario, attachment));
                                                } else {
                                                    fullData.addComments(comentario);
                                                }
                                            }
                                        }
                                    }*/
                                        if (comment.has("tb_comment")) {
                                            JSONArray tb_comment = comment.getJSONArray("tb_comment");
                                            for (int i = 0; i < tb_comment.length(); i++) {
                                                JSONObject temp = tb_comment.getJSONObject(i);
                                                Comentario comentario = new Comentario();

                                                if (!temp.has("error")) {
                                                    int id_activity_student = temp.getInt("id_activity_student");
                                                    int id_author = temp.getInt("id_author");
                                                    String tx_comment = temp.getString("tx_comment");
                                                    if (temp.has("tx_reference")) {
                                                        String tx_reference = temp.getString("tx_reference");
                                                        comentario.setTxtReference(tx_reference);
                                                    }

                                                    String tp_comment = temp.getString("tp_comment");
                                                    String dt_comment = temp.getString("dt_comment");
                                                    String dt_send = dt_comment;
                                                    if (temp.has("dt_send"))
                                                        dt_send = temp.getString("dt_send");
                                                    int id_comment_srv = temp.getInt("id_comment");

                                                    int id_comment_version = -1;
                                                    if (temp.get("id_comment_version") != null && temp.get("id_comment_version") instanceof String) {
                                                        if (temp.getString("id_comment_version") != null && !temp.getString("id_comment_version").isEmpty())
                                                            //id_comment_version=Integer.getInteger(temp.getString("id_comment_version"));
                                                            id_comment_version = temp.getInt("id_comment_version");
                                                    } else {
                                                        id_comment_version = temp.getInt("id_comment_version");
                                                    }


                                                    comentario.setIdActivityStudent(id_activity_student);
                                                    comentario.setIdAuthor(id_author);
                                                    comentario.setTxtComment(tx_comment);

                                                    comentario.setTypeComment(tp_comment);
                                                    comentario.setDateComment(dt_comment);
                                                    comentario.setDateSend(dt_send);
                                                    //comentario.setTxtComment(tx_comment);
                                                    comentario.setId_comment_version(id_comment_version);
                                                    comentario.setIdCommentSrv(id_comment_srv);

//                                                    JSONObject attachments = new JSONObject();
//                                                    try {
//                                                        attachments = temp.getJSONObject("attachment");
//                                                    } catch (Exception e) {
//                                                        e.printStackTrace();
//                                                    }
//
//                                                    if (attachments.has("id_attachment")) {
//                                                        Attachment attachment = new Attachment();
//
//                                                        String tp_attachment = attachments.getString("tp_attachment");
//                                                        String nm_file = attachments.getString("nm_file");
//                                                        String nm_system = attachments.getString("nm_system");
//                                                        int id_attachment_srv = attachments.getInt("id_attachment");
//
//                                                        attachment.setTpAttachment(tp_attachment);
//                                                        attachment.setNmFile(nm_file);
//                                                        attachment.setNmSystem(nm_system);
//                                                        attachment.setIdAttachmentSrv(id_attachment_srv);
//
//                                                        fullData.addCommentAttachment(new Tuple<Comentario, Attachment>(comentario, attachment));
//                                                    }

                                                  //  else {
                                                        Log.d(tag, "adding comment:" + comentario);
                                                        fullData.addComments(comentario);
                                                   // }
                                                }
                                            }
                                        }

                                    }

                                    if (data.has("reference")) {
                                        JSONObject references = data.getJSONObject("reference");
                                        if (references.has("tb_reference")) {
                                            JSONArray tb_reference = references.getJSONArray("tb_reference");
                                            for (int i = 0; i < tb_reference.length(); i++) {
                                                JSONObject temp = tb_reference.getJSONObject(i);
                                                Reference reference = new Reference();

                                                int id_activity_student = temp.getInt("id_activity_student");
                                                int id_reference_srv = temp.getInt("id_reference");
                                                String ds_url = temp.getString("ds_url");

                                                reference.setIdActStudent(id_activity_student);
                                                reference.setIdRefSrv(id_reference_srv);
                                                reference.setDsUrl(ds_url);

                                                fullData.addReference(reference);
                                            }
                                        }
                                    }

                                    if (data.has("attach_activity")) {
                                        JSONObject attach = data.getJSONObject("attach_activity");
                                        if (attach.has("tb_attach_activity")) {
                                            JSONArray tb_attach = attach.getJSONArray("tb_attach_activity");
                                            ArrayList<Attachment> attachments = new ArrayList<Attachment>();
                                            HashMap<Integer, ArrayList<Attachment>> map = new HashMap<>();
                                            for (int i = 0; i < tb_attach.length(); i++) {
                                                JSONObject temp = tb_attach.getJSONObject(i);
                                                Attachment a = new Attachment();
                                                int id_attachment_srv = temp.getInt("id_attachment");
                                                int id_act_student = temp.getInt("id_activity_student");
                                                int id_att_activity = temp.getInt("id_attach_activity");

                                                if (temp.has("attachment")) {
                                                    JSONObject att = temp.getJSONObject("attachment");
                                                    String tp_attachment = att.getString("tp_attachment");
                                                    String nm_file = att.getString("nm_file");
                                                    String nm_system = att.getString("nm_system");
                                                    a.setNmFile(nm_file);
                                                    a.setTpAttachment(tp_attachment);
                                                    a.setNmSystem(nm_system);
                                                }

                                                // popula objeto attach

                                                a.setIdAttachmentSrv(id_attachment_srv);

                                                attachments.add(a);
                                                if (!map.containsKey(id_act_student)) {
                                                    map.put(id_act_student, attachments);
                                                } else {
                                                    ArrayList<Attachment> tmp = map.get(id_act_student);
//                                                    tmp.add(a);
                                                    map.put(id_act_student,tmp);

                                                }

                                                // fullData.();
                                            }

                                            fullData.addAttachments(map);

                                        }
                                    }

                                    if (data.has("activity_student")) {
                                        JSONObject activityStudent = data.getJSONObject("activity_student");
                                        if (activityStudent.has("tb_activity_student")) {
                                            JSONArray tb_activity_student = activityStudent.getJSONArray("tb_activity_student");
                                            for (int i = 0; i < tb_activity_student.length(); i++) {
                                                JSONObject temp = tb_activity_student.getJSONObject(i);
                                                ActivityStudent as = new ActivityStudent();
                                                int id_activity_student = temp.getInt("id_activity_student");
                                                String dt_conclusion = temp.getString("dt_conclusion");
                                                as = DataBaseAdapter.getInstance(context).getActivityStudentById(id_activity_student);
                                                as.setDt_conclusion(dt_conclusion);
                                                DataBaseAdapter.getInstance(context).updateTBActivityStudent(as);
                                                Log.d("activityStudent",as.toString());
                                            }
                                        }
                                    }


                                    if (data.has("version_activity")) {
                                        JSONObject version = data.getJSONObject("version_activity");
                                        if (version.has("tb_version_activity")) {
                                            JSONArray tb_version_activity = version.getJSONArray("tb_version_activity");
                                            for (int i = 0; i < tb_version_activity.length(); i++) {
                                                JSONObject temp = tb_version_activity.getJSONObject(i);
                                                VersionActivity versionActivity = new VersionActivity();

                                                int id_activity_student = temp.getInt("id_activity_student");
                                                String tx_activity = temp.getString("tx_activity");
                                                tx_activity = tx_activity.replaceAll("\'", "");

                                                tx_activity = tx_activity.replaceAll("<span", "<font");
                                                tx_activity = tx_activity.replaceAll("class='bolinhaFolio'", "");
                                                tx_activity = tx_activity.replaceAll("</span>", "</font>");
                                                tx_activity = tx_activity.replaceAll("\\\\\"", "\"");
                                                tx_activity = tx_activity.replaceAll("<img src=\"", "<img src=\""+ Environment.getExternalStorageDirectory()+"/Android/data/com.ufcspa.unasus.appportfolio/files/images" + File.separator);
                                                tx_activity = tx_activity.replaceAll("<video src=\"", "<img src=\""+Environment.getExternalStorageDirectory()+"/Android/data/com.ufcspa.unasus.appportfolio/files/images" + File.separator);
                                                tx_activity = tx_activity.replaceAll("class=\"mce-object mce-object-video\"","");

                                                System.out.println("json replace tx activity" + "\nold:" + temp.getString("tx_activity") + " new:" + tx_activity);
                                                String dt_last_access = temp.getString("dt_last_access");
                                                if (dt_last_access.equals("null")){
                                                    dt_last_access="0000-00-00 00:00:00";
                                                }
                                                String dt_submission = temp.getString("dt_submission");
                                                if (dt_submission.equals("null")){
                                                    dt_submission="0000-00-00 00:00:00";
                                                }
                                                String dt_verification = temp.getString("dt_verification");
                                                if (dt_verification.equals("null")){
                                                    dt_verification=null;
                                                }
                                                int id_version_activity_srv=0;
                                                if (temp.has("id_version_activity_srv")) {
                                                    id_version_activity_srv = temp.getInt("id_version_activity_srv");
//                                                }else if (Singleton.getInstance().portfolioClass.getPerfil().equalsIgnoreCase("S")) {
//                                                    id_version_activity_srv = 0;
                                                }else
                                                    id_version_activity_srv = temp.getInt("id_version_activity");

                                                versionActivity.setId_activity_student(id_activity_student);
                                                versionActivity.setTx_activity(tx_activity);
                                                versionActivity.setDt_last_access(dt_last_access);
                                                versionActivity.setDt_submission(dt_submission);
                                                versionActivity.setDt_verification(dt_verification);
                                                versionActivity.setId_version_activit_srv(id_version_activity_srv);

                                                fullData.addVersion(versionActivity);
                                            }
                                        }
                                    }

                                    if (data.has("version_activity_update")) {
                                        JSONObject version = data.getJSONObject("version_activity_update");
                                        if (version.has("tb_version_activity_update")) {
                                            JSONArray tb_version_activity = version.getJSONArray("tb_version_activity_update");
                                            for (int i = 0; i < tb_version_activity.length(); i++) {
                                                JSONObject temp = tb_version_activity.getJSONObject(i);
                                                VersionActivity versionActivity = new VersionActivity();

                                                int id_activity_student = temp.getInt("id_activity_student");
                                                String tx_activity = temp.getString("tx_activity");

                                                tx_activity = tx_activity.replaceAll("<span", "<font");
                                                tx_activity = tx_activity.replaceAll("class='bolinhaFolio'", "");
                                                tx_activity = tx_activity.replaceAll("</span>", "</font>");
                                                tx_activity = tx_activity.replaceAll("\\\\\"", "\"");
                                                tx_activity = tx_activity.replaceAll("<img src=\"", "<img src=\""+Environment.getExternalStorageDirectory()+"/Android/data/com.ufcspa.unasus.appportfolio/files/images" + File.separator);
                                                tx_activity = tx_activity.replaceAll("<video src=\"", "<img src=\""+Environment.getExternalStorageDirectory()+"/Android/data/com.ufcspa.unasus.appportfolio/files/images" + File.separator);
                                                tx_activity = tx_activity.replaceAll("class=\"mce-object mce-object-video\"","");

                                                String dt_last_access = temp.getString("dt_last_access");
                                                if (dt_last_access.equals("null")){
                                                    dt_last_access="0000-00-00 00:00:00";
                                                }
                                                String dt_submission = temp.getString("dt_submission");
                                                if (dt_submission.equals("null")){
                                                    dt_submission="0000-00-00 00:00:00";
                                                }
                                                String dt_verification = temp.getString("dt_verification");
                                                if (dt_verification.equals("null")){
                                                    dt_verification=null;
                                                }
                                                int id_version_activity_srv = 0;
                                                if (temp.has("id_version_activity")) {
                                                    id_version_activity_srv = temp.getInt("id_version_activity");
                                                }else if (Singleton.getInstance().portfolioClass.getPerfil().equalsIgnoreCase("S")) {
                                                    id_version_activity_srv = 0;
                                                }else
                                                    id_version_activity_srv = temp.getInt("id_version_activity");

                                                versionActivity.setId_activity_student(id_activity_student);
                                                versionActivity.setTx_activity(tx_activity);
                                                versionActivity.setDt_last_access(dt_last_access);
                                                versionActivity.setDt_submission(dt_submission);
                                                versionActivity.setDt_verification(dt_verification);
                                                versionActivity.setId_version_activit_srv(id_version_activity_srv);

                                                fullData.addVersion(versionActivity);
                                            }
                                        }
                                    }

                                    if (data.has("user")) {
                                        JSONObject user = data.getJSONObject("user");
                                        if (user.has("tb_user")) {
                                            JSONArray tb_user = user.getJSONArray("tb_user");
                                            for (int i = 0; i < tb_user.length(); i++) {
                                                JSONObject temp = tb_user.getJSONObject(i);
                                                com.ufcspa.unasus.appportfolio.Model.basicData.User u = new com.ufcspa.unasus.appportfolio.Model.basicData.User();

                                                int id_user = temp.getInt("id_user");
                                                String nm_user = temp.getString("nm_user");
                                                String nu_identification = temp.getString("nu_identification");
                                                String nu_cellphone = temp.getString("nu_cellphone");
                                                String ds_email = temp.getString("ds_email");
                                                String im_photo = temp.getString("im_photo");

                                                u.setIdUser(id_user);
                                                u.setNm_user(nm_user);
                                                u.setNu_identification(nu_identification);
                                                u.setEmail(ds_email);
                                                u.setCellphone(nu_cellphone);
                                                u.setPhoto(im_photo);

                                                fullData.addUser(u);
                                            }
                                        }
                                    }

                               /* VERSÃO ANTIGA
                               if (data.has("comment_version")) {
                                    JSONObject comment_version = data.getJSONObject("comment_version");
                                    if (comment_version.has("tb_comment_version")) {
                                        JSONArray tb_comment_version = comment_version.getJSONArray("tb_comment_version");
                                        for (int i = 0; i < tb_comment_version.length(); i++) {
                                            JSONObject temp = tb_comment_version.getJSONObject(i);
                                            CommentVersion commentVersion = new CommentVersion();
                                            int id_version_activity = temp.getInt("id_version_activity");
                                            int id_comment = temp.getInt("id_comment");

                                            commentVersion.setId_comment(id_comment);
                                            commentVersion.setId_version_activity(id_version_activity);

                                            fullData.addCommentVersion(commentVersion);
                                        }
                                    }
                                }*/
                                    if (data.has("comment_version")) {
                                        JSONObject comment_version = data.getJSONObject("comment_version");
                                        if (comment_version.has("tb_comment_version")) {
                                            JSONArray tb_comment_version = comment_version.getJSONArray("tb_comment_version");
                                            for (int i = 0; i < tb_comment_version.length(); i++) {
                                                JSONObject temp = tb_comment_version.getJSONObject(i);
                                                Observation obs = new Observation();
                                                int id_version_activity = temp.getInt("id_version_activity");
                                                int nu_comment_activity = temp.getInt("nu_comment_activity");
                                                Integer nu_initial_pos = null;
                                                try {
                                                    nu_initial_pos = temp.getInt("nu_initial_pos");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    if (nu_initial_pos == null) {
                                                        nu_initial_pos = 0;
                                                    }
                                                }
                                                int nu_size = temp.getInt("nu_size");
                                                String tx_reference = temp.getString("tx_reference");

                                                int id_comment_version = temp.getInt("id_comment_version");

                                                if (temp.has("id_comment_version_srv")) {
                                                    obs.setId_comment_version_srv(temp.getInt("id_comment_version_srv"));
                                                }
                                                obs.setNu_initial_position(nu_initial_pos);
                                                obs.setNu_size(nu_size);
                                                obs.setId_comment_version(id_comment_version);
                                                obs.setId_version_activity(id_version_activity);
                                                obs.setTx_reference(tx_reference);
                                                obs.setNu_comment_activity(nu_comment_activity);
                                                fullData.addObservation(obs);
                                            }
                                        }
                                    }


                                    if (data.has("activity_student")) {
                                        JSONObject activityStudent = data.getJSONObject("activity_student");
                                        if (activityStudent.has("tb_activity_student")) {
                                            JSONArray tb_activity_student = activityStudent.getJSONArray("tb_activity_student");
                                            HashMap<Integer, ArrayList<Attachment>> list = new HashMap<>();
                                            for (int i = 0; i < tb_activity_student.length(); i++) {
                                                JSONObject temp = tb_activity_student.getJSONObject(i);
                                                ArrayList<Attachment> attachments = new ArrayList<>();

                                                int id_activity_student = temp.getInt("id_activity_student");
                                                JSONArray attachmentsArray = temp.getJSONArray("attachment");

                                                for (int j = 0; j < attachmentsArray.length(); j++) {
                                                    JSONObject attachmentTemp = attachmentsArray.getJSONObject(j);
                                                    Attachment attachment = new Attachment();

                                                    String tp_attachment = attachmentTemp.getString("tp_attachment").replaceAll("\\s+", "");
                                                    String nm_file = attachmentTemp.getString("nm_file");
                                                    String nm_system = attachmentTemp.getString("nm_system");
                                                    int id_attachment_srv = attachmentTemp.getInt("id_attachment_srv");
                                                    int id_author = attachmentTemp.getInt("id_author");

                                                    attachment.setTpAttachment(tp_attachment);
                                                    attachment.setNmFile(nm_file);
                                                    attachment.setNmSystem(nm_system);
                                                    attachment.setIdAttachmentSrv(id_attachment_srv);
                                                    attachment.setId_author(id_author);

                                                    attachments.add(attachment);
                                                }

                                                list.put(id_activity_student, attachments);
                                            }
                                            fullData.addAttachments(list);
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                MainActivity.isFullSyncNotSucessful = true;
                                e.printStackTrace();
                            } catch (Exception v) {
                                MainActivity.isFullSyncNotSucessful = true;
                                v.printStackTrace();
                            }

                            // Log.d(tag, "Fim  da request");
                            //EXECUTE INSERTS IN SQLITE
                            fullData.insertDataIntoSQLITE();

                            MainActivity.isFullDataSucessful = true;
                        }
                    }
                } finally {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                MainActivity.isFullSyncNotSucessful = true;
                // Log.e(tag, "Erro  na request");
                Log.e(tag, "erro=" + volleyError.getMessage());
                volleyError.printStackTrace();
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
