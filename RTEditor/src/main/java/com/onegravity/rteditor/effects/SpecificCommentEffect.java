package com.onegravity.rteditor.effects;

import android.text.Spannable;
import android.text.Spanned;

import com.onegravity.rteditor.RTEditText;
import com.onegravity.rteditor.spans.RTSpan;
import com.onegravity.rteditor.spans.SpecificCommentSpan;
import com.onegravity.rteditor.utils.Selection;

/**
 * Created by Arthur Zettler on 26/01/2016.
 */
public class SpecificCommentEffect extends CharacterEffect<String, SpecificCommentSpan> {

    @Override
    protected RTSpan<String> newSpan(String value) {
        return new SpecificCommentSpan(value);
    }

    @Override
    public void applyToSelection(RTEditText editor, String url) {
        Selection selection = getSelection(editor);
        Spannable str = editor.getText();

        if (url == null) {
            // adjacent links need to be removed --> expand the selection by [1, 1]
            for (RTSpan<String> span : getSpans(str, selection.offset(1, 1), SpanCollectMode.EXACT)) {
                str.removeSpan(span);
            }
        }
        else {
            for (RTSpan<String> span : getSpans(str, selection, SpanCollectMode.EXACT)) {
                str.removeSpan(span);
            }

            // if url is Null then the link won't be set meaning existing links will be removed
            str.setSpan(newSpan(url), selection.start(), selection.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

}