package com.ufcspa.unasus.appportfolio.Model;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by Desenvolvimento on 14/12/2015.
 */
public class WebAppInterface {


    Context mContext;

    WebAppInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void getText(String text) {
        // put selected text into clipdata
        ClipboardManager clipboard = (ClipboardManager)
                mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simple text", text);
        clipboard.setPrimaryClip(clip);
        // gives the toast for selected text
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }
}
