package com.ufcspa.unasus.appportfolio.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.model.OneComment;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBase;

import java.util.List;

/**
 * Created by icaromsc on 26/02/2016.
 */
public class SpecificCommentAdapter extends BaseAdapter {
    private static LayoutInflater sInflater = null;
    private Context mContext;
    private List<OneComment> mCommentList;
    private String mLastDate;

    public SpecificCommentAdapter(Context mContext, List<OneComment> mCommentList) {
        this.mContext = mContext;
        this.mCommentList = mCommentList;
        sInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLastDate = "01/01/2001";
    }


    @Override
    public int getCount() {
        return mCommentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCommentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        OneComment c = mCommentList.get(position);
        if (c.atach)
            rowView = sInflater.inflate(R.layout.item_attachment, null);
        else
            rowView = sInflater.inflate(R.layout.item_specific_comment, null);

        Holder holder = new Holder();

        //getting from xml
        holder.message = (TextView) rowView.findViewById(R.id.comment);
        holder.hour = (TextView) rowView.findViewById(R.id.hour);
        holder.date = (TextView) rowView.findViewById(R.id.date);
        holder.userName = (TextView) rowView.findViewById(R.id.userName);
        holder.wraper = (RelativeLayout) rowView.findViewById(R.id.wrapper);

        //populating
        holder.message.setText(c.comment);
        holder.hour.setText(c.hour);
        holder.date.setText(c.date);
        String[] name = DataBase.getInstance(parent.getContext()).getNameByUserId(c.idAuthor).split(" ");
        holder.userName.setText(name[0] + ":");
        //Log.d("comment specific","message in holder"+holder.message.getText().toString());

        ///////////change visibility////////////////

        //Log.d("comment", "item position:" + position);

        if (position == 0) {
            // Log.d("mCommentList","first position");
            mLastDate = c.date;
            holder.date.setText(c.date);
            // Log.d("mCommentList", "datenow get:" + mLastDate);
            holder.date.setVisibility(View.VISIBLE);
        } else {
            if (!mLastDate.equals(c.date)) {
                //Log.d("mCommentList", "dateNow:" + c.date + "is diferent to mLastDate:" + mLastDate);
                mLastDate = c.date;
                holder.date.setText(c.date);
                // Log.d("mCommentList", "dateNow in component:" + holder.date.getText().toString());
            } else {
                //Log.d("mCommentList", "dateNow:" + c.date + "is equal to mLastDate:" + mLastDate);
                holder.date.setVisibility(View.GONE);
            }
        }
        if (c.atach == false) {
            holder.message.setBackgroundResource(c.orientation ? R.drawable.bg_balloon_right : R.drawable.bg_balloon_left);
            holder.wraper.setGravity(c.orientation ? Gravity.RIGHT : Gravity.LEFT);
        }
        //////////---------------////////////////////
        return rowView;
    }

    public void clearAdapter() {
        mCommentList.clear();
        notifyDataSetChanged();
    }

    public void refresh(List<OneComment> comnts) {
        this.mCommentList = comnts;
        notifyDataSetChanged();
    }

    public class Holder {
        TextView message;
        TextView hour;
        TextView date;
        TextView userName;
        RelativeLayout wraper;
    }

}
