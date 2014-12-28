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

package net.tacks_a.ankicard.manager;

import android.content.Context;

import net.tacks_a.ankicard.R;
import net.tacks_a.ankicard.entity.AnkiCard;
import net.tacks_a.ankicard.entity.ExamCond;
import net.tacks_a.ankicard.entity.PointAllocation;
import net.tacks_a.ankicard.entity.PointHistory;
import net.tacks_a.ankicard.model.AnkiCardModel;
import net.tacks_a.ankicard.model.PointAllocationModel;
import net.tacks_a.ankicard.model.PointHistoryModel;
import net.tacks_a.ankicard.util.AnkiCardUtil;
import net.tacks_a.ankicard.util.LogUtil;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.res.StringArrayRes;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


/**
 * 試験管理クラス
 *
 * @author tacks_a
 */
@SuppressWarnings("WeakerAccess")
@EBean
public class ExamManager {
    @RootContext
    protected Context mContext;
    @StringArrayRes(R.array.ary_point_allocation)
    protected String[] mAryPointAllocation;
    @Bean
    protected AnkiCardModel mAnkiCardModel;
    @Bean
    protected PointHistoryModel mPointHistoryModel;
    @Bean
    protected PointAllocationModel mPointAllocationModel;
    protected ExamCond mExamCond = null;
    protected List<AnkiCard> mAnkiCardList = null;
    protected LinkedList<AnkiCard> mBeforeExam = null;
    protected LinkedList<AnkiCard> mAfterExam = null;

    protected AnkiCard mCurrentAnkiCard = null;

    // 初期化
    public void Init(ExamCond examCond) {
        LogUtil.logDebug();
        mExamCond = examCond;
        mExamCond.setCorrectCount(0);
        mExamCond.setIncorrectCount(0);
        mExamCond.setTotalPoint(0);

        mPointAllocationModel.setPointAllocationList(mAryPointAllocation, false);

        //カード一覧を取得
        mAnkiCardList = mAnkiCardModel.findByQuestionCondition(mExamCond);
        LogUtil.logDebug(String.format("mAnkiCardList.size:%d", mAnkiCardList.size()));

        mBeforeExam = new LinkedList<>();
        mAfterExam = new LinkedList<>();
        for (int i = 0; i < mAnkiCardList.size(); i++) {
            mBeforeExam.offer(mAnkiCardList.get(i));
        }
        LogUtil.logDebug(String.format("mBeforeExam.size:%d", mBeforeExam.size()));
        LogUtil.logDebug(String.format("mAfterExam.size:%d", mAfterExam.size()));

    }


    // カードを取得
    public AnkiCard getNextCard() {
        LogUtil.logDebug();

        mCurrentAnkiCard = mBeforeExam.poll();

        return mCurrentAnkiCard;
    }

    // 結果を設定
    public void setResult(int pointAllocationId) {
        LogUtil.logDebug();
        if (mCurrentAnkiCard == null) {
            return;
        }

        //結果を設定
        boolean isCorrect;

        if (pointAllocationId == mContext.getResources().getInteger(R.integer.point_allocation_id_correct)) {
            isCorrect = true;
        } else {
            isCorrect = false;
        }

        if (isCorrect) {
            mAfterExam.offer(mCurrentAnkiCard);
            mCurrentAnkiCard
                    .setCorrectCount(mCurrentAnkiCard.getCorrectCount() + 1);
            mExamCond.setCorrectCount(mExamCond.getCorrectCount() + 1);
        } else {
            //mBeforeExam.offer(mCurrentAnkiCard);
            mAfterExam.offer(mCurrentAnkiCard);
            mCurrentAnkiCard.setIncorrectCount(mCurrentAnkiCard
                    .getIncorrectCount() + 1);
            mExamCond.setIncorrectCount(mExamCond.getIncorrectCount() + 1);

        }

        //ポイントを設定
        PointHistory pointHistory = new PointHistory();

        pointHistory.setAnkiFolderId(mCurrentAnkiCard.getAnkiFolderId());
        pointHistory.setPointDate(AnkiCardUtil.getToday());

        PointAllocation pointAllocation = mPointAllocationModel.calcPointAllocation(mCurrentAnkiCard, pointAllocationId);
        pointHistory.setPointAllocationId(pointAllocation.getId());
        pointHistory.setTotalCount(1);
        pointHistory.setTotalPoint(pointAllocation.getPoint());

        mPointHistoryModel.save(pointHistory);

        mExamCond.setTotalPoint(mExamCond.getTotalPoint() + pointAllocation.getPoint());

        //暗記カード
        mCurrentAnkiCard.setLastPointAllocation(pointAllocationId);
        mCurrentAnkiCard.setLastExamDate(new Date());
        mAnkiCardModel.save(mCurrentAnkiCard);
        mCurrentAnkiCard = null;
    }


    //試験情報を取得
    public String getExamInfo() {
        int count = mBeforeExam.size() + mAfterExam.size() + (mCurrentAnkiCard == null ? 0 : 1);
        int answer = Math.min(mExamCond.getAnswerCount() + 1, count);

        LogUtil.logDebug(String.format(Locale.JAPAN, "mBeforeExam.size:%d", mBeforeExam.size()));
        LogUtil.logDebug(String.format(Locale.JAPAN, "mAfterExam.size:%d", mAfterExam.size()));

        return String.format(Locale.JAPAN, "%d/%d", answer, count);
    }

    //ポイント情報を取得
    public String getExamPoint() {
        int point = mExamCond.getTotalPoint();

        return Integer.toString(point);
    }
}
