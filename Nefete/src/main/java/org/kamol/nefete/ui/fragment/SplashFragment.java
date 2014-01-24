package org.kamol.nefete.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import org.kamol.nefete.R;

public class SplashFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_splash, container, false);
        ImageView ivTest = (ImageView) root.findViewById(R.id.iv_test);
        ivTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destroyFragment();
            }
        });
        return root;
    }

    private void destroyFragment() {
        getChildFragmentManager().beginTransaction().hide(this).commit();
    }
}
