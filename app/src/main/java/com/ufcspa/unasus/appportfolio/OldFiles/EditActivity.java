package com.ufcspa.unasus.appportfolio.OldFiles;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ListView;

import com.ufcspa.unasus.appportfolio.Activities.FragEditText;
import com.ufcspa.unasus.appportfolio.Activities.FragmentComments;
import com.ufcspa.unasus.appportfolio.Activities.FragmentEditText;
import com.ufcspa.unasus.appportfolio.R;


/**
 * Created by Convidado on 20/11/2015.
 */
public class EditActivity extends AppCompatActivity {
}
//    private DrawerLayout mDrawerLayout;
//    private ListView mDrawerList;
////    private ListView mRightDrawerList;
//    private CharSequence mDrawerTitle;
//    private CharSequence mTitle;
//    //private CommentArrayAdapter commentAdapter;
//    private ActionBarDrawerToggle mDrawerToggle;
//
//    private FragmentTabHost slider;
//    private Animation animLeft;
//    private Animation animRight;
//    private boolean visible;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_activity);
//
//        mTitle = mDrawerTitle = getTitle();
//        //commentAdapter = new CommentArrayAdapter(getApplicationContext(), R.layout.comment_item);
//        String[] itens = {"Editar Texto", "Anexos", "Referências", "Comentários"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, itens);
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerList = (ListView) findViewById(R.id.left_drawer);
////        mRightDrawerList = (ListView) findViewById(R.id.right_drawer);
//
//        // Set the adapter for the list view
//        mDrawerList.setAdapter(adapter);
////        mRightDrawerList.setAdapter(commentAdapter);
//        // Set the list's click listener
//        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
////        mRightDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                selectItem(3);
////            }
////        });
//
//        // enable ActionBar app icon to behave as action to toggle nav drawer
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//
//        // ActionBarDrawerToggle ties together the the proper interactions
//        // between the sliding drawer and the action bar app icon
//        mDrawerToggle = new ActionBarDrawerToggle(
//                this,                  /* host Activity */
//                mDrawerLayout,         /* DrawerLayout object */
//                R.string.drawer_open,  /* "open drawer" description for accessibility */
//                R.string.drawer_close  /* "close drawer" description for accessibility */
//        )
//
//
//        {
//            public void onDrawerClosed(View view) {
//                getSupportActionBar().setTitle(mTitle);
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//                Log.d("Toolbar", "close");
//            }
//
//            public void onDrawerOpened(View drawerView) {
//                getSupportActionBar().setTitle(mDrawerTitle);
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//                //mDrawerLayout.openDrawer(mDrawerList);
//                Log.d("Toolbar", "open");
//            }
//        };
//        mDrawerLayout.setDrawerListener(mDrawerToggle);
//        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_drawer);
//
//
//        if (savedInstanceState == null) {
//            selectItem(0);
//        }
//
//        //getFragmentManager().beginTransaction().replace(R.id.content_right, new FragmentComments()).commit();
//
//        slider = (FragmentTabHost) findViewById(R.id.slider);
//        slider.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
//        slider.setVisibility(View.GONE);
//
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;
//
//        slider.getLayoutParams().width = width / 2;
//        slider.requestLayout();
//
//        slider.addTab(slider.newTabSpec("Comments").setIndicator(null, getResources().getDrawable(R.drawable.ic_announcement_black_24dp)), FragmentComments.class, null);
//        slider.addTab(slider.newTabSpec("New Edit Text").setIndicator(null, getResources().getDrawable(R.drawable.ic_copy)), FragEditText.class, null);
//        slider.addTab(slider.newTabSpec("Old Edit Text").setIndicator(null, getResources().getDrawable(R.drawable.ic_share)), FragmentEditText.class, null);
//
//        animLeft = AnimationUtils.loadAnimation(this, R.anim.anim_right);
//        animRight = AnimationUtils.loadAnimation(this, R.anim.anim_left);
//
//        visible = false;
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        return true;
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("Toolbar", "foi");
//            }
//        });
//        // addItems();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//    }
//
//    private class DrawerItemClickListener implements ListView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            selectItem(position);
//        }
//    }
//
//    private void selectItem(int position) {
//        switch (position) {
//            case 0:
//                //getFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentEditText()).commit();//FragmentEditText
//                break;
//            case 1:
//                // getFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentAttachment()).commit();
//                break;
//            case 3:
//                //getFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentComments()).commit();
//                break;
//        }
//        mDrawerList.setItemChecked(position, true);
//        mDrawerLayout.closeDrawer(mDrawerList);
//    }
//
//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        mDrawerToggle.syncState();
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            if (visible)
//                showDrawer();
//            return true;
//        }
//        if (item.getItemId() == R.id.action_favorite)
//            showDrawer();
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void showDrawer()
//    {
//        if(!visible)
//        {
//            mDrawerLayout.closeDrawer(mDrawerList);
//            slider.setVisibility(View.VISIBLE);
//            slider.startAnimation(animLeft);
//            visible = true;
//        }
//        else
//        {
//            slider.startAnimation(animRight);
//            slider.setVisibility(View.GONE);
//            visible = false;
//        }
//    }
//
//
////    private void addItems() {
////        //adapter.add(new OneComment(true, "Hello bubbles!"));
////        try {
////            DataBaseAdapter db = new DataBaseAdapter(this);
////            Singleton singleton = Singleton.getInstance();
////            ArrayList<Comentario> lista = (ArrayList<Comentario>) db.listComments(singleton.activity.getIdAtivity());
////            if (lista.size() != 0) {
////                for (int i = 0; i < lista.size(); i++) {
////                    commentAdapter.add(new OneComment(lista.get(i).getIdAuthor() != singleton.user.getIdUser(),
////                            lista.get(i).getTxtComment() + "\n" + lista.get(i).getDateComment()));
////                }
////                Log.d("Banco", "Lista populada:" + lista);
////            } else {
////                ArrayAdapter ad = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.select_dialog_item,
////                        new String[]{"Não há mensagens"});
////                //mRightDrawerList.setAdapter(ad);
////                //mRightDrawerList.setBackgroundColor(Color.BLACK);
////                Log.d("Banco", "Lista retornou vazia!");
////            }
////        } catch (Exception e) {
////
////        }
////
////    }
//
////    @Override
////    public boolean onTouchEvent(MotionEvent event) {
////        switch (event.getAction()) {
////            case MotionEvent.ACTION_DOWN: {
////                startX = event.getX();
////                break;
////            }
////            case MotionEvent.ACTION_UP: {
////                float endX = event.getX();
////
////                if (endX < startX) {
////                    System.out.println("Move left");
////                    slider.setVisibility(View.VISIBLE);
////                    slider.startAnimation(animLeft);
////                }
////                else {
////                    slider.startAnimation(animRight);
////                    slider.setVisibility(View.GONE);
////                }
////            }
////        }
////        return true;
////    }
//}
//
////<?xml version="1.0" encoding="utf-8"?>
////<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
////        android:orientation="vertical"
////        android:layout_width="match_parent"
////        android:layout_height="match_parent">
////
////<android.support.v4.widget.DrawerLayout xmlns:android="http://schemas.android.com/apk/res/android"
////        android:id="@+id/drawer_layout"
////        android:layout_width="match_parent"
////        android:layout_height="match_parent">
////<!-- The main content view -->
////<FrameLayout
////android:id="@+id/content_frame"
////        android:layout_width="match_parent"
////        android:layout_height="match_parent"></FrameLayout>
////
////<!-- The navigation drawer -->
////<ListView
////android:id="@+id/left_drawer"
////        android:layout_width="240dp"
////        android:layout_height="match_parent"
////        android:layout_gravity="start"
////        android:choiceMode="singleChoice"
////        android:divider="@android:color/transparent"
////        android:dividerHeight="0dp"
////        android:background="#111" />
////
////<!--<ListView-->
////<!--android:id="@+id/right_drawer"-->
////<!--android:layout_width="240dp"-->
////<!--android:layout_height="match_parent"-->
////<!--android:layout_gravity="end"-->
////<!--android:choiceMode="singleChoice"-->
////<!--android:divider="@android:color/transparent"-->
////<!--android:dividerHeight="0dp"-->
////<!--android:background="#FFFFFF"-->
////<!--android:headerDividersEnabled="false" />-->
////</android.support.v4.widget.DrawerLayout>
////
////<!--<LinearLayout-->
////<!--android:id="@+id/slider"-->
////<!--android:layout_width="200dp"-->
////<!--android:layout_height="match_parent"-->
////<!--android:layout_alignParentRight="true"-->
////<!--android:background="@android:color/darker_gray"-->
////<!--android:content="@+id/content"-->
////<!--android:gravity="bottom|center_horizontal"-->
////<!--android:orientation="vertical" >-->
////
////<!--<FrameLayout-->
////<!--android:id="@+id/content_right"-->
////<!--android:layout_width="match_parent"-->
////<!--android:layout_height="match_parent"></FrameLayout>-->
////<!--</LinearLayout>-->
////
////<android.support.v4.app.FragmentTabHost xmlns:android="http://schemas.android.com/apk/res/android"
////        android:id="@+id/slider"
////        android:layout_width="200dp"
////        android:layout_height="match_parent"
////        android:layout_alignParentRight="true"
////        android:background="@android:color/darker_gray"
////        android:gravity="bottom|center_horizontal"
////        android:orientation="vertical" >
////
////<LinearLayout
////android:orientation="vertical"
////        android:layout_width="match_parent"
////        android:layout_height="match_parent">
////
////<TabWidget
////android:id="@android:id/tabs"
////        android:orientation="horizontal"
////        android:layout_width="match_parent"
////        android:layout_height="wrap_content"
////        android:layout_weight="0" />
////
////<FrameLayout
////android:id="@android:id/tabcontent"
////        android:layout_width="0dp"
////        android:layout_height="0dp"
////        android:layout_weight="0" />
////
////<FrameLayout
////android:id="@+id/realtabcontent"
////        android:layout_width="match_parent"
////        android:layout_height="0dp"
////        android:layout_weight="1" />
////
////</LinearLayout>
////</android.support.v4.app.FragmentTabHost>
////
////</RelativeLayout>
