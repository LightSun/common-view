package org.heaven7.core.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.heaven7.core.view.container.Container;

import java.util.ArrayList;
import java.util.List;

/**
 * item info
 *
 * @author heaven7
 */
public class ContainerLayout extends LinearLayout {

    private final List<Container> mContainers = new ArrayList<>();

    public ContainerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContainerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ContainerLayout);
        try {
            //TODO\
        } finally {
            a.recycle();
        }
    }

    public ContainerLayout addChild(Container container) {
        mContainers.add(container);
        addView(container.getView(this, getLayoutInflater()));
        return this;
    }
    public ContainerLayout addChildAt(int index, Container container) {
        mContainers.add(index, container);
        addView(container.getView(this, getLayoutInflater()), index);
        return this;
    }
    public ContainerLayout clearChildren() {
        for (int size = mContainers.size(), i = 0; i < size; i++) {
            removeViewAt(i);
        }
        mContainers.clear();
        return this;
    }
    public ContainerLayout removeChildAt(int index) {
        View view = mContainers.get(index).getView();
        if (view != null) {
            removeViewAt(index);
        }
        return this;
    }
    public ContainerLayout addChildren(List<Container> containers) {
        mContainers.addAll(containers);
        for (Container container : containers){
            this.addView(container.getView(this, getLayoutInflater()));
        }
        return this;
    }

    public void inflate() {
        for (int size = mContainers.size(), i = 0; i < size; i++) {
            Container container = mContainers.get(i);
            this.addView(container.getView(this, getLayoutInflater()));
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        dispatchChildAttached(index);
    }

    @Override
    public void removeViewAt(int index) {
        dispatchChildDetached(index);
        super.removeViewAt(index);
    }

    private void dispatchChildAttached(int index) {
        //onViewAttachedToWindow
        Container container = mContainers.get(index);
        container.onAttach();
    }

    private void dispatchChildDetached(int index) {
        //onViewDetachedFromWindow
        //ignore
        Container container = mContainers.get(index);
        // Clear any android.view.animation.Animation that may prevent the item from
        // detaching when being removed. If a child is re-added before the
        // lazy detach occurs, it will receive invalid attach/detach sequencing.
        container.getView().clearAnimation();
        container.onDetach();
    }
    private LayoutInflater getLayoutInflater(){
        return LayoutInflater.from(getContext());
    }

}
