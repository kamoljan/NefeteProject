package org.kamol.nefete.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.widget.LoginButton;
import org.kamol.nefete.R;

public class SplashFragment extends Fragment {
    private static final String TAG = "SplashFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        LoginButton lbFacebook = (LoginButton) view.findViewById(R.id.lb_facebook);
        lbFacebook.setFragment(this);
        return view;
    }

    private void destroyFragment() {
        getChildFragmentManager().beginTransaction().hide(this).commit();
    }
}
