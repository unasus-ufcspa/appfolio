package com.ufcspa.unasus.appportfolio.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.activities.fragments.ConfigFragment;
import com.ufcspa.unasus.appportfolio.model.Comentario;
import com.ufcspa.unasus.appportfolio.model.Singleton;
import com.ufcspa.unasus.appportfolio.model.User;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBase;

import java.util.ArrayList;

/**
 * Created by Steffano on 26/06/2017.
 */

public class UserListAdapter extends BaseAdapter {
    private ArrayList<User> mUserList;
    private DataBase mDataBase;
    private Context mContext;
    private static LayoutInflater sInflater = null;
    private static Singleton sSingleton;

    public UserListAdapter(Context mContext, ArrayList<User> mUserList) {
        this.mUserList = mUserList;
        this.mContext = mContext;

        sSingleton = Singleton.getInstance();
        mDataBase = DataBase.getInstance(mContext);
        sInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mUserList.size();
    }

    @Override
    public User getItem(int position) {
        return mUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User aux = mUserList.get(position);

        convertView = sInflater.inflate(R.layout.item_student_report, null);

        TextView studentName = (TextView) convertView.findViewById(R.id.p_student_name);
//        studentName.setText(aux.getName());
        studentName.setVisibility(View.INVISIBLE);
        final ImageView studentPhoto = (ImageView) convertView.findViewById(R.id.student_image);

        Bitmap photo = aux.getPhotoBitmap();
        if (photo != null)
            studentPhoto.setImageBitmap(ConfigFragment.getRoundedRectBitmap(photo, 100));
        else
            studentPhoto.setImageResource(R.drawable.ic_default_picture);

        studentPhoto.setLayoutParams(new LinearLayout.LayoutParams(48, 48));

        studentPhoto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("altura personal", v.getRootView().findViewById(R.id.personal_comment_container).getHeight() + "");
                displayPersonalComment(v.getRootView().findViewById(R.id.personal_comment_container), aux, event.getRawY() - v.getHeight());
                return false;
            }
        });

        return convertView;

    }

    public void displayPersonalComment(View anchorView, User userPerfil, float y) {
        String cellphone = userPerfil.getCellphone();
        RelativeLayout dialogStudent = (RelativeLayout) anchorView.findViewById(R.id.personal_dialog_student);
        TextView studentName = (TextView) anchorView.findViewById(R.id.p_student_name);
        TextView studentCell = (TextView) anchorView.findViewById(R.id.student_phone);
        EditText edtTextPersonal = (EditText) anchorView.findViewById(R.id.edittext_personal_comment);

        if (anchorView.getVisibility() == View.VISIBLE)
            anchorView.setVisibility(View.GONE);
        else {
            anchorView.setVisibility(View.VISIBLE);
            anchorView.setY(y);
            studentCell.setText(cellphone);
            ArrayList<Comentario> cs = (ArrayList) mDataBase.listGComments(sSingleton.idActivityStudent, "P");
            if (cs != null && cs.size() != 0) {
                if (cs.get(0).getTxtComment() != null) {
                    edtTextPersonal.setText(cs.get(0).getTxtComment());
                }
            }

            if (sSingleton.portfolioClass.getPerfil().equals("S") || userPerfil.getUserType() == 'T') {
                studentName.setText(userPerfil.getName());
                dialogStudent.setVisibility(View.GONE);

            } else {
                dialogStudent.setVisibility(View.VISIBLE);
                studentName.setText(sSingleton.portfolioClass.getStudentName());
                cellphone = sSingleton.portfolioClass.getCellphone();
                if (cellphone != null && !cellphone.equals("null") && sSingleton.portfolioClass.getPerfil().equals("S"))
                    studentCell.setText(sSingleton.portfolioClass.getCellphone());
                else
                    studentCell.setText("");
            }


        }
    }
}
