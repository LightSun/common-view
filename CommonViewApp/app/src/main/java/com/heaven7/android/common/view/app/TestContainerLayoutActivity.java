package com.heaven7.android.common.view.app;

import android.content.Context;
import android.os.Bundle;

import org.heaven7.core.view.ContainerLayout;
import org.heaven7.core.view.container.VerticalTextsContainer;

import java.util.Arrays;

import butterknife.BindView;

public class TestContainerLayoutActivity extends BaseActivity {

    @BindView(R.id.vg_container)
    ContainerLayout mContainer;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_container;
    }
    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {

        mContainer.addChild(new VerticalTextsContainer(R.layout.container_simple_text,
                Arrays.asList("Hello World!",
                        "Hello Goggle!",
                        "Hello Container Layout")));
    }
}
