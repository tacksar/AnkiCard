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
@DatabaseTable(tableName = AnkiFolder.TABLE_NAME)
public class AnkiFolder implements Serializable {
    public static final String
            TABLE_NAME = "anki_folder",
            ID_COLUMN = "_id",
            NAME_COLUMN = "name",
            ORDER_NO_COLUMN = "order_no",
            UPDATE_DATE_COLUMN = "update_date";
    private static final long serialVersionUID = -8960384688343124828L;
    @DatabaseField(generatedId = true, columnName = ID_COLUMN)
    private int id;
    @DatabaseField(columnName = NAME_COLUMN)
    private String name;
    @DatabaseField(columnName = ORDER_NO_COLUMN)
    private int orderNo;
    @DatabaseField(columnName = UPDATE_DATE_COLUMN)
    private Date updateDate;

    // info
    private int infoAnkiCardCount = 0;
    private int infoTotalCorrectCount = 0;
    private int infoTotalIncorrectCount = 0;
}
