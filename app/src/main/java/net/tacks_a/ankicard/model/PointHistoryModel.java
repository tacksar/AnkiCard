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
import com.j256.ormlite.stmt.Where;

import net.tacks_a.ankicard.entity.PointHistory;
import net.tacks_a.ankicard.entity.PointHistoryWithInfoMapper;
import net.tacks_a.ankicard.helper.AnkiCardUtil;
import net.tacks_a.ankicard.helper.DatabaseHelper;
import net.tacks_a.ankicard.helper.LogUtil;

import org.androidannotations.annotations.EBean;

import java.util.Date;
import java.util.List;

@EBean
public class PointHistoryModel {
    public PointHistoryModel() {
        LogUtil.logDebug();
    }

    public void save(PointHistory pointHistory) {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<PointHistory, Integer> dao = helper.getPointHistoryDao();
            List<PointHistory> list;
            PointHistory savePointHistory;
            Where<PointHistory, Integer> where = dao.queryBuilder().where()
                    .eq(PointHistory.ANKI_FOLDER_ID_COLUMN, pointHistory.getAnkiFolderId())
                    .and().eq(PointHistory.POINT_DATE_COLUMN, pointHistory.getPointDate())
                    .and().eq(PointHistory.POINT_ALLOCATION_ID_COLUMN, pointHistory.getPointAllocationId());
            LogUtil.logDebug("statement:" + where.getStatement());
            list = where.query();
            LogUtil.logDebug("size:" + list.size());
            if (list.size() == 0) {
                savePointHistory = pointHistory;
            } else {
                savePointHistory = list.get(0);
                savePointHistory.setTotalCount(savePointHistory.getTotalCount() + pointHistory.getTotalCount());
                savePointHistory.setTotalPoint(savePointHistory.getTotalPoint() + pointHistory.getTotalPoint());
            }
            savePointHistory.setUpdateDate(new Date());
            dao.createOrUpdate(savePointHistory);
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
        }
    }

    public int delete(PointHistory pointHistory) {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<PointHistory, Integer> dao = helper.getPointHistoryDao();
            return dao.delete(pointHistory);
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
        }
        return 0;
    }

    public List<PointHistory> findByPointSummaryType(int pointSummaryType) {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<PointHistory, Integer> dao = helper.getPointHistoryDao();
            String sql;
            switch (pointSummaryType) {
                case PointHistory.POINT_SUMMARY_TYPE_FOLDER:
                    sql = new StringBuilder()
                            .append(" SELECT")
                            .append("   A._id")
                            .append(" , A.anki_folder_id")
                            .append(" , A.point_date")
                            .append(" , A.point_allocation_id")
                            .append(" , A.total_point")
                            .append(" , A.update_date")
                            .append(" , ifnull(B.name, '') AS anki_folder_name")
                            .append(" , ifnull(C.name, '') AS point_allocation_name")
                            .append(" , ").append(pointSummaryType).append(" AS point_summary_type")
                            .append(" FROM (")
                            .append("   SELECT")
                            .append("     MIN(_id) AS _id")
                            .append("   , anki_folder_id")
                            .append("   , MIN(point_date) AS point_date")
                            .append("   , MIN(point_allocation_id) AS point_allocation_id")
                            .append("   , SUM(total_point) AS total_point")
                            .append("   , MIN(update_date) AS update_date")
                            .append("   FROM point_history")
                            .append("   GROUP BY")
                            .append("      anki_folder_id")
                            .append(" ) AS A")
                            .append(" LEFT JOIN anki_folder AS B")
                            .append(" ON A.anki_folder_id = B._id")
                            .append(" LEFT JOIN point_allocation AS C")
                            .append(" ON A.point_allocation_id = C._id")
                            .append(" ORDER BY")
                            .append("   B.order_no")
                            .toString();
                    break;
                case PointHistory.POINT_SUMMARY_TYPE_ALLOCATION:
                    sql = new StringBuilder()
                            .append(" SELECT")
                            .append("   A._id")
                            .append(" , A.anki_folder_id")
                            .append(" , A.point_date")
                            .append(" , A.point_allocation_id")
                            .append(" , A.total_point")
                            .append(" , A.update_date")
                            .append(" , ifnull(B.name, '') AS anki_folder_name")
                            .append(" , ifnull(C.name, '') AS point_allocation_name")
                            .append(" , ").append(pointSummaryType).append(" AS point_summary_type")
                            .append(" FROM (")
                            .append("   SELECT")
                            .append("     MIN(_id) AS _id")
                            .append("   , MIN(anki_folder_id) AS anki_folder_id")
                            .append("   , MIN(point_date) AS point_date")
                            .append("   , point_allocation_id")
                            .append("   , SUM(total_point) AS total_point")
                            .append("   , MIN(update_date) AS update_date")
                            .append("   FROM point_history")
                            .append("   GROUP BY")
                            .append("      point_allocation_id")
                            .append(" ) AS A")
                            .append(" LEFT JOIN anki_folder AS B")
                            .append(" ON A.anki_folder_id = B._id")
                            .append(" LEFT JOIN point_allocation AS C")
                            .append(" ON A.point_allocation_id = C._id")
                            .append(" ORDER BY")
                            .append("   A.point_allocation_id")
                            .toString();
                    break;
                case PointHistory.POINT_SUMMARY_TYPE_DATE_FOLDER:
                    sql = new StringBuilder()
                            .append(" SELECT")
                            .append("   A._id")
                            .append(" , A.anki_folder_id")
                            .append(" , A.point_date")
                            .append(" , A.point_allocation_id")
                            .append(" , A.total_point")
                            .append(" , A.update_date")
                            .append(" , ifnull(B.name, '') AS anki_folder_name")
                            .append(" , ifnull(C.name, '') AS point_allocation_name")
                            .append(" , ").append(pointSummaryType).append(" AS point_summary_type")
                            .append(" FROM (")
                            .append("   SELECT")
                            .append("     MIN(_id) AS _id")
                            .append("   , anki_folder_id")
                            .append("   , point_date")
                            .append("   , MIN(point_allocation_id) AS point_allocation_id")
                            .append("   , SUM(total_point) AS total_point")
                            .append("   , MIN(update_date) AS update_date")
                            .append("   FROM point_history")
                            .append("   GROUP BY")
                            .append("     anki_folder_id")
                            .append("   , point_date")
                            .append(" ) AS A")
                            .append(" LEFT JOIN anki_folder AS B")
                            .append(" ON A.anki_folder_id = B._id")
                            .append(" LEFT JOIN point_allocation AS C")
                            .append(" ON A.point_allocation_id = C._id")
                            .append(" ORDER BY")
                            .append("   B.order_no")
                            .append(" , A.anki_folder_id DESC")
                            .append(" , A.point_date DESC")
                            .toString();
                    break;
                case PointHistory.POINT_SUMMARY_TYPE_DATE_FOLDER_ALLOCATION:
                    sql = new StringBuilder()
                            .append(" SELECT")
                            .append("   A._id")
                            .append(" , A.anki_folder_id")
                            .append(" , A.point_date")
                            .append(" , A.point_allocation_id")
                            .append(" , A.total_point")
                            .append(" , A.update_date")
                            .append(" , ifnull(B.name, '') AS anki_folder_name")
                            .append(" , ifnull(C.name, '') AS point_allocation_name")
                            .append(" , ").append(pointSummaryType).append(" AS point_summary_type")
                            .append(" FROM (")
                            .append("   SELECT")
                            .append("     MIN(_id) AS _id")
                            .append("   , anki_folder_id")
                            .append("   , point_date")
                            .append("   , point_allocation_id")
                            .append("   , SUM(total_point) AS total_point")
                            .append("   , MIN(update_date) AS update_date")
                            .append("   FROM point_history")
                            .append("   GROUP BY")
                            .append("     anki_folder_id")
                            .append("   , point_date")
                            .append("   , point_allocation_id")
                            .append(" ) AS A")
                            .append(" LEFT JOIN anki_folder AS B")
                            .append(" ON A.anki_folder_id = B._id")
                            .append(" LEFT JOIN point_allocation AS C")
                            .append(" ON A.point_allocation_id = C._id")
                            .append(" ORDER BY")
                            .append("   B.order_no")
                            .append(" , A.anki_folder_id DESC")
                            .append(" , A.point_date DESC")
                            .append(" , point_allocation_id")
                            .toString();
                    break;
                default:
                    sql = new StringBuilder()
                            .append(" SELECT")
                            .append("   A._id")
                            .append(" , A.anki_folder_id")
                            .append(" , A.point_date")
                            .append(" , A.point_allocation_id")
                            .append(" , A.total_point")
                            .append(" , A.update_date")
                            .append(" , ifnull(B.name, '') AS anki_folder_name")
                            .append(" , ifnull(C.name, '') AS point_allocation_name")
                            .append(" , ").append(pointSummaryType).append(" AS point_summary_type")
                            .append(" FROM (")
                            .append("   SELECT")
                            .append("     MIN(_id) AS _id")
                            .append("   , MIN(anki_folder_id) AS anki_folder_id")
                            .append("   , point_date")
                            .append("   , MIN(point_allocation_id) AS point_allocation_id")
                            .append("   , SUM(total_point) AS total_point")
                            .append("   , MIN(update_date) AS update_date")
                            .append("   FROM point_history")
                            .append("   GROUP BY")
                            .append("      point_date")
                            .append(" ) AS A")
                            .append(" LEFT JOIN anki_folder AS B")
                            .append(" ON A.anki_folder_id = B._id")
                            .append(" LEFT JOIN point_allocation AS C")
                            .append(" ON A.point_allocation_id = C._id")
                            .append(" ORDER BY")
                            .append("   A.point_date DESC")
                            .toString();
                    break;
            }

            LogUtil.logDebug(sql);
            PointHistoryWithInfoMapper map = new PointHistoryWithInfoMapper();
            GenericRawResults<PointHistory> rawResults = dao.queryRaw(sql, map);

            return rawResults.getResults();
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
            return null;
        }
    }

    public List<PointHistory> findAll() {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<PointHistory, Integer> dao = helper.getPointHistoryDao();
            return dao.queryForAll();
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
            return null;
        }
    }

    public int getTodayPoint() {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        int todayPoint = 0;
        try {
            Dao<PointHistory, Integer> dao = helper.getPointHistoryDao();
            QueryBuilder<PointHistory, Integer> q = dao.queryBuilder();
            q.where().eq(PointHistory.POINT_DATE_COLUMN, AnkiCardUtil.getToday());
            List<PointHistory> list = dao.query(q.prepare());
            for (PointHistory pointHistory : list) {
                todayPoint += pointHistory.getTotalPoint();
            }
            return todayPoint;
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
            return todayPoint;
        }
    }

    public int getTotalPoint() {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        int totalPoint = 0;
        try {
            Dao<PointHistory, Integer> dao = helper.getPointHistoryDao();
            QueryBuilder<PointHistory, Integer> q = dao.queryBuilder();
            q.selectRaw(String.format("SUM(%s)", PointHistory.TOTAL_POINT_COLUMN));
            GenericRawResults results = dao.queryRaw(q.prepareStatementString());
            String[] values = (String[]) results.getFirstResult();
            if (values[0] != null) {
                totalPoint = Integer.parseInt(values[0]);
            }
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
        }
        return totalPoint;
    }

    public int deleteByAnkiFolderId(int ankiFolderId) {
        LogUtil.logDebug();
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            Dao<PointHistory, Integer> dao = helper.getPointHistoryDao();
            DeleteBuilder<PointHistory, Integer> q = dao.deleteBuilder();
            q.where().eq(PointHistory.ANKI_FOLDER_ID_COLUMN, ankiFolderId);
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
            Dao<PointHistory, Integer> dao = helper.getPointHistoryDao();
            return dao.delete(findAll());
        } catch (Exception e) {
            LogUtil.logError("Database exception", e);
        }
        return 0;
    }
}
