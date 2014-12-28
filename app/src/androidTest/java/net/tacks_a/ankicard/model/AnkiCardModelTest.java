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

import net.tacks_a.ankicard.entity.AnkiCard;
import net.tacks_a.ankicard.manager.DatabaseHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

import static org.fest.assertions.api.Assertions.assertThat;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class AnkiCardModelTest {

    private AnkiCardModel target;

    @Before
    public void setup() {
        target = new AnkiCardModel();
    }

    @After
    public void tearDown() {
        DatabaseHelper.releaseInstance();
    }

    @Test
    public void TestSave1() {

        AnkiCard AnkiCard = new AnkiCard();
        AnkiCard.setWord1("test");
        AnkiCard.setWord2("test");
        AnkiCard.setOrderNo(0);
        AnkiCard.setUpdateDate(new Date());
        target.save(AnkiCard);

        int count = target.findAll().size();
        assertThat(count).isEqualTo(1);
    }

    @Test
    public void TestSaveAndDelete() {

        AnkiCard AnkiCard = new AnkiCard();
        AnkiCard.setWord1("test");
        AnkiCard.setWord2("test");
        AnkiCard.setOrderNo(0);
        AnkiCard.setUpdateDate(new Date());
        target.save(AnkiCard);

        AnkiCard = new AnkiCard();
        AnkiCard.setWord1("test");
        AnkiCard.setWord2("test");
        AnkiCard.setOrderNo(0);
        AnkiCard.setUpdateDate(new Date());
        target.save(AnkiCard);

        int count = target.findAll().size();
        assertThat(count).isEqualTo(2);

        target.deleteAll();
        count = target.findAll().size();
        assertThat(count).isEqualTo(0);
    }

}
