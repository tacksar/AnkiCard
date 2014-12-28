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

/**
 *
 */
package net.tacks_a.ankicard.entity;

import android.text.TextUtils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import net.tacks_a.ankicard.util.LogUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import au.com.bytecode.opencsv.CSVParser;
import lombok.Data;

/**
 * 出題条件
 *
 * @author tacks_a
 */
@SuppressWarnings("WeakerAccess")
@Data
@DatabaseTable(tableName = ExamCond.TABLE_NAME)
public class ExamCond implements Serializable {
    public static final String TABLE_NAME = "exam_cond",
            ID_COLUMN = "_id",
            ANKI_FOLDER_IDS_COLUMN = "anki_folder_ids",
            QUESTION_COUNT_COLUMN = "question_count",
            SORT_TYPE_COLUMN = "sort_type",
            EXAM_TYPE_COLUMN = "exam_type",
            CORRECT_COUNT_COLUMN = "correct_count",
            INCORRECT_COUNT_COLUMN = "incorrect_count",
            TOTAL_POINT = "total_point",
            UPDATE_DATE_COLUMN = "update_date";
    private static final long serialVersionUID = -9155778601755375977L;
    @DatabaseField(generatedId = true, columnName = ID_COLUMN)
    private int id;
    @DatabaseField(columnName = ANKI_FOLDER_IDS_COLUMN)
    private String ankiFolderIds;
    @DatabaseField(columnName = QUESTION_COUNT_COLUMN)
    private int questionCount;
    @DatabaseField(columnName = SORT_TYPE_COLUMN)
    private int sortType;
    @DatabaseField(columnName = EXAM_TYPE_COLUMN)
    private int examType;
    @DatabaseField(columnName = CORRECT_COUNT_COLUMN)
    private int correctCount;
    @DatabaseField(columnName = INCORRECT_COUNT_COLUMN)
    private int incorrectCount;
    @DatabaseField(columnName = TOTAL_POINT)
    private int totalPoint;
    @DatabaseField(columnName = UPDATE_DATE_COLUMN)
    private Date updateDate;

    public List<Integer> getAnkiFolderIdList() {
        LogUtil.logDebug();
        List<Integer> list = new ArrayList<>();

        String csv = "";
        if (ankiFolderIds != null) {
            csv = ankiFolderIds;
        }
        CSVParser csvParser = new CSVParser();
        String[] row = null;

        try {
            row = csvParser.parseLine(csv);
        } catch (IOException e) {
            LogUtil.logError("parse error", e);
        }
        if (row != null) {
            for (String num : row) {
                try {
                    if (!TextUtils.isEmpty(num)) {
                        list.add(Integer.parseInt(num));
                    }
                } catch (Exception e) {
                    LogUtil.logError("num parse error", e);
                }
            }
        }
        return list;
    }

    public int getAnswerCount() {
        return this.getCorrectCount() + this.getIncorrectCount();
    }

    //値未設定を上書きする
    public void overwriteNullExamCond(ExamCond cond) {
        if (this.ankiFolderIds == null) this.ankiFolderIds = cond.getAnkiFolderIds();
        if (this.questionCount == 0) this.questionCount = cond.getQuestionCount();
        if (this.sortType == 0) this.sortType = cond.getSortType();
        if (this.examType == 0) this.examType = cond.getExamType();
    }

}
