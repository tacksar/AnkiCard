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

package net.tacks_a.ankicard;

import android.app.Application;

import net.tacks_a.ankicard.helper.DatabaseHelper;
import net.tacks_a.ankicard.helper.LogUtil;

@SuppressWarnings("ALL")
public class AnkiCardApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 設定ファイルからログレベルを切り替え
        int showLogLevel = getResources().getInteger(R.integer.show_log_level);
        LogUtil.setShowLogLevel(showLogLevel);
        LogUtil.logDebug();
        // DB初期化
        DatabaseHelper.init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LogUtil.logDebug();
        // DB開放
        DatabaseHelper.releaseInstance();
    }
}
