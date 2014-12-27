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

package net.tacks_a.ankicard.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.tacks_a.ankicard.entity.PointAllocation;
import net.tacks_a.ankicard.helper.LogUtil;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import lombok.Setter;

@EBean
public class PointAllocationListAdapter extends BaseAdapter {
    @RootContext
    protected Context mContext;
    @Setter
    private List<PointAllocation> pointAllocationList;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LogUtil.logDebug();

        PointAllocationItemView view;
        if (convertView == null) {
            view = PointAllocationListAdapter_.PointAllocationItemView_.build(mContext);
        } else {
            view = (PointAllocationItemView) convertView;
        }

        view.bind(getItem(position));

        return view;
    }

    @Override
    public int getCount() {
        return pointAllocationList.size();
    }

    @Override
    public PointAllocation getItem(int position) {
        return pointAllocationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @EViewGroup(android.R.layout.simple_list_item_2)
    public static class PointAllocationItemView extends RelativeLayout {
        @ViewById(android.R.id.text1)
        public TextView text1;
        @ViewById(android.R.id.text2)
        public TextView text2;

        public PointAllocationItemView(Context context) {
            super(context);
        }

        public void bind(PointAllocation pointAllocation) {
            LogUtil.logDebug();
            text1.setText(pointAllocation.getName());
            text2.setText(Integer.toString(pointAllocation.getPoint()));
        }
    }
}
