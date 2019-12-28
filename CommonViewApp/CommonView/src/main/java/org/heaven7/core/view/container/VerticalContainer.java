package org.heaven7.core.view.container;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

public class VerticalContainer extends MultiContainer {

    public VerticalContainer(List<Container> cells) {
        super(cells);
    }

    protected ViewGroup.LayoutParams generateLayoutParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected ViewGroup createLayout(ViewGroup parent) {
        LinearLayout layout = new LinearLayout(parent.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(generateLayoutParams());
        return layout;
    }
}
