package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.onegravity.rteditor.media.crop.CropImageActivity;
import com.onegravity.rteditor.utils.Constants;
import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by desenvolvimento on 10/12/2015.
 */
public class Frag extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int RESULT_LOAD_IMAGE = 2;
    static final int PICKFILE_RESULT_CODE = 3;
    static final int REQUEST_VIDEO_CAPTURE = 4;
    public static String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA};
    public static String[] mediaColumns = {MediaStore.Video.Media._ID};
    protected String mCurrentPhotoPath;
    protected DataBaseAdapter source;
    protected Singleton singleton;

    public Frag() {
    }

    public static String getThumbnailPathForLocalFile(Activity context, Uri fileUri) {

        long fileId = getFileId(context, fileUri);

        MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(),
                fileId, MediaStore.Video.Thumbnails.MICRO_KIND, null);

        Cursor thumbCursor = null;
        try {

            thumbCursor = context.managedQuery(
                    MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                    thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID + " = "
                            + fileId, null, null);

            if (thumbCursor.moveToFirst()) {
                String thumbPath = thumbCursor.getString(thumbCursor
                        .getColumnIndex(MediaStore.Video.Thumbnails.DATA));

                return thumbPath;
            }

        } finally {
        }

        return null;
    }

    public static long getFileId(Activity context, Uri fileUri) {

        Cursor cursor = context.managedQuery(fileUri, mediaColumns, null, null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int id = cursor.getInt(columnIndex);

            return id;
        }

        return 0;
    }

    private static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_text, null);
        source = DataBaseAdapter.getInstance(getActivity());

        singleton = Singleton.getInstance();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /*
        Exemplo de onActivityResult.
        Importante para pegar o caminho dos anexos criados.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Quando o usuário escolhe a opção Take Picture
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            galleryAddPic();
            insertFileIntoDataBase(mCurrentPhotoPath, "I");
            launchCropImageIntent();
        }

        // Quando o usuário escolhe a opção Gallery
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri selectedUri = data.getData();
            String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.MIME_TYPE};

            Cursor cursor = getActivity().getContentResolver().query(selectedUri, columns, null, null, null);
            cursor.moveToFirst();

            int pathColumnIndex = cursor.getColumnIndex(columns[0]);
            int mimeTypeColumnIndex = cursor.getColumnIndex(columns[1]);

            String contentPath = cursor.getString(pathColumnIndex);
            String mimeType = cursor.getString(mimeTypeColumnIndex);

            cursor.close();

            mCurrentPhotoPath = contentPath;

            if (mimeType.startsWith("image")) {
                insertFileIntoDataBase(mCurrentPhotoPath, "I");
                launchCropImageIntent();
            } else if (mimeType.startsWith("video")) {
                insertFileIntoDataBase(mCurrentPhotoPath, "V");
                mCurrentPhotoPath = getThumbnailPathForLocalFile(getActivity(), selectedUri);
            }
        }

        // Quando o usuário escolhe a opção Text
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            insertFileIntoDataBase(data.getData().getPath(), "T");
        }

        // Quando o usuário escolhe a opção Videos
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
            insertFileIntoDataBase(data.getData().getPath(), "V");
            galleryAddPic();
//            mCurrentPhotoPath = getThumbnailPathForLocalFile(getActivity(), data.getData());
        }
    }

    /*
    ***********|
    * Pictures |
    ***********|
    */
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Toast.makeText(getActivity(), "Caminho para a foto criado com sucesso!", Toast.LENGTH_SHORT).show();
            } catch (IOException ex) {
                Toast.makeText(getActivity(), "Impossível criar um caminho para a foto!", Toast.LENGTH_SHORT).show();
                System.out.println(ex.toString());
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    public void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);//getContext().getExternalFilesDir(null);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void dispatchGetPictureFromGallery() {
        Intent getPicutureFromGalleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getPicutureFromGalleryIntent.setType("image/* video/*");
        startActivityForResult(getPicutureFromGalleryIntent, RESULT_LOAD_IMAGE);
    }

    public void saveSmallImage() {
        String[] path = mCurrentPhotoPath.split("/");
        String[] secondPath = path[path.length - 1].split("\\.");
        secondPath[0] += "_small";
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

            Bitmap bitmap = createThumbnailFromPath(mCurrentPhotoPath, MediaStore.Images.Thumbnails.MICRO_KIND);
            mCurrentPhotoPath = newPath;

            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 320, 240, true);
            resized.compress(Bitmap.CompressFormat.PNG, 40, fOutputStream);

            fOutputStream.flush();
            fOutputStream.close();

            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Save Failed", Toast.LENGTH_SHORT).show();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Save Failed", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public Bitmap createThumbnailFromBitmap(String filePath, int width, int height) {
        return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(filePath), width, height);
    }

    public void launchCropImageIntent() {
        String filePath = mCurrentPhotoPath;

        Intent intent = new Intent(getActivity(), CropImageActivity.class)

                // tell CropImage activity to look for image to crop
                .putExtra(CropImageActivity.IMAGE_SOURCE_FILE, filePath)
                .putExtra(CropImageActivity.IMAGE_DESTINATION_FILE, filePath)

                        // allow CropImage activity to re-scale image
                .putExtra(CropImageActivity.SCALE, true)
                .putExtra(CropImageActivity.SCALE_UP_IF_NEEDED, false)

                        // no fixed aspect ratio
                .putExtra(CropImageActivity.ASPECT_X, 0)
                .putExtra(CropImageActivity.ASPECT_Y, 0);

        // start activity CropImageActivity
        startActivityForResult(intent, Constants.CROP_IMAGE);
    }

    /*
    *********|
    * Videos |
    *********|
    */
    public void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File videoFile = null;
            try {
                videoFile = createVideoFile();
                Toast.makeText(getActivity(), "Caminho para o vídeo criado com sucesso!", Toast.LENGTH_SHORT).show();
            } catch (IOException ex) {
                Toast.makeText(getActivity(), "Impossível criar um caminho para o vídeo!", Toast.LENGTH_SHORT).show();
                System.out.println(ex.toString());
            }

            if (videoFile != null) {
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }
        }
    }

    private File createVideoFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String videoFileName = "MP4_" + timeStamp + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File video = File.createTempFile(
                videoFileName,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = video.getAbsolutePath();
        return video;
    }

    /*
    *********|
    * Others |
    *********|
    */

    public Bitmap createThumbnailFromPath(String filePath, int type) {
        return ThumbnailUtils.createVideoThumbnail(filePath, type);
    }

    /*
    ********|
    * Files |
    ********|
    */
    public void dispatchFileBrowserIntent() {
        Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
        fileintent.setType("application/pdf"); //"file/*"
        try {
            startActivityForResult(fileintent, PICKFILE_RESULT_CODE);
        } catch (ActivityNotFoundException e) {
            System.out.println("No activity can handle picking a file. Showing alternatives.");
            System.out.println(e);
        }
    }

    public void openPDF(String filename) {
        File file = new File(filename);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }
    }

    public void addAttachmentToComments() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.attachment_popup);
        dialog.setTitle(R.string.files_popup_attachment);

        ImageButton img_gallery = (ImageButton) dialog.findViewById(R.id.img_gallery);
        img_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Gallery");
                dispatchGetPictureFromGallery();
                dialog.dismiss();
            }
        });

        ImageButton img_photos = (ImageButton) dialog.findViewById(R.id.img_photos);
        img_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Photos");
                dispatchTakePictureIntent();
                dialog.dismiss();
            }
        });

        ImageButton img_videos = (ImageButton) dialog.findViewById(R.id.img_videos);
        img_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Videos");
                dispatchTakeVideoIntent();
                dialog.dismiss();
            }
        });

        ImageButton img_text = (ImageButton) dialog.findViewById(R.id.img_text);
        img_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Text");
                dispatchFileBrowserIntent();
                dialog.dismiss();
            }
        });

        Button bt_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void insertFileIntoDataBase(final String path, final String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Escolha um nome:");

        // Set up the input
        final EditText input = new EditText(getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //source.saveAttachmentActivityStudent(path, type, singleton.idActivityStudent); //input.getText().toString()
                Singleton single = Singleton.getInstance();

                String name = input.getText().toString();
                if (name.isEmpty()) {
                    name = "Anexo";
                }
                single.lastIdAttach = source.insertAttachment(new Attachment(0, path, "", type, name, 0));
            }
        });

        builder.show();
    }

    /**
     * ALTERAR PARA O CAMINHO NECESSÁRIO
     */
    public void saveAttachment() {
        String[] path = mCurrentPhotoPath.split("\\.");
        path[0] += "_small";

        OutputStream fOutputStream = null;
        File file = new File(path[0] + "." + path[1]);
        String name = file.getName();
        File newFile = new File(getContext().getExternalFilesDir(null) + "/" + name);
        if (!newFile.exists()) {
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            fOutputStream = new FileOutputStream(newFile);

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            mCurrentPhotoPath = newFile.getAbsolutePath();

            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 320, 240, true);
            resized.compress(Bitmap.CompressFormat.PNG, 40, fOutputStream);

            fOutputStream.flush();
            fOutputStream.close();

//            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Save Failed", Toast.LENGTH_SHORT).show();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Save Failed", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    /**
     * ALTERAR
     */
}
