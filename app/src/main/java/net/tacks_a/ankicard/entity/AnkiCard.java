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

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author tacks_a
 */
@SuppressWarnings("WeakerAccess")
@Data
@DatabaseTable(tableName = AnkiCard.TABLE_NAME)
public class AnkiCard implements Serializable {
    public static final String
            TABLE_NAME = "anki_card",
            ID_COLUMN = "_id",
            ANKI_FOLDER_ID_COLUMN = "anki_folder_id",
            WORD1_COLUMN = "word1",
            WORD2_COLUMN = "word2",
            CORRECT_COUNT_COLUMN = "correct_count",
            INCORRECT_COUNT_COLUMN = "incorrect_count",
            LAST_POINT_ALLOCATION_ID_COLUMN = "last_point_allocation_id",
            LAST_EXAM_DATE_COLUMN = "last_exam_date",
            ORDER_NO_COLUMN = "order_no",
            UPDATE_DATE_COLUMN = "update_date";
    private static final long serialVersionUID = 3263579375965147452L;
    @DatabaseField(generatedId = true, columnName = ID_COLUMN)
    private int id;
    @DatabaseField(columnName = ANKI_FOLDER_ID_COLUMN)
    private int ankiFolderId;
    @DatabaseField(columnName = WORD1_COLUMN)
    private String word1;
    @DatabaseField(columnName = WORD2_COLUMN)
    private String word2;
    @DatabaseField(columnName = CORRECT_COUNT_COLUMN)
    private int correctCount;
    @DatabaseField(columnName = INCORRECT_COUNT_COLUMN)
    private int incorrectCount;
    @DatabaseField(columnName = LAST_POINT_ALLOCATION_ID_COLUMN)
    private int lastPointAllocation;
    @DatabaseField(columnName = LAST_EXAM_DATE_COLUMN)
    private Date lastExamDate;
    @DatabaseField(columnName = ORDER_NO_COLUMN)
    private int orderNo;
    @DatabaseField(columnName = UPDATE_DATE_COLUMN)
    private Date updateDate;

    // info
    private String infoAnkiFolderName;
    private String infoLastDistributionName;
}
