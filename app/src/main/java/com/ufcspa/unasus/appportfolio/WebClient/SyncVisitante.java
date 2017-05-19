package com.ufcspa.unasus.appportfolio.WebClient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.coolerfall.download.DownloadCallback;
import com.coolerfall.download.DownloadManager;
import com.coolerfall.download.DownloadRequest;
import com.ufcspa.unasus.appportfolio.Model.Annotation;
import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.Comentario;
import com.ufcspa.unasus.appportfolio.Model.CommentVersion;
import com.ufcspa.unasus.appportfolio.Model.Notification;
import com.ufcspa.unasus.appportfolio.Model.Observation;
import com.ufcspa.unasus.appportfolio.Model.Reference;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.Model.Tuple;
import com.ufcspa.unasus.appportfolio.Model.VersionActivity;
import com.ufcspa.unasus.appportfolio.Model.basicData.User;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Steffano on 12/07/2016.
 */
public class SyncVisitante {

    private LinkedList<Notification> notifications;
    private LinkedList<com.ufcspa.unasus.appportfolio.Model.basicData.Activity> activities;
    private LinkedList<Reference> references;
    private LinkedList<Annotation> annotations;
    private LinkedList<User> users;
    private LinkedList<Comentario> comments;
    private HashMap<Integer, ArrayList<Attachment>> anexos;
    private LinkedList<Tuple<Comentario, Attachment>> comentarioAttachment;
    private LinkedList<VersionActivity> versionActivities;
    private LinkedList<CommentVersion> commentVersions;
    private LinkedList<Observation> observations;
    private Context context;

    public SyncVisitante(Context c) {
        notifications = new LinkedList<Notification>();
        activities = new LinkedList<com.ufcspa.unasus.appportfolio.Model.basicData.Activity>();
        references = new LinkedList<Reference>();
        annotations = new LinkedList<Annotation>();
        users = new LinkedList<>();
        comments = new LinkedList<Comentario>();
        anexos = new HashMap<>();
        comentarioAttachment = new LinkedList<>();
        versionActivities = new LinkedList<VersionActivity>();
        commentVersions = new LinkedList<CommentVersion>();
        observations= new LinkedList<Observation>();
        this.context = c;
    }

