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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import net.tacks_a.ankicard.R;
import net.tacks_a.ankicard.adapter.AnkiFolderListAdapter;
import net.tacks_a.ankicard.entity.AnkiFolder;
import net.tacks_a.ankicard.entity.ExamCond;
import net.tacks_a.ankicard.helper.LogUtil;
import net.tacks_a.ankicard.model.AnkiCardModel;
import net.tacks_a.ankicard.model.AnkiFolderModel;
import net.tacks_a.ankicard.model.PointHistoryModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.Date;
import java.util.List;

/**
 * 暗記フォルダ一覧Fragment
 *
 * @author tacks_a
 */
@SuppressWarnings("WeakerAccess")
@EFragment(R.layout.fragment_anki_folder_list)
public class AnkiFolderListFragment extends Fragment {

    @ViewById(R.id.lstAnkiFolder)
    protected ListView mLstFolder = null;
    @ViewById(R.id.btnAddAnkiFolder)
    protected Button mBtnAddFolder = null;

    @Bean
    protected AnkiFolderModel mAnkiFolderModel;
    @Bean
    protected AnkiCardModel mAnkiCardModel;
    @Bean
    protected PointHistoryModel mPointHistoryModel;
    @Bean
    protected AnkiFolderListAdapter mAnkiFolderListAdapter;

    private List<AnkiFolder> mAnkiFolderList;

    @AfterViews
    protected void initViews() {
        LogUtil.logDebug();

        // メニュー名を設定
        getActivity().setTitle(R.string.menu_anki_card_folder_list);

        // 長押しでコンテキストメニュー
        registerForContextMenu(mLstFolder);

        // リストを表示
        setDataToAdapter();
    }

    // フォルダ追加ボタン
    @Click(R.id.btnAddAnkiFolder)
    protected void btnAddAnkiFolderClick() {
        LogUtil.logDebug();
        showEditFolder(null);
    }

    // 一覧クリック
    @ItemClick(R.id.lstAnkiFolder)
    public void lstAnkiFolderItemClick(AnkiFolder ankiFolder) {
        LogUtil.logDebug();
        // 暗記カード一覧
        showAnkiCardList(ankiFolder);
    }

    private void setDataToAdapter() {
        LogUtil.logDebug();
        mAnkiFolderList = mAnkiFolderModel.findAllWithInfo();
        mAnkiFolderListAdapter.setAnkiFolderList(mAnkiFolderList);
        mLstFolder.setAdapter(mAnkiFolderListAdapter);
    }

    // フォルダ編集
    private void showEditFolder(AnkiFolder currentAnkiFolder) {
        LogUtil.logDebug();
        final AnkiFolder ankiFolder;
        if (currentAnkiFolder == null) {
            currentAnkiFolder = new AnkiFolder();
        }
        ankiFolder = currentAnkiFolder;
        FolderNameDialogFragment.newInstance(ankiFolder).show(getFragmentManager(), "showEditFolder");

    }

    // フォルダ保存
    private void saveFolder(AnkiFolder ankiFolder) {
        LogUtil.logDebug();
        // 保存
        ankiFolder.setUpdateDate(new Date());
        mAnkiFolderModel.save(ankiFolder);
        // 一覧を再表示
        setDataToAdapter();
    }

    // フォルダ削除
    private void deleteFolder(final AnkiFolder ankiFolder) {
        LogUtil.logDebug();
        DeleteFolderDialogFragment.newInstance(ankiFolder).show(getFragmentManager(), "deleteFolder");
    }

