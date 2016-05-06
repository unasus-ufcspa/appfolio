package com.ufcspa.unasus.appportfolio.WebClient;

import android.content.Context;
import android.util.Log;

import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.Comentario;
import com.ufcspa.unasus.appportfolio.Model.Notification;
import com.ufcspa.unasus.appportfolio.Model.Reference;
import com.ufcspa.unasus.appportfolio.Model.Tuple;
import com.ufcspa.unasus.appportfolio.Model.VersionActivity;
import com.ufcspa.unasus.appportfolio.Model.basicData.User;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by icaromsc on 18/04/2016.
 */
public class FullData {
    private LinkedList<VersionActivity> versionActs;
    private LinkedList<Comentario> comentarios;
    private HashMap<Integer, ArrayList<Attachment>> anexos;
    private LinkedList<Tuple<Comentario, Attachment>> comentarioAttachment;
    private LinkedList<Reference> references;
    private LinkedList<Notification> notifications;
    private LinkedList<User> users;

    private Context context;

    public FullData(Context context) {
        this.versionActs = new LinkedList<>();
        this.comentarios = new LinkedList<>();
        this.anexos = new HashMap<>();
        this.comentarioAttachment = new LinkedList<>();
        this.references = new LinkedList<>();
        this.notifications = new LinkedList<>();
        this.users = new LinkedList<>();

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

    public void addUser(User u) {
        users.add(u);
    }

    public void addVersion(VersionActivity versionActivity) {
        versionActs.add(versionActivity);
    }

    public void addComments(Comentario comentario) {
        comentarios.add(comentario);
    }

    public void addCommentAttachment(Tuple<Comentario, Attachment> comentarioAttachmentTuple) {
        comentarioAttachment.add(comentarioAttachmentTuple);
    }

    public void addAttachments(HashMap<Integer, ArrayList<Attachment>> list) {
        Integer[] keys = list.keySet().toArray(new Integer[list.keySet().size()]);
        for (int i = 0; i < keys.length; i++) {
            Integer activity_student = keys[i];
            list.put(activity_student, list.get(activity_student));
        }
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

        data.insertNotifications(notifications);
        data.insertVersionActivity(versionActs);
        data.insertReference(references);
        data.insertComments(comentarios);
        data.updateTBUser(users);

        Integer[] keys = anexos.keySet().toArray(new Integer[anexos.keySet().size()]);
        for (int i = 0; i < keys.length; i++) {
            Integer id_activity_student = keys[i];
            ArrayList<Attachment> attachments = anexos.get(id_activity_student);
            for (Attachment a : attachments) {
                int id_attachment = data.insertAttachment(a);
                data.insertAttachmentActivity(id_activity_student, id_attachment);
                // BAIXAR OS ATTACHMENTS!!!!!!!!
//                downloadAttachment("http://blog.concretesolutions.com.br/wp-content/uploads/2015/04/Android1.png", "android.png");
            }
        }

        for (Tuple<Comentario, Attachment> tupla : comentarioAttachment) {
            int id_attachment = data.insertAttachment(tupla.y);
            int id_comment = data.insertComment(tupla.x);

            data.insertAttachComment(id_comment, id_attachment);
        }
    }

    /*
        VERIFICAR O CAMINHO EM QUE VAI SER ADICIONADO
     */
    protected void downloadAttachment(final String urlLink, final String fileName) {
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Content2/");
        if (dir.exists() == false) {
            dir.mkdirs();
        }
        //Save the path as a string value

        try {
            URL url = new URL(urlLink);
            Log.i("FILE_NAME", "File name is " + fileName);
            Log.i("FILE_URLLINK", "File URL is " + url);
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(dir + "/" + fileName);

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;

                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("ERROR DOWNLOADING FILES", "ERROR IS" + e);
        }
    }


}
