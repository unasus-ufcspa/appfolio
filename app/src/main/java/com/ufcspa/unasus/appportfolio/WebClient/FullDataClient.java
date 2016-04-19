package com.ufcspa.unasus.appportfolio.WebClient;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.AttachmentActivity;
import com.ufcspa.unasus.appportfolio.Model.AttachmentComment;
import com.ufcspa.unasus.appportfolio.Model.Comentario;
import com.ufcspa.unasus.appportfolio.Model.Reference;
import com.ufcspa.unasus.appportfolio.Model.VersionActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Arthur Zettler on 18/04/2016.
 */
public class FullDataClient extends HttpClient {
    private String method = "fullData";
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
                JSONObject rsp = response;
                String jsonStringName = "fullData_response";

                try {
                    Log.d(tag, "JSON RESPONSE: " + response.toString().replaceAll("\\{", "\n{"));
                    if (response.has("erro")) {
                        Log.e(tag, "sincronizacao de dados full falhou");
                        Log.d(tag, "JSon response receiving:" + response.toString());
                    } else if (response.has("fullData_response")) {
                        Log.d(tag, "JSON POST existe Full Data response");
                        // INSTANCE FULL DATA COMPLEX OBJECT
                        fullData = new FullData(context);

                        //Log.d(tag, "user encontrado!\n cadastrando no banco...");
                        JSONObject resp = new JSONObject();
                        try {
                            resp = response.getJSONObject("fullData_response");
                            Log.d(tag, "JSon response::" + resp.toString());

                            if (resp.has("versionActivity")) {
                                // GET Version Activity
                                JSONObject objPs = resp.getJSONObject("versionActivity");
                                JSONArray tb_version_activity = objPs.getJSONArray("tb_version_activity");
                                // POPULATE Version Activity
                                for (int i = 0; i < tb_version_activity.length(); i++) {
                                    JSONObject temp = tb_version_activity.getJSONObject(i);
                                    VersionActivity va = new VersionActivity();

                                    // GET DATA FROM JSON
                                    int id_activity_student = temp.getInt("id_activity_student");
                                    String tx_activity = temp.getString("tx_activity");
                                    String dt_last_access = temp.getString("dt_last_access");
                                    String dt_submission = temp.getString("dt_submission");
                                    String dt_verification = temp.getString("dt_verification");
                                    int id_version_activit_srv = temp.getInt("id_version_activit_srv");

                                    //POPULATE OBJECT WITH DATA
                                    va.setId_activity_student(id_activity_student);
                                    va.setTx_activity(tx_activity);
                                    va.setDt_last_access(dt_last_access);
                                    va.setDt_submission(dt_submission);
                                    va.setDt_verification(dt_verification);
                                    va.setId_version_activit_srv(id_version_activit_srv);

                                    //ADD TO LINKEDLIST
                                    fullData.addVersion(va);
                                }
                            }

                            if (resp.has("Comment")) {
                                // GET Comments
                                JSONObject objPs = resp.getJSONObject("Comment");
                                JSONArray tb_comment = objPs.getJSONArray("tb_comment");
                                // POPULATE Comments
                                for (int i = 0; i < tb_comment.length(); i++) {
                                    JSONObject temp = tb_comment.getJSONObject(i);
                                    Comentario c = new Comentario();

                                    // GET DATA FROM JSON
                                    int idActivityStudent = temp.getInt("id_activity_student");
                                    int idAuthor = temp.getInt("id_author");
                                    String txtComment = temp.getString("tx_comment");
                                    String txtReference = temp.getString("tx_reference");
                                    String typeComment = temp.getString("tp_comment");
                                    int nuCommentActivity = temp.getInt("nu_comment_activity");
                                    int idCommentSrv = temp.getInt("id_comment_srv");
                                    String dateComment = temp.getString("dt_comment");
                                    String dateSend = temp.getString("dt_send");

                                    //POPULATE OBJECT WITH DATA
                                    c.setIdActivityStudent(idActivityStudent);
                                    c.setIdAuthor(idAuthor);
                                    c.setTxtComment(txtComment);
                                    c.setTxtReference(txtReference);
                                    c.setTypeComment(typeComment);
                                    c.setIdNote(nuCommentActivity);
                                    c.setIdCommentSrv(idCommentSrv);
                                    c.setDateComment(dateComment);
                                    c.setDateSend(dateSend);

                                    //ADD TO LINKEDLIST
                                    fullData.addComments(c);
                                }
                            }

                            if (resp.has("AttachComment")) {
                                // GET AttachComment
                                JSONObject objPs = resp.getJSONObject("AttachComment");
                                JSONArray tb_attach_comment = objPs.getJSONArray("tb_attach_comment");
                                // POPULATE AttachComment
                                for (int i = 0; i < tb_attach_comment.length(); i++) {
                                    JSONObject temp = tb_attach_comment.getJSONObject(i);
                                    AttachmentComment ac = new AttachmentComment();

                                    // GET DATA FROM JSON
                                    int id_attachment = temp.getInt("id_attachment");
                                    int id_comment = temp.getInt("id_comment");
//                                    int id_attach_comment_srv = temp.getInt("id_attach_comment_srv");

                                    //POPULATE OBJECT WITH DATA
                                    ac.setId_attachment(id_attachment);
                                    ac.setId_comment(id_comment);
//                                    ac.setId_srv(id_attach_comment_srv);

                                    //ADD TO LINKEDLIST
                                    fullData.addAttachmentComment(ac);
                                }
                            }

                            if (resp.has("AttachActivity")) {
                                // GET AttachActivity
                                JSONObject objPs = resp.getJSONObject("AttachActivity");
                                JSONArray tb_attach_activity = objPs.getJSONArray("tb_attach_activity");
                                // POPULATE AttachActivity
                                for (int i = 0; i < tb_attach_activity.length(); i++) {
                                    JSONObject temp = tb_attach_activity.getJSONObject(i);
                                    AttachmentActivity aa = new AttachmentActivity();

                                    // GET DATA FROM JSON
                                    int id_attachment = temp.getInt("id_attachment");
                                    int id_activity_student = temp.getInt("id_activity_student");
//                                    int id_attach_activity_srv = temp.getInt("id_attach_activity_srv");

                                    //POPULATE OBJECT WITH DATA
                                    aa.setId_attachment(id_attachment);
                                    aa.setId_activity_student(id_activity_student);
//                                    aa.setId_srv(id_attach_activity_srv);

                                    //ADD TO LINKEDLIST
                                    fullData.addAttachmentActivity(aa);
                                }
                            }

                            if (resp.has("Attachment")) {
                                // GET Attachment
                                JSONObject objPs = resp.getJSONObject("Attachment");
                                JSONArray tb_attachment = objPs.getJSONArray("tb_attachment");
                                // POPULATE Attachment
                                for (int i = 0; i < tb_attachment.length(); i++) {
                                    JSONObject temp = tb_attachment.getJSONObject(i);
                                    Attachment a = new Attachment();

                                    // GET DATA FROM JSON
                                    String ds_local_path = temp.getString("ds_local_path");
                                    String ds_server_path = temp.getString("ds_server_path");
                                    String tp_attachment = temp.getString("tp_attachment");
                                    String nm_file = temp.getString("nm_file");
                                    int id_attachment_srv = temp.getInt("id_attachment_srv");

                                    //POPULATE OBJECT WITH DATA
                                    a.setLocalPath(ds_local_path);
                                    a.setServerPath(ds_server_path);
                                    a.setType(tp_attachment);
                                    a.setNameFile(nm_file);
                                    a.setIdAttachmentSrv(id_attachment_srv);

                                    //ADD TO LINKEDLIST
                                    fullData.addAttachments(a);
                                }
                            }

                            if (resp.has("Reference")) {
                                // GET Reference
                                JSONObject objPs = resp.getJSONObject("Reference");
                                JSONArray tb_reference = objPs.getJSONArray("tb_reference");
                                // POPULATE Reference
                                for (int i = 0; i < tb_reference.length(); i++) {
                                    JSONObject temp = tb_reference.getJSONObject(i);
                                    Reference r = new Reference();

                                    // GET DATA FROM JSON
                                    int id_activity_student = temp.getInt("id_activity_student");
                                    String ds_url = temp.getString("ds_url");
                                    int id_reference_srv = temp.getInt("id_reference_srv");

                                    //POPULATE OBJECT WITH DATA
                                    r.setIdActStudent(id_activity_student);
                                    r.setDsUrl(ds_url);
                                    r.setIdRefSrv(id_reference_srv);

                                    //ADD TO LINKEDLIST
                                    fullData.addReference(r);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception v) {
                            v.printStackTrace();
                        }
                    }
                } finally {
                    Log.d(tag, "Fim  da request");
                    //EXECUTE INSERTS IN SQLITE
                    fullData.insertDataIntoSQLITE();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(tag, "Erro  na request");
                Log.e(tag, "erro=" + volleyError.getMessage());
                volleyError.printStackTrace();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjReq);
    }

}
