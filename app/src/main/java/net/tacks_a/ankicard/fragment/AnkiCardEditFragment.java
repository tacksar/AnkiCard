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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.tacks_a.ankicard.R;
import net.tacks_a.ankicard.adapter.AnkiFolderListAdapter;
import net.tacks_a.ankicard.entity.AnkiCard;
import net.tacks_a.ankicard.entity.AnkiFolder;
import net.tacks_a.ankicard.helper.AnkiCardUtil;
import net.tacks_a.ankicard.helper.LogUtil;
import net.tacks_a.ankicard.model.AnkiCardModel;
import net.tacks_a.ankicard.model.AnkiFolderModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.Date;
import java.util.List;

/**
 * 暗記カード編集Fragment
 *
 * @author tacks_a
 */
@SuppressWarnings("WeakerAccess")
@EFragment(R.layout.fragment_anki_card_edit)
public class AnkiCardEditFragment extends Fragment {

    @ViewById(R.id.spnFolder)
    protected Spinner mSpnFolder = null;
    @ViewById(R.id.txvAnkiCardInfo)
    protected TextView mTxvAnkiCardInfo = null;
    @ViewById(R.id.edtWord1)
    protected EditText mEdtWord1 = null;
    @ViewById(R.id.edtWord2)
    protected EditText mEdtWord2 = null;
    @ViewById(R.id.btnResist)
    protected Button mBtnResist = null;
    @ViewById(R.id.btnCancel)
    protected Button mBtnCancel = null;
    @Bean
    protected AnkiFolderModel mAnkiFolderModel;
    @Bean
    protected AnkiCardModel mAnkiCardModel;
    @Bean
    protected AnkiFolderListAdapter mAnkiFolderListAdapter;

    @FragmentArg
    protected AnkiCard mAnkiCard;

    private List<AnkiFolder> mAnkiFolderList;


    @AfterViews
    protected void initViews() {
        LogUtil.logDebug();

        // メニュー名を設定
        getActivity().setTitle(R.string.menu_anki_card_edit);

        // 初期表示
        mTxvAnkiCardInfo.setText(getString(R.string.msg_card_info,
                mAnkiCard.getCorrectCount(),
                mAnkiCard.getIncorrectCount(),
                AnkiCardUtil.FormatDate(this.getActivity(), mAnkiCard.getLastExamDate())));
        mEdtWord1.setText(mAnkiCard.getWord1());
        mEdtWord2.setText(mAnkiCard.getWord2());

        // リストを表示
        setDataToAdapter();

    }

    // 登録ボタン
    @Click(R.id.btnResist)
    protected void btnResistClick() {
        LogUtil.logDebug();

        // 暗記カード保存
        if (saveAnkiCard(mAnkiCard)) {
            // 前のフラグメントに戻る
            getFragmentManager().popBackStack();
        }
        AnkiCardUtil.HideIme(getActivity());
    }

    // キャンセルボタン
    @Click(R.id.btnCancel)
    protected void btnCancelClick() {
        LogUtil.logDebug();
        AnkiCardUtil.HideIme(getActivity());
        // 前のフラグメントに戻る
        getFragmentManager().popBackStack();

    }

    private void setDataToAdapter() {
        LogUtil.logDebug();
        mAnkiFolderList = mAnkiFolderModel.findAllWithInfo();
        mAnkiFolderListAdapter.setAnkiFolderList(mAnkiFolderList);
        mSpnFolder.setAdapter(mAnkiFolderListAdapter);

        // 初期値選択
        if (mAnkiFolderList.size() > 0) {
            int selectedIndex = 0;
            for (AnkiFolder ankiFolder : mAnkiFolderList) {
                if (mAnkiCard.getAnkiFolderId() == ankiFolder.getId()) {
                    selectedIndex = mAnkiFolderList.indexOf(ankiFolder);
                }
            }
            mSpnFolder.setSelection(selectedIndex);
        }
    }

    private boolean saveAnkiCard(AnkiCard ankiCard) {
        LogUtil.logDebug();

        if (mEdtWord1.getText().toString().length() == 0) {
            Toast.makeText(getActivity(),
                    getString(R.string.msg_must_input, getString(R.string.title_word1)),
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if (mEdtWord2.getText().toString().length() == 0) {
            Toast.makeText(getActivity(),
                    getString(R.string.msg_must_input, getString(R.string.title_word2)),
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        ankiCard.setAnkiFolderId(mAnkiFolderList.get(
                mSpnFolder.getSelectedItemPosition()).getId());
        ankiCard.setWord1(mEdtWord1.getText().toString());
        ankiCard.setWord2(mEdtWord2.getText().toString());
        ankiCard.setUpdateDate(new Date());
        mAnkiCardModel.save(ankiCard);
        return true;
    }

}
