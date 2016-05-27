package com.ufcspa.unasus.appportfolio.Activities.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.WebClient.PasswordClient;

import java.security.MessageDigest;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentConfigPassword extends Frag implements View.OnClickListener {

    private EditText old_pass;
    private EditText new_pass;
    private EditText confirm_new_pass;
    private Button update_pass;

    public FragmentConfigPassword() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_config_password, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        old_pass = (EditText) view.findViewById(R.id.edt_old_pass);
        new_pass = (EditText) view.findViewById(R.id.edt_new_pass);
        confirm_new_pass = (EditText) view.findViewById(R.id.edt_confirm_pass);

        update_pass = (Button) view.findViewById(R.id.btn_update);
        update_pass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d("Password", sha256(new_pass.getText().toString()));
        if (isOnline()) {
            if (old_pass.getText().toString().trim().length() == 0)
                old_pass.setError("Por favor, preencha a senha antiga.");
            else if (new_pass.getText().toString().trim().length() == 0)
                new_pass.setError("Por favor, preencha a sua nova senha.");
            else if (confirm_new_pass.getText().toString().trim().length() == 0)
                confirm_new_pass.setError("Por favor, preencha o campo confirme a sua senha.");
            else if (!new_pass.getText().equals(confirm_new_pass.getText())) {
                confirm_new_pass.setError("As duas senhas não são iguais.");
            } else {
                // TODO Enviar nova senha ...
                String oldPass = sha256(old_pass.getText().toString());
                String newPass = sha256(new_pass.getText().toString());

                if (oldPass != null && newPass != null) {
                    PasswordClient passwordClient = new PasswordClient(getContext(), getActivity());
                    passwordClient.postJson(oldPass, newPass);
                }
            }
        } else {
            Toast.makeText(getContext(), "Você deve estar online para trocar a sua senha.", Toast.LENGTH_SHORT).show();
        }
    }
}
