package org.heaven7.core.view.container;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import static android.widget.LinearLayout.HORIZONTAL;

public class HorizontalContainer extends MultiContainer {

    public HorizontalContainer(List<Container> cells) {
        super(cells);
    }

    protected ViewGroup.LayoutParams generateLayoutParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected ViewGroup createLayout(ViewGroup parent) {
        LinearLayout layout = new LinearLayout(parent.getContext());
        layout.setOrientation(HORIZONTAL);
        layout.setLayoutParams(generateLayoutParams());
        return layout;
    }

}