/*
 * Copyright 2014 tacks_a
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.tacks_a.ankicard.activity;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.tacks_a.ankicard.R;
import net.tacks_a.ankicard.entity.ExamCond;
import net.tacks_a.ankicard.fragment.AnkiFolderListFragment_;
import net.tacks_a.ankicard.fragment.ExamCondFragment_;
import net.tacks_a.ankicard.fragment.HomeFragment_;
import net.tacks_a.ankicard.fragment.OpenSourceLicenseFragment_;
import net.tacks_a.ankicard.fragment.PointAllocationListFragment_;
import net.tacks_a.ankicard.fragment.PointHistoryListFragment_;
import net.tacks_a.ankicard.util.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@SuppressWarnings("WeakerAccess")
@SuppressLint("Registered")
@EActivity(R.layout.activity_main)
public class MainActivity extends ActionBarActivity {

    @ViewById(R.id.drawer_layout)
    protected DrawerLayout mDrawerLayout;
    @ViewById(R.id.left_drawer)
    protected ListView mDrawerList;

    protected ActionBarDrawerToggle mDrawerToggle;

    protected CharSequence mTitle;
    protected String[] mDrawerMenu;

    private ArrayAdapter<String> mAdapter;

    @AfterViews
    protected void initViews() {
        LogUtil.logDebug();

        if (getSupportFragmentManager().findFragmentById(R.id.container) == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new HomeFragment_(), HomeFragment_.class.getSimpleName()).commit();
        }

        mDrawerMenu = getResources().getStringArray(R.array.ary_drawer_title);

        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mDrawerMenu);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.title_navigation_drawer_open, R.string.title_navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mTitle);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    private void selectItem(int position) {
        LogUtil.logDebug();
        LogUtil.logDebug("position=" + String.valueOf(position));
        Fragment selectFragment = null;
        switch (position) {
            case 0:
                selectFragment = new HomeFragment_();
                break;
            case 1:
                selectFragment = ExamCondFragment_.builder()
                        .mExamCond(new ExamCond())
                        .build();
                break;
            case 2:
                selectFragment = new AnkiFolderListFragment_();
                break;
            case 3:
                selectFragment = new PointHistoryListFragment_();
                break;
            case 4:
                selectFragment = new PointAllocationListFragment_();
                break;
            case 5:
                selectFragment = new OpenSourceLicenseFragment_();
                break;
        }

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, selectFragment, selectFragment.getClass().getSimpleName())
                .commit();
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LogUtil.logDebug();
        int id = item.getItemId();

        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }

}
