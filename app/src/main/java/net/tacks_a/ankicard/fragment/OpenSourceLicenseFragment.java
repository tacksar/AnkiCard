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

package net.tacks_a.ankicard.fragment;


import android.support.v4.app.Fragment;
import android.webkit.WebView;

import net.tacks_a.ankicard.R;
import net.tacks_a.ankicard.util.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_open_source_license)
public class OpenSourceLicenseFragment extends Fragment {
    @ViewById(R.id.webOpenSourceLicence)
    protected WebView mWebOpenSourceLicence;

    @AfterViews
    protected void initViews() {
        LogUtil.logDebug();

        // メニュー名を設定
        getActivity().setTitle(R.string.menu_open_source_license);

        mWebOpenSourceLicence.setHorizontalScrollBarEnabled(false);
        mWebOpenSourceLicence.loadUrl("file:///android_asset/open_source_license.html");
    }
}
