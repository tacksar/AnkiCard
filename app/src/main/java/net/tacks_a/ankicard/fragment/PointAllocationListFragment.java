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
import android.text.InputType;
import android.widget.EditText;
import android.widget.ListView;

import net.tacks_a.ankicard.R;
import net.tacks_a.ankicard.adapter.PointAllocationListAdapter;
import net.tacks_a.ankicard.entity.PointAllocation;
import net.tacks_a.ankicard.helper.LogUtil;
import net.tacks_a.ankicard.model.PointAllocationModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_point_allocation_list)
public class PointAllocationListFragment extends Fragment {

    @ViewById(R.id.lstPointAllocation)
    protected ListView mLstPointAllocation;

    @StringArrayRes(R.array.ary_point_allocation)
    protected String[] mAryPointAllocation;
    @Bean
    protected PointAllocationModel mPointAllocationModel;
    @Bean
    protected PointAllocationListAdapter mPointAllocationListAdapter;

    protected List<PointAllocation> mPointAllocationList;

    @AfterViews
    protected void initViews() {
        LogUtil.logDebug();

        // メニュー名を設定
        getActivity().setTitle(R.string.menu_point_allocation);

        // リストを表示
        setDataToAdapter();
    }

    private void setDataToAdapter() {
        LogUtil.logDebug();
        mPointAllocationModel.setPointAllocationList(mAryPointAllocation, false);
        mPointAllocationList = mPointAllocationModel.findAll();
        mPointAllocationListAdapter.setPointAllocationList(mPointAllocationList);
        mLstPointAllocation.setAdapter(mPointAllocationListAdapter);
    }

    @ItemClick(R.id.lstPointAllocation)
    public void lstPointAllocationItemClick(PointAllocation pointAllocation) {
        LogUtil.logDebug();

        showEditPointAllocation(pointAllocation);
    }

    private void showEditPointAllocation(PointAllocation currentPointAllocation) {
        LogUtil.logDebug();
        final PointAllocation pointAllocation;
        if (currentPointAllocation == null) {
            currentPointAllocation = new PointAllocation();
        }
        pointAllocation = currentPointAllocation;
        PointAllocationDialogFragment.newInstance(pointAllocation).show(getFragmentManager(), "showEditPointAllocation");
    }

    public static class PointAllocationDialogFragment extends DialogFragment {
        public static PointAllocationDialogFragment newInstance(PointAllocation pointAllocation) {
            LogUtil.logDebug();
            PointAllocationDialogFragment frag = new PointAllocationDialogFragment();
            Bundle args = new Bundle();
            args.putSerializable(PointAllocation.TABLE_NAME, pointAllocation);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LogUtil.logDebug();
            final PointAllocation pointAllocation = (PointAllocation) getArguments().getSerializable(PointAllocation.TABLE_NAME);
            final EditText editView = new EditText(this.getActivity());
            editView.setText(Integer.toString(pointAllocation.getPoint()));
            editView.setInputType(InputType.TYPE_CLASS_NUMBER);
            editView.setBackgroundColor(Color.WHITE);
            return new AlertDialog.Builder(this.getActivity())
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle(R.string.title_point)
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
                            if ("".equals(editView.getText().toString())) return;
                            pointAllocation.setPoint(Integer.parseInt(editView.getText().toString()));
                            PointAllocationListFragment f = (PointAllocationListFragment) getActivity().getSupportFragmentManager().findFragmentByTag(PointAllocationListFragment_.class.getSimpleName());
                            f.mPointAllocationModel.save(pointAllocation);
                        }
                    }).create();
        }
    }


}
