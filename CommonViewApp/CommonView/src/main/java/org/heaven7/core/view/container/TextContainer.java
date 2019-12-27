package org.heaven7.core.view.container;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class TextContainer extends BaseContainer {

    public abstract CharSequence text();

    public abstract int layoutId();

    public int textViewId() {
        return 0;
    }

    @Override
    public View onCreateView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId(), parent, false);
    }

    @Override
    public void onAttach() {
        View view = getView();
        int textViewId = textViewId();
        TextView tv;
        if (textViewId == 0) {
            tv = (TextView) view;
        } else {
            tv = view.findViewById(textViewId);
        }
        tv.setText(text());
    }
    @Override
    public void onDetach() {

    }
}