package com.ufcspa.unasus.appportfolio.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

/**
 * Created by icaromsc on 15/02/2016.
 */
public class SpecificCommentArrayAdapter extends ArrayAdapter<OneComment>{
    private TextView messageText;
    private TextView referenceText;
    private TextView hour;
    private List<OneComment> comment = new ArrayList<OneComment>();
    private RelativeLayout wrapper;



    @Override
    public void add(OneComment object) {
        comment.add(object);
        super.add(object);
    }

    public SpecificCommentArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public int getCount() {
        return this.comment.size();
    }

    public OneComment getItem(int index) {
        return this.comment.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.comment_item, parent, false);
            }

            wrapper = (RelativeLayout) row.findViewById(R.id.wrapper);
            OneComment coment = getItem(position);

            if(coment.hour!=null && !coment.hour.isEmpty()) {
                hour = (TextView) row.findViewById(R.id.hour);
                hour.setText(coment.hour);
            }

            messageText = (TextView) row.findViewById(R.id.comment);
            messageText.setText(coment.comment);
            messageText.setBackgroundResource(coment.orientation ? R.drawable.tutor_ballon_specific_comment : R.drawable.my_ballon_specific_comment);
            wrapper.setGravity(coment.orientation ? Gravity.LEFT : Gravity.RIGHT);
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
    public void clearAdapter(){
        comment.clear();
        notifyDataSetChanged();
    }


    public String convertDateToTime(String atualDate){
        String shortTimeStr="01/01/2015";
        Log.d("comments", "date receiving :" + atualDate);
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            date = df.parse(atualDate);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            shortTimeStr = sdf.format(date);
            Log.d("comments","date to hour :"+shortTimeStr);
        } catch (ParseException e) {
            // To change body of catch statement use File | Settings | File Templates.
            e.printStackTrace();
        }
        return shortTimeStr;
    }

}
