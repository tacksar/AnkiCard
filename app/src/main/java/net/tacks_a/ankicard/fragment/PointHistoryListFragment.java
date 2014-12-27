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

/**
 *
 */
package net.tacks_a.ankicard.fragment;

import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import net.tacks_a.ankicard.R;
import net.tacks_a.ankicard.adapter.PointHistoryListAdapter;
import net.tacks_a.ankicard.entity.PointHistory;
import net.tacks_a.ankicard.helper.LogUtil;
import net.tacks_a.ankicard.model.PointHistoryModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * @author tacks_a
 */
@EFragment(R.layout.fragment_point_history_list)
public class PointHistoryListFragment extends Fragment {

    @ViewById(R.id.spnPointSummaryType)
    protected Spinner mSpnPointSummaryType;
    @ViewById(R.id.lstPointHistory)
    protected ListView mLstPointHistory;

    @Bean
    protected PointHistoryModel mPointHistoryModel;
    @Bean
    protected PointHistoryListAdapter mPointListAdapter;
    protected List<PointHistory> mPointHistoryList;

    @AfterViews
    protected void initViews() {
        LogUtil.logDebug();

        // メニュー名を設定
        getActivity().setTitle(R.string.menu_point_list);

        // リストを表示
        setPointSummaryTypeToAdapter();
        setDataToAdapter();
    }

    @ItemSelect(R.id.spnPointSummaryType)
    protected void spnPointSummaryTypeClicked(boolean selected, int position) {
        LogUtil.logDebug();
        setDataToAdapter();
    }

    private void setPointSummaryTypeToAdapter() {
        LogUtil.logDebug();

        // Spinnerの設定
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this.getActivity(), R.array.ary_point_summary_type,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnPointSummaryType.setAdapter(adapter);

        if (0 < mSpnPointSummaryType.getCount()) {
            mSpnPointSummaryType.setSelection(0);
        }
    }

    private void setDataToAdapter() {
        LogUtil.logDebug();
        mPointHistoryList = mPointHistoryModel.findByPointSummaryType(mSpnPointSummaryType.getSelectedItemPosition());
        mPointListAdapter.setPointHistoryList(mPointHistoryList);
        mLstPointHistory.setAdapter(mPointListAdapter);
    }
}