    // 暗記カード一覧
    private void showAnkiCardList(AnkiFolder currentAnkiFolder) {
        LogUtil.logDebug();
        final AnkiFolder ankiFolder;
        if (currentAnkiFolder == null) {
            return;
        }
        ankiFolder = currentAnkiFolder;

        AnkiCardListFragment fragment = AnkiCardListFragment_.builder()
                .mAnkiFolder(ankiFolder).build();

        getFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, fragment)
                .commit();
    }

    /**
     * 問題出題
     *
     * @param currentAnkiFolder
     */
    @SuppressWarnings("JavaDoc")
    private void showQuestionCondition(AnkiFolder currentAnkiFolder) {
        LogUtil.logDebug();
        ExamCond questionCondition = new ExamCond();
        if (currentAnkiFolder == null) {
            return;
        }
        questionCondition.setAnkiFolderIds(String.valueOf(currentAnkiFolder
                .getId()));

        Fragment fragment = ExamCondFragment_.builder()
                .mExamCond(questionCondition).build();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        LogUtil.logDebug();
        switch (v.getId()) {
            case R.id.lstAnkiFolder:
                getActivity().getMenuInflater().inflate(
                        R.menu.lst_anki_folder_context, menu);

                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        LogUtil.logDebug();
        AdapterView.AdapterContextMenuInfo info;
        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.lst_anki_folder_edit:
                // 編集
                showEditFolder(mAnkiFolderList.get(info.position));
                return true;
            case R.id.lst_anki_folder_delete:
                // 削除
                deleteFolder(mAnkiFolderList.get(info.position));
                return true;
            case R.id.lst_anki_folder_show_anki_card_list:
                // 暗記カード一覧
                showAnkiCardList(mAnkiFolderList.get(info.position));
                return true;
            case R.id.lst_anki_folder_show_exam:
                // 問題出題
                showQuestionCondition(mAnkiFolderList.get(info.position));
                return true;
        }
        return false;
    }

    public static class FolderNameDialogFragment extends DialogFragment {
        public static FolderNameDialogFragment newInstance(AnkiFolder ankiFolder) {
            LogUtil.logDebug();
            FolderNameDialogFragment frag = new FolderNameDialogFragment();
            Bundle args = new Bundle();
            args.putSerializable(AnkiFolder.TABLE_NAME, ankiFolder);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LogUtil.logDebug();
            final AnkiFolder ankiFolder = (AnkiFolder) getArguments().getSerializable(AnkiFolder.TABLE_NAME);
            final EditText editView = new EditText(this.getActivity());
            editView.setText(ankiFolder.getName());
            editView.setBackgroundColor(Color.WHITE);
            return new AlertDialog.Builder(this.getActivity())
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle(R.string.title_folder_name)
                    .setView(editView)
                    .setNegativeButton(R.string.title_cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    LogUtil.logDebug();
                                }
                            })
                    .setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            LogUtil.logDebug();
                            String value = editView.getText().toString();
                            if (value.length() == 0) {
                                return;
                            }
                            ankiFolder.setName(value);
                            AnkiFolderListFragment f = (AnkiFolderListFragment) getActivity().getSupportFragmentManager().findFragmentByTag(AnkiFolderListFragment_.class.getSimpleName());
                            f.saveFolder(ankiFolder);
                        }
                    }).create();
        }
    }

    public static class DeleteFolderDialogFragment extends DialogFragment {
        public static DeleteFolderDialogFragment newInstance(AnkiFolder ankiFolder) {
            LogUtil.logDebug();
            DeleteFolderDialogFragment frag = new DeleteFolderDialogFragment();
            Bundle args = new Bundle();
            args.putSerializable(AnkiFolder.TABLE_NAME, ankiFolder);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LogUtil.logDebug();
            final AnkiFolder ankiFolder = (AnkiFolder) getArguments().getSerializable(AnkiFolder.TABLE_NAME);
            return new AlertDialog.Builder(this.getActivity())
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle(R.string.title_folder_name)
                    .setMessage(R.string.info_delete_folder)
                    .setNegativeButton(R.string.title_cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    LogUtil.logDebug();
                                }
                            })
                    .setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            LogUtil.logDebug();
                            AnkiFolderListFragment f = (AnkiFolderListFragment) getActivity().getSupportFragmentManager().findFragmentByTag(AnkiFolderListFragment_.class.getSimpleName());
                            // カード削除
                            f.mAnkiCardModel.deleteByAnkiFolderId(ankiFolder.getId());
                            // ポイント削除
                            f.mPointHistoryModel.deleteByAnkiFolderId(ankiFolder.getId());
                            // フォルダ削除
                            f.mAnkiFolderModel.delete(ankiFolder);
                            // 一覧を再表示
                            f.setDataToAdapter();
                        }
                    }).create();
        }
    }

}
