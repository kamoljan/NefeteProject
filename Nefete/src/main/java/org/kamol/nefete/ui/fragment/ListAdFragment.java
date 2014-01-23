package org.kamol.nefete.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.kamol.nefete.R;

public class ListAdFragment extends Fragment {

    public static ListAdFragment newInstance() {
        return new ListAdFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_ad, container, false);
    }
}