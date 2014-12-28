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

import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import net.tacks_a.ankicard.R;
import net.tacks_a.ankicard.entity.AnkiFolder;
import net.tacks_a.ankicard.entity.ExamCond;
import net.tacks_a.ankicard.model.AnkiFolderModel;
import net.tacks_a.ankicard.model.ExamCondModel;
import net.tacks_a.ankicard.util.LogUtil;
import net.tacks_a.ankicard.view.adapter.AnkiFolderSpinnerAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 出題条件Fragment
 *
 * @author tacks_a
 */
@SuppressWarnings("WeakerAccess")
@EFragment(R.layout.fragment_exam_cond)
public class ExamCondFragment extends Fragment {

    @ViewById(R.id.btnExam)
    protected Button mBtnExam = null;
    @ViewById(R.id.spnFolder)
    protected Spinner mSpnFolder = null;
    @ViewById(R.id.spnQuestionCount)
    protected Spinner mSpnQuestionCount = null;
    @ViewById(R.id.spnSortType)
    protected Spinner mSpnSortType = null;

    @FragmentArg
    protected ExamCond mExamCond = new ExamCond();
    @Bean
    protected AnkiFolderModel mAnkiFolderModel;
    @Bean
    protected AnkiFolderSpinnerAdapter mAnkiFolderSpinnerAdapter;
    @Bean
    protected ExamCondModel mExamCondModel;
    List<AnkiFolder> mAnkiFolderList;

    @AfterViews
    protected void initViews() {
        LogUtil.logDebug();

        // メニュー名を設定
        getActivity().setTitle(R.string.menu_exam_cond);

        // 初期値設定
        loadQuestionCondition();

        // スピナー
        setAnkiFolderToAdapter();
        setQuestionCountToAdapter();
        setSortToAdapter();
    }

    // 問題ボタン
    @Click(R.id.btnExam)
    protected void btnExamClick() {
        LogUtil.logDebug();
        if (!setExamCondition()) {
            return;
        }

        ExamFragment fragment = ExamFragment_.builder().mExamCond(mExamCond)
                .build();

        getFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, fragment)
                .commit();
    }

    private void loadQuestionCondition() {
        LogUtil.logDebug();

        ExamCond latest = mExamCondModel.findLatest();
        if (latest == null) {
            // ない場合は、初期値設定
            latest = new ExamCond();
            latest.setQuestionCount(10);

        }

        // 保存された値で条件を上書き
        mExamCond.overwriteNullExamCond(latest);

    }

    private boolean setExamCondition() {
        LogUtil.logDebug();

        // フォルダ
        if (mSpnFolder.getSelectedItemPosition() < 0) {
            Toast.makeText(this.getActivity(),
                    R.string.error_empty_anki_folder_list, Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        mExamCond.setAnkiFolderIds(Integer.toString(mAnkiFolderList.get(
                mSpnFolder.getSelectedItemPosition()).getId()));

        // 問題数
        if (mSpnQuestionCount.getSelectedItem().toString().length() == 0) {
            Toast.makeText(this.getActivity(),
                    R.string.error_empty_question_count, Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        mExamCond.setQuestionCount(Integer.parseInt(mSpnQuestionCount
                .getSelectedItem().toString()));

        // 並び順
        if (mSpnSortType.getSelectedItemPosition() < 0) {
            Toast.makeText(this.getActivity(), R.string.error_empty_sort_type,
                    Toast.LENGTH_LONG).show();
            return false;
        }
        mExamCond.setSortType(mSpnSortType.getSelectedItemPosition());

        // 保存
        mExamCondModel.save(mExamCond);

        return true;
    }

    private void setAnkiFolderToAdapter() {
        LogUtil.logDebug();
        mAnkiFolderList = mAnkiFolderModel.findAllWithInfo();
        mAnkiFolderSpinnerAdapter.setAnkiFolderList(mAnkiFolderList);
        mSpnFolder.setAdapter(mAnkiFolderSpinnerAdapter);

        // 初期値選択
        if (mAnkiFolderList.size() > 0) {
            int selectedIndex = 0;
            List<Integer> ankiFolderIdList = mExamCond.getAnkiFolderIdList();
            for (AnkiFolder ankiFolder : mAnkiFolderList) {
                if (ankiFolderIdList.size() > 0
                        && ankiFolderIdList.get(0) == ankiFolder.getId()) {
                    selectedIndex = mAnkiFolderList.indexOf(ankiFolder);
                }
            }
            mSpnFolder.setSelection(selectedIndex);
        }
    }

    private void setQuestionCountToAdapter() {
        LogUtil.logDebug();

        // Spinnerの設定
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this.getActivity(), R.array.ary_question_count,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnQuestionCount.setAdapter(adapter);

        // 初期値選択
        for (int i = 0; i < adapter.getCount(); i++) {
            String str1 = adapter.getItem(i).toString();
            String str2 = Integer.toString(mExamCond.getQuestionCount());
            if (str1.equals(str2)) {
                mSpnQuestionCount.setSelection(i);
            }
        }

    }

    private void setSortToAdapter() {
        LogUtil.logDebug();

        // Spinnerの設定
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this.getActivity(), R.array.ary_sort_type,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnSortType.setAdapter(adapter);

        // 初期値選択
        for (int i = 0; i < adapter.getCount(); i++) {
            if (mExamCond.getSortType() == i) {
                mSpnSortType.setSelection(i);
            }
        }

    }

}
