package org.heaven7.core.view.container;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class OutInContainer extends BaseContainer {

    private final List<List<Container>> grid;

    public OutInContainer(List<List<Container>> grid) {
        this.grid = grid;
    }

    public List<List<Container>> getContainers() {
        return grid;
    }
    protected abstract ViewGroup createOutView(ViewGroup parent, LayoutInflater inflater);

    protected abstract ViewGroup createInView(ViewGroup parent, LayoutInflater inflater);

    @Override
    public View onCreateView(ViewGroup parent, LayoutInflater inflater) {
        ViewGroup outView = createOutView(parent, inflater);
        for (int size1 = grid.size(), i = 0; i < size1; i++) {
            List<Container> list = grid.get(i);
            ViewGroup child = createInView(outView, inflater);
            for (int size2 = list.size(), j = 0; j < size2; j++) {
                Container column = list.get(j);
                child.addView(column.getView(child, inflater));
            }
            outView.addView(child);
        }
        return outView;
    }

    @Override
    public void onAttach() {
        for (int size1 = grid.size(), i = 0; i < size1; i++) {
            List<Container> list = grid.get(i);
            for (int size2 = list.size(), j = 0; j < size2; j++) {
                list.get(j).onAttach();
            }
        }
    }
    @Override
    public void onDetach() {
        for (int size1 = grid.size(), i = 0; i < size1; i++) {
            List<Container> list = grid.get(i);
            for (int size2 = list.size(), j = 0; j < size2; j++) {
                list.get(j).onDetach();
            }
        }
    }
}