package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.Database.DataBaseAdapter;

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

        if (cursor.moveToFirst()) {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_text, null);
        source = new DataBaseAdapter(getActivity());

        singleton = Singleton.getInstance();
        singleton.idActivityStudent = source.getActivityStudentID(singleton.activity.getIdAtivity(), singleton.portfolioClass.getIdPortfolioStudent());

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
            } else if (mimeType.startsWith("video")) {
                insertFileIntoDataBase(mCurrentPhotoPath, "V");
                mCurrentPhotoPath = getThumbnailPathForLocalFile(getActivity(), selectedUri);
            }
        }

        if (requestCode == PICKFILE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            insertFileIntoDataBase(data.getData().getPath(), "T");
        }

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
            insertFileIntoDataBase(data.getData().getPath(), "V");
            mCurrentPhotoPath = getThumbnailPathForLocalFile(getActivity(), data.getData());
        }
    }

    /*
    *******************|
    * Public Interface |
    *******************|
    */
    public void showChooseGalleryOrTakePicture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Were is your picture?")
                .setCancelable(false)
                .setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dispatchGetPictureFromGallery();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Take Picture", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dispatchTakePictureIntent();
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    public void dispatchFileBrowserIntent() {
        Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
        fileintent.setType("file/*"); //"file/*"
        try {
            startActivityForResult(fileintent, PICKFILE_RESULT_CODE);
        } catch (ActivityNotFoundException e) {
            System.out.println("No activity can handle picking a file. Showing alternatives.");
            System.out.println(e);
        }
    }

    /*
    ************|
    *  Pictures |
    ************|
    */

    public void insertFileIntoDataBase(String path, String type) {
        source.saveAttachmentActivityStudent(path, type, singleton.idActivityStudent);
    }

    /*
    *********|
    * Videos |
    *********|
    */

    public void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    public void dispatchGetPictureFromGallery() {
        Intent getPicutureFromGalleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getPicutureFromGalleryIntent.setType("image/* video/*");
        startActivityForResult(getPicutureFromGalleryIntent, RESULT_LOAD_IMAGE);
    }

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

    public void saveSmallImage() {
        String[] path = mCurrentPhotoPath.split("\\.");
        path[0] += "_small";

        OutputStream fOutputStream = null;
        File file = new File(path[0] + "." + path[1]);
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
            mCurrentPhotoPath = path[0] + "." + path[1];

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

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

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
    private void openPDF(String filename) {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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


    /*
    |**************|
    |* Older Code *|
    |**************|
    */
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == getActivity().RESULT_OK) {
//            if (requestCode == SELECT_PICTURE) {
//                Uri selectedImageUri = data.getData();
//
//                //OI FILE Manager
//                filemanagerstring = selectedImageUri.getPath();
//
//                //MEDIA GALLERY
//                selectedImagePath = getPath(selectedImageUri);
//
//                //DEBUG PURPOSE - you can delete this if you want
//                if(selectedImagePath!=null)
//                    System.out.println("primeiro:"+selectedImagePath);
//                else System.out.println("selectedImagePath is null");
//                if(filemanagerstring!=null)
//                    System.out.println("file string:"+filemanagerstring);
//                else System.out.println("filemanagerstring is null");
//
//                //NOW WE HAVE OUR WANTED STRING
//                if(selectedImagePath!=null)
//                    System.out.println("selectedImagePath is the right one for you!");
//                else
//                    System.out.println("filemanagerstring is the right one for you!");
//            }
//        }
//        mEditor.insertImage(selectedImagePath,"Testando imagem!");
//    }
//
//    //UPDATED!
//    public String getPath(Uri uri) {
//        String[] projection = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
//        if (cursor != null) {
//            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
//            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
//            int column_index = cursor
//                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        } else return null;
//    }

//    findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mEditor.undo();
//        }
//    });
//
//    findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mEditor.redo();
//        }
//    });
//
//    findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mEditor.setBold();
//        }
//    });
//
//    findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mEditor.setItalic();
//        }
//    });
//
//    findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mEditor.setSubscript();
//        }
//    });
//
//    findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mEditor.setSuperscript();
//        }
//    });
//
//    findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mEditor.setStrikeThrough();
//        }
//    });
//
//    findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mEditor.setUnderline();
//        }
//    });
//
//    findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mEditor.setHeading(1);
//        }
//    });
//
//    findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mEditor.setHeading(2);
//        }
//    });
//
//    findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mEditor.setHeading(3);
//        }
//    });
//
//    findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mEditor.setHeading(4);
//        }
//    });
//
//    findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mEditor.setHeading(5);
//        }
//    });
//
//    findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mEditor.setHeading(6);
//        }
//    });
//
//    findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
//        boolean isChanged;
//
//        @Override
//        public void onClick(View v) {
//            mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
//            isChanged = !isChanged;
//        }
//    });
//
//    findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
//        boolean isChanged;
//
//        @Override
//        public void onClick(View v) {
//            mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
//            isChanged = !isChanged;
//        }
//    });
//
//    findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mEditor.setIndent();
//        }
//    });
//
//    findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mEditor.setOutdent();
//        }
//    });
//
//    findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mEditor.setAlignLeft();
//        }
//    });
//
//    findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mEditor.setAlignCenter();
//        }
//    });
//
//    findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mEditor.setAlignRight();
//        }
//    });
//
//    findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mEditor.setBlockquote();
//        }
//    });
//
//    findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            showChooseGalleryOrTakePicture();
//        }
//    });
//
//    findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
//        }
//    });
//    findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            //mEditor.insertTodo();
//            openFileBrowser();
//        }
//    })
}
