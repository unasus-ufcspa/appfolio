package com.onegravity.rteditor.spans;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;

/**
 * Created by Arthur Zettler on 12/02/2016.
 */
public class MyBackgroundColorSpan extends CharacterStyle
        implements UpdateAppearance, ParcelableSpan {

    private int mColor;

    public MyBackgroundColorSpan(int color) {
        mColor = color;
    }

    public MyBackgroundColorSpan(Parcel src) {
        mColor = src.readInt();
    }

    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    /** @hide */
    public int getSpanTypeIdInternal() {
        return 1;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }

    /** @hide */
    public void writeToParcelInternal(Parcel dest, int flags) {
        dest.writeInt(mColor);
    }

    public int getBackgroundColor() {
        return mColor;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.bgColor = mColor;
    }

    public void setColor(int i)
    {
        mColor = i;
    }
}