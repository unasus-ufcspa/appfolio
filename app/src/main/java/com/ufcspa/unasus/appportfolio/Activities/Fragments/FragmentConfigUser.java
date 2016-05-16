package com.ufcspa.unasus.appportfolio.Activities.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.onegravity.rteditor.utils.io.output.ByteArrayOutputStream;
import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.Model.Sync;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentConfigUser extends Frag implements View.OnClickListener {
    private ImageView btn_change_image;
    private ImageView img_user;
    private EditText edt_email;
    private EditText edt_telefone;
    private Button btn_alterar;

    public FragmentConfigUser() {
    }

    public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            int color = 0xff424242;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            RectF rectF = new RectF(rect);
            int roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
        } catch (NullPointerException e) {
        } catch (OutOfMemoryError o) {
        }

        return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_config_user, container, false);

        singleton = Singleton.getInstance();
        source = DataBaseAdapter.getInstance(getContext());

        init(view);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (singleton.user.getPhotoBitmap() != null) {
            img_user.setImageBitmap(singleton.user.getPhotoBitmap());
        } else if (singleton.user.getPhoto() != null) {
            byte[] decodedString = Base64.decode(singleton.user.getPhoto(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            img_user.setImageBitmap(decodedByte);
        }
    }

    private void init(View view) {
        btn_change_image = (ImageView) view.findViewById(R.id.btn_change_image);
        img_user = (ImageView) view.findViewById(R.id.img_user);
        edt_email = (EditText) view.findViewById(R.id.edt_email);
        edt_telefone = (EditText) view.findViewById(R.id.edt_telefone);
        btn_alterar = (Button) view.findViewById(R.id.btn_alterar);

        if (singleton.user.getPhoto() != null) {
            img_user.setImageBitmap(singleton.user.getPhotoBitmap());
        }

        edt_email.setText(singleton.user.getEmail());

        if (singleton.user.getCellphone() != null && !singleton.user.getCellphone().equals("null"))
            edt_telefone.setText(singleton.user.getCellphone());

        btn_change_image.setOnClickListener(this);
        btn_alterar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_alterar) {
            verifyEditText();
        }
        if (v.getId() == R.id.btn_change_image) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void verifyEditText() {
        if (!isOnline())
            Toast.makeText(getContext(), "Suas alterações serão realizadas quando você entrar online.", Toast.LENGTH_LONG).show();

        String email = edt_email.getText().toString();
        String telefone = edt_telefone.getText().toString();

        if (email != null)
            singleton.user.setEmail(email);

        if (telefone != null)
            singleton.user.setCellphone(telefone);

        if (source.updateTBUser(singleton.user)) {
            Toast.makeText(getContext(), "Alterações salvas", Toast.LENGTH_LONG).show();
        }

        Sync sync = new Sync(singleton.device.get_id_device(), "tb_user", singleton.user.getIdUser(), -1);
        source.insertIntoTBSync(sync);

        MainActivity main = ((MainActivity) getActivity());
        if (main != null)
            main.sendFullData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bitmap picture = Bitmap.createScaledBitmap((Bitmap) data.getExtras().get("data"), 180, 180, true);

                Bitmap resized = getRoundedRectBitmap(picture, 100);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                resized.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] b = baos.toByteArray();
                String base64Image = Base64.encodeToString(b, Base64.DEFAULT);

                img_user.setImageBitmap(resized);
                singleton.user.setPhoto(base64Image, resized);

//                System.out.println(base64Image);
/*
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
*/
            }
        }
    }
}
