package com.ufcspa.unasus.appportfolio.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.ufcspa.unasus.appportfolio.R;

/**
 * Created by Desenvolvimento on 01/12/2015.
 */
public class EditActivityWDrawer extends Activity {
    private FragmentManager fragmentManager = getFragmentManager();
    FragmentEditText fragmentEditText;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setHomeButtonEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teste_drawer_layout);
        Log.d("cycle", "on create");
        //if(savedInstanceState==null){
            fragmentEditText=new FragmentEditText();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(R.id.content_frame,fragmentEditText);
            ft.commit();
        //}
        mTitle = mDrawerTitle = getTitle();
        String[] itens={"Editar Texto","Anexos","Referências","Comentários"};
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,itens);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);


        // Set the adapter for the list view
        mDrawerList.setAdapter(adapter);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

//        ListView listView = (ListView)findViewById(R.id.edit_activity_listview);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch (position) {
//                    case 0:
//                        fragmentEditText = (FragmentEditText) fragmentManager.findFragmentById(R.id.fragment);
//                        break;
//                    case 1:
//
//                }
//            }
//        });
        // enable ActionBar app icon to behave as action to toggle nav drawer


        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
//        mDrawerToggle = new ActionBarDrawerToggle(
//                this,                  /* host Activity */
//                mDrawerLayout,         /* DrawerLayout object */
//                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
//                R.string.drawer_open,  /* "open drawer" description for accessibility */
//                R.string.drawer_close  /* "close drawer" description for accessibility */
//        )
//
//        {
//            public void onDrawerClosed(View view) {
//                getActionBar().setTitle(mTitle);
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//            }
//
//            public void onDrawerOpened(View drawerView) {
//                getActionBar().setTitle(mDrawerTitle);
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//            }
//        };
//        mDrawerLayout.setDrawerListener(mDrawerToggle);
//
//        if (savedInstanceState == null) {
//            selectItem(0);
//        }

        Log.d("cycle", "saiu do on create");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("cycle", "on resume");
//
//        fragmentEditText=new FragmentEditText();
//        FragmentTransaction ft = fragmentManager.beginTransaction();
//        ft.add(R.id.activity_edit_acitivity_left_layout,fragmentEditText);
//        ft.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("cycle","on stop");
    }





    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    private void selectItem(int position) {
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
//        mDrawerList.setItemChecked(position, true);
//        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

}
