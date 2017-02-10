
package com.ufcspa.unasus.appportfolio.Activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ufcspa.unasus.appportfolio.Model.Device;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.Model.User;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.WebClient.BasicData;
import com.ufcspa.unasus.appportfolio.WebClient.BasicDataClient;
import com.ufcspa.unasus.appportfolio.WebClient.FirstLogin;
import com.ufcspa.unasus.appportfolio.WebClient.FirstSync;
import com.ufcspa.unasus.appportfolio.WebClient.FirstSyncClient;
import com.ufcspa.unasus.appportfolio.WebClient.FistLoginClient;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

//import io.github.skyhacker2.sqliteonweb.SQLiteOnWeb;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity2 extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    public static final int MY_PERMISSIONS_REQUEST = 1;

    public static boolean isLoginSucessful;
    public static boolean isDataSyncNotSucessful;
    public static boolean isBasicDataSucessful;
    public static boolean isBasicDataSyncNotSucessful;
    LoginActivity2 l = this;
    ProgressDialog dialog;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    //    private View mLoginFormView;
    private DataBaseAdapter bd;
    private User user;
    // Singleton
    private Singleton session;

    public static String sha256(String base) {
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
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        Singleton.getInstance().user = new User(0,null,null);
        Singleton.getInstance().device = new Device();

        bd = DataBaseAdapter.getInstance(this);
        //SQLiteOnWeb.init(this).start();

        isBasicDataSucessful = false;
        isBasicDataSyncNotSucessful = false;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);
        }

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        populateAutoComplete();
        mPasswordView = (EditText) findViewById(R.id.password);
//        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    //attemptLogin();
                    return true;
                }
                return false;
            }
        });


        final DataBaseAdapter data = DataBaseAdapter.getInstance(this);
