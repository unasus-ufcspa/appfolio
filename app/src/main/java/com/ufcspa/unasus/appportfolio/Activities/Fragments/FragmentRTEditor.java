package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.onegravity.rteditor.RTEditText;
import com.onegravity.rteditor.RTManager;
import com.onegravity.rteditor.RTToolbar;
import com.onegravity.rteditor.api.RTApi;
import com.onegravity.rteditor.api.RTMediaFactoryImpl;
import com.onegravity.rteditor.api.RTProxyImpl;
import com.onegravity.rteditor.api.format.RTFormat;
import com.onegravity.rteditor.api.media.RTImage;
import com.onegravity.rteditor.api.media.RTImageImpl;
import com.onegravity.rteditor.api.media.RTVideo;
import com.onegravity.rteditor.api.media.RTVideoImpl;
import com.onegravity.rteditor.media.MediaUtils;
import com.ufcspa.unasus.appportfolio.Model.NewRTManager;
import com.ufcspa.unasus.appportfolio.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by desenvolvimento on 15/12/2015.
 */
public class FragmentRTEditor extends Frag {
    private static final int REQUEST_PERMISSION = 3;
    private static final String PREFERENCE_PERMISSION_DENIED = "PREFERENCE_PERMISSION_DENIED";

    private RTManager mRTManager;
    private RTEditText mRTMessageField;
    private Button btCopyTxt;
    private boolean mUseDarkTheme;
    private boolean mSplitToolbar;

