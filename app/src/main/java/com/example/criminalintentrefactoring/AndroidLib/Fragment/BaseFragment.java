package com.example.criminalintentrefactoring.AndroidLib.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.criminalintentrefactoring.R;

/**
 * Created by 离子态狍子 on 2016/9/30.
 */

public abstract class BaseFragment extends Fragment{


    protected abstract void initVariables();
    protected abstract void initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, View view);
    protected abstract void loadData(LayoutInflater inflater);
}
