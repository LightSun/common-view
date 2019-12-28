package org.heaven7.core.view.container;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.heaven7.core.view.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class MultiContainer extends BaseContainer {

    private List<Container> cells;

    public MultiContainer(){
        this.cells = new ArrayList<>();
    }
    public List<Container> getContainers(){
        return cells;
    }
    public void addContainer(Container container){
        cells.add(container);
    }
    public void removeContainer(Container container){
        cells.remove(container);
    }

    protected ViewGroup createLayout(ViewGroup parent, LayoutInflater inflater) {
        return new FlowLayout(parent.getContext());
    }

    @Override
    protected View onCreateView(ViewGroup parent, LayoutInflater inflater) {
        ViewGroup layout = createLayout(parent, inflater);
        for (Container cell : cells) {
            layout.addView(cell.getView(layout, inflater));
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