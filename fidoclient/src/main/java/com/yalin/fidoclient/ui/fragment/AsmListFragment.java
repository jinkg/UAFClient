package com.yalin.fidoclient.ui.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yalin.fidoclient.R;
import com.yalin.fidoclient.asm.api.ASMIntent;
import com.yalin.fidoclient.msg.AsmInfo;

import java.util.List;

/**
 * Created by YaLin on 2016/2/26.
 */
public class AsmListFragment extends Fragment implements AsmListAdapter.ASMClickListener {
    public interface AsmItemPickListener {
        void onAsmItemPick(AsmInfo info);
    }

    private static final String TAG = AsmListFragment.class.getSimpleName();

    private TextView tvAsmListPrompt;
    private RecyclerView rvAsmList;

    private AsmItemPickListener listener;

    public static AsmListFragment getInstance(AsmItemPickListener listener) {
        AsmListFragment fragment = new AsmListFragment();
        fragment.listener = listener;
        return fragment;
    }

    public static void open(int container, FragmentManager manager, AsmItemPickListener listener) {
        if (manager.findFragmentByTag(TAG) != null) {
            return;
        }
        manager.beginTransaction()
                .add(container, getInstance(listener), TAG)
                .addToBackStack(null)
                .commit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asm_list, container, false);
        findView(view);
        initData();
        return view;
    }

    private void findView(View view) {
        tvAsmListPrompt = (TextView) view.findViewById(R.id.tv_asm_list_prompt);
        rvAsmList = (RecyclerView) view.findViewById(R.id.rv_asm_list);

        rvAsmList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initData() {
        Intent intent = ASMIntent.getASMIntent();
        PackageManager pm = getActivity().getApplicationContext().getPackageManager();

        List<ResolveInfo> infos = pm.queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);
        if (infos != null && infos.size() > 0) {
            AsmListAdapter asmListAdapter = new AsmListAdapter(getActivity(), infos, this);
            rvAsmList.setAdapter(asmListAdapter);
        } else {
            tvAsmListPrompt.setText(R.string.no_asm);
        }
    }

    @Override
    public void onAsmItemClicked(AsmInfo info) {
        if (listener != null) {
            listener.onAsmItemPick(info);
        }
    }
}
