package com.ufcspa.unasus.appportfolio.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Model.Policy;
import com.ufcspa.unasus.appportfolio.Model.PolicyUser;
import com.ufcspa.unasus.appportfolio.Model.User;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PrivacyPolicyActivity extends AppCompatActivity {

    private CheckBox checkBox;
    private Button acceptBT;
    private TextView policyTX;
    private int idUser;
    private Policy policy;
    private PolicyUser policyUser;
    private DataBaseAdapter dataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.RTE_ThemeLight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        getSupportActionBar().hide();

        final Intent intent = new Intent(this, MainActivity.class);

        idUser = dataBaseAdapter.getUser().getIdUser();

        String txPolicy = dataBaseAdapter.getPolicyByUserID(idUser).getTxPolicy();

        policyTX.setText(txPolicy);

        acceptBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

}