//        ArrayList<PortfolioClass> lista= (ArrayList<PortfolioClass>) data.selectListClassAndUserType(5);
//        Log.d("lista","tamanho:"+lista.size());
//
//        Log.d("lista","portfolio:"+lista.toString());
        /*********************************************************/
        /*********************************************************/
        /**************************TESTE**************************/
        mEmailView.setText("");
        mPasswordView.setText("");
        /*********************************************************/
        /*********************************************************/
        /*********************************************************/

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public synchronized void onClick(View view) {
                if (isOnline()) {
//                    showProgress(true);
                    dialog = ProgressDialog.show(LoginActivity2.this, "Baixando novos dados", "Por favor aguarde...", true);
                    final Thread myThread =new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (!isLoginSucessful)
                                verificarLogin();
                            while(!isLoginSucessful){
                                if(isDataSyncNotSucessful){
                                    Log.d("acitivity login", "data sync not sucesseful");
                                    break;
                                }

                            }
                            if(!isDataSyncNotSucessful){
                                Log.d("acitivity login", "user get by json:" + Singleton.getInstance().user.toString());
                                getBasicData();

                                while (!isBasicDataSucessful)
                                    if (isBasicDataSyncNotSucessful)
                                        break;

                                if (!isBasicDataSyncNotSucessful) {
                                    bd.updateDeviceBasicDataSync();
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                            Log.d("tela login", "terminou conexão");
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                finish();
                                        }
                                    });
                                } else {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Erro interno. Por favor tente novamente", Toast.LENGTH_LONG).show();
//                                            showProgress(false);
                                            dialog.dismiss();
                                            mEmailView.setEnabled(false);
                                            mPasswordView.setEnabled(false);
                                        }
                                    });
                                }
                            }else{
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
//                                        showProgress(false);
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(), Singleton.getInstance().erro, Toast.LENGTH_LONG).show();
                                        findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
                                        isLoginSucessful = false;
                                        isDataSyncNotSucessful = false;
                                    }
                                });
                            }
                        }
                    });
                    myThread.start();
                } else {
                    Toast.makeText(getApplicationContext(), "Sem conexão com a internet", Toast.LENGTH_LONG).show();
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("dont_have_basic_data")) {
                if (isOnline()) {
//                    showProgress(true);
                    dialog = ProgressDialog.show(LoginActivity2.this, "Baixando novos dados", "Por favor aguarde...", true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getBasicData();

                            while (!isBasicDataSucessful)
                                if (isBasicDataSyncNotSucessful)
                                    break;

                            if (!isBasicDataSyncNotSucessful) {
                                bd.updateDeviceBasicDataSync();
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                        Log.d("tela login", "terminou conexão");
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();
                                    }
                                });
                            } else {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Erro interno. Por favor tente novamente", Toast.LENGTH_LONG).show();
//                                        showProgress(false);
                                        dialog.dismiss();
                                        mEmailView.setEnabled(false);
                                        mPasswordView.setEnabled(false);
                                        isLoginSucessful = true;
                                        isDataSyncNotSucessful = false;
                                    }
                                });
                            }
                        }
                    }).start();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Sem conexão com a internet", Toast.LENGTH_LONG).show();
                mEmailView.setEnabled(false);
                mPasswordView.setEnabled(false);
            }
        }
    }


    public void getBasicData() {
        if (isOnline()) {
            BasicDataClient client = new BasicDataClient(getBaseContext());
            JSONObject jsonObject = new JSONObject();
            client.postJson(BasicData.toJSON(Singleton.getInstance().user.getIdUser(), Singleton.getInstance().device.get_id_device()));// mandando id
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Sem conexão com a internet", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    public void getFirstSync() {
        if (isOnline()) {
            FirstSyncClient fsclient = new FirstSyncClient(getBaseContext());
            JSONObject jsonObject = new JSONObject();
            fsclient.postJson(FirstSync.toJSON(Singleton.getInstance().user.getIdUser(), Singleton.getInstance().device.get_id_device()));// mandando id
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Sem conexão com a internet", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public boolean isTablet() {
        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            return true;
        } else {
            return false;
        }
    }

    private void showChooseTutorOrStudentPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.popup_login)
                .setCancelable(false)
                .setPositiveButton(R.string.popup_login_student, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        session.user.setUserType('S');
                        dialog.cancel();
                        loginSuccess();
                    }
                })
                .setNegativeButton(R.string.popup_login_tutor, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        session.user.setUserType('T');
                        dialog.cancel();
                        loginSuccess();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void loginSuccess() {

        //  Log.d("singletonn", "id:" + session.user.getIdUser() + " uT:" + session.user.getUserType());
        while (!isDataSyncNotSucessful) {

        }
        showProgress(false);
        Log.d("Act Login", "Finishing activity");
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private char checkUserType(int idUser) {
        DataBaseAdapter bd = DataBaseAdapter.getInstance(this);
        return bd.verifyUserType(idUser);
    }

    @Override
    protected void onResume() {
        super.onResume();
        criarBD();
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private void criarBD() {
        bd = DataBaseAdapter.getInstance(this);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private boolean verificarLogin() {

        //  try {
//            DataBaseAdapter bd = DataBaseAdapter.getInstance(this);
//            user = bd.verifyPass(mEmailView.getText().toString(), mPasswordView.getText().toString());
        //result=user.getIdUser();
        //Log.d("BANCO", " pass:" + result);
//        if (isOnline()) {
        FirstLogin first = new FirstLogin();

        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("ANDROID ID", "Android ID:" + android_id);
        String tpDevice = (isTablet()) ? "T" : "C";
        first.setIdDevice(android_id);
        first.setTpDevice(tpDevice);
        first.setEmail(mEmailView.getText().toString());
        // TODO SHA-256 -> (sha256(mPasswordView.getText().toString()));
        // http://stackoverflow.com/questions/3103652/hash-string-via-sha-256-in-java
        first.setPasswd(sha256(mPasswordView.getText().toString()));//(sha256(mPasswordView.getText().toString()));//(mPasswordView.getText().toString());

        //ADD TO SINGLETON
        Singleton.getInstance().device.set_id_device(android_id);
        Singleton.getInstance().device.set_tp_device(tpDevice.charAt(0));

        FistLoginClient client = new FistLoginClient(getBaseContext());
        client.postJson(first.toJSON());

//               } catch (Exception e) {
//                 Log.d("BANCO", "verificando pass:" + e.getMessage());
//               } finally {
//
//              }
//        }else{
//            Toast.makeText(getApplicationContext(), "Erro ao logar, favor verifique sua conexão com a internet", Toast.LENGTH_LONG).show();
//        }
        //showProgress(true);
        return isLoginSucessful;
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);
            int shortTime = 75;

//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            findViewById(R.id.scrollView).setVisibility(show ? View.GONE : View.VISIBLE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            findViewById(R.id.scrollView).setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

//        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
//
//    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
//        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<>(LoginActivity2.this,
//                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
//
//        mEmailView.setAdapter(adapter);
//    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }


    }
}

