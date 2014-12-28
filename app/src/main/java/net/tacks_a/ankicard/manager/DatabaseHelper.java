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
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import net.tacks_a.ankicard.entity.AnkiCard;
import net.tacks_a.ankicard.entity.AnkiFolder;
import net.tacks_a.ankicard.entity.ExamCond;
import net.tacks_a.ankicard.entity.PointAllocation;
import net.tacks_a.ankicard.entity.PointHistory;
import net.tacks_a.ankicard.util.LogUtil;

import java.sql.SQLException;

/**
 * データベースヘルパー
 *
 * @author tacks_a
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "ankicard.db";

    private static final int APP_DB_VERSION_1_0_0 = 22;
    private static final int DATABASE_VERSION = APP_DB_VERSION_1_0_0;

    private static DatabaseHelper instance;

    private Dao<AnkiFolder, Integer> ankiFolderDao;
    private Dao<AnkiCard, Integer> ankiCardDao;
    private Dao<PointHistory, Integer> pointHistoryDao;
    private Dao<ExamCond, Integer> examCondDao;
    private Dao<PointAllocation, Integer> pointAllocationDao;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        LogUtil.logDebug();
    }

    public static void init(Context context) {
        LogUtil.logDebug();
        releaseInstance();
        instance = new DatabaseHelper(context);
    }

    public static DatabaseHelper getInstance() {
        LogUtil.logDebug();
        return instance;
    }

    public static void releaseInstance() {
        LogUtil.logDebug();
        if (instance != null) {
            OpenHelperManager.releaseHelper();
        }
        instance = null;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        LogUtil.logDebug();
        try {
            TableUtils.createTable(connectionSource, AnkiFolder.class);
            TableUtils.createTable(connectionSource, AnkiCard.class);
            TableUtils.createTable(connectionSource, PointHistory.class);
            TableUtils.createTable(connectionSource, ExamCond.class);
            TableUtils.createTable(connectionSource, PointAllocation.class);
        } catch (SQLException e) {
            LogUtil.logError("Could not create new table", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        LogUtil.logDebug();
        try {
            if (newVersion <= APP_DB_VERSION_1_0_0) {
                TableUtils.dropTable(connectionSource, AnkiFolder.class, true);
                TableUtils.dropTable(connectionSource, AnkiCard.class, true);
                TableUtils.dropTable(connectionSource, PointHistory.class, true);
                TableUtils.dropTable(connectionSource, ExamCond.class, true);
                TableUtils.dropTable(connectionSource, PointAllocation.class, true);
            }
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            LogUtil.logError("Could not upgrade the table", e);
        }
    }

    public Dao<AnkiFolder, Integer> getAnkiFolderDao() throws SQLException {
        LogUtil.logDebug();
        if (ankiFolderDao == null) {
            ankiFolderDao = getDao(AnkiFolder.class);
        }
        return ankiFolderDao;
    }

    public Dao<AnkiCard, Integer> getAnkiCardDao() throws SQLException {
        LogUtil.logDebug();
        if (ankiCardDao == null) {
            ankiCardDao = getDao(AnkiCard.class);
        }
        return ankiCardDao;
    }

    public Dao<PointHistory, Integer> getPointHistoryDao() throws SQLException {
        LogUtil.logDebug();
        if (pointHistoryDao == null) {
            pointHistoryDao = getDao(PointHistory.class);
        }
        return pointHistoryDao;
    }

    public Dao<ExamCond, Integer> getExamCondDao() throws SQLException {
        LogUtil.logDebug();
        if (examCondDao == null) {
            examCondDao = getDao(ExamCond.class);
        }
        return examCondDao;
    }

    public Dao<PointAllocation, Integer> getPointAllocationDao() throws SQLException {
        LogUtil.logDebug();
        if (pointAllocationDao == null) {
            pointAllocationDao = getDao(PointAllocation.class);
        }
        return pointAllocationDao;
    }
}
