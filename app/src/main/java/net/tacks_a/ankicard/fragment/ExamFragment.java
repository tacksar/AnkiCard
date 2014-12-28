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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.tacks_a.ankicard.R;
import net.tacks_a.ankicard.entity.AnkiCard;
import net.tacks_a.ankicard.entity.ExamCond;
import net.tacks_a.ankicard.manager.ExamManager;
import net.tacks_a.ankicard.util.AnkiCardUtil;
import net.tacks_a.ankicard.util.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

/**
 * 試験Fragment
 *
 * @author tacks_a
 */
@SuppressWarnings("WeakerAccess")
@EFragment(R.layout.fragment_exam)
public class ExamFragment extends Fragment {

    @ViewById(R.id.txvQuestion)
    protected TextView mTxvQuestion = null;
    @ViewById(R.id.txvExamPoint)
    protected TextView mTxvExamPoint = null;
    @ViewById(R.id.txvAnkiCardInfo)
    protected TextView mTxvAnkiCardInfo = null;
    @ViewById(R.id.txvWord1)
    protected TextView mTxvWord1 = null;
    @ViewById(R.id.txvWord2)
    protected TextView mTxvWord2 = null;
    @ViewById(R.id.btnAnswer)
    protected Button mBtnAnswer = null;
    @ViewById(R.id.btnCorrect)
    protected Button mBtnCorrect = null;
    @ViewById(R.id.btnIncorrect)
    protected Button mBtnIncorrect = null;
    @Bean
    protected ExamManager mExamManager;
    @FragmentArg
    protected ExamCond mExamCond = new ExamCond();

    @AfterViews
    protected void initViews() {
        LogUtil.logDebug();

        // メニュー名を設定
        getActivity().setTitle(R.string.menu_exam);

        mExamManager.Init(mExamCond);

        // 初期表示
        showNextCard();

    }

    // 解答ボタン
    @Click(R.id.btnAnswer)
    protected void btnAnswerClick() {
        LogUtil.logDebug();
        mBtnAnswer.setVisibility(View.INVISIBLE);
        mBtnCorrect.setVisibility(View.VISIBLE);
        mBtnIncorrect.setVisibility(View.VISIBLE);
        mTxvWord2.setVisibility(View.VISIBLE);
    }

    // 正解ボタン
    @Click(R.id.btnCorrect)
    protected void btnCorrectClick() {
        LogUtil.logDebug();
        mExamManager.setResult(getActivity().getResources().getInteger(
                R.integer.point_allocation_id_correct));
        showNextCard();
    }

    // 不正解ボタン
    @Click(R.id.btnIncorrect)
    protected void btnIncorrectClick() {
        LogUtil.logDebug();
        mExamManager.setResult(getActivity().getResources().getInteger(
                R.integer.point_allocation_id_incorrect));
        showNextCard();
    }

    // 次のカードを表示
    private boolean showNextCard() {
        LogUtil.logDebug();
        AnkiCard nextAnkiCard = mExamManager.getNextCard();
        mTxvQuestion.setText(mExamManager.getExamInfo());
        mTxvExamPoint.setText(mExamManager.getExamPoint());

        if (nextAnkiCard == null) {
            // 問題終了
            mBtnAnswer.setVisibility(View.INVISIBLE);
            mBtnCorrect.setVisibility(View.INVISIBLE);
            mBtnIncorrect.setVisibility(View.INVISIBLE);
            showExamResultDialogFragment();
            return false;
        } else {
            // 問題表示
            mBtnAnswer.setVisibility(View.VISIBLE);
            mBtnCorrect.setVisibility(View.INVISIBLE);
            mBtnIncorrect.setVisibility(View.INVISIBLE);
            mTxvWord2.setVisibility(View.INVISIBLE);
            mTxvWord1.setText(nextAnkiCard.getWord1());
            mTxvWord2.setText(nextAnkiCard.getWord2());

            mTxvAnkiCardInfo.setText(getString(R.string.msg_card_info,
                    nextAnkiCard.getCorrectCount(),
                    nextAnkiCard.getIncorrectCount(),
                    AnkiCardUtil.FormatDate(this.getActivity(), nextAnkiCard.getLastExamDate())));
            return true;
        }
    }

    // 結果を表示
    private void showExamResultDialogFragment() {
        ExamResultDialogFragment dialog = ExamResultDialogFragment
                .newInstance(mExamCond);
        dialog.show(getFragmentManager(), "dialog");
    }

    public static class ExamResultDialogFragment extends DialogFragment {
        public static ExamResultDialogFragment newInstance(ExamCond examCond) {
            LogUtil.logDebug();
            ExamResultDialogFragment frag = new ExamResultDialogFragment();
            Bundle args = new Bundle();
            args.putSerializable("examCond", examCond);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LogUtil.logDebug();
            ExamCond examCond = (ExamCond) getArguments().getSerializable(
                    "examCond");
            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.title_result)
                    .setMessage(getString(R.string.msg_exam_result,
                            examCond.getCorrectCount(),
                            examCond.getIncorrectCount(),
                            examCond.getTotalPoint()))
                    .setCancelable(false)
                    .setPositiveButton(R.string.title_ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    getFragmentManager().popBackStack();
                                }
                            }).create();
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            getFragmentManager().popBackStack();
        }
    }

}
