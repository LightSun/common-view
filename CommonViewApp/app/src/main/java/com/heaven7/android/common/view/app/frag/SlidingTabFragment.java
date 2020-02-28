package com.heaven7.android.common.view.app.frag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.heaven7.android.common.view.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SlidingTabFragment extends Fragment {

    public static final String KEY_TEXT = "text";

    @BindView(R.id.bt)
    Button mBt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_test_sliding_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Bundle arguments = getArguments();
        if(arguments != null){
            String text = arguments.getString(KEY_TEXT);
            mBt.setText(text);
        }
    }
}