    public static JSONObject toJSON(int idUser, String idDevice) {
        JSONObject json = new JSONObject();
        JSONObject firstSync = new JSONObject();
        try {
            firstSync.put("ds_hash", idDevice);
            firstSync.put("id_user", idUser);
            json.put("syncVisitante_request", firstSync);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("syncVisitante_request",firstSync.toString());
        return json;
    }

    public void addUser(User u) {
        users.add(u);
    }
    public void addNotification(Notification notification){
        notifications.add(notification);
    }
    public void addCommentVersion(CommentVersion cv) {
        commentVersions.add(cv);
    }
    public void addObservation(Observation obs) {
        observations.add(obs);
    }
    public void addActivities(com.ufcspa.unasus.appportfolio.Model.basicData.Activity activity){
        activities.add(activity);
    }
    public void addReference(Reference reference){
        references.add(reference);
    }
    public void addAnnotation(Annotation annotation){
        annotations.add(annotation);
    }
    public void addComments(Comentario comment){
        comments.add(comment);
    }

    public void addCommentAttachment(Tuple<Comentario, Attachment> comentarioAttachmentTuple) {
        comentarioAttachment.add(comentarioAttachmentTuple);
    }

    public void addAttachments(HashMap<Integer, ArrayList<Attachment>> list) {
        Integer[] keys = list.keySet().toArray(new Integer[list.keySet().size()]);
        for (int i = 0; i < keys.length; i++) {
            Integer activity_student = keys[i];
            anexos.put(activity_student, list.get(activity_student));
        }
    }
    public void addVersionActivity(VersionActivity versionActivity){
        versionActivities.add(versionActivity);
    }

    /*public LinkedList<Notification> getNotifications() {
        return notifications;
    }
    public LinkedList<com.ufcspa.unasus.appportfolio.Model.basicData.Activity> getActivities() {
        return activities;
    }*/
    public LinkedList<Reference> getReferences() {
        return references;
    }
    public LinkedList<Annotation> getAnnotations() {
        return annotations;
    }
    public LinkedList<Comentario> getComments() {
        return comments;
    }

    public LinkedList<VersionActivity> getVersionActivities() {
        return versionActivities;
    }

    public synchronized void insertDataIntoSQLITE() {
        final DataBaseAdapter data = DataBaseAdapter.getInstance(context);

//        data.insertNotifications(notifications);
        data.insertTBActivity(activities);
        data.insertReference(references);
        data.insertAnnotation(annotations);
        data.insertComments(comments);
        data.insertVersionActivity(versionActivities);
        ArrayList<VersionActivity> tmp = new ArrayList<VersionActivity>();
        /*if (Singleton.getInstance().firstSync && data.selectListClassAndUserType(Singleton.getInstance().user.getIdUser()).size()-1 >= 0 && data.selectListClassAndUserType(Singleton.getInstance().user.getIdUser()).get(data.selectListClassAndUserType(Singleton.getInstance().user.getIdUser()).size()-1).getPerfil().equalsIgnoreCase("S")) {
            for (VersionActivity versionAct:versionActivities) {
                if (versionAct.getId_version_activit_srv() == data.getLastIDVersionActivitySrv(versionAct.getId_activity_student()))
                    tmp.add(versionAct);
            }
            for (VersionActivity versionAct:tmp) {
                    versionAct.setId_version_activit_srv(0);
                    data.insertVersionActivity(versionAct);
            }
        }*/

        for (CommentVersion cv: commentVersions) {
            data.insertCommentVersion(cv);
        }
        for(Observation obs: observations){
            data.insertObservationByVersion(obs);
        }
        data.updateTBUser(users);

        Integer[] keys = anexos.keySet().toArray(new Integer[anexos.keySet().size()]);
        for (int i = 0; i < keys.length; i++) {
            Integer id_activity_student = keys[i];
            ArrayList<Attachment> attachments = anexos.get(id_activity_student);
            DownloadManager manager = new DownloadManager();
                for (Attachment a : attachments) {
                    final int id_attachment = data.insertAttachmentDownload(a);
                    data.insertAttachmentActivity(id_activity_student, id_attachment);
                    // BAIXAR OS ATTACHMENTS!!!!!!!!
                    if (!Singleton.getInstance().guestUser) {
                        String filePath = Environment.getExternalStorageDirectory()+"/Android/data/com.ufcspa.unasus.appportfolio/files/images" + File.separator + a.getNmSystem();
                        File file = new File(filePath);
                        if (!file.exists()) {
                            Log.d("File Path", filePath);
                            DownloadRequest request = new DownloadRequest()
                                    .setUrl("http://" + new HttpClient(context).ip + "/webfolio/app_dev.php/download/" + a.getNmSystem())
                                    .setDestFilePath(filePath)
                                    .setDownloadCallback(new DownloadCallback() {
                                        @Override
                                        public void onSuccess(int downloadId, String filePath) {
                                            super.onSuccess(downloadId, filePath);
                                            data.updateAttachmentFlDownload(id_attachment);
                                            if (filePath.contains(".mp4")) {
                                                saveSmallImage(filePath);
                                            }
                                        }

                                        @Override
                                        public void onFailure(int downloadId, int statusCode, String errMsg) {
                                            super.onFailure(downloadId, statusCode, errMsg);
                                        }
                                    });
                            manager.add(request);
                        } else
                            Log.d("anexos",filePath+" já existe e não baixado");
                        //                downloadAttachment("http://stuffpoint.com/stardoll/image/54056-stardoll-sdfs.jpg" + a.getNmSystem(), a.getNmFile());
                    }
                }
        }

        for (Tuple<Comentario, Attachment> tupla : comentarioAttachment) {
            int id_attachment = data.insertAttachment(tupla.y);
            int id_comment = data.insertComment(tupla.x);

            data.insertAttachComment(id_comment, id_attachment);
        }

        if (comments.size() > 0)
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("call.connection.action"));
    }

    public String saveSmallImage(String videoPath) {
        String[] path = videoPath.split("/");
        String[] secondPath = path[path.length - 1].split("\\.");
        secondPath[0] += "_video";
        path[path.length - 1] = secondPath[0] + "." + secondPath[1];

        String newPath = "";
        for (int i = 1; i < path.length; i++)
            newPath += "/" + path[i];

        OutputStream fOutputStream = null;
        File file = new File(newPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            fOutputStream = new FileOutputStream(file);

            Bitmap bitmap = createThumbnailFromPath(videoPath, MediaStore.Images.Thumbnails.MICRO_KIND);
            videoPath = newPath;

            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 320, 240, true);
            resized.compress(Bitmap.CompressFormat.PNG, 40, fOutputStream);

            fOutputStream.flush();
            fOutputStream.close();

            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videoPath;
    }

    public Bitmap createThumbnailFromPath(String filePath, int type) {
        return ThumbnailUtils.createVideoThumbnail(filePath, type);
    }

    /*
        VERIFICAR O CAMINHO EM QUE VAI SER ADICIONADO
     */
    protected void downloadAttachment(final String urlLink, final String fileName) {
        File root = Environment.getExternalStorageDirectory();
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
