package com.ufcspa.unasus.appportfolio.Adapter;

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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentConfig;
import com.ufcspa.unasus.appportfolio.Model.Comentario;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.Model.User;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.ArrayList;

/**
 * Created by Steffano on 26/06/2017.
 */

public class UserListadapter extends BaseAdapter {
    private ArrayList<User> userList;
    private DataBaseAdapter source;
    private Context context;
    private static LayoutInflater inflater = null;
    private Singleton singleton;

    public UserListadapter(Context context, ArrayList<User> userList) {
        this.userList = userList;
        this.context = context;

        singleton = Singleton.getInstance();
        source = DataBaseAdapter.getInstance(context);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public User getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User aux = userList.get(position);

        convertView = inflater.inflate(R.layout.student_report_item,null);

        TextView studentName=(TextView)convertView.findViewById(R.id.p_student_name);
//        studentName.setText(aux.getName());
        studentName.setVisibility(View.INVISIBLE);
        final ImageView studentPhoto = (ImageView) convertView.findViewById(R.id.student_image);

        Bitmap photo = aux.getPhotoBitmap();
        if (photo != null)
            studentPhoto.setImageBitmap(FragmentConfig.getRoundedRectBitmap(photo,100));
        else
            studentPhoto.setImageResource(R.drawable.default_profile_picture);

        studentPhoto.setLayoutParams(new LinearLayout.LayoutParams(48,48));

        studentPhoto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("altura personal",v.getRootView().findViewById(R.id.personal_comment_container).getHeight()+"");
                displayPersonalComment(v.getRootView().findViewById(R.id.personal_comment_container),aux,event.getRawY()-v.getHeight());
                return false;
            }
        });

        return convertView;

    }

    public void displayPersonalComment(View anchorView,User userPerfil,float y) {
        String cellphone = userPerfil.getCellphone();
        RelativeLayout dialogStudent = (RelativeLayout) anchorView.findViewById(R.id.personal_dialog_student);
        TextView student_name = (TextView) anchorView.findViewById(R.id.p_student_name);
        TextView student_cell = (TextView) anchorView.findViewById(R.id.student_phone);
        EditText edtTextPersonal = (EditText) anchorView.findViewById(R.id.edittext_personal_comment);

        if (anchorView.getVisibility() == View.VISIBLE)
            anchorView.setVisibility(View.GONE);
        else {
            anchorView.setVisibility(View.VISIBLE);
            anchorView.setY(y);
            student_cell.setText(cellphone);
            ArrayList<Comentario> cs=(ArrayList)source.listGComments(singleton.idActivityStudent, "P");
            if(cs!=null && cs.size()!=0){
                if(cs.get(0).getTxtComment()!=null){
                    edtTextPersonal.setText(cs.get(0).getTxtComment());
                }
            }

            if(singleton.portfolioClass.getPerfil().equals("S")||userPerfil.getUserType()=='T'){
                student_name.setText(userPerfil.getName());
                dialogStudent.setVisibility(View.GONE);

            }else{
                dialogStudent.setVisibility(View.VISIBLE);
                student_name.setText(singleton.portfolioClass.getStudentName());
                cellphone = singleton.portfolioClass.getCellphone();
                if (cellphone != null && !cellphone.equals("null") && singleton.portfolioClass.getPerfil().equals("S") )
                    student_cell.setText(singleton.portfolioClass.getCellphone());
                else
                    student_cell.setText("");
            }


        }
    }
}
