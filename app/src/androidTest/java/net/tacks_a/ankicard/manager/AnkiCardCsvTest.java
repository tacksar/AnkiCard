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

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.StringReader;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class AnkiCardCsvTest {

    @Before
    public void setup() {
//        activity = new MainActivity_();
    }

    @After
    public void teardown() {
    }

    @Test
    public void testCsv() throws Exception {
        AnkiCardCsv target = new AnkiCardCsv();
        String csv = "folder1,word1,word2\n" +
                "folder2,word3,word4";

        Assert.assertEquals(2, target.readAnkiCardCsv(new StringReader(csv)).size());
    }
}