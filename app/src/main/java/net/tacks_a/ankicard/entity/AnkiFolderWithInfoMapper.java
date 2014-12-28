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

import net.tacks_a.ankicard.util.AnkiCardUtil;

import java.sql.SQLException;

public class AnkiFolderWithInfoMapper
        implements RawRowMapper<AnkiFolder> {
    @Override
    public AnkiFolder mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
        AnkiFolder result = new AnkiFolder();
        result.setId(Integer.parseInt(resultColumns[0]));
        result.setName(resultColumns[1]);
        result.setOrderNo(Integer.parseInt(resultColumns[2]));
        result.setUpdateDate(AnkiCardUtil.toDate(resultColumns[3]));
        result.setInfoAnkiCardCount(Integer.parseInt(resultColumns[4]));
        result.setInfoTotalCorrectCount(Integer.parseInt(resultColumns[5]));
        result.setInfoTotalIncorrectCount(Integer.parseInt(resultColumns[6]));

        return result;
    }
}