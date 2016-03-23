package com.ufcspa.unasus.appportfolio.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Model.OneComment;
import com.ufcspa.unasus.appportfolio.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by icaromsc on 25/02/2016.
 */
public class CommentAdapter extends BaseAdapter {
    private Context context;
    private static LayoutInflater inflater = null;
    private List<OneComment> comments;
    private String lastDate;

    public CommentAdapter(Context context, List<OneComment> comments) {
        this.context = context;
        this.comments = comments;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }




    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        OneComment c = comments.get(position);
        View rowView;
        if(c.atach==true){
            rowView = inflater.inflate(R.layout.atach_item, null);
            Log.e("comment attach", "atach true:" + position);
        }else {
            rowView = inflater.inflate(R.layout.comment_item, null);
        }
        Holder holder = new Holder();

        //getting from xml
        holder.message=(TextView)rowView.findViewById(R.id.comment);
        holder.hour=(TextView)rowView.findViewById(R.id.hour);
        holder.date=(TextView)rowView.findViewById(R.id.date);
        holder.wraper=(RelativeLayout)rowView.findViewById(R.id.wrapper);


        //populating
        holder.message.setText(c.comment);
        holder.hour.setText(c.hour);
        holder.date.setText(c.date);


        ///////////change visibility////////////////

        //Log.d("comment", "item position:" + position);
        if (position == 0) {
            //Log.d("comments","first position");
            lastDate =c.date;
            holder.date.setText(c.date);
            //Log.d("comments", "datenow get:" + lastDate);
            holder.date.setVisibility(rowView.VISIBLE);
        }else {

            if (!lastDate.equals(c.date)) {
                //Log.d("comments", "dateNow:" + c.date + "is diferent to lastDate:" + lastDate);
                lastDate = c.date;
                holder.date.setText(c.date);
                //Log.d("comments", "dateNow in component:" + holder.date.getText().toString());
            } else {
                //Log.d("comments", "dateNow:" + c.date + "is equal to lastDate:" + lastDate);
                holder.date.setVisibility(rowView.GONE);
            }
        }
        if(c.atach==false) {
            holder.message.setBackgroundResource(c.orientation ? R.drawable.final_b_ger_right : R.drawable.final_b_ger_left);
            holder.wraper.setGravity(c.orientation ? Gravity.LEFT : Gravity.RIGHT);
        }
        //////////---------------////////////////////
        return rowView;
    }

    public void clearAdapter(){
        comments.clear();
        notifyDataSetChanged();
    }

    public class Holder
    {
        TextView message;
        TextView hour;
        TextView date;
        RelativeLayout wraper;
    }



    public void refresh(List<OneComment> comnts){
        this.comments=comnts;
        notifyDataSetChanged();
    }
}
