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

package net.tacks_a.ankicard.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import net.tacks_a.ankicard.R;
import net.tacks_a.ankicard.helper.AnkiCardCsv;
import net.tacks_a.ankicard.helper.LogUtil;
import net.tacks_a.ankicard.model.AnkiCardModel;
import net.tacks_a.ankicard.model.AnkiFolderModel;
import net.tacks_a.ankicard.model.PointHistoryModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.InputStream;
import java.io.InputStreamReader;

@SuppressWarnings("WeakerAccess")
@EFragment(R.layout.fragment_home)
public class HomeFragment extends Fragment {

    @ViewById(R.id.txvAppInfo)
    protected TextView mTxvAppInfo;
    @ViewById(R.id.txvTodayPoint)
    protected TextView mTxvTodayPoint;
    @ViewById(R.id.txvTotalPoint)
    protected TextView mTxvTotalPoint;
    @ViewById(R.id.txvAnkiCardCount)
    protected TextView mTxvAnkiCardCount;

    @Bean
    protected AnkiFolderModel mAnkiFolderModel;
    @Bean
    protected AnkiCardModel mAnkiCardModel;
    @Bean
    protected PointHistoryModel mPointHistoryModel;
    @Bean
    protected AnkiCardCsv mAnkiCardCsv;

    @AfterViews
    protected void initViews() {
        LogUtil.logDebug();

        // メニュー名を設定
        getActivity().setTitle(R.string.menu_home);

        // 初期データ
        createInitData();

        // 画面表示
        PackageInfo packageInfo = null;
        try {
            packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.logError("packageInfo not found", e);
        }
        if (packageInfo != null) {
            mTxvAppInfo.setText(getString(R.string.msg_app_info,
                    getString(R.string.app_name),
                    packageInfo.versionName));
        }
        mTxvTodayPoint.setText(Integer.toString(mPointHistoryModel.getTodayPoint()));
        mTxvTotalPoint.setText(Integer.toString(mPointHistoryModel.getTotalPoint()));
        mTxvAnkiCardCount.setText(Integer.toString(mAnkiCardModel.getCount()));
    }

    @Background
    protected void createInitData() {
        LogUtil.logDebug();
        try {

            if (mAnkiFolderModel.findAll().size() == 0) {
                mAnkiFolderModel.deleteAll();
                mAnkiCardModel.deleteAll();

                AssetManager assets = getResources().getAssets();
                InputStream in = assets.open("init_data.csv");
                InputStreamReader f = new InputStreamReader(in);
                mAnkiCardCsv.importAnkiCardCsv(f);
            }
        } catch (Exception e) {
            LogUtil.logError("create init data exception", e);
        }
    }
}
