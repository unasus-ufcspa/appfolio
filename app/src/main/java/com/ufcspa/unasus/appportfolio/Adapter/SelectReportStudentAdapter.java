package com.ufcspa.unasus.appportfolio.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coolerfall.download.DownloadCallback;
import com.coolerfall.download.DownloadManager;
import com.coolerfall.download.DownloadRequest;
import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.User;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.WebClient.HttpClient;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Desenvolvimento on 08/01/2016.
 */
public class SelectReportStudentAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Context context;
    private List<User> listaUsers;
    private DataBaseAdapter dataBaseAdapter;
    private List<Attachment> attachments;
    private static final String URL="/webfolio/app_dev.php/download/";

    public SelectReportStudentAdapter(Context context, List<User> classes)
    {
        this.context = context;
        this.listaUsers = classes;
        this.dataBaseAdapter = DataBaseAdapter.getInstance(context);

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listaUsers.size();
    }

    @Override
    public User getItem(int position) {
        return listaUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        final View rowView;
        Singleton.getInstance().idStudent=listaUsers.get(position).getIdUser();

        rowView = inflater.inflate(R.layout.item_student_report, null);

        holder.p_student_name=(TextView)rowView.findViewById(R.id.p_student_name);
        holder.p_student_name.setText(listaUsers.get(position).getName());
        holder.student_image=(ImageView)rowView.findViewById(R.id.student_image);
        if (listaUsers.get(position).getPhotoBitmap()!=null) {
            holder.student_image.setImageBitmap(listaUsers.get(position).getPhotoBitmap());
        } else {
            holder.student_image.setImageResource(R.drawable.ic_default_picture);
        }

        holder.student_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Singleton.getInstance().guestUser) {
                    attachments = dataBaseAdapter.getAttachmentFromStudentId(listaUsers.get(position).getIdUser());
                    if (attachments.size() > 0) {
                        downloadAttachments(attachments);
                    }
                }
                if (!Singleton.getInstance().guestUser || attachments.size() == 0) {
                    Singleton.getInstance().idStudent = listaUsers.get(position).getIdUser();
                    changeScreen();
                }
            }
        });

        return rowView;
    }

    private void changeScreen() {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("call.fragments.action").putExtra("ID", 7));
    }

    public class Holder
    {
        TextView p_student_name;
        ImageView student_image;
    }

    public void downloadAttachments(final List<Attachment> attachments){
        DownloadManager manager = new DownloadManager();
        final int[] cont = {attachments.size()};
        for (final Attachment a : attachments) {
            // BAIXAR OS ATTACHMENTS!!!!!!!!
            String path = null;
            if (a.getNmSystem() != null) {
                String[] aux = a.getNmSystem().split("/");
                path = a.getNmSystem();
                if (aux.length > 0)
                    path = aux[aux.length - 1];
            }
            String filePath = Environment.getExternalStorageDirectory() + "/Android/data/com.ufcspa.unasus.appportfolio/files/images" + File.separator + path;
            File file = new File(filePath);
            if (!file.exists()) { //verifica se arquivo já existe antes de baixar
                //Log.d("File Path", filePath);
                String url = "http://" + new HttpClient(context).ip + URL + path;
                DownloadRequest request = new DownloadRequest()
                        .setUrl(url)
                        .setDestFilePath(filePath)
                        .setDownloadCallback(new DownloadCallback() {
                            @Override
                            public void onSuccess(int downloadId, String filePath) {
                                super.onSuccess(downloadId, filePath);
                                cont[0]--;
                                dataBaseAdapter.updateAttachmentFlDownload(a.getIdAttachment());
                                if (filePath.contains(".mp4")) {
                                    saveSmallImage(filePath);
                                }
                                Log.d("atividade - anexos", "conseguiu baixar anexo com sucesso: " + filePath);
                                Toast.makeText(context, "Conseguiu baixar anexo com sucesso", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(int downloadId, int statusCode, String errMsg) {
                                super.onFailure(downloadId, statusCode, errMsg);
                                Toast.makeText(context, "Erro ao baixar anexos", Toast.LENGTH_LONG).show();
                            }
                        });
                manager.add(request);
                Log.d("atividade - anexos", "url do anexo: " + url);
            } else{
                cont[0]--;
                dataBaseAdapter.updateAttachmentFlDownload(a.getIdAttachment());
                Log.d("atividade - anexos", filePath + " já existe e não baixado");
            }
//                downloadAttachment("http://stuffpoint.com/stardoll/image/54056-stardoll-sdfs.jpg" + a.getNmSystem(), a.getNmFile());
        }
        if (cont[0]==0) {
            changeScreen();
        }
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
}

