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

import net.tacks_a.ankicard.entity.ExamCond;
import net.tacks_a.ankicard.manager.DatabaseHelper;
import net.tacks_a.ankicard.util.LogUtil;

import org.androidannotations.annotations.EBean;

import java.util.List;

@SuppressWarnings("WeakerAccess")
@EBean
public class ExamCondModel {
    public ExamCondModel() {
        LogUtil.logDebug();
    }

    public void save(ExamCond questionCondition) {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<ExamCond, Integer> dao = helper
                    .getExamCondDao();
            dao.createOrUpdate(questionCondition);
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
        }
    }

    public int delete(ExamCond questionCondition) {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<ExamCond, Integer> dao = helper
                    .getExamCondDao();
            return dao.delete(questionCondition);
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
        }
        return 0;
    }

    public List<ExamCond> findAll() {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<ExamCond, Integer> dao = helper
                    .getExamCondDao();
            return dao.queryForAll();
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
            return null;
        }
    }

    public int deleteAll() {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<ExamCond, Integer> dao = helper
                    .getExamCondDao();
            return dao.delete(findAll());
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
        }
        return 0;
    }

    public ExamCond findLatest() {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        List<ExamCond> list;
        try {
            Dao<ExamCond, Integer> dao = helper
                    .getExamCondDao();
            list = dao.query(dao.queryBuilder()
                    .orderBy(ExamCond.ID_COLUMN, false).prepare());
            if (list.size() == 0) {
                return null;
            } else {
                return list.get(0);
            }
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
            return null;
        }
    }
}
