package com.onegravity.rteditor.spans;

import android.text.style.URLSpan;
import android.view.View;

/**
 * Created by Arthur Zettler on 26/01/2016.
 */
public class SpecificCommentSpan extends URLSpan implements RTSpan<String> {

    public interface SpecificCommentSpanListener {
        public void onClick(SpecificCommentSpan linkSpan);
    }

    public SpecificCommentSpan(String url) {
        super(url);
    }

    @Override
    public void onClick(View view) {
        if (view instanceof SpecificCommentSpanListener) {
            ((SpecificCommentSpanListener) view).onClick(this);
        }
    }

    @Override
    public String getValue() {
        return getURL();
    }

}
