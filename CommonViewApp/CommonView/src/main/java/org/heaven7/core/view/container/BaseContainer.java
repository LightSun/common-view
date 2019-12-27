package org.heaven7.core.view.container;

import android.view.View;
import android.view.ViewGroup;

public abstract class BaseContainer implements Container {
    private View contentView;

    public View getView(ViewGroup parent) {
        if (contentView != null) {
            return contentView;
        }
        contentView = onCreateView(parent);
        return contentView;
    }

    public View getView() {
        return contentView;
    }

    public abstract View onCreateView(ViewGroup parent);
}