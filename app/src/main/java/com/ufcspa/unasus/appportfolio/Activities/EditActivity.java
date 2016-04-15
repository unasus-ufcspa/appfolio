package com.ufcspa.unasus.appportfolio.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentAttachment;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentComments;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentReference;
import com.ufcspa.unasus.appportfolio.R;


/**
 * Created by Convidado on 20/11/2015.
 */
public class EditActivity extends AppActivity {
    private FragmentTabHost slider;
    private Animation animLeft;
    private Animation animRight;
    private boolean visible;
    //save our header or result

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_activity);

        slider = (FragmentTabHost) findViewById(R.id.slider);
        slider.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        slider.setVisibility(View.GONE);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        slider.getLayoutParams().width = width / 2;
        slider.requestLayout();
        slider.bringToFront();

        slider.addTab(slider.newTabSpec("Comments").setIndicator(null, getResources().getDrawable(R.drawable.ic_announcement_black_24dp)), FragmentComments.class, null);
        slider.addTab(slider.newTabSpec("References").setIndicator(null, getResources().getDrawable(R.drawable.ic_copy)), FragmentReference.class, null);
        slider.addTab(slider.newTabSpec("Old Edit Text").setIndicator(null, getResources().getDrawable(R.drawable.ic_share)), FragmentAttachment.class, null);

        animLeft = AnimationUtils.loadAnimation(this, R.anim.anim_right);
        animRight = AnimationUtils.loadAnimation(this, R.anim.anim_left);

        visible = false;

        createDrawer(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_favorite)
            showDrawer();
        return super.onOptionsItemSelected(item);
    }

    public void showDrawer()
    {
        if(!visible)
        {
            slider.setVisibility(View.VISIBLE);
            slider.startAnimation(animLeft);
            visible = true;
        }
        else
        {
            slider.startAnimation(animRight);
            slider.setVisibility(View.GONE);
            visible = false;
        }
    }

}
