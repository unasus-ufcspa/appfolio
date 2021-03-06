package com.ufcspa.unasus.appportfolio.webClient;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ufcspa.unasus.appportfolio.database.DataBase;
import com.ufcspa.unasus.appportfolio.model.Annotation;
import com.ufcspa.unasus.appportfolio.model.Attachment;
import com.ufcspa.unasus.appportfolio.model.AttachmentComment;
import com.ufcspa.unasus.appportfolio.model.Comentario;
import com.ufcspa.unasus.appportfolio.model.Observation;
import com.ufcspa.unasus.appportfolio.model.Singleton;
import com.ufcspa.unasus.appportfolio.model.Sync;
import com.ufcspa.unasus.appportfolio.model.User;
import com.ufcspa.unasus.appportfolio.model.VersionActivity;
import com.ufcspa.unasus.appportfolio.model.basicData.ActivityStudent;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
    private LinkedHashMap observationByVersions;
    private LinkedList<Comentario> comentarios;
    private LinkedList<AttachmentComment> attachmentComments;
    private LinkedList<User> users;
    private LinkedList<VersionActivity> versions;
    private LinkedList<Integer> notices;
    private LinkedHashMap<Integer, LinkedList<Attachment>> attachments;
    private LinkedList<ActivityStudent> activityStudents;
    private LinkedList<Annotation> annotations;
    private DataBase data;
    private String tbComm="tb_comment";
    private String tbAttachComm="tb_attach_comment";
    private String tbVers="tb_version_activity";
    private String tbUser = "tb_user";
    private String tbCommVers = "tb_comment_version";
    private String tbNotice = "tb_notice";
    private String tbAttachActivity = "tb_attach_activity";
    private String tbActivityStudent= "tb_activity_student";
    private String tbAnnotation= "tb_annotation";
    private Singleton singleton;
    //ids to response
    //-2 is default value
    private int id=-2;
    private int idserver=-2;


    public SendData(Context context) {
        this.context = context;
        data = DataBase.getInstance(context);
        sincronias= new ArrayList<>();
        versions = new LinkedList<>();
        comentarios = new LinkedList<>();
        attachmentComments = new LinkedList<>();
        commentsByVersions = new LinkedHashMap<>();
        observationByVersions= new LinkedHashMap<Integer, LinkedList<Observation>>();
        notices = new LinkedList<>();
        idSync = new LinkedList<>();
        attachments = new LinkedHashMap<>();
        activityStudents = new LinkedList<>();
        annotations = new LinkedList<>();
        singleton = Singleton.getInstance();
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
        if (dadosAgrupados.get(tbAttachComm) != null) {
            attachmentComments = (LinkedList) data.getAttachCommentsByIDs(dadosAgrupados.get(tbAttachComm));
        }
        if (dadosAgrupados.get(tbVers) != null) {
            versions = (LinkedList) data.getVersionActivitiesByIDs(dadosAgrupados.get(tbVers));
//            for (VersionActivity v : versions) {
//                commentsByVersions.put(v.getId_version_activity(), data.getCommentVersion(v.getId_version_activity()));
//            }
        }
        if (dadosAgrupados.get(tbUser) != null) {
            users = (LinkedList) data.getUsersByIDs(dadosAgrupados.get(tbUser));
        }
        if (dadosAgrupados.get(tbCommVers) != null) {
           // commentsByVersions = data.getCommentVersion(dadosAgrupados.get(tbCommVers));
            observationByVersions= data.getObservationByVersions(dadosAgrupados.get(tbCommVers));
        }


        if (dadosAgrupados.get(tbNotice) != null) {
            notices = dadosAgrupados.get(tbNotice);
        }
        if (dadosAgrupados.get(tbAttachActivity) != null) {
            attachments = data.getAttachments(dadosAgrupados.get(tbAttachActivity));
        }
        if (dadosAgrupados.get(tbActivityStudent) != null) {
            activityStudents = data.getActivitiesStudentByIds(dadosAgrupados.get(tbActivityStudent));
        }
        if (dadosAgrupados.get(tbAnnotation) != null) {
            annotations = data.getAnnotationsById(dadosAgrupados.get(tbAnnotation));
        }
    }

    public void insertDataOnResponse(){
        // insert data by json response sendfulldata
        if(dadosResponse.get(tbComm)!=null){
            Log.d("json send full data ", "atualizando tabela " + tbComm + "...");
            data.updateCommentBySendFullData(dadosResponse.get(tbComm));
            Log.d("json send full data ", "conseguiu atualizar com sucesso comment to server");
        }
//        if(dadosResponse.get(tbAttachComm)!=null){
//            Log.d("json send full data ", "atualizando tabela " + tbAttachComm + "...");
//            data.updateCommentBySendFullData(dadosResponse.get(tbAttachComm));
//            Log.d("json send full data ", "conseguiu atualizar com sucesso comment to server");
//        }
        if (dadosResponse.get(tbVers) != null) {
            Log.d("json send full data ", "atualizando tabela " + tbVers + "...");
            data.updateVersionsBySendFullData(dadosResponse.get(tbVers));
            Log.d("json send full data ", "conseguiu atualizar com sucesso id server");
        }

        if(dadosResponse.get(tbCommVers)!= null){
            Log.d("json send full data ", "atualizando tabela " + tbCommVers + "...");
            data.updateCommentVersionBySendFullData(dadosResponse.get(tbCommVers));
            Log.d("sendfulldataResponse ", "conseguiu atualizar com sucesso id server da tb_comm_version");
        }

        if(dadosResponse.get(tbAnnotation)!= null){
            Log.d("json send full data ", "atualizando tabela " + tbAnnotation + "...");
            data.updateAnnotationsBySendFullData(dadosResponse.get(tbAnnotation));
            Log.d("sendfulldataResponse ", "conseguiu atualizar com sucesso id server da tb_annotation");
        }

        data.deleteSync(idSync);
        for (Integer id : attachments.keySet()) {
            for (Attachment a : attachments.get(id)) {
                Log.d("sendMedia","send data from:"+a.getNmSystem());
                Log.d("sendMedia","url"+"http://" + new HttpClient(context).ip + "/webfolio/app_dev.php/upload");
                uploadMultipart(context, "http://" + new HttpClient(context).ip + "/webfolio/app_dev.php/upload", a.getNmSystem());
            }
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("call.connection.action"));
    }


