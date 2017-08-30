package com.ufcspa.unasus.appportfolio.activities.fragments;


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
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.onegravity.rteditor.utils.io.output.ByteArrayOutputStream;
import com.ufcspa.unasus.appportfolio.activities.MainActivity;
import com.ufcspa.unasus.appportfolio.model.Singleton;
import com.ufcspa.unasus.appportfolio.model.Sync;
import com.ufcspa.unasus.appportfolio.notifications.NotificationEventReceiver;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.webClient.LogoutClient;
import com.ufcspa.unasus.appportfolio.webClient.PasswordClient;
import com.ufcspa.unasus.appportfolio.database.DataBase;

import java.security.MessageDigest;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigFragment extends HelperFragment implements View.OnClickListener {
    private static ProgressDialog sDialog;
    private Button mBtnLogout;
    private Button mBtnNotifications;
    private Button mBtnDeviceInfo;

    private EditText mOldPass;
    private EditText mNewPass;
    private EditText mConfirmNewPass;
    private Button mUpdatePass;

    private ImageView mBtnChangeImage;
    private EditText mEdtEmail;
    private EditText mEdtTelefone;
    private Button mBtnAlterar;

    public ConfigFragment() {
        // Required empty public constructor
    }

    public static void logout(FragmentActivity main) {
        ((MainActivity) main).logout();
    }

    public static void couldNotLogout() {
        sDialog.dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_config, container, false);

        singleton = Singleton.getInstance();
        source = DataBase.getInstance(getContext());

        init(view);

        return view;
    }

    private void init(View view) {
        mBtnLogout = (Button) view.findViewById(R.id.btn_logout);

        mBtnLogout.setOnClickListener(this);

        mBtnNotifications = (Button) view.findViewById(R.id.btn_notifications);
        mBtnNotifications.setOnClickListener(this);

        mBtnChangeImage = (ImageView) view.findViewById(R.id.btn_change_image);
        mEdtEmail = (EditText) view.findViewById(R.id.edt_email);
        mEdtTelefone = (EditText) view.findViewById(R.id.edt_telefone);
        mBtnAlterar = (Button) view.findViewById(R.id.btn_alterar);

        mBtnDeviceInfo = (Button) view.findViewById(R.id.btn_device_info);
        mBtnDeviceInfo.setOnClickListener(this);

        if (singleton.user.getPhoto() != null) {
            mBtnChangeImage
                    .setImageBitmap(singleton.user.getPhotoBitmap());
        }

        mEdtEmail.setText(singleton.user.getEmail());

        if (singleton.user.getCellphone() != null && !singleton.user.getCellphone().equals("null"))
            mEdtTelefone.setText(singleton.user.getCellphone());

        mBtnChangeImage.setOnClickListener(this);
        mBtnAlterar.setOnClickListener(this);

        mOldPass = (EditText) view.findViewById(R.id.edt_old_pass);
        mNewPass = (EditText) view.findViewById(R.id.edt_new_pass);
        mConfirmNewPass = (EditText) view.findViewById(R.id.edt_confirm_pass);

        mUpdatePass = (Button) view.findViewById(R.id.btn_update);
        mUpdatePass.setOnClickListener(this);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (singleton.user.getPhotoBitmap() != null) {
            mBtnChangeImage
                    .setImageBitmap(singleton.user.getPhotoBitmap());
        } else if (singleton.user.getPhoto() != null) {
            byte[] decodedString = Base64.decode(singleton.user.getPhoto(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            mBtnChangeImage
                    .setImageBitmap(decodedByte);
        }
    }

    private void verifyEditText() {
        if (!isOnline())
            Toast.makeText(getContext(), "Suas alterações serão realizadas quando você entrar online.", Toast.LENGTH_LONG).show();

        String email = mEdtEmail.getText().toString();
        String telefone = mEdtTelefone.getText().toString();

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
        //Log.d("Password", sha256(mNewPass.getText().toString()));
        String oldPassword = mOldPass.getText().toString();
        String newPassword = mNewPass.getText().toString();
        String confirmNewPassword = mConfirmNewPass.getText().toString();

        if (v.getId() == R.id.btn_update) {
            if (isOnline()) {
                if (oldPassword.trim().length() == 0)
                    mOldPass.setError("Por favor, preencha a senha antiga.");
                else if (newPassword.trim().length() == 0)
                    mNewPass.setError("Por favor, preencha a sua nova senha.");
                else if (confirmNewPassword.trim().length() == 0)
                    mConfirmNewPass.setError("Por favor, preencha o campo confirme a sua senha.");
                else if (!(newPassword.equals(confirmNewPassword)))
                    mConfirmNewPass.setError("As duas senhas não são iguais.");
                else {
                    // TODO Enviar nova senha ...
                    String oldPass = sha256(oldPassword);
                    String newPass = sha256(newPassword);

                    if (oldPass != null && newPass != null) {
                        PasswordClient passwordClient = new PasswordClient(getContext(), getActivity());
                        passwordClient.postJson(oldPass, newPass);
                        Toast.makeText(getContext(), "Senha alterada", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(getContext(), "Você deve estar online para trocar a sua senha.", Toast.LENGTH_SHORT).show();
            }
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
            dialog.setContentView(R.layout.dialog_info);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            TextView deviceId = (TextView) dialog.findViewById(R.id.device_id);
            TextView versionApp = (TextView) dialog.findViewById(R.id.version_app);
            TextView versionOs = (TextView) dialog.findViewById(R.id.version_os);

            dialog.show();

            deviceId.setText(singleton.device.get_id_device());
            versionApp.setText("1.0.0");
            versionOs.setText(Build.VERSION.RELEASE);

            Button btnOk = (Button) dialog.findViewById(R.id.btn_info_ok);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        if (v.getId() == R.id.btn_logout) {
            if (isOnline()) {
                final Dialog dialogLogout = new Dialog(getActivity());
                dialogLogout.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogLogout.setContentView(R.layout.dialog_logout);
                dialogLogout.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                dialogLogout.show();
                Button btnLogoutOk = (Button) dialogLogout.findViewById(R.id.btn_logout_ok);
                btnLogoutOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sDialog = ProgressDialog.show(getContext(), "Saindo", "Por favor aguarde...", true);
                        LogoutClient logoutClient = new LogoutClient(getContext(), getActivity());
                        logoutClient.postJson();
                        dialogLogout.dismiss();
                    }
                });
                Button btnLogoutCancel = (Button) dialogLogout.findViewById(R.id.btn_logout_cancel);
                btnLogoutCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogLogout.dismiss();
                    }
                });
            }
        }
        if (v.getId() == R.id.btn_notifications) {
            if (isOnline()) {
                final Dialog dialogNotification = new Dialog(getActivity());
                dialogNotification.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogNotification.setContentView(R.layout.dialog_notification);
                dialogNotification.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialogNotification.show();

                final RadioGroup notificationInterval = (RadioGroup) dialogNotification.findViewById(R.id.notification_interval);
                switch (singleton.interval) {
                    case 1:
                        notificationInterval.check(R.id.interval1);
                        break;
                    case 6:
                        notificationInterval.check(R.id.interval6);
                        break;
                    case 24:
                        notificationInterval.check(R.id.interval24);
                        break;
                    case 48:
                        notificationInterval.check(R.id.interval48);
                        break;
                }
                Button btnNotificationOk = (Button) dialogNotification.findViewById(R.id.btn_notification_ok);
                Button btnNotificationCancel = (Button) dialogNotification.findViewById(R.id.btn_notification_cancel);
                btnNotificationOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = notificationInterval.getCheckedRadioButtonId();
                        int interval = 0;
                        switch (id) {
                            case R.id.interval1:
                                interval = 1;
                                break;
                            case R.id.interval6:
                                interval = 6;
                                break;
                            case R.id.interval24:
                                interval = 24;
                                break;
                            case R.id.interval48:
                                interval = 48;
                                break;
                        }
                        singleton.interval = interval;
                        NotificationEventReceiver.setupAlarm(getContext(), interval);
                        dialogNotification.dismiss();
                    }
                });
                btnNotificationCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogNotification.dismiss();
                    }
                });
            }
        }
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

                mBtnChangeImage.setImageBitmap(resized);
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
