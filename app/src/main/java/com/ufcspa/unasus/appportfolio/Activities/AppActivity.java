package com.ufcspa.unasus.appportfolio.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.mikepenz.crossfader.Crossfader;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
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
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialize.util.UIUtils;
import com.ufcspa.unasus.appportfolio.Model.CrossfadeWrapper;
import com.ufcspa.unasus.appportfolio.R;

/**
 * Created by Zago on 18/12/2015.
 */
public class AppActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener {
    public static final int PROFILE_SETTING = 1;
    public AccountHeader headerResult = null;
    public Drawer result = null;
    public MiniDrawer miniResult = null;
    public Crossfader crossFader;

    public void createDrawer(Bundle savedInstanceState) {
        // Create a few sample profile
        final IProfile profile = new ProfileDrawerItem().withName("Tutor").withEmail("tutor@folio.com");
        final IProfile profile2 = new ProfileDrawerItem().withName("Aluno").withEmail("aluno@folio.com");

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
                        new PrimaryDrawerItem().withName(R.string.drawer_portfolio).withIcon(FontAwesome.Icon.faw_photo).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_activities).withIcon(FontAwesome.Icon.faw_pencil),
                        new SectionDrawerItem().withName(R.string.drawer_general),
                        new SecondaryDrawerItem().withName(R.string.drawer_files).withIcon(FontAwesome.Icon.faw_paste),
                        new SecondaryDrawerItem().withName(R.string.drawer_reports).withIcon(FontAwesome.Icon.faw_calendar),
                        new SecondaryDrawerItem().withName(R.string.drawer_settings).withIcon(FontAwesome.Icon.faw_cog)
                )
                .withSavedInstance(savedInstanceState)
                .withOnDrawerItemClickListener(this);

//        //set the back arrow in the toolbar
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(false);

        // build only the view of the Drawer (don't inflate it automatically in our layout which is done with .build())
        result = builder.buildView();
        // create the MiniDrawer and define the drawer and header to be used (it will automatically use the items from them)
        miniResult = new MiniDrawer()
                .withDrawer(result)
                .withIncludeSecondaryDrawerItems(true)
                .withAccountHeader(headerResult);

        //get the widths in px for the first and second panel
        int firstWidth = (int) com.mikepenz.crossfader.util.UIUtils.convertDpToPixel(250, this);
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
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        //add the values which need to be saved from the crossFader to the bundle
        outState = crossFader.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        System.out.println(position);
        switch (position) {
            case 1:
                startActivity(new Intent(this, SelectPortfolioActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, SelectActivitiesActivity.class));
                break;
            default:
                break;
        }
        return false;
    }
}
