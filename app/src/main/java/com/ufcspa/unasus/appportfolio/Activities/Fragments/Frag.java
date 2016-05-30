package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import java.io.FileInputStream;
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
    static final int REQUEST_FOLIO_ATTACHMENT = 5;
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

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    public static String getPDFPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {

                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attachment_dialog, null);
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
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.CROP_IMAGE) {
                saveImageOnAppDir();
                insertFileIntoDataBase(mCurrentPhotoPath, "I");
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                galleryAddPic();
                launchCropImageIntent();
            } else if (requestCode == RESULT_LOAD_IMAGE) {
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
                    saveImage();
                    launchCropImageIntent();
                } else if (mimeType.startsWith("video")) {
                    saveVideoOnAppDir();
                    insertFileIntoDataBase(mCurrentPhotoPath, "V");
                }

            } else if (requestCode == PICKFILE_RESULT_CODE) {
                mCurrentPhotoPath = getPDFPath(getContext(), data.getData());
                savePDFOnAppDir();
                insertFileIntoDataBase(mCurrentPhotoPath, "T");
            } else if (requestCode == REQUEST_VIDEO_CAPTURE) {
                galleryAddPic();
                saveVideoOnAppDir();
                insertFileIntoDataBase(mCurrentPhotoPath, "V");
            } else if (requestCode == REQUEST_FOLIO_ATTACHMENT) {
                String url = null;
                String type = null;
                String smallImagePath = null;//Imagem do vídeo

                int idAttachment = data.getIntExtra("idAttachment", -1);

                if (data.hasExtra("url"))
                    url = data.getStringExtra("url");
                if (data.hasExtra("type"))
                    type = data.getStringExtra("type");
                if (data.hasExtra("smallImagePath"))
                    smallImagePath = data.getStringExtra("smallImagePath");

                switch (type) {
                    case "I":
                        if (url != null)
                            //Faz algo com a imagem
                            break;
                    case "V":
                        if (smallImagePath != null)
                            //Faz algo com o video
                            break;
                    case "T":
                        break;
                    default:
                        break;
                }

                if (idAttachment != -1)
                    //Inserir no banco
                    System.out.println();
            }
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

    /*
    *********|
    * Others |
    *********|
    */

    public void dispatchGetPictureFromGallery() {
        Intent getPicutureFromGalleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getPicutureFromGalleryIntent.setType("image/* video/*");
        startActivityForResult(getPicutureFromGalleryIntent, RESULT_LOAD_IMAGE);
    }

    public void saveSmallImage() {
        String[] path = mCurrentPhotoPath.split("/");
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
            Toast.makeText(getActivity(), "Nenhum leitor de PDF instalado.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addAttachmentToComments() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.attachment_popup_comments);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

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

        ImageButton img_folio = (ImageButton) dialog.findViewById(R.id.img_my_gallery);
        img_folio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new FragmentAttachmentDialog();
                newFragment.setTargetFragment(getParentFragment(), REQUEST_FOLIO_ATTACHMENT);
                newFragment.show(getChildFragmentManager(), "Attachment");
//                dialog.dismiss();
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

    public void addAttachment() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.attachment_popup);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

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
        builder.setCancelable(false);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            builder.setOnDismissListener(null);
//        }

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
                single.lastIdAttach = source.insertAttachment(new Attachment(0, type, name, path, 0));
            }
        });

        builder.show();
    }

    public void savePDFOnAppDir() {
        String[] path = mCurrentPhotoPath.split("/");

        File file = new File(getContext().getExternalFilesDir(null) + "/" + path[path.length - 1]);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream newFile = new FileOutputStream(file);
            //path 0 = current path of the video
            FileInputStream oldFile = new FileInputStream(mCurrentPhotoPath);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = oldFile.read(buf)) > 0) {
                newFile.write(buf, 0, len);
            }

            mCurrentPhotoPath = file.getAbsolutePath();
            newFile.flush();
            newFile.close();
            oldFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "O salvamento falhou.", Toast.LENGTH_SHORT).show();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "O salvamento falhou.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void saveImageOnAppDir() {
        String[] path = mCurrentPhotoPath.split("/");

        OutputStream fOutputStream = null;
        File file = new File(getContext().getExternalFilesDir(null) + "/" + path[path.length - 1]);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            fOutputStream = new FileOutputStream(file);

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            mCurrentPhotoPath = file.getAbsolutePath();

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOutputStream);

            fOutputStream.flush();
            fOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "O salvamento falhou.", Toast.LENGTH_SHORT).show();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "O salvamento falhou.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void saveVideoOnAppDir() {
        String[] path = mCurrentPhotoPath.split("/");

        File file = new File(getContext().getExternalFilesDir(null) + "/" + path[path.length - 1]);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream newFile = new FileOutputStream(file);
            //path 0 = current path of the video
            FileInputStream oldFile = new FileInputStream(mCurrentPhotoPath);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = oldFile.read(buf)) > 0) {
                newFile.write(buf, 0, len);
            }

            mCurrentPhotoPath = file.getAbsolutePath();
            newFile.flush();
            newFile.close();
            oldFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "O salvamento falhou.", Toast.LENGTH_SHORT).show();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "O salvamento falhou.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void saveImage() {
        String[] path = mCurrentPhotoPath.split("/");
        String[] secondPath = path[path.length - 1].split("\\.");
        secondPath[0] += "_new";
        if (path.length > 0)
            path[path.length - 1] = secondPath[0] + "." + secondPath[1];
        else
            path[0] = secondPath[0] + "." + secondPath[1];
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

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            mCurrentPhotoPath = newPath;

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOutputStream);

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

    public void loadPhoto(final String url) {
        if (url != null) {

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            startActivity(intent);
        }
    }

    public void loadVideo(final String url) {
        if(url != null) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromFile(new File(url)));
            intent.setDataAndType(Uri.fromFile(new File(url)), "video/*");
            startActivity(intent);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

}
