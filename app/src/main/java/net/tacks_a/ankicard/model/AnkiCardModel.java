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

package net.tacks_a.ankicard.model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import net.tacks_a.ankicard.entity.AnkiCard;
import net.tacks_a.ankicard.entity.ExamCond;
import net.tacks_a.ankicard.manager.DatabaseHelper;
import net.tacks_a.ankicard.util.LogUtil;

import org.androidannotations.annotations.EBean;

import java.util.List;

@EBean
public class AnkiCardModel {
    public AnkiCardModel() {
        LogUtil.logDebug();
    }

    public void save(AnkiCard ankiCard) {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<AnkiCard, Integer> dao = helper.getAnkiCardDao();
            dao.createOrUpdate(ankiCard);
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
        }
    }

    public int delete(AnkiCard ankiCard) {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<AnkiCard, Integer> dao = helper.getAnkiCardDao();
            return dao.delete(ankiCard);
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
        }
        return 0;
    }

    public List<AnkiCard> findAll() {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<AnkiCard, Integer> dao = helper.getAnkiCardDao();
            return dao.queryForAll();
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
            return null;
        }
    }

    public List<AnkiCard> findByAnkiFolderId(int ankiFolderId) {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<AnkiCard, Integer> dao = helper.getAnkiCardDao();
            return dao.queryForEq(AnkiCard.ANKI_FOLDER_ID_COLUMN, ankiFolderId);
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
            return null;
        }
    }

    public List<AnkiCard> findByQuestionCondition(ExamCond examCond) {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<AnkiCard, Integer> dao = helper.getAnkiCardDao();
            QueryBuilder<AnkiCard, Integer> q = dao.queryBuilder();
            q.where().in(AnkiCard.ANKI_FOLDER_ID_COLUMN, examCond.getAnkiFolderIdList());
            if (examCond.getQuestionCount() > 0) {
                q.limit((long) examCond.getQuestionCount());
            }
            //ary_sort_type
            switch (examCond.getSortType()) {
                case 0:    //正解数の少ない順
                    q.orderBy(AnkiCard.CORRECT_COUNT_COLUMN, true);
                    break;
                case 1:    //不正解の多い順
                    q.orderBy(AnkiCard.INCORRECT_COUNT_COLUMN, false);
                    break;
                case 2:    //ランダム
                    q.orderByRaw("RANDOM()");
                    break;
                case 3:    //カードの登録順
                    q.orderBy(AnkiCard.ORDER_NO_COLUMN, true);
                    break;
                case 4:    //最終出題日の古い順
                    q.orderBy(AnkiCard.LAST_EXAM_DATE_COLUMN, true);
                    break;
            }
            return dao.query(q.prepare());
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
            return null;
        }
    }

    public int getCount() {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        int count = 0;
        try {
            Dao<AnkiCard, Integer> dao = helper.getAnkiCardDao();
            QueryBuilder<AnkiCard, Integer> q = dao.queryBuilder();
            q.selectRaw("COUNT(*)");
            GenericRawResults results = dao.queryRaw(q.prepareStatementString());
            String[] values = (String[]) results.getFirstResult();
            count = Integer.parseInt(values[0]);

            return count;
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
            return count;
        }
    }

    public int deleteByAnkiFolderId(int ankiFolderId) {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<AnkiCard, Integer> dao = helper.getAnkiCardDao();
            DeleteBuilder<AnkiCard, Integer> q = dao.deleteBuilder();
            q.where().eq(AnkiCard.ANKI_FOLDER_ID_COLUMN, ankiFolderId);
            return dao.delete(q.prepare());
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
        }
        return 0;
    }

    public int deleteAll() {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<AnkiCard, Integer> dao = helper.getAnkiCardDao();
            return dao.delete(findAll());
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
        }
        return 0;
    }

}
