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

package net.tacks_a.ankicard.util;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.inputmethod.InputMethodManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
 * 暗記カードプロジェクトのユーティリティクラス
 */
@SuppressWarnings("WeakerAccess")
public class AnkiCardUtil {

    public static Date getToday() {
        return truncateDate(new Date());
    }

    public static Date truncateDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // ソフトウェアキーボードを非表示
    public static void HideIme(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    // 日付フォーマット
    public static String FormatDate(Context context, Date date) {
        LogUtil.logDebug();
        if (date == null) {
            return "";
        } else {
            return DateUtils.formatDateTime(context, date.getTime(),
                    DateUtils.FORMAT_SHOW_YEAR |
                            DateUtils.FORMAT_SHOW_DATE);
        }
    }

    // 日付変換
    public static Date toDate(String value) {
        LogUtil.logDebug(value);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSSSSS");
        Date date = new Date();

        try {
            date = sdf.parse(value);
        } catch (ParseException e) {
            LogUtil.logError("Date ParseException:" + value, e);
        }
        return date;
    }

}
