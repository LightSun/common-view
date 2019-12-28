package org.heaven7.core.view.container;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseContainer implements Container {

    private View contentView;

    public View getView(ViewGroup parent, LayoutInflater inflater) {
        if (contentView != null) {
            return contentView;
        }
        contentView = onCreateView(parent, inflater);
        return contentView;
    }
    public View getView() {
        return contentView;
    }

    protected abstract View onCreateView(ViewGroup parent, LayoutInflater inflater);
}