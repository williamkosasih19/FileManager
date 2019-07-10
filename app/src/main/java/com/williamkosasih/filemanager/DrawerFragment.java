package com.williamkosasih.filemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class DrawerFragment extends Fragment {
    public DrawerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater lif, ViewGroup cont, Bundle bundle) {
        super.onCreateView(lif, cont, bundle);
        return getLayoutInflater().inflate(R.layout.drawer_fragment, cont, false);

    }
}
