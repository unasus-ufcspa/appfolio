package com.ufcspa.unasus.appportfolio.WebClient;

import android.content.Context;

import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.AttachmentActivity;
import com.ufcspa.unasus.appportfolio.Model.AttachmentComment;
import com.ufcspa.unasus.appportfolio.Model.Comentario;
import com.ufcspa.unasus.appportfolio.Model.Reference;
import com.ufcspa.unasus.appportfolio.Model.VersionActivity;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import org.json.JSONException;
import org.json.JSONObject;

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

    private Context context;

    public FullData(Context context) {
        this.versionActs = new LinkedList<VersionActivity>();
        this.comentarios = new LinkedList<Comentario>();
        this.anexos = new LinkedList<Attachment>();
        this.anexosAtivade = new LinkedList<AttachmentActivity>();
        this.anexosComentario = new LinkedList<AttachmentComment>();
        this.references = new LinkedList<Reference>();

        this.context = context;
    }

    public static JSONObject toJSON(String idDevice) {
        JSONObject json = new JSONObject();
        try {
            json.put("sync_request", new JSONObject().put("tb_sync", new JSONObject().put("id_device", idDevice)));
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

    public void addAttachments(Attachment attachment) {
        anexos.add(attachment);
    }

    public void addAttachmentActivity(AttachmentActivity attachmentActivity) {
        anexosAtivade.add(attachmentActivity);
    }

    public void addAttachmentComment(AttachmentComment attachmentComment) {
        anexosComentario.add(attachmentComment);
    }

    public void addReference(Reference reference) {
        references.add(reference);
    }

    public LinkedList<VersionActivity> getVersionActs() {
        return versionActs;
    }

    public LinkedList<Comentario> getComentarios() {
        return comentarios;
    }

    public LinkedList<Attachment> getAnexos() {
        return anexos;
    }

    public LinkedList<AttachmentActivity> getAnexosAtivade() {
        return anexosAtivade;
    }

    public LinkedList<AttachmentComment> getAnexosComentario() {
        return anexosComentario;
    }

    public LinkedList<Reference> getReferences() {
        return references;
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
