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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import net.tacks_a.ankicard.R;
import net.tacks_a.ankicard.entity.AnkiCard;
import net.tacks_a.ankicard.entity.AnkiFolder;
import net.tacks_a.ankicard.model.AnkiCardModel;
import net.tacks_a.ankicard.util.LogUtil;
import net.tacks_a.ankicard.view.adapter.AnkiCardListAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 暗記カード一覧Fragment
 *
 * @author tacks_a
 */
@SuppressWarnings("WeakerAccess")
@EFragment(R.layout.fragment_anki_card_list)
public class AnkiCardListFragment extends Fragment {

    @ViewById(R.id.lstAnkiCard)
    protected ListView mLstAnkiCard = null;
    @ViewById(R.id.btnAddAnkiCard)
    protected Button mBtnAddAnkiCard = null;

    @FragmentArg
    protected AnkiFolder mAnkiFolder;

    @Bean
    protected AnkiCardModel mAnkiCardModel;
    @Bean
    protected AnkiCardListAdapter mAnkiCardListAdapter;

    private List<AnkiCard> mAnkiCardList;

    @AfterViews
    protected void initViews() {
        LogUtil.logDebug();

        // メニュー名を設定
        getActivity().setTitle(R.string.menu_anki_card_list);

        // 長押しでコンテキストメニュー
        registerForContextMenu(mLstAnkiCard);

        // リストを表示
        setDataToAdapter();
    }

    // カード追加ボタン
    @Click(R.id.btnAddAnkiCard)
    protected void btnAddAnkiCardClick() {
        LogUtil.logDebug();
        showEditAnkiCard(null);
    }

    // 一覧クリック
    @ItemClick(R.id.lstAnkiCard)
    public void lstAnkiCardItemClick(AnkiCard ankiCard) {
        LogUtil.logDebug();
        // 編集
        showEditAnkiCard(ankiCard);
    }

    private void setDataToAdapter() {
        LogUtil.logDebug();
        mAnkiCardList = mAnkiCardModel.findByAnkiFolderId(mAnkiFolder.getId());
        mAnkiCardListAdapter.setAnkiCardList(mAnkiCardList);
        mLstAnkiCard.setAdapter(mAnkiCardListAdapter);
    }

    // カード編集
    private void showEditAnkiCard(AnkiCard currentAnkiCard) {
        LogUtil.logDebug();
        final AnkiCard ankiCard;
        if (currentAnkiCard == null) {
            currentAnkiCard = new AnkiCard();
            currentAnkiCard.setAnkiFolderId(mAnkiFolder.getId());
        }
        ankiCard = currentAnkiCard;
        AnkiCardEditFragment fragment = AnkiCardEditFragment_.builder()
                .mAnkiCard(ankiCard)
                .build();

        getFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, fragment)
                .commit();
    }

    // カード削除
    private void deleteAnkiCard(AnkiCard ankiCard) {
        LogUtil.logDebug();
        // 削除
        mAnkiCardModel.delete(ankiCard);
        // 一覧を再表示
        setDataToAdapter();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        LogUtil.logDebug();

        switch (v.getId()) {
            case R.id.lstAnkiCard:
                getActivity().getMenuInflater().inflate(
                        R.menu.lst_anki_card_context, menu);
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        LogUtil.logDebug();
        AdapterView.AdapterContextMenuInfo info;
        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.lst_anki_card_edit:
                // 編集
                showEditAnkiCard(mAnkiCardList.get(info.position));
                return true;
            case R.id.lst_anki_card_delete:
                // 削除
                deleteAnkiCard(mAnkiCardList.get(info.position));
                return true;
        }
        return false;
    }

}