/*
    // versão antiga
    public JSONObject GenerateJSONOLD(String idDevice){
        getSyncs();// obtem lista de sincronizações
        getDataFromTables(); // obtem listas das tabelas a serem enviadas

        //gera json a partir das listas de tabelas

        JSONObject jsonFinal= new JSONObject();
        JSONObject jsonPseudoFinal= new JSONObject();
        JSONObject reference = new JSONObject();
        JSONObject device = new JSONObject();

        JSONArray jsonArrayVersions = new JSONArray();
        JSONArray jsonComments = new JSONArray();
        JSONArray jsonNotice = new JSONArray();

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
            if (btn_versions != null) {
                for (VersionActivity v : btn_versions) {
                    JSONObject jsonVersion = new JSONObject();
                    jsonVersion.put("id_version_activity", v.getId_version_activity());
                    if (v.getId_version_activit_srv() != -1)
                        jsonVersion.put("id_version_activity_srv", v.getId_version_activit_srv());
                    else
                        jsonVersion.put("id_version_activity_srv", "");
                    jsonVersion.put("id_activity_student", v.getId_activity_student());
                    jsonVersion.put("tx_activity", v.getTx_activity());
                    jsonVersion.put("dt_last_access", v.getDt_last_access());

                    Log.wtf("json send data:", jsonVersion.toString());

                    JSONArray jsonCommentsByVersion = new JSONArray();
                    if (commentsByVersions.containsKey(v.getId_version_activity())) {
                        LinkedList<Comentario> commentByVersion = commentsByVersions.get(v.getId_version_activity());
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
                    }
                    JSONObject jTb_comment = new JSONObject();
                    jTb_comment.put("tb_comment", jsonCommentsByVersion);
                    jsonVersion.put("comment", jTb_comment);

                    jsonArrayVersions.put(jsonVersion);
                }
            }

            if (commentsByVersions != null && commentsByVersions.size() != 0) {
                JSONObject jsonVersion = new JSONObject();
                for (Integer versionId : commentsByVersions.keySet()) {
                    VersionActivity version = data.getVersionActivitiesByID(versionId);

                    jsonVersion.put("id_version_activity", version.getId_version_activity());
                    jsonVersion.put("id_version_activity_srv", version.getId_version_activit_srv());
                    jsonVersion.put("id_activity_student", version.getId_activity_student());

                    if (singleton.isFirstSpecificComment)
                        jsonVersion.put("tx_activity", version.getTx_activity());
                    else
                        jsonVersion.put("tx_activity", "");

                    jsonVersion.put("dt_last_access", "");

                    JSONArray jsonCommentsByVersion = new JSONArray();
                    for (Comentario comentario : commentsByVersions.get(versionId)) {
                        JSONObject jComment = new JSONObject();
                        jComment.put("id_comment", comentario.getIdComment());
                        jComment.put("id_activity_student", comentario.getIdActivityStudent());
                        jComment.put("id_author", comentario.getIdAuthor());
                        jComment.put("tx_comment", comentario.getTxtComment());
                        jComment.put("tx_reference", comentario.getTxtReference());
                        jComment.put("tp_comment", comentario.getTypeComment());
                        jComment.put("dt_comment", comentario.getDateComment());
                        jComment.put("nu_comment_activity", comentario.getIdNote());

                        jsonCommentsByVersion.put(jComment);
                    }
                    JSONObject jTb_comment = new JSONObject();
                    jTb_comment.put("tb_comment", jsonCommentsByVersion);
                    jsonVersion.put("comment", jTb_comment);
                }
                jsonArrayVersions.put(jsonVersion);
                singleton.isFirstSpecificComment = false;
            }

            if (users != null && users.size() > 0) {
                jsonUser.put("id_user", users.getFirst().getIdUser());
                jsonUser.put("nm_user", users.getFirst().getName());
                jsonUser.put("nu_identification", users.getFirst().getIdCode());
                jsonUser.put("ds_email", users.getFirst().getEmail());
                jsonUser.put("nu_cellphone", users.getFirst().getCellphone());
                jsonUser.put("im_photo", users.getFirst().getPhoto());
            }

            if (notices != null && notices.size() != 0) {
                for (Integer notice : notices) {
                    JSONObject jnotice = new JSONObject();
                    jnotice.put("id_notice", notice);

                    jsonNotice.put(jnotice);
                }
            }

            if (attachments != null && attachments.size() != 0) {
                JSONObject attachActivityStudent = new JSONObject();
                JSONArray attachTb_activity_student = new JSONArray();
                for (Integer idActivityStudent : attachments.keySet()) {
                    JSONObject attachAttachment = new JSONObject();
                    JSONArray attachment = new JSONArray();

                    for (Attachment a : attachments.get(idActivityStudent)) {
                        JSONObject attach = new JSONObject();
                        attach.put("id_attachment", a.getIdAttachment());
                        attach.put("tp_attachment", a.getTpAttachment());
                        attach.put("nm_file", a.getNmFile());
                        String[] aux = a.getNmSystem().split("/");
                        String path = a.getNmSystem();
                        if (aux.length > 0)
                            path = aux[aux.length - 1];
                        attach.put("nm_system", path);
                        attach.put("id_attachment_srv", a.getidAttachmentSrv());

                        attachment.put(attach);
                    }
                    attachAttachment.put("id_activity_student", idActivityStudent).put("attachment", attachment);

                    attachTb_activity_student.put(attachAttachment);
                }

                attachActivityStudent.put("tb_activity_student", attachTb_activity_student);
                jsonPseudoFinal.put("activityStudent", attachActivityStudent);
            }

            //mount device
            device.put("id_device",idDevice);

            //mount pseudo final
            jsonPseudoFinal.put("device",device);
            jsonPseudoFinal.put("comment", new JSONObject().put("tb_comment", jsonComments));
            jsonPseudoFinal.put("version", new JSONObject().put("tb_version_activity", jsonArrayVersions));
            jsonPseudoFinal.put("user", new JSONObject().put("tb_user", jsonUser));
            jsonPseudoFinal.put("notice", new JSONObject().put("tb_notice", jsonNotice));

            jsonFinal.put("fullDataDevSrv_request", jsonPseudoFinal);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("jsonsendfulldata ", jsonFinal.toString().replaceAll("\\{", "\n{"));
        return jsonFinal;
    }*/

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
        JSONArray jsonNotice = new JSONArray();
        JSONArray jsonAnnotations = new JSONArray();

        JSONObject jsonUser = new JSONObject();
        boolean entrou = false;

        try {
            // mount JSON comment
            if(comentarios!=null) {
                for (Comentario comment : comentarios) {
                    JSONObject jsonComment = new JSONObject();
                    jsonComment.put("id_comment", comment.getIdComment());
                    jsonComment.put("id_activity_student", comment.getIdActivityStudent());

                    if(comment.getId_comment_version()<=0){
                        jsonComment.put("id_comment_version", "");
                    }else{
                        jsonComment.put("id_comment_version", comment.getId_comment_version());

                    }

                    if(data.isSync("tb_comment_version",comment.getId_comment_version())){
                        int idServer =data.getIdCommentVersionSrv(comment.getId_comment_version());
                        jsonComment.put("id_comment_version_srv",idServer);
                    }else{
                        entrou = true;
                        jsonComment.put("id_comment_version_srv","");
                    }


                    //jsonComment.put("id_comment_srv", comment.getIdCommentSrv());
                    jsonComment.put("id_author", comment.getIdAuthor());
                    jsonComment.put("tx_comment", comment.getTxtComment());
                    jsonComment.put("tp_comment", comment.getTypeComment());
                    jsonComment.put("dt_comment", comment.getDateComment());

                    if(attachmentComments!=null) {
                        for (AttachmentComment attachmentComment : attachmentComments) {
                            if (attachmentComment.getId_comment() == comment.getIdComment()) {
                                JSONObject jsonAttachComment = new JSONObject();
                                Attachment attachment = data.getAttachmentByID(attachmentComment.getId_attachment());
                                jsonAttachComment.put("id_attachment", attachment.getIdAttachment());
                                jsonAttachComment.put("tp_attachment",attachment.getTpAttachment());
                                jsonAttachComment.put("nm_file",attachment.getNmFile());
                                String path = null;
                                if (attachment.getNmSystem()!=null) {
                                    String[] aux = attachment.getNmSystem().split("/");
                                    path = attachment.getNmSystem();
                                    if (aux.length > 0)
                                        path = aux[aux.length - 1];
                                }
                                jsonAttachComment.put("nm_system",path);
                                jsonAttachComment.put("id_attachment_srv", attachmentComment.getId_srv());
                                jsonComment.put("attachment",jsonAttachComment);
                            }
                        }
                    }
                    jsonComments.put(jsonComment);
                }
            }

            if (versions != null && singleton.portfolioClass!=null) {
                for (VersionActivity v : versions) {
                    JSONObject jsonVersion = new JSONObject();
                    jsonVersion.put("id_version_activity", v.getId_version_activity());
                    if (singleton.portfolioClass.getPerfil().equals("T"))
                    {
                        jsonVersion.put("id_version_activity_srv", v.getId_version_activit_srv());
                    }else{
                        if (data.isSync("tb_version_activity",v.getId_version_activit_srv()))
                            jsonVersion.put("id_version_activity_srv", v.getId_version_activit_srv());
                        else
                            jsonVersion.put("id_version_activity_srv", "");
                    }

                    jsonVersion.put("id_activity_student", v.getId_activity_student());

                    String tx_activity = v.getTx_activity();
                    tx_activity = tx_activity.replaceAll("<font","<span");
                    tx_activity = tx_activity.replaceAll(Environment.getExternalStorageDirectory()+"/Android/data/com.ufcspa.unasus.appportfolio/files/images" + File.separator,"");
                    tx_activity = tx_activity.replaceAll("</font>","</span>");

                    jsonVersion.put("tx_activity", tx_activity);
                    jsonVersion.put("dt_last_access", v.getDt_last_access());
                    jsonVersion.put("dt_submission", v.getDt_submission());

                    Log.d("json send data:", jsonVersion.toString());

                    JSONArray jsonCommentsByVersion = new JSONArray();
                    if (observationByVersions.containsKey(v.getId_version_activit_srv())) {
                        LinkedList<Observation> observationLinkedList = (LinkedList<Observation>) observationByVersions.get(v.getId_version_activit_srv());
                        for (Observation c : observationLinkedList) {
                            JSONObject jComment = new JSONObject();
                            jComment.put("id_comment_version", c.getId_comment_version());
                            jComment.put("id_version_activity", c.getId_version_activity());
                            jComment.put("tx_reference", c.getTx_reference());
                            jComment.put("nu_comment_activity", c.getNu_comment_activity());
                            jComment.put("nu_initial_pos", c.getNu_initial_position());
                            jComment.put("nu_size", c.getNu_size());
                            jComment.put("id_comment_version_srv",c.getId_comment_version_srv());

                            jsonCommentsByVersion.put(jComment);
                        }
                    }
//                    JSONObject jTb_comment = new JSONObject();
//                    jTb_comment.put("tb_comment", jsonCommentsByVersion);
//                    jsonVersion.put("comment", jTb_comment);

                    //jTb_comment.put("tb_comment", jsonCommentsByVersion);
                    if (entrou) {
                        jsonVersion.put("tb_comment_version", jsonCommentsByVersion);
                    }

                    jsonArrayVersions.put(jsonVersion);
                    Log.d("json send data:", jsonVersion.toString());


                }
            }
            if (annotations != null) {
                for (Annotation a : annotations) {
                    JSONObject jsonAnnotation = new JSONObject();
                    jsonAnnotation.put("id_annotation", a.getIdAnnotation());

                    jsonAnnotation.put("id_user", a.getIdUser());
                    jsonAnnotation.put("ds_annotation", a.getDsAnnotation());

                    Log.d("json send data:", jsonAnnotation.toString());

                    jsonAnnotations.put(jsonAnnotation);
                    Log.d("json send data:", jsonAnnotation.toString());
                }
            }

//            if (commentsByVersions != null && commentsByVersions.size() != 0) {
//                JSONObject jsonVersion = new JSONObject();
//                for (Integer versionId : commentsByVersions.keySet()) {
//                    VersionActivity version = data.getVersionActivitiesByID(versionId);
//
//                    jsonVersion.put("id_version_activity", version.getId_version_activity());
//                    jsonVersion.put("id_version_activity_srv", version.getId_version_activit_srv());
//                    jsonVersion.put("id_activity_student", version.getId_activity_student());
//
//                    if (singleton.isFirstSpecificComment)
//                        jsonVersion.put("tx_activity", version.getTx_activity());
//                    else
//                        jsonVersion.put("tx_activity", "");
//
//                    jsonVersion.put("dt_last_access", "");
//
//                    JSONArray jsonCommentsByVersion = new JSONArray();
//                    for (Comentario comentario : commentsByVersions.get(versionId)) {
//                        JSONObject jComment = new JSONObject();
//                        jComment.put("id_comment", comentario.getIdComment());
//                        jComment.put("id_activity_student", comentario.getIdActivityStudent());
//                        jComment.put("id_author", comentario.getIdAuthor());
//                        jComment.put("tx_comment", comentario.getTxtComment());
//                        jComment.put("tx_reference", comentario.getTxtReference());
//                        jComment.put("tp_comment", comentario.getTypeComment());
//                        jComment.put("dt_comment", comentario.getDateComment());
//                        jComment.put("nu_comment_activity", comentario.getIdNote());
//
//                        jsonCommentsByVersion.put(jComment);
//                    }
//                    JSONObject jTb_comment = new JSONObject();
//                    jTb_comment.put("tb_comment", jsonCommentsByVersion);
//                    jsonVersion.put("comment", jTb_comment);
//                }
//                jsonArrayVersions.put(jsonVersion);
//                singleton.isFirstSpecificComment = false;
//            }

            if (users != null && users.size() > 0) {
                jsonUser.put("id_user", users.getFirst().getIdUser());
                jsonUser.put("nm_user", users.getFirst().getName());
                jsonUser.put("nu_identification", users.getFirst().getIdCode());
                jsonUser.put("ds_email", users.getFirst().getEmail());
                jsonUser.put("nu_cellphone", users.getFirst().getCellphone());
                jsonUser.put("im_photo", users.getFirst().getPhoto());
            }

            if (notices != null && notices.size() != 0) {
                for (Integer notice : notices) {
                    JSONObject jnotice = new JSONObject();
                    jnotice.put("id_notice", notice);

                    jsonNotice.put(jnotice);
                }
            }

            if (activityStudents != null && activityStudents.size() > 0) {
                JSONObject activitiesStudents = new JSONObject();

                JSONArray activitiesStudent = new JSONArray();
                for (com.ufcspa.unasus.appportfolio.model.basicData.ActivityStudent activityStudent:activityStudents){
                    JSONObject temp = new JSONObject();
                    temp.put("id_activity_student", activityStudent.getIdActivityStudent());
                    temp.put("dt_conclusion", activityStudent.getDt_conclusion());

                    activitiesStudent.put(temp);
                }

                activitiesStudents.put("tb_activity_student", activitiesStudent);
                jsonPseudoFinal.put("activityStudent", activitiesStudents);
            }

            if (attachments != null && attachments.size() != 0) {
                JSONObject attachActivityStudent = new JSONObject();
                JSONArray attachTb_activity_student = new JSONArray();
                for (Integer idActivityStudent : attachments.keySet()) {
                    JSONObject attachAttachment = new JSONObject();
                    JSONArray attachment = new JSONArray();

                    for (Attachment a : attachments.get(idActivityStudent)) {
                        JSONObject attach = new JSONObject();
                        attach.put("id_attachment", a.getIdAttachment());
                        attach.put("tp_attachment", a.getTpAttachment());
                        attach.put("nm_file", a.getNmFile());
                        String[] aux = a.getNmSystem().split("/");
                        String path = a.getNmSystem();
                        if (aux.length > 0)
                            path = aux[aux.length - 1];
                        attach.put("nm_system", path);
                        attach.put("id_attachment_srv", a.getidAttachmentSrv());

                        if (a.getidAttachmentSrv()==0) {
                            attachment.put(attach);
                        }
                    }
                    attachAttachment.put("id_activity_student", idActivityStudent).put("attachment", attachment);

                    attachTb_activity_student.put(attachAttachment);
                }

                attachActivityStudent.put("tb_activity_student", attachTb_activity_student);
                jsonPseudoFinal.put("activityStudent", attachActivityStudent);
            }
            Singleton.getInstance().device= DataBase.getInstance(context).getDevice();
            Log.d("sendData", "GET FROM DEVICE: "+Singleton.getInstance().device);

            //mount device
            device.put("ds_hash",idDevice);
            device.put("id_user",Singleton.getInstance().device.get_id_user());

            //mount pseudo final
            jsonPseudoFinal.put("device",device);
            jsonPseudoFinal.put("comment", new JSONObject().put("tb_comment", jsonComments));
            jsonPseudoFinal.put("version", new JSONObject().put("tb_version_activity", jsonArrayVersions));
            jsonPseudoFinal.put("user", new JSONObject().put("tb_user", jsonUser));
            jsonPseudoFinal.put("notice", new JSONObject().put("tb_notice", jsonNotice));
            jsonPseudoFinal.put("annotation", new JSONObject().put("tb_annotation", jsonAnnotations));

            jsonFinal.put("fullDataDevSrv_request", jsonPseudoFinal);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("jsonsendfulldata ", jsonFinal.toString().replaceAll("\\{", "\n{"));
        Log.d("sendData", "JSON REQUEST: " + jsonFinal.toString().replaceAll("\\{", "\n{"));
        //System.out.println("sendData"+ "JSON REQUEST: " + jsonFinal.toString().replaceAll("\\{", "\n{"));
        return jsonFinal;
    }



    private void clear(){
        id=-2;
        idserver=-2;
    }

    //https://github.com/gotev/android-upload-service
    public void uploadMultipart(final Context context, String server, String filePath) {
        try {
            String uploadId = new MultipartUploadRequest(context, server)
                    .addFileToUpload(filePath, "arquivo")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload();
        } catch (Exception exc) {
            Log.e("AndroidUploadService", exc.getMessage(), exc);
        }
    }

}
