package com.ufcspa.unasus.appportfolio.Activities.Fragments;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.onegravity.rteditor.utils.io.output.ByteArrayOutputStream;
import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.Model.Sync;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.WebClient.LogoutClient;
import com.ufcspa.unasus.appportfolio.WebClient.PasswordClient;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.security.MessageDigest;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentConfig extends Frag implements View.OnClickListener {
    private static ProgressDialog dialog;
    private Button btn_profile;
    private Button btn_password;
    private Button btn_logout;
    private ImageButton btn_device_info;

    public FragmentConfig() {
        // Required empty public constructor
    }

    public static void logout(FragmentActivity main) {
        ((MainActivity) main).logout();
    }

    public static void couldNotLogout() {
        dialog.dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_config2, container, false);

        singleton = Singleton.getInstance();
        source = DataBaseAdapter.getInstance(getContext());

        init(view);

        return view;
    }

    private void init(View view) {
        btn_logout = (Button) view.findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(this);

        btn_change_image = (ImageView) view.findViewById(R.id.btn_change_image);
        img_user = (ImageView) view.findViewById(R.id.img_user);
        edt_email = (EditText) view.findViewById(R.id.edt_email);
        edt_telefone = (EditText) view.findViewById(R.id.edt_telefone);
        btn_alterar = (Button) view.findViewById(R.id.btn_alterar);

        btn_device_info = (ImageButton) view.findViewById(R.id.btn_device_info);
        btn_device_info.setOnClickListener(this);

        if (singleton.user.getPhoto() != null) {
            img_user.setImageBitmap(singleton.user.getPhotoBitmap());
        }

        edt_email.setText(singleton.user.getEmail());

        if (singleton.user.getCellphone() != null && !singleton.user.getCellphone().equals("null"))
            edt_telefone.setText(singleton.user.getCellphone());

        btn_change_image.setOnClickListener(this);
        btn_alterar.setOnClickListener(this);

        old_pass = (EditText) view.findViewById(R.id.edt_old_pass);
        new_pass = (EditText) view.findViewById(R.id.edt_new_pass);
        confirm_new_pass = (EditText) view.findViewById(R.id.edt_confirm_pass);

        update_pass = (Button) view.findViewById(R.id.btn_update);
        update_pass.setOnClickListener(this);
    }

  /*  @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_profile) {
            ((MainActivity) getActivity()).initChangeProfileFragment();
        }
        if (v.getId() == R.id.btn_password) {
            ((MainActivity) getActivity()).initChangePasswordFragment();
        }
        if (v.getId() == R.id.btn_logout) {
            if (isOnline()) {
                dialog = ProgressDialog.show(getContext(), "Saindo", "Por favor aguarde...", true);
                LogoutClient logoutClient = new LogoutClient(getContext(), getActivity());
                logoutClient.postJson();
            }
        }
    }*/

    private ImageView btn_change_image;
    private ImageView img_user;
    private EditText edt_email;
    private EditText edt_telefone;
    private Button btn_alterar;

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

    private EditText old_pass;
    private EditText new_pass;
    private EditText confirm_new_pass;
    private Button update_pass;

    public String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
//            throw new RuntimeException(ex);
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        //Log.d("Password", sha256(new_pass.getText().toString()));
        String oldPassword = old_pass.getText().toString();
        String newPassword = new_pass.getText().toString();
        String confirmNewPassword = confirm_new_pass.getText().toString();

        if (isOnline()) {
            if (oldPassword.trim().length() == 0)
                old_pass.setError("Por favor, preencha a senha antiga.");
            else if (newPassword.trim().length() == 0)
                new_pass.setError("Por favor, preencha a sua nova senha.");
            else if (confirmNewPassword.trim().length() == 0)
                confirm_new_pass.setError("Por favor, preencha o campo confirme a sua senha.");
            else if (!(newPassword.equals(confirmNewPassword)))
                confirm_new_pass.setError("As duas senhas não são iguais.");
            else {
                // TODO Enviar nova senha ...
                String oldPass = sha256(oldPassword);
                String newPass = sha256(newPassword);

                if (oldPass != null && newPass != null) {
                    PasswordClient passwordClient = new PasswordClient(getContext(), getActivity());
                    passwordClient.postJson(oldPass, newPass);
                }
            }
        } else {
            Toast.makeText(getContext(), "Você deve estar online para trocar a sua senha.", Toast.LENGTH_SHORT).show();
        }
        if (v.getId() == R.id.btn_alterar) {
            verifyEditText();
        }
        if (v.getId() == R.id.btn_change_image) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
        if (v.getId() == R.id.btn_device_info) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.device_info_dialog);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            TextView device_id = (TextView) dialog.findViewById(R.id.device_id);
            TextView version_app = (TextView) dialog.findViewById(R.id.version_app);
            TextView version_os = (TextView) dialog.findViewById(R.id.version_os);

            dialog.show();

            device_id.setText(singleton.device.get_id_device());
            version_app.setText("1.0");
            version_os.setText(Build.VERSION.RELEASE);

            Button btn_ok = (Button) dialog.findViewById(R.id.btn_info_ok);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        if (v.getId() == R.id.btn_logout) {
            if (isOnline()) {
                dialog = ProgressDialog.show(getContext(), "Saindo", "Por favor aguarde...", true);
                LogoutClient logoutClient = new LogoutClient(getContext(), getActivity());
                logoutClient.postJson();
            }
        }
    }
}
