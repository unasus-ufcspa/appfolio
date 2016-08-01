package com.ufcspa.unasus.appportfolio.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ufcspa.unasus.appportfolio.Model.Policy;
import com.ufcspa.unasus.appportfolio.Model.PolicyUser;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.Model.User;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.WebClient.PolicyUserClient;
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
    private PolicyUserClient policyUserClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.RTE_ThemeLight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        getSupportActionBar().hide();
        policyTX = (TextView) findViewById(R.id.term_text);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        acceptBT = (Button) findViewById(R.id.btAgree);

        final Intent intent = new Intent(this, MainActivity.class);

        idUser = Singleton.getInstance().user.getIdUser();

        String txPolicy = DataBaseAdapter.getInstance(getBaseContext()).getPolicyByUserID(idUser).getTxPolicy();
        Log.d("Policy", txPolicy);

        policyTX.setText("TERMOS DE USO \n" + txPolicy);

        acceptBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    policyUser = DataBaseAdapter.getInstance(getBaseContext()).getPolicyUserByUserId(idUser);
                    DataBaseAdapter.getInstance(getBaseContext()).updateFlAccept(policyUser.getIdPolicyUser());
                    policyUser = DataBaseAdapter.getInstance(getBaseContext()).getPolicyUserByUserId(idUser);
                    Log.d("policyUser",policyUser.toString());
                    policyUserClient = new PolicyUserClient(getBaseContext());
                    policyUserClient.postJson(PolicyUser.toJSON(policyUser.getIdPolicyUser(), policyUser.getIdUser(), policyUser.getFlAccept()));
                    startActivity(intent);
                    finish();
                } else
                    Toast.makeText(getApplicationContext(),"Você deve aceitar os termos para continuar",Toast.LENGTH_LONG);
            }
        });

    }

}
