package com.ufcspa.unasus.appportfolio.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Model.OneComment;
import com.ufcspa.unasus.appportfolio.R;

import java.util.List;

/**
 * Created by icaromsc on 26/02/2016.
 */
public class SpecificCommentAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Context context;
    private List<OneComment> comments;
    private String lastDate;

    public SpecificCommentAdapter(Context context, List<OneComment> comments) {
        this.context = context;
        this.comments = comments;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        lastDate="01/01/2001";
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
        View rowView;
        OneComment c = comments.get(position);
        if(c.atach)
            rowView = inflater.inflate(R.layout.atach_item, null);
        else
            rowView = inflater.inflate(R.layout.specific_comment_item, null);




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
        //Log.d("comment specific","message in holder"+holder.message.getText().toString());

        ///////////change visibility////////////////

        //Log.d("comment", "item position:" + position);

        if (position == 0) {
           // Log.d("comments","first position");
            lastDate = c.date;
            holder.date.setText(c.date);
           // Log.d("comments", "datenow get:" + lastDate);
            holder.date.setVisibility(rowView.VISIBLE);
        }else {

            if (!lastDate.equals(c.date)) {
                //Log.d("comments", "dateNow:" + c.date + "is diferent to lastDate:" + lastDate);
                lastDate = c.date;
                holder.date.setText(c.date);
               // Log.d("comments", "dateNow in component:" + holder.date.getText().toString());
            } else {
                //Log.d("comments", "dateNow:" + c.date + "is equal to lastDate:" + lastDate);
                holder.date.setVisibility(rowView.GONE);
            }
        }
        if(c.atach==false) {
            holder.message.setBackgroundResource(c.orientation ? R.drawable.final_b_ger_left : R.drawable.final_b_ger_right);
            holder.wraper.setGravity(c.orientation ? Gravity.LEFT : Gravity.RIGHT);
        }
        //////////---------------////////////////////
        return rowView;
    }

    public void clearAdapter(){
        comments.clear();
        notifyDataSetChanged();
    }

    public void refresh(List<OneComment> comnts) {
        this.comments = comnts;
        notifyDataSetChanged();
    }

    public class Holder
    {
        TextView message;
        TextView hour;
        TextView date;
        RelativeLayout wraper;
    }

}
