package com.ufcspa.unasus.appportfolio.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ufcspa.unasus.appportfolio.Model.DragLayout;
import com.ufcspa.unasus.appportfolio.R;


public class MainActivity extends AppCompatActivity {//implements View.OnTouchListener {

    private DragLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        drawer = (DragLayout) findViewById(R.id.drag);
        drawer.setDragHorizontal(true);
    }

    //http://stackoverflow.com/questions/9398057/android-move-a-view-on-touch-move-action-move
    //http://stackoverflow.com/questions/4946295/android-expand-collapse-animation

    //    private Button button;
//    private boolean isClicked = false;
//    private float x1,x2;
//    static final int MIN_DISTANCE = 150;
//    private int _xDelta;

//        button = (Button) findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isClicked) {
//                    isClicked = false;
////                    drawer.animate()
////                            .translationXBy(150)
////                            .translationX(0)
////                            .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
//                } else {
//                    isClicked = true;
////                    drawer.animate()
////                            .translationXBy(0)
////                            .translationX(150)
////                            .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
//                }
//            }
//        });
//
//        drawer.setOnTouchListener(this);
////    @Override
////    public boolean onTouchEvent(MotionEvent event)
////    {
////        switch(event.getAction())
////        {
////            case MotionEvent.ACTION_DOWN:
////                x1 = event.getX();
////                break;
////            case MotionEvent.ACTION_UP:
////                x2 = event.getX();
////                float deltaX = x2 - x1;
////
////                if (Math.abs(deltaX) > MIN_DISTANCE)
////                {
////                    // Left to Right swipe action
////                    if (x2 > x1)
////                    {
////                        Toast.makeText(this, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show ();
////                    }
////
////                    // Right to left swipe action
////                    else
////                    {
////                        Toast.makeText(this, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show ();
////                    }
////
////                }
////                else
////                {
////                    // consider as something else - a screen tap for example
////                }
////                break;
////        }
////        return super.onTouchEvent(event);
////    }
//
////    @Override
////    public boolean onTouch(View v, MotionEvent event) {
////        switch(event.getAction())
////        {
////            case MotionEvent.ACTION_DOWN:
////                x1 = event.getX();
////                break;
////            case MotionEvent.ACTION_UP:
////                x2 = event.getX();
////                float deltaX = x2 - x1;
////
////                if (Math.abs(deltaX) > MIN_DISTANCE)
////                {
////                    // Left to Right swipe action
////                    if (x2 > x1)
////                    {
////                        //drawer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.drawer_open));
////                        Toast.makeText(this, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show();
////                    }
////
////                    // Right to left swipe action
////                    else
////                    {
////                        Toast.makeText(this, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show ();
////                    }
////
////                }
////                else
////                {
////                    // consider as something else - a screen tap for example
////                }
////                break;
////        }
////        return true;
////    }
//    public boolean onTouch(View view, MotionEvent event) {
//        final int X = (int) event.getRawX();
//        switch (event.getAction() & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN:
//                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
//                _xDelta = X - lParams.leftMargin;
////                _yDelta = Y - lParams.topMargin;
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                break;
//            case MotionEvent.ACTION_POINTER_UP:
//                break;
//            case MotionEvent.ACTION_MOVE:
//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
//                layoutParams.leftMargin = X - _xDelta;
//                layoutParams.rightMargin = -500;
//                view.setLayoutParams(layoutParams);
//                break;
//        }
//        drawer.invalidate();
//        return true;
//    }
}
