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
    private LinkedList<CommentVersion> commentVersions;
    private LinkedList<Observation> observations;
    private static final String URL="/webfolio/app_dev.php/download/";


    private static Context context;

    public FullData(Context context) {
        this.versionActs = new LinkedList<>();
        this.comentarios = new LinkedList<>();
        this.anexos = new HashMap<>();
        this.comentarioAttachment = new LinkedList<>();
        this.references = new LinkedList<>();
        this.notifications = new LinkedList<>();
        this.users = new LinkedList<>();
        this.commentVersions = new LinkedList<CommentVersion>();
        this.context = context;
        this.observations= new LinkedList<Observation>();

    }

    public static JSONObject toJSON(String idDevice, int idActivityStudent) {
        JSONObject json = new JSONObject();
        try {
            Singleton.getInstance().device=DataBaseAdapter.getInstance(context).getDevice();
            Log.d("sendData", "GET FROM DEVICE: "+Singleton.getInstance().device);

            json.put("fullDataSrvDev_request", new JSONObject().put("ds_hash", idDevice).put("id_activity_student", 0).put("id_user", Singleton.getInstance().device.get_id_user()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public void addUser(User u) {
        users.add(u);
    }

    public void addCommentVersion(CommentVersion cv) {
        commentVersions.add(cv);
    }

    public void addObservation(Observation obs) {
        observations.add(obs);
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
            anexos.put(activity_student, list.get(activity_student));
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
                int id_attachment = data.insertAttachmentDownload(a);
                data.insertAttachmentActivity(id_activity_student, id_attachment);
                // BAIXAR OS ATTACHMENTS!!!!!!!!
                String filePath = Environment.getExternalStorageDirectory()+"/Android/data/com.ufcspa.unasus.appportfolio/files/images" + File.separator + a.getNmSystem();
                File file = new File(filePath);
                if (!file.exists()) { //verifica se arquivo já existe antes de baixar
                    //Log.d("File Path", filePath);
                    String url = "http://" + new HttpClient(context).ip + URL + a.getNmSystem();
                    DownloadRequest request = new DownloadRequest()
                            .setUrl(url)
                            .setDestFilePath(filePath)
                            .setDownloadCallback(new DownloadCallback() {
                                @Override
                                public void onSuccess(int downloadId, String filePath) {
                                    super.onSuccess(downloadId, filePath);
                                    if (filePath.contains(".mp4")) {
                                        saveSmallImage(filePath);
                                    }
                                    Log.d("anexos","conseguiu baixar anexo com sucesso: "+filePath);
                                }

                                @Override
                                public void onFailure(int downloadId, int statusCode, String errMsg) {
                                    super.onFailure(downloadId, statusCode, errMsg);
                                    Log.e("anexos", "erro ao baixar anexo: " + statusCode+"\n "+errMsg);
                                }
                            });
                    manager.add(request);
                    Log.d("anexos", "url do anexo: " + url);
                } else
                    Log.d("anexos",filePath+" já existe e não baixado");
//                downloadAttachment("http://stuffpoint.com/stardoll/image/54056-stardoll-sdfs.jpg" + a.getNmSystem(), a.getNmFile());
            }
        }

        for (Tuple<Comentario, Attachment> tupla : comentarioAttachment) {
            int id_attachment = data.insertAttachment(tupla.y);
            int id_comment = data.insertComment(tupla.x);

            data.insertAttachComment(id_comment, id_attachment);
        }

        if (comentarios.size() > 0)
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
