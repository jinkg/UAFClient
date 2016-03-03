/*
 * Copyright 2016 YaLin Jin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yalin.fidoclient.ui.fragment;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yalin.fidoclient.R;
import com.yalin.fidoclient.msg.AsmInfo;

import java.util.List;

/**
 * Created by YaLin on 2016/2/26.
 */
public class AsmListAdapter extends RecyclerView.Adapter<AsmListViewHolder> {
    public interface ASMClickListener {
        void onAsmItemClicked(AsmInfo info);
    }

    private final Context mContext;
    private final List<ResolveInfo> mInfos;
    private final ASMClickListener mCallback;

    public AsmListAdapter(Context context, List<ResolveInfo> infos, ASMClickListener callback) {
        mContext = context;
        mInfos = infos;
        mCallback = callback;
    }

    @Override
    public AsmListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.asm_item, parent, false);
        return new AsmListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AsmListViewHolder holder, int position) {
        final ResolveInfo info = mInfos.get(position);
        final String label = info.loadLabel(mContext.getPackageManager()).toString();
        Drawable drawable = info.loadIcon(mContext.getPackageManager());
        final AsmInfo asmInfo = new AsmInfo();
        asmInfo.appName(label)
                .pack(info.activityInfo.packageName)
                .icon(drawable);

        holder.tvName.setText(label);
        holder.tvPack.setText(info.activityInfo.packageName);
        holder.ivIcon.setImageDrawable(drawable);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   if (mCallback != null) {
                                                       mCallback.onAsmItemClicked(asmInfo);
                                                   }
                                               }
                                           }
        );
    }

    @Override
    public int getItemCount() {
        return mInfos == null ? 0 : mInfos.size();
    }
}

class AsmListViewHolder extends RecyclerView.ViewHolder {
    ImageView ivIcon;
    TextView tvName;
    TextView tvPack;

    public AsmListViewHolder(View itemView) {
        super(itemView);
        ivIcon = (ImageView) itemView.findViewById(R.id.item_iv_icon);
        tvName = (TextView) itemView.findViewById(R.id.item_tv_name);
        tvPack = (TextView) itemView.findViewById(R.id.item_tv_pack);
    }
}