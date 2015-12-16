package com.ufcspa.unasus.appportfolio.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ufcspa.unasus.appportfolio.Adapter.CommentArrayAdapter;
import com.ufcspa.unasus.appportfolio.Model.Comentario;
import com.ufcspa.unasus.appportfolio.Model.OneComment;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.ArrayList;


/**
 * Created by Convidado on 20/11/2015.
 */
public class EditActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
//    private ListView mRightDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private CommentArrayAdapter commentAdapter;
    private ActionBarDrawerToggle mDrawerToggle;

    private LinearLayout ll;
    float startX;
    private Animation animLeft;
    private Animation animRight;
    private boolean visible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_activity);
        Log.d("cycle", "on create");

        mTitle = mDrawerTitle = getTitle();
        commentAdapter = new CommentArrayAdapter(getApplicationContext(), R.layout.comment_item);
        String[] itens = {"Editar Texto", "Anexos", "Referências", "Comentários"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, itens);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
//        mRightDrawerList = (ListView) findViewById(R.id.right_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(adapter);
//        mRightDrawerList.setAdapter(commentAdapter);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
//        mRightDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                selectItem(3);
//            }
//        });

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        )


        {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                Log.d("Toolbar", "close");
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                //mDrawerLayout.openDrawer(mDrawerList);
                Log.d("Toolbar", "open");
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_drawer);


        if (savedInstanceState == null) {
            selectItem(0);
        }

        getFragmentManager().beginTransaction().replace(R.id.content_right, new FragmentComments()).commit();

        ll = (LinearLayout) findViewById(R.id.slider);
        ll.setVisibility(View.GONE);

        animLeft = AnimationUtils.loadAnimation(this, R.anim.anim_right);
        animRight = AnimationUtils.loadAnimation(this, R.anim.anim_left);

        visible = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Toolbar", "foi");
            }
        });
        addItems();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        switch (position) {
            case 0:
                getFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentEditText()).commit();//FragmentEditText
                break;
            case 1:
                showDrawer();
                //getFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentAttachment()).commit();
                break;
            case 3:
                getFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentComments()).commit();
                break;
        }
        // update the main content by replacing fragments
//        Fragment fragment = new PlanetFragment();
//        Bundle args = new Bundle();
//        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
//        fragment.setArguments(args);
//
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
//
//        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
//        setTitle(itens[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            //actionbar clicked
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void addItems() {
        //adapter.add(new OneComment(true, "Hello bubbles!"));
        try {
            DataBaseAdapter db = new DataBaseAdapter(this);
            Singleton singleton = Singleton.getInstance();
            ArrayList<Comentario> lista = (ArrayList<Comentario>) db.listComments(singleton.activity.getIdAtivity());
            if (lista.size() != 0) {
                for (int i = 0; i < lista.size(); i++) {
                    commentAdapter.add(new OneComment(lista.get(i).getIdAuthor() != singleton.user.getIdUser(),
                            lista.get(i).getTxtComment() + "\n" + lista.get(i).getDateComment()));
                }
                Log.d("Banco", "Lista populada:" + lista);
            } else {
                ArrayAdapter ad = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.select_dialog_item,
                        new String[]{"Não há mensagens"});
                //mRightDrawerList.setAdapter(ad);
                //mRightDrawerList.setBackgroundColor(Color.BLACK);
                Log.d("Banco", "Lista retornou vazia!");
            }
        } catch (Exception e) {

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                startX = event.getX();
                break;
            }
            case MotionEvent.ACTION_UP: {
                float endX = event.getX();

                if (endX < startX) {
                    System.out.println("Move left");
                    ll.setVisibility(View.VISIBLE);
                    ll.startAnimation(animLeft);
                }
                else {
                    ll.startAnimation(animRight);
                    ll.setVisibility(View.GONE);
                }
            }
        }
        return true;
    }

    public void showDrawer()
    {
        if(!visible)
        {
            ll.setVisibility(View.VISIBLE);
            ll.startAnimation(animLeft);
            visible = true;
        }
        else
        {
            ll.startAnimation(animRight);
            ll.setVisibility(View.GONE);
            visible = false;
        }
    }
}
