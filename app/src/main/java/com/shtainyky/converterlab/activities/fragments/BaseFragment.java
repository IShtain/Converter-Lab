package com.shtainyky.converterlab.activities.fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shtainyky.converterlab.activities.activities.BaseActivity;

import butterknife.ButterKnife;

public abstract class BaseFragment<A extends BaseActivity> extends Fragment {

    @LayoutRes
    protected abstract int getLayoutResId();

    @SuppressWarnings("unchecked")
    protected A getActivityGeneric(){
        return (A) getActivity();
    }

    @Override
    public final View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                                   final Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
