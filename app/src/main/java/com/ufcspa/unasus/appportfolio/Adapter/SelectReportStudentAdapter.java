package com.ufcspa.unasus.appportfolio.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Model.User;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.List;

/**
 * Created by Desenvolvimento on 08/01/2016.
 */
public class SelectReportStudentAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Context context;
    private List<User> listaUsers;
    private DataBaseAdapter dataBaseAdapter;

    public SelectReportStudentAdapter(Context context, List<User> classes)
    {
        this.context = context;
        this.listaUsers = classes;
        this.dataBaseAdapter = DataBaseAdapter.getInstance(context);

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listaUsers.size();
    }

    @Override
    public User getItem(int position) {
        return listaUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        final View rowView;
        Singleton.getInstance().idStudent=listaUsers.get(position).getIdUser();

        rowView = inflater.inflate(R.layout.student_report_item, null);

        holder.p_student_name=(TextView)rowView.findViewById(R.id.p_student_name);
        holder.p_student_name.setText(listaUsers.get(position).getName());
        holder.student_image=(ImageView)rowView.findViewById(R.id.student_image);
        if (listaUsers.get(position).getPhotoBitmap()!=null) {
            holder.student_image.setImageBitmap(listaUsers.get(position).getPhotoBitmap());
        } else {
            holder.student_image.setImageResource(R.drawable.default_profile_picture);
        }

        holder.student_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance().idStudent=listaUsers.get(position).getIdUser();
                changeScreen();
            }
        });

        return rowView;
    }

    private void changeScreen() {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("call.fragments.action").putExtra("ID", 7));
    }

    public class Holder
    {
        TextView p_student_name;
        ImageView student_image;
    }
}

