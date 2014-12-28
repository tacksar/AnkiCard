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

import net.tacks_a.ankicard.entity.AnkiFolder;
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
public class AnkiFolderModelTest {

    private AnkiFolderModel target;

    @Before
    public void setup() {
        target = new AnkiFolderModel();
    }

    @After
    public void tearDown() {
        DatabaseHelper.releaseInstance();
    }

    @Test
    public void TestSave() {

        AnkiFolder ankiFolder = new AnkiFolder();
        ankiFolder.setName("test");
        ankiFolder.setOrderNo(0);
        ankiFolder.setUpdateDate(new Date());
        target.save(ankiFolder);

        int count = target.findAll().size();
        assertThat(count).isEqualTo(1);
    }

    @Test
    public void TestSaveAndDelete() {

        target.deleteAll();
        AnkiFolder ankiFolder = new AnkiFolder();
        ankiFolder.setName("test");
        ankiFolder.setOrderNo(0);
        ankiFolder.setUpdateDate(new Date());
        target.save(ankiFolder);

        ankiFolder = new AnkiFolder();
        ankiFolder.setName("test");
        ankiFolder.setOrderNo(0);
        ankiFolder.setUpdateDate(new Date());
        target.save(ankiFolder);

        int count = target.findAll().size();
        assertThat(count).isEqualTo(2);

        target.deleteAll();
        count = target.findAll().size();
        assertThat(count).isEqualTo(0);
    }

}
