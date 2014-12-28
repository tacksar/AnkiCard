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

import net.tacks_a.ankicard.entity.AnkiFolder;
import net.tacks_a.ankicard.entity.AnkiFolderWithInfoMapper;
import net.tacks_a.ankicard.manager.DatabaseHelper;
import net.tacks_a.ankicard.util.LogUtil;

import org.androidannotations.annotations.EBean;

import java.util.List;

@EBean
public class AnkiFolderModel {
    public AnkiFolderModel() {
        LogUtil.logDebug();
    }

    public void save(AnkiFolder ankiFolder) {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<AnkiFolder, Integer> dao = helper.getAnkiFolderDao();
            dao.createOrUpdate(ankiFolder);
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
        }
    }

    public int delete(AnkiFolder ankiFolder) {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<AnkiFolder, Integer> dao = helper.getAnkiFolderDao();
            return dao.delete(ankiFolder);
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
        }
        return 0;
    }

    public AnkiFolder findByName(String name) {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<AnkiFolder, Integer> dao = helper.getAnkiFolderDao();
            List<AnkiFolder> list = dao.queryForEq(AnkiFolder.NAME_COLUMN, name);
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

    public List<AnkiFolder> findAll() {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<AnkiFolder, Integer> dao = helper.getAnkiFolderDao();
            return dao.queryForAll();
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
            return null;
        }
    }

    public List<AnkiFolder> findAllWithInfo() {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<AnkiFolder, Integer> dao = helper.getAnkiFolderDao();
            String sql = new StringBuilder()
                    .append(" SELECT")
                    .append("   A._id")
                    .append(" , A.name")
                    .append(" , A.order_no")
                    .append(" , A.update_date")
                    .append(" , ifnull(B.count, 0) AS info_anki_card_count")
                    .append(" , ifnull(B.correct_count, 0) AS info_total_correct_count")
                    .append(" , ifnull(B.incorrect_count, 0) AS info_total_incorrect_count")
                    .append(" FROM anki_folder AS A")
                    .append(" LEFT JOIN (")
                    .append("   SELECT")
                    .append("     anki_folder_id")
                    .append("   , COUNT(*) AS count")
                    .append("   , SUM(correct_count) AS correct_count")
                    .append("   , SUM(incorrect_count) AS incorrect_count")
                    .append("   FROM anki_card")
                    .append("   GROUP BY")
                    .append("      anki_folder_id")
                    .append(" ) AS B")
                    .append(" ON A._id = B.anki_folder_id")
                    .append(" ORDER BY")
                    .append("   A.order_no")
                    .append(" , A._id")
                    .toString();

            LogUtil.logDebug(sql);
            AnkiFolderWithInfoMapper map = new AnkiFolderWithInfoMapper();
            GenericRawResults<AnkiFolder> rawResults = dao.queryRaw(sql, map);

            return rawResults.getResults();
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
            return null;
        }
    }

    public int deleteAll() {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<AnkiFolder, Integer> dao = helper.getAnkiFolderDao();
            return dao.delete(findAll());
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
        }
        return 0;
    }

}
