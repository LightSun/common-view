package org.heaven7.core.view.container;

import android.view.View;
import android.view.ViewGroup;

public interface Container {

    View getView(ViewGroup parent);

    View getView();

    void onAttach();

    void onDetach();
}