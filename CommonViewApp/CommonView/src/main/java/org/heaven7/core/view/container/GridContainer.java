package org.heaven7.core.view.container;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.GridLayout;


public abstract class GridContainer extends MultiContainer {

    @Override
    protected abstract GridLayout createLayout(ViewGroup parent, LayoutInflater inflater);

}