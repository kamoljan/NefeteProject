package org.kamol.nefete.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import org.kamol.nefete.R;

public class InsertAdContainerFragment extends Fragment {
    private static final String TAG = "InsertAdContainerFragment";

    private UiLifecycleHelper uiHelper;
    private Fragment insertAdFragment;
    private Fragment splashFragment;

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
            showInsertAdFragment();
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
            showSplashFragment();
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }

    public static InsertAdContainerFragment newInstance() {
        return new InsertAdContainerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_container_insert_ad, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null && (session.isOpened() || session.isClosed())) {
            onSessionStateChange(session, session.getState(), null);
        } else {
            showSplashFragment();
        }
        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    private void showInsertAdFragment() {
        if (getChildFragmentManager().findFragmentByTag("InsertAdFragment") != null) {
            insertAdFragment = getChildFragmentManager().findFragmentByTag("InsertAdFragment");
        } else {
            insertAdFragment = new InsertAdFragment();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.ll_list_ad_container, insertAdFragment, "InsertAdFragment");
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void showSplashFragment() {
        if (getChildFragmentManager().findFragmentByTag("SplashFragment") != null) {
            splashFragment = getChildFragmentManager().findFragmentByTag("SplashFragment");
        } else {
            splashFragment = new SplashFragment();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.ll_list_ad_container, splashFragment, "SplashFragment");
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}