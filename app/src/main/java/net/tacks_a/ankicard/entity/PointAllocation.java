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
 * master.xmlのデータ
 *
 * @author tacks_a
 */
@Data
@DatabaseTable(tableName = PointAllocation.TABLE_NAME)
public class PointAllocation implements Serializable {
    public static final String
            TABLE_NAME = "point_allocation",
            ID_COLUMN = "_id",
            NAME_COLUMN = "name",
            FORMULA_COLUMN = "formula",
            POINT_COLUMN = "point",
            ORDER_NO_COLUMN = "order_no",
            UPDATE_DATE_COLUMN = "update_date";
    private static final long serialVersionUID = -3259675166077469144L;
    @DatabaseField(generatedId = true, columnName = ID_COLUMN)
    private int id;
    @DatabaseField(columnName = NAME_COLUMN)
    private String name;
    @DatabaseField(columnName = FORMULA_COLUMN)
    private String formula;
    @DatabaseField(columnName = POINT_COLUMN)
    private int point;
    @DatabaseField(columnName = ORDER_NO_COLUMN)
    private int orderNo;
    @DatabaseField(columnName = UPDATE_DATE_COLUMN)
    private Date updateDate;

}
