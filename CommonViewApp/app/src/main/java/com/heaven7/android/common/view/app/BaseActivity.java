package com.heaven7.android.common.view.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        onInit(this, savedInstanceState);
    }

    protected abstract int getLayoutId();

    protected abstract void onInit(Context context, Bundle savedInstanceState);
}