    public FragmentRTEditor() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // read extras
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Log.d("editor", "OnCreateView saved instance null entrou");
            Intent intent = getActivity().getIntent();
            mUseDarkTheme = intent.getBooleanExtra("mUseDarkTheme", false);
            mSplitToolbar = intent.getBooleanExtra("mSplitToolbar", false);
        } else {
            Log.d("editor", "OnCreateView recuperando extras...");
            mUseDarkTheme = savedInstanceState.getBoolean("mUseDarkTheme", false);
            mSplitToolbar = savedInstanceState.getBoolean("mSplitToolbar", false);
            boolean tmp = savedInstanceState.getBoolean("mRequestPermissionsInProcess", false);
            mRequestPermissionsInProcess.set(tmp);
        }

        // set theme
        getActivity().setTheme(R.style.RTE_ThemeLight);





        // set layout
        View view = inflater.inflate(R.layout.fragment_rteditor, null);

        // initialize rich text manager
        RTApi rtApi = new RTApi(getActivity(), new RTProxyImpl(getActivity()), new RTMediaFactoryImpl(getActivity(), true));
        mRTManager = new RTManager(rtApi, savedInstanceState);
        Log.d("editor", "OnCreateView retornando view...");
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("editor", "OnviewCreated registrando RTMessage");
        mRTMessageField = (RTEditText) getView().findViewById(R.id.rtEditText);
        mRTManager.registerEditor(mRTMessageField, true);
        mRTMessageField.requestFocus();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("editor", "OnResume entrou");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("editor", "OnStart entrou");

        ViewGroup toolbarContainer = (ViewGroup) getView().findViewById(R.id.rte_toolbar_container);

        // register toolbar 0 (if it exists)
        RTToolbar rtToolbar0 = (RTToolbar) getView().findViewById(R.id.rte_toolbar);
        if (rtToolbar0 != null) {
            mRTManager.registerToolbar(toolbarContainer, rtToolbar0);
        }

        // register message editor



        checkPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
        btCopyTxt=(Button)getView().findViewById(R.id.btCopyText);
        btCopyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRTMessageField.getText().toString().isEmpty()) {
                    int startSelection = mRTMessageField.getSelectionStart();
                    int endSelection = mRTMessageField.getSelectionEnd();
                    String selectedText = mRTMessageField.getText().toString().substring(startSelection, endSelection);
                    if(!selectedText.isEmpty()){
                        if(selectedText.length()>0)
                            Toast.makeText(getActivity(),"texto:"+selectedText,Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getActivity(),"texto:null",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(),"Nenhum texto selecionado!",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private String getStringExtra(Intent intent, String key) {
        String s = intent.getStringExtra(key);
        return s == null ? "" : s;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRTManager != null) {
            mRTManager.onDestroy(true);
            Log.d("editor", "On destroy mRTManager");
        }else{
            Log.d("editor","On destroy mRTManager null");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mRTManager.onSaveInstanceState(outState);

        outState.putBoolean("mUseDarkTheme", mUseDarkTheme);
        outState.putBoolean("mSplitToolbar", mSplitToolbar);

        outState.putBoolean("mRequestPermissionsInProcess", mRequestPermissionsInProcess.get());
    }

    // ****************************************** Check Storage Permissions *******************************************

    private AtomicBoolean mRequestPermissionsInProcess = new AtomicBoolean();

    private void checkPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissionInternal(permissions);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkPermissionInternal(String[] permissions) {
        ArrayList<String> requestPerms = new ArrayList<String>();
        for (String permission : permissions) {
            if (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED && !userDeniedPermissionAfterRationale(permission)) {
                requestPerms.add(permission);
            }
        }
        if (requestPerms.size() > 0 && ! mRequestPermissionsInProcess.getAndSet(true)) {
            //  We do not have this essential permission, ask for it
            requestPermissions(requestPerms.toArray(new String[requestPerms.size()]), REQUEST_PERMISSION);
            return true;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
                        showRationale(permission, R.string.app_name);
                    }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void showRationale(final String permission, int promptResId) {
        if (shouldShowRequestPermissionRationale(permission) && !userDeniedPermissionAfterRationale(permission)) {

            //  Notify the user of the reduction in functionality and possibly exit (app dependent)
            MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                    .title("Permission Denied")
                    .content(promptResId)
                    .positiveText("Permission Deny")
                    .negativeText("Permission Retry")
                    .autoDismiss(false)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            try { dialog.dismiss(); } catch (Exception ignore) { }
                            setUserDeniedPermissionAfterRationale(permission);
                            mRequestPermissionsInProcess.set(false);
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            try { dialog.dismiss(); } catch (Exception ignore) { }
                            mRequestPermissionsInProcess.set(false);
                            checkPermissions(new String[]{permission});
                        }
                    })
                    .show();
        }
        else {
            mRequestPermissionsInProcess.set(false);
        }
    }

    private boolean userDeniedPermissionAfterRationale(String permission) {
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(PREFERENCE_PERMISSION_DENIED + permission, false);
    }

    private void setUserDeniedPermissionAfterRationale(String permission) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFERENCE_PERMISSION_DENIED + permission, true).commit();
    }
}
//    private NewRTManager rtManager;
//    private RTEditText rtEditText;
//
//    public FragmentRTEditor() {
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        getActivity().setTheme(R.style.RTE_ThemeLight);
//
//        View view = inflater.inflate(R.layout.fragment_rteditor, null);
//
//        // create RTManager
//        RTApi rtApi = new RTApi(getActivity(), new RTProxyImpl(getActivity()), new RTMediaFactoryImpl(getActivity(), true));
//        rtManager = new NewRTManager(rtApi, savedInstanceState);
//
//        return view;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // register toolbar
//        ViewGroup toolbarContainer = (ViewGroup) getView().findViewById(R.id.rte_toolbar_container);
//        RTToolbar rtToolbar = (RTToolbar) getView().findViewById(R.id.rte_toolbar);
//        if (rtToolbar != null) {
//            rtManager.registerToolbar(toolbarContainer, rtToolbar);
//        }
//
//        // register editor & set text
//        rtEditText = (RTEditText) getView().findViewById(R.id.rtEditText);
//        rtManager.registerEditor(rtEditText, true);
//        rtEditText.setRichTextEditing(true, "<b>Bem Vindo!</b>");
//
//        rtEditText.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return false;
//            }
//        });
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        rtManager.onSaveInstanceState(outState);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (rtManager != null) {
//            rtManager.onDestroy(true);
//        }
//    }
//}
