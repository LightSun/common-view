package org.heaven7.core.view.container;

import android.view.ViewGroup;
import android.widget.GridLayout;

import java.util.List;

public abstract class GridContainer extends MultiContainer {

    public GridContainer(List<Container> cells) {
        super(cells);
    }
    @Override
    protected abstract GridLayout createLayout(ViewGroup parent);

}