package com.onegravity.rteditor;

import android.content.Context;

/**
 * Created by Arthur Zettler on 04/03/2016.
 */
public class RTEditorSingleton {
    private static RTEditorSingleton ourInstance = new RTEditorSingleton();
    public Context context;

    private RTEditorSingleton() {
    }

    public static RTEditorSingleton getInstance() {
        return ourInstance;
    }
}
