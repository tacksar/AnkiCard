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

import com.j256.ormlite.dao.RawRowMapper;

import net.tacks_a.ankicard.helper.AnkiCardUtil;

import java.sql.SQLException;

public class PointHistoryWithInfoMapper
        implements RawRowMapper<PointHistory> {
    @Override
    public PointHistory mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
        PointHistory result = new PointHistory();
        result.setId(Integer.parseInt(resultColumns[0]));
        result.setAnkiFolderId(Integer.parseInt(resultColumns[1]));
        result.setPointDate(AnkiCardUtil.toDate(resultColumns[2]));
        result.setPointAllocationId(Integer.parseInt(resultColumns[3]));
        result.setTotalPoint(Integer.parseInt(resultColumns[4]));
        result.setUpdateDate(AnkiCardUtil.toDate(resultColumns[5]));

        result.setInfoAnkiFolderName(resultColumns[6]);
        result.setInfoPointAllocationName(resultColumns[7]);
        result.setInfoPointSummaryType(Integer.parseInt(resultColumns[8]));
        return result;
    }
}