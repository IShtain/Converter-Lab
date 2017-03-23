package com.shtainyky.converterlab.fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shtainyky.converterlab.activities.BaseActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<A extends BaseActivity> extends Fragment {

    private Unbinder mBinder;

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
        mBinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinder.unbind();
    }
}
