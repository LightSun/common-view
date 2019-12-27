package org.heaven7.core.view.container;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import java.util.List;

public abstract class GridContainer extends BaseContainer {

    private List<Container> cells; //row-column

    public GridContainer(List<Container> cells) {
        this.cells = cells;
    }

    protected abstract GridLayout createGridLayout(ViewGroup parent);

    @Override
    public View onCreateView(ViewGroup parent) {
        GridLayout layout = createGridLayout(parent);
        for (int size1 = cells.size(), i = 0; i < size1; i++) {
            Container cell = cells.get(i);
            layout.addView(cell.getView(layout));
        }
        return layout;
    }

    @Override
    public void onAttach() {
        for (int size1 = cells.size(), i = 0; i < size1; i++) {
            cells.get(i).onAttach();
        }
    }
}