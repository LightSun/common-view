package org.heaven7.core.view.container;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class FixedSizeContainer implements Container {
    private final Container base;
    private final int width;
    private final int height;

    public FixedSizeContainer(Container base, int width, int height) {
        this.base = base;
        this.width = width;
        this.height = height;
    }

    @Override
    public View getView(ViewGroup parent, LayoutInflater layoutInflater) {
        View view = base.getView(parent, layoutInflater);
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = width;
        lp.height = height;
        return view;
    }

    @Override
    public void onAttach() {
        base.onAttach();
    }

    @Override
    public void onDetach() {
        base.onDetach();
    }
}