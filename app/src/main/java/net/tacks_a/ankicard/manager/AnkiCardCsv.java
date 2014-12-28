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

import net.tacks_a.ankicard.entity.AnkiCard;
import net.tacks_a.ankicard.entity.AnkiFolder;
import net.tacks_a.ankicard.model.AnkiCardModel;
import net.tacks_a.ankicard.model.AnkiFolderModel;
import net.tacks_a.ankicard.util.LogUtil;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

@SuppressWarnings("WeakerAccess")
@EBean
public class AnkiCardCsv {
    @Bean
    protected AnkiFolderModel mAnkiFolderModel;
    @Bean
    protected AnkiCardModel mAnkiCardModel;

    public List<AnkiCard> readAnkiCardCsv(Reader reader) {
        LogUtil.logDebug();
        List<AnkiCard> list = new ArrayList<>();
        try {
            CSVReader csvReader = new CSVReader(reader);
            String[] row;
            AnkiCard ankiCard;
            while ((row = csvReader.readNext()) != null) {
                ankiCard = new AnkiCard();
                ankiCard.setInfoAnkiFolderName(row[CsvCol.FOLDER_NAME]);
                ankiCard.setWord1(row[CsvCol.WORD1]);
                ankiCard.setWord2(row[CsvCol.WORD2]);
                list.add(ankiCard);
            }
            csvReader.close();
            return list;
        } catch (Exception e) {
            LogUtil.logError("READ CSV exception", e);
            return null;
        }
    }

    public void importAnkiCardCsv(Reader reader) {
        LogUtil.logDebug();
        try {
            AnkiFolder ankiFolder;
            AnkiCard ankiCard;
            List<AnkiCard> list = readAnkiCardCsv(reader);
            if (list != null) {
                for (AnkiCard ankiCardName : list) {
                    ankiFolder = mAnkiFolderModel.findByName(ankiCardName.getInfoAnkiFolderName());
                    if (ankiFolder == null) {
                        ankiFolder = new AnkiFolder();
                        ankiFolder.setName(ankiCardName.getInfoAnkiFolderName());
                        ankiFolder.setUpdateDate(new Date());
                        mAnkiFolderModel.save(ankiFolder);
                    }
                    ankiCard = new AnkiCard();
                    ankiCard.setAnkiFolderId(ankiFolder.getId());
                    ankiCard.setWord1(ankiCardName.getWord1());
                    ankiCard.setWord2(ankiCardName.getWord2());
                    ankiCard.setUpdateDate(new Date());
                    mAnkiCardModel.save(ankiCard);
                }
            }

        } catch (Exception e) {
            LogUtil.logError("READ CSV exception", e);
        }
    }

    private class CsvCol {
        public static final int FOLDER_NAME = 0;
        public static final int WORD1 = 1;
        public static final int WORD2 = 2;
    }

}
