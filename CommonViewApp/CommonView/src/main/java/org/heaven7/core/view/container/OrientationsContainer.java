package org.heaven7.core.view.container;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public class OrientationsContainer extends OutInContainer {

    private final int outOrientation;
    private final int inOrientation;

    public OrientationsContainer(List<List<Container>> grid, int outOrientation, int inOrientation) {
        super(grid);
        this.outOrientation = outOrientation;
        this.inOrientation = inOrientation;
    }

    protected ViewGroup.LayoutParams generateOutLayoutParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    protected ViewGroup.LayoutParams generateInLayoutParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected ViewGroup createOutView(ViewGroup parent, LayoutInflater inflater) {
        LinearLayout ll = new LinearLayout(parent.getContext());
        ll.setOrientation(outOrientation);
        ll.setLayoutParams(generateOutLayoutParams());
        return ll;
    }

    @Override
    protected ViewGroup createInView(ViewGroup parent, LayoutInflater inflater) {
        LinearLayout ll = new LinearLayout(parent.getContext());
        ll.setOrientation(inOrientation);
        ll.setLayoutParams(generateInLayoutParams());
        return ll;
    }

    public static class HorizontalVeticalContainer extends OrientationsContainer {
        public HorizontalVeticalContainer(List<List<Container>> grid) {
            super(grid, HORIZONTAL, VERTICAL);
        }
    }
    public static class VeticalHorizontalContainer extends OrientationsContainer {
        public VeticalHorizontalContainer(List<List<Container>> grid) {
            super(grid, VERTICAL, HORIZONTAL);
        }
    }
}