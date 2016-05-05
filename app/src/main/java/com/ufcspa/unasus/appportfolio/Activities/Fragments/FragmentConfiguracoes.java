package com.ufcspa.unasus.appportfolio.Activities.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.onegravity.rteditor.utils.Constants;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.Model.User;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentConfiguracoes extends Frag implements View.OnClickListener {

    private ImageView btn_change_image;
    private ImageView img_user;
    private EditText edt_nome;
    private EditText edt_email;
    private EditText edt_telefone;
    private EditText edt_senha;
    private EditText edt_confirma_senha;
    private Button btn_alterar;

    private User myUser;

    public FragmentConfiguracoes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_configuracoes, container, false);

        singleton = Singleton.getInstance();
        source = DataBaseAdapter.getInstance(getContext());

        init(view);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mCurrentPhotoPath != null)
            outState.putString("image", mCurrentPhotoPath);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey("image")) {
            mCurrentPhotoPath = savedInstanceState.getString("image");
            Bitmap bmImg = BitmapFactory.decodeFile(mCurrentPhotoPath);
            img_user.setImageBitmap(bmImg);
        }
    }

    private void init(View view) {
        btn_change_image = (ImageView) view.findViewById(R.id.btn_change_image);
        img_user = (ImageView) view.findViewById(R.id.img_user);
        edt_nome = (EditText) view.findViewById(R.id.edt_nome);
        edt_email = (EditText) view.findViewById(R.id.edt_email);
        edt_telefone = (EditText) view.findViewById(R.id.edt_telefone);
        edt_senha = (EditText) view.findViewById(R.id.edt_senha);
        edt_confirma_senha = (EditText) view.findViewById(R.id.edt_confirma_senha);
        btn_alterar = (Button) view.findViewById(R.id.btn_alterar);

        myUser = singleton.user;

        if (myUser.getPhoto() != null) {
            Bitmap bmImg = BitmapFactory.decodeFile(myUser.getPhoto());
            img_user.setImageBitmap(bmImg);
        }

        edt_nome.setText(myUser.getName());
        edt_email.setText(myUser.getEmail());
        if (myUser.getCellphone() != null)
            edt_telefone.setText(myUser.getCellphone());

        btn_change_image.setOnClickListener(this);
        btn_alterar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_alterar) {
            verifyEditText();
        }
        if (v.getId() == R.id.btn_change_image) {
            dispatchTakePictureIntent();
        }
    }

    private void verifyEditText() {
        String email = edt_email.getText().toString();
        String telefone = edt_telefone.getText().toString();
        String senha = edt_senha.getText().toString();
        String confirma_senha = edt_confirma_senha.getText().toString();

        User newUser = myUser;

        if (mCurrentPhotoPath != null)
            newUser.setPhoto(mCurrentPhotoPath);

        if (email != null)
            newUser.setEmail(email);

        if (telefone != null)
            newUser.setCellphone(telefone);

        if (senha != null && confirma_senha != null)
            if (senha.equals(confirma_senha))
                newUser.setPassword(senha);

        singleton.user = newUser;

        source.updateTBUser(singleton.user);

        /*
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            base64Image = Base64.encodeToString(b,Base64.DEFAULT);
         */
        //Sync sync = new Sync(singleton.device.get_id_device(), "tb_user", singleton.user.getIdUser(), -1);
        //source.insertIntoTBSync(sync);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.CROP_IMAGE) {
                saveImageOnAppDir();
                insertFileIntoDataBase(mCurrentPhotoPath, "I");

                Bitmap bmImg = BitmapFactory.decodeFile(mCurrentPhotoPath);
                img_user.setImageBitmap(bmImg);

            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                galleryAddPic();
                launchCropImageIntent();
            }
        }
    }
}
