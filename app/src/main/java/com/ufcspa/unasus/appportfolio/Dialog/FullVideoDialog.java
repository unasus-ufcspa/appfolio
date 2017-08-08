package com.ufcspa.unasus.appportfolio.Dialog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.VideoView;

import com.ufcspa.unasus.appportfolio.R;

/**
 * Created by Arthur Zettler on 23/02/2016.
 */
public class FullVideoDialog extends Activity
{
    private boolean isRTEditor;
    private String url;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_video);

        if (savedInstanceState == null)
        {
            Bundle extras = getIntent().getExtras();
            if(extras == null)
            {
                isRTEditor = false;
                url = null;
                position = -1;
            }
            else
            {
                url = extras.getString("URL");
                isRTEditor = extras.getBoolean("isRTEditor", true);
                position = extras.getInt("Position", -1);
            }
        }
        else
        {
            url = (String) savedInstanceState.getSerializable("URL");
            isRTEditor = savedInstanceState.getBoolean("isRTEditor", true);
            position = savedInstanceState.getInt("Position", -1);
        }

        init();
    }

    private void init()
    {
        if(url != null)
        {
            VideoView video = (VideoView) findViewById(R.id.videoView);

            video.setVideoURI(Uri.parse(url));
            video.requestFocus();
            video.start();
        }

        Button btnPositive = (Button) findViewById(R.id.btn_positive_video);
        Button btnNegative = (Button) findViewById(R.id.btn_negative_video);

        if (isRTEditor)
        {
            btnNegative.setText(getResources().getText(R.string.attachment_negative));
            btnPositive.setText(getResources().getText(R.string.attachment_positive));
        }
        else
        {
            btnNegative.setText(getResources().getText(R.string.attachment_delete));
            btnPositive.setText(getResources().getText(R.string.attachment_fechar));
        }

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRTEditor)
                    LocalBroadcastManager.getInstance(getApplicationContext())
                            .sendBroadcast(new Intent("call.dialog_close.action")
                                    .putExtra("Type", 1)
                                    .putExtra("isPositive", true)
                                    .putExtra("URL", url)
                                    .putExtra("Position", position));
                finish();
            }
        });

        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRTEditor)
                    LocalBroadcastManager.getInstance(getApplicationContext())
                            .sendBroadcast(new Intent("call.dialog_close.action")
                                    .putExtra("Type", 1)
                                    .putExtra("isPositive", false)
                                    .putExtra("URL", url)
                                    .putExtra("Position", position));
                finish();
            }
        });
    }
}
