package com.ufcspa.unasus.appportfolio.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Model.OneComment;
import com.ufcspa.unasus.appportfolio.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentArrayAdapter extends ArrayAdapter<OneComment> {

    private TextView countryName;
    private TextView hour;
    private TextView date;
    private String lastDate;
    private String dateNow;
    private List<OneComment> countries = new ArrayList<OneComment>();
    private RelativeLayout wrapper;

    @Override
    public void add(OneComment object) {
        countries.add(object);
        super.add(object);
    }

    public CommentArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public int getCount() {
        return this.countries.size();
    }

    public OneComment getItem(int index) {
        return this.countries.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.comment_item, parent, false);
        }

        wrapper = (RelativeLayout) row.findViewById(R.id.wrapper);

        OneComment coment = getItem(position);

        countryName = (TextView) row.findViewById(R.id.comment);

        countryName.setText(coment.comment);
        if(coment.hour!=null && !coment.hour.isEmpty()) {
            hour = (TextView) row.findViewById(R.id.hour);
            hour.setText(coment.hour);
        }
        if(coment.date!=null && !coment.date.isEmpty()) {
            date = (TextView) row.findViewById(R.id.date);
            Log.d("comments","getting date...");

           /* if (position == 0) {
                Log.d("comments","first position");
                lastDate =convertDateToDate(coment.date);
                dateNow=lastDate;
                date.setText(dateNow);
                Log.d("comments", "datenow get:" + lastDate);
                date.setVisibility(View.VISIBLE);
            }

            dateNow=convertDateToDate(coment.date);

            if(!lastDate.equals(dateNow)){
                Log.d("comments","dateNow:"+dateNow+ "is diferent to lastDate:"+lastDate);
                lastDate=dateNow;
                date.setText(dateNow);
                Log.d("comments", "dateNow in component:" + date.getText().toString());
            }else{
                Log.d("comments","dateNow:"+dateNow+ "is equal to lastDate:"+lastDate);
                date.setVisibility(row.GONE);
                date.setVisibility(View.GONE);
                notifyDataSetChanged();
            }*/
            Log.d("comment", "item position:" + position);
            lastDate =convertDateToDate(coment.date);
            date.setText(lastDate);
            if(position%2==0) {

                date.setVisibility(View.GONE);
            }else{
                //countryName.setBackgroundResource(coment.orientation ? R.drawable.tutor_ballon : R.drawable.my_ballon);
                countryName.setBackgroundColor(Color.BLUE);
                wrapper.setGravity(coment.orientation ? Gravity.LEFT : Gravity.RIGHT);
            }
        }



        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
    public void clearAdapter(){
        countries.clear();
        notifyDataSetChanged();
    }

    public String convertDateToDate(String atualDate){
        String shortTimeStr="00:00";
        Log.d("comments","date receiving :"+atualDate);
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            date = df.parse(atualDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            shortTimeStr = sdf.format(date);
            Log.d("comments","date to other date format :"+shortTimeStr);
        } catch (ParseException e) {
            // To change body of catch statement use File | Settings | File Templates.
            e.printStackTrace();
        }
        return shortTimeStr;
    }


}