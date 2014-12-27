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

import net.tacks_a.ankicard.entity.AnkiCard;
import net.tacks_a.ankicard.entity.PointAllocation;
import net.tacks_a.ankicard.entity.PointHistory;
import net.tacks_a.ankicard.helper.DatabaseHelper;
import net.tacks_a.ankicard.helper.LogUtil;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

@EBean
public class PointAllocationModel {
    private List<PointAllocation> mPointAllocationList;

    public void setPointAllocationList(String[] aryPointAllocation, boolean isUpdate) {
        LogUtil.logDebug();
        if (!isUpdate && findAll().size() != 0) {
            mPointAllocationList = findAll();
            return;
        }

        mPointAllocationList = new ArrayList<>();
        String[] items;
        PointAllocation pointAllocation;
        for (String line : aryPointAllocation) {
            items = line.split(",");
            pointAllocation = new PointAllocation();
            pointAllocation.setId(Integer.parseInt(items[0].trim()));
            pointAllocation.setName(items[1].trim());
            pointAllocation.setFormula(items[2].trim());
            pointAllocation.setPoint(Integer.parseInt(items[3].trim()));
            pointAllocation.setOrderNo(Integer.parseInt(items[4].trim()));

            mPointAllocationList.add(pointAllocation);
        }
        saveAll(mPointAllocationList);
    }

    public PointAllocation calcPointAllocation(AnkiCard ankiCard, int pointAllocationId) {
        LogUtil.logDebug();
        PointAllocation pointAllocation = new PointAllocation();
        pointAllocation.setId(pointAllocationId);

        // SQLを使用して判定をする
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<AnkiCard, Integer> dao = helper.getAnkiCardDao();
            GenericRawResults<String[]> rawResults;
            List<String[]> results;
            String formula;
            for (PointAllocation bp : mPointAllocationList) {
                formula = bp.getFormula();
                formula = formula.replace(PointHistory.POINT_ALLOCATION_ID_COLUMN, Integer.toString(pointAllocationId));
                formula = formula.replace(AnkiCard.CORRECT_COUNT_COLUMN, Integer.toString(ankiCard.getCorrectCount()));
                formula = formula.replace(AnkiCard.INCORRECT_COUNT_COLUMN, Integer.toString(ankiCard.getIncorrectCount()));
                formula = formula.replace(AnkiCard.LAST_POINT_ALLOCATION_ID_COLUMN, Integer.toString(ankiCard.getLastPointAllocation()));

                LogUtil.logDebug("formula[" + formula + "]=>" + Integer.toString(bp.getPoint()));

                rawResults = dao.queryRaw("select (" + formula + ") as result");
                results = rawResults.getResults();

                LogUtil.logDebug("result[" + results.get(0)[0] + "]");

                if (!results.isEmpty() &&
                        Integer.parseInt(results.get(0)[0]) != 0) {
                    pointAllocation.setPoint(bp.getPoint());
                    break;
                }
            }
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
        }

        return pointAllocation;
    }

    public void save(PointAllocation pointAllocation) {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<PointAllocation, Integer> dao = helper
                    .getPointAllocationDao();

            dao.createOrUpdate(pointAllocation);
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
        }
    }

    public void saveAll(List<PointAllocation> pointAllocationList) {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<PointAllocation, Integer> dao = helper
                    .getPointAllocationDao();
            for (PointAllocation pointAllocation : pointAllocationList) {
                dao.createOrUpdate(pointAllocation);
            }
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
        }
    }

    public List<PointAllocation> findAll() {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<PointAllocation, Integer> dao = helper.getPointAllocationDao();
            return dao.queryForAll();
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
            return null;
        }
    }
}
