package com.ufcspa.unasus.appportfolio.WebClient;

import android.content.Context;

import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.AttachmentActivity;
import com.ufcspa.unasus.appportfolio.Model.AttachmentComment;
import com.ufcspa.unasus.appportfolio.Model.Comentario;
import com.ufcspa.unasus.appportfolio.Model.Notification;
import com.ufcspa.unasus.appportfolio.Model.Reference;
import com.ufcspa.unasus.appportfolio.Model.VersionActivity;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by icaromsc on 18/04/2016.
 */
public class FullData {
    private LinkedList<VersionActivity> versionActs;
    private LinkedList<Comentario> comentarios;
    private LinkedList<Attachment> anexos;
    private LinkedList<AttachmentActivity> anexosAtivade;
    private LinkedList<AttachmentComment> anexosComentario;
    private LinkedList<Reference> references;
    private LinkedList<Notification> notifications;

    private Context context;

    public FullData(Context context) {
        this.versionActs = new LinkedList<>();
        this.comentarios = new LinkedList<>();
        this.anexos = new LinkedList<>();
        this.anexosAtivade = new LinkedList<>();
        this.anexosComentario = new LinkedList<>();
        this.references = new LinkedList<>();
        this.notifications = new LinkedList<>();

        this.context = context;
    }

    public static JSONObject toJSON(String idDevice, int idActivityStudent) {
        JSONObject json = new JSONObject();
        try {
            json.put("fullDataSrvDev_request", new JSONObject().put("id_device", idDevice).put("id_activity_student", idActivityStudent));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public void addVersion(VersionActivity versionActivity) {
        versionActs.add(versionActivity);
    }

    public void addComments(Comentario comentario) {
        comentarios.add(comentario);
    }

    public void addCommentAttachment(Comentario comentario, Attachment attachment) {
//        comentarios.add(comentario);
    }

    public void addAttachments(HashMap<Integer, ArrayList<Attachment>> list) {
//        anexos.add(attachment);
    }

    public void addReference(Reference reference) {
        references.add(reference);
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public Context getContext() {
        return context;
    }

    public synchronized void insertDataIntoSQLITE() {
        DataBaseAdapter data = DataBaseAdapter.getInstance(context);

        data.insertVersionActivity(versionActs);
        data.insertComments(comentarios);
        data.insertAttachment(anexos);
        data.insertAttachmentActivity(anexosAtivade);
        data.insertAttachmentComment(anexosComentario);
        data.insertReference(references);
    }
}
