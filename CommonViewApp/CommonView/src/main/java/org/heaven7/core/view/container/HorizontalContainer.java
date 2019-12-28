package org.heaven7.core.view.container;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import static android.widget.LinearLayout.HORIZONTAL;

public class HorizontalContainer extends MultiContainer {

    public HorizontalContainer() {
        super();
    }

    protected ViewGroup.LayoutParams generateLayoutParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected ViewGroup createLayout(ViewGroup parent, LayoutInflater inflater) {
        LinearLayout layout = new LinearLayout(parent.getContext());
        layout.setOrientation(HORIZONTAL);
        layout.setLayoutParams(generateLayoutParams());
        return layout;
    }

}