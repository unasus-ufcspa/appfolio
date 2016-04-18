package com.ufcspa.unasus.appportfolio.WebClient;

import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.AttachmentActivity;
import com.ufcspa.unasus.appportfolio.Model.AttachmentComment;
import com.ufcspa.unasus.appportfolio.Model.Comentario;
import com.ufcspa.unasus.appportfolio.Model.Reference;
import com.ufcspa.unasus.appportfolio.Model.VersionActivity;

import java.util.LinkedList;

/**
 * Created by icaromsc on 18/04/2016.
 */
public class FullSyncData {
    private LinkedList<VersionActivity> versionActs;
    private LinkedList<Comentario> comentarios;
    private LinkedList<Attachment> anexos;
    private LinkedList<AttachmentActivity> anexosAtivade;
    private LinkedList<AttachmentComment> anexosComentario;
    private LinkedList<Reference> references;

    public FullSyncData() {
        this.versionActs = new LinkedList<VersionActivity>();
        this.comentarios= new LinkedList<Comentario>();
        this.anexos= new LinkedList<Attachment>();
        this.anexosAtivade= new LinkedList<AttachmentActivity>();
        this.anexosComentario= new LinkedList<AttachmentComment>();
        this.references= new LinkedList<Reference>();
    }
}
