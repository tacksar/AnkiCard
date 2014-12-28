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

import android.util.Log;

/*
 * ログクラス
 */
public class LogUtil {
    private static int mShowLogLevel = Log.DEBUG;

    public static void setShowLogLevel(int showLogLevel) {
        mShowLogLevel = showLogLevel;
    }

    public static void logError(String message) {
        outputLog(Log.ERROR, message, null);
    }

    public static void logError(String message, Throwable throwable) {
        outputLog(Log.ERROR, message, throwable);
    }

    public static void logWarn(String message) {
        outputLog(Log.WARN, message, null);
    }

    public static void logWarn(String message, Throwable throwable) {
        outputLog(Log.WARN, message, throwable);
    }

    public static void logInfo(String message) {
        outputLog(Log.INFO, message, null);
    }

    public static void logInfo(String message, Throwable throwable) {
        outputLog(Log.INFO, message, throwable);
    }

    public static void logDebug() {
        outputLog(Log.DEBUG, null, null);
    }

    public static void logDebug(String message) {
        outputLog(Log.DEBUG, message, null);
    }

    public static void logDebug(String message, Throwable throwable) {
        outputLog(Log.DEBUG, message, throwable);
    }

    private static void outputLog(int type, String message, Throwable throwable) {
        if (type < mShowLogLevel) {
            return;
        }

        String tag = getTag();
        if (message == null) {
            message = getStackTraceInfo();
        } else {
            message = getStackTraceInfo() + message;
        }

        switch (type) {
            case Log.ERROR:
                if (throwable == null) {
                    Log.e(tag, message);
                } else {
                    Log.e(tag, message, throwable);
                }
                break;
            case Log.WARN:
                if (throwable == null) {
                    Log.w(tag, message);
                } else {
                    Log.w(tag, message, throwable);
                }
                break;
            case Log.INFO:
                if (throwable == null) {
                    Log.i(tag, message);
                } else {
                    Log.i(tag, message, throwable);
                }
                break;
            case Log.DEBUG:
                if (throwable == null) {
                    Log.d(tag, message);
                } else {
                    Log.d(tag, message, throwable);
                }
                break;

        }
    }

    /**
     * タグを取得
     */
    private static String getTag() {
        // 現在のスタックトレースを取得。
        // 0:VM 1:スレッド 2:getStackTraceInfo() 3:outputLog() 4:logDebug()等 5:呼び出し元
        StackTraceElement element = Thread.currentThread().getStackTrace()[5];

        String fullName = element.getClassName();

        return fullName.substring(fullName.lastIndexOf(".") + 1);
    }

    /**
     * スタックトレースから呼び出し元の基本情報を取得
     */
    private static String getStackTraceInfo() {
        // 現在のスタックトレースを取得。
        // 0:VM 1:スレッド 2:getStackTraceInfo() 3:outputLog() 4:logDebug()等 5:呼び出し元
        StackTraceElement element = Thread.currentThread().getStackTrace()[5];

        // String fullName = element.getClassName();
        // String className = fullName.substring(fullName.lastIndexOf(".") + 1);
        String methodName = element.getMethodName();
        int lineNumber = element.getLineNumber();

        return "<<" + methodName + ":" + lineNumber + ">> ";
    }

}
