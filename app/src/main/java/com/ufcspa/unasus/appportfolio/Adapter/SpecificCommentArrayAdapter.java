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
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Model.OneComment;
import com.ufcspa.unasus.appportfolio.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by icaromsc on 15/02/2016.
 */
public class SpecificCommentArrayAdapter extends ArrayAdapter<OneComment>{
    private TextView messageText;
    private TextView referenceText;
    private List<OneComment> comment = new ArrayList<OneComment>();
    private LinearLayout wrapper;
    private boolean insertingReference=false;

    public boolean isInsertingReference() {
        return insertingReference;
    }
    public void setInsertingReference(boolean insertingReference) {
        this.insertingReference = insertingReference;
    }

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

        wrapper = (LinearLayout) row.findViewById(R.id.wrapper);

        OneComment coment = getItem(position);

        messageText = (TextView) row.findViewById(R.id.comment);

        messageText.setText(coment.comment);
            if(insertingReference) {
                messageText.setText(coment.comment);
                insertingReference=false;
            }else{
                if(position!=0) {
                    messageText.setBackgroundResource(coment.orientation ? R.drawable.tutor_ballon_specific_comment : R.drawable.my_ballon_specific_comment);
                    wrapper.setGravity(coment.orientation ? Gravity.LEFT : Gravity.RIGHT);
                }else{
                    Log.d("view","fisrt position");
                }

            }
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
    public void clearAdapter(){
        comment.clear();
        notifyDataSetChanged();
    }
}
