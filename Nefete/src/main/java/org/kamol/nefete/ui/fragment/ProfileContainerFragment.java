package org.kamol.nefete.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.kamol.nefete.R;

public class ProfileContainerFragment extends Fragment {

    public static ProfileContainerFragment newInstance() {
        return new ProfileContainerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_container_profile, container, false);
    }
}