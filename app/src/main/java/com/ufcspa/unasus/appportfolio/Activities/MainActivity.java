package com.ufcspa.unasus.appportfolio.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ufcspa.unasus.appportfolio.R;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private LinearLayout drawer;
    private Button button;
    private boolean isClicked = false;
    private int _xDelta;
    private int _yDelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        drawer = (LinearLayout) findViewById(R.id.mini_drawer);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClicked) {
                    isClicked = false;
                    drawer.animate()
                            .translationXBy(150)
                            .translationX(0)
                            .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));

                } else {
                    isClicked = true;
                    drawer.animate()
                            .translationXBy(0)
                            .translationX(150)
                            .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
                }
            }
        });

        drawer.setOnTouchListener(this);
    }

    //http://stackoverflow.com/questions/9398057/android-move-a-view-on-touch-move-action-move
    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                drawer.setLayoutParams(layoutParams);
                break;
        }
        drawer.invalidate();
        return true;
    }
}
