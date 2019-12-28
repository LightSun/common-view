package org.heaven7.core.view.container;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface Container {

    View getView(ViewGroup parent, LayoutInflater layoutInflater);

    View getView();

    void onAttach();

    void onDetach();
}