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
import net.tacks_a.ankicard.entity.PointAllocation;
import net.tacks_a.ankicard.helper.DatabaseHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.Date;

import static org.fest.assertions.api.Assertions.assertThat;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class PointAllocationModelTest {

    private PointAllocationModel target;

    @Before
    public void setup() {
        ShadowLog.stream = System.out;

//        target = new PointAllocationModel(
//                activity.getResources().getStringArray(R.array.ary_point_allocation));
    }

    @After
    public void tearDown() {
        DatabaseHelper.releaseInstance();
    }

    @Test
    public void TestCalcPointAllocation1() {
        String[] param = {
                "1,correct,point_allocation_id=1,3,1",
                "2,incorrect,point_allocation_id=2,2,2"};
        target = new PointAllocationModel();
        target.setPointAllocationList(param, true);

        AnkiCard ankiCard = new AnkiCard();
        ankiCard.setWord1("test");
        ankiCard.setWord2("test");
        ankiCard.setOrderNo(0);
        ankiCard.setUpdateDate(new Date());
        int pointAllocationId = 1;

        PointAllocation pointAllocation = target.calcPointAllocation(ankiCard, pointAllocationId);


        assertThat(pointAllocation.getPoint()).isEqualTo(3);
    }

    @Test
    public void TestCalcPointAllocation2() {
        String[] param = {
                "1,correct,point_allocation_id=1,3,1",
                "2,incorrect,point_allocation_id=2,2,2"};
        target = new PointAllocationModel();
        target.setPointAllocationList(param, true);

        AnkiCard ankiCard = new AnkiCard();
        ankiCard.setWord1("test");
        ankiCard.setWord2("test");
        ankiCard.setOrderNo(0);
        ankiCard.setUpdateDate(new Date());
        int pointAllocationId = 2;

        PointAllocation pointAllocation = target.calcPointAllocation(ankiCard, pointAllocationId);


        assertThat(pointAllocation.getPoint()).isEqualTo(2);
    }
}