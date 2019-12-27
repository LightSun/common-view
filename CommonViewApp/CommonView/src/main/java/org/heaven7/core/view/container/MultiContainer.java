package org.heaven7.core.view.container;

import android.view.View;
import android.view.ViewGroup;

import org.heaven7.core.view.FlowLayout;

import java.util.List;

public class MultiContainer extends BaseContainer {

    private List<Container> cells;

    public MultiContainer(List<Container> cells) {
        this.cells = cells;
    }

    protected ViewGroup createLayout(ViewGroup parent) {
        return new FlowLayout(parent.getContext());
    }

    @Override
    public View onCreateView(ViewGroup parent) {
        ViewGroup layout = createLayout(parent);
        for (Container cell : cells) {
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

    @Override
    public void onDetach() {
        for (int size1 = cells.size(), i = 0; i < size1; i++) {
            cells.get(i).onDetach();
        }
    }
}