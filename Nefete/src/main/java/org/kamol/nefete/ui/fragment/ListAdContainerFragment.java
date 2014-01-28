package org.kamol.nefete.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.kamol.nefete.R;

public class ListAdContainerFragment extends Fragment {

    public static ListAdContainerFragment newInstance() {
        return new ListAdContainerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_container_list_ad, container, false);
    }
}