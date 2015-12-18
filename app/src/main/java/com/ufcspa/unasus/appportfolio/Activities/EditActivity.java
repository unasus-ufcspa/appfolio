package com.ufcspa.unasus.appportfolio.Activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.mikepenz.crossfader.Crossfader;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialize.util.UIUtils;
import com.ufcspa.unasus.appportfolio.Model.CrossfadeWrapper;
import com.ufcspa.unasus.appportfolio.R;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;


/**
 * Created by Convidado on 20/11/2015.
 */
public class EditActivity extends AppCompatActivity {
    private static final int PROFILE_SETTING = 1;
    private FragmentTabHost slider;
    private Animation animLeft;
    private Animation animRight;
    private boolean visible;
    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private MiniDrawer miniResult = null;
    private Crossfader crossFader;

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

        slider.addTab(slider.newTabSpec("Comments").setIndicator(null, getResources().getDrawable(R.drawable.ic_announcement_black_24dp)), FragmentComments.class, null);
        slider.addTab(slider.newTabSpec("New Edit Text").setIndicator(null, getResources().getDrawable(R.drawable.ic_copy)), FragEditText.class, null);
        slider.addTab(slider.newTabSpec("Old Edit Text").setIndicator(null, getResources().getDrawable(R.drawable.ic_share)), FragmentEditText.class, null);

        animLeft = AnimationUtils.loadAnimation(this, R.anim.anim_right);
        animRight = AnimationUtils.loadAnimation(this, R.anim.anim_left);

        visible = false;

        // Create a few sample profile
        final IProfile profile = new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(R.drawable.profile);
        final IProfile profile2 = new ProfileDrawerItem().withName("Max Muster").withEmail("max.mustermann@gmail.com").withIcon(R.drawable.profile2);

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(true)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(new ColorDrawable(Color.parseColor("#FDFDFD")))
                .withHeightPx(UIUtils.getActionBarHeight(this))
                .withAccountHeader(R.layout.material_drawer_compact_persistent_header)
                .withTextColor(Color.BLACK)
                .addProfiles(
                        profile,
                        profile2
                )
                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer
        DrawerBuilder builder = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                        new PrimaryDrawerItem().withName("Free to Play").withIcon(FontAwesome.Icon.faw_gamepad),
                        new PrimaryDrawerItem().withName("Custom").withIcon(FontAwesome.Icon.faw_eye),
                        new SectionDrawerItem().withName("Section Header"),
                        new SecondaryDrawerItem().withName("Settings").withIcon(FontAwesome.Icon.faw_cog),
                        new SecondaryDrawerItem().withName("Help").withIcon(FontAwesome.Icon.faw_question).withEnabled(false),
                        new SecondaryDrawerItem().withName("Source").withIcon(FontAwesome.Icon.faw_github),
                        new SecondaryDrawerItem().withName("Contacts").withIcon(FontAwesome.Icon.faw_bullhorn)
                )
                .withSavedInstance(savedInstanceState);

        //set the back arrow in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        // build only the view of the Drawer (don't inflate it automatically in our layout which is done with .build())
        result = builder.buildView();
        // create the MiniDrawer and define the drawer and header to be used (it will automatically use the items from them)
        miniResult = new MiniDrawer()
                .withDrawer(result)
                .withIncludeSecondaryDrawerItems(true)
                .withAccountHeader(headerResult);

        //get the widths in px for the first and second panel
        int firstWidth = (int) com.mikepenz.crossfader.util.UIUtils.convertDpToPixel(300, this);
        int secondWidth = (int) com.mikepenz.crossfader.util.UIUtils.convertDpToPixel(72, this);

        //create and build our crossfader (see the MiniDrawer is also builded in here, as the build method returns the view to be used in the crossfader)
        crossFader = new Crossfader()
                .withContent(findViewById(R.id.crossfade_content))
                .withFirst(result.getSlider(), firstWidth)
                .withSecond(miniResult.build(this), secondWidth)
                .withSavedInstance(savedInstanceState)
                .build();

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniResult.withCrossFader(new CrossfadeWrapper(crossFader));

        //define and create the arrow ;)
        ImageView toggle = (ImageView) headerResult.getView().findViewById(R.id.material_drawer_account_header_toggle);
        //for RTL you would have to define the other arrow
        toggle.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_chevron_left).sizeDp(16).color(Color.BLACK));
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crossFader.crossFade();
            }
        });
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        //add the values which need to be saved from the crossFader to the bundle
        outState = crossFader.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
}
