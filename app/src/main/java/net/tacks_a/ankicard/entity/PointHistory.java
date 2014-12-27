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

package net.tacks_a.ankicard.entity;

import android.content.Context;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import net.tacks_a.ankicard.helper.AnkiCardUtil;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author tacks_a
 */
@Data
@DatabaseTable(tableName = PointHistory.TABLE_NAME)
public class PointHistory implements Serializable {
    public static final String
            TABLE_NAME = "point_history",
            ID_COLUMN = "_id",
            ANKI_FOLDER_ID_COLUMN = "anki_folder_id",
            POINT_DATE_COLUMN = "point_date",
            POINT_ALLOCATION_ID_COLUMN = "point_allocation_id",
            TOTAL_COUNT_COLUMN = "total_count",
            TOTAL_POINT_COLUMN = "total_point",
            UPDATE_DATE_COLUMN = "update_date";
    public static final int
            POINT_SUMMARY_TYPE_DATE = 0,
            POINT_SUMMARY_TYPE_FOLDER = 1,
            POINT_SUMMARY_TYPE_ALLOCATION = 2,
            POINT_SUMMARY_TYPE_DATE_FOLDER = 3,
            POINT_SUMMARY_TYPE_DATE_FOLDER_ALLOCATION = 4;
    private static final long serialVersionUID = -8009682311183092448L;
    @DatabaseField(generatedId = true, columnName = ID_COLUMN)
    private int id;
    @DatabaseField(columnName = ANKI_FOLDER_ID_COLUMN)
    private int ankiFolderId;
    @DatabaseField(columnName = POINT_DATE_COLUMN)
    private Date pointDate;
    @DatabaseField(columnName = POINT_ALLOCATION_ID_COLUMN)
    private int pointAllocationId;
    @DatabaseField(columnName = TOTAL_COUNT_COLUMN)
    private int totalCount;
    @DatabaseField(columnName = TOTAL_POINT_COLUMN)
    private int totalPoint;
    @DatabaseField(columnName = UPDATE_DATE_COLUMN)
    private Date updateDate;
    //info
    private String infoAnkiFolderName;
    private String infoPointAllocationName;
    private int infoPointSummaryType;

    public String getGroupHeaderName(Context context) {
        String name;
        switch (infoPointSummaryType) {
            case POINT_SUMMARY_TYPE_FOLDER:
                name = infoAnkiFolderName;
                break;
            case POINT_SUMMARY_TYPE_ALLOCATION:
                name = infoPointAllocationName;
                break;
            case POINT_SUMMARY_TYPE_DATE_FOLDER:
                name = AnkiCardUtil.FormatDate(context, pointDate) + ">" + infoAnkiFolderName;
                break;
            case POINT_SUMMARY_TYPE_DATE_FOLDER_ALLOCATION:
                name = AnkiCardUtil.FormatDate(context, pointDate) + ">" + infoAnkiFolderName + ">" + infoPointAllocationName;
                break;
            case POINT_SUMMARY_TYPE_DATE:
            default:
                name = AnkiCardUtil.FormatDate(context, pointDate);
                break;
        }
        return name;
    }
}
