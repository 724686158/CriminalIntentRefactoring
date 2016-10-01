package com.example.criminalintentrefactoring.CriminalIntent.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.criminalintentrefactoring.AndroidLib.Fragment.BaseFragment;
import com.example.criminalintentrefactoring.CriminalIntent.adapter.CrimeAdapter;
import com.example.criminalintentrefactoring.CriminalIntent.engine.CrimeLab;
import com.example.criminalintentrefactoring.CriminalIntent.entity.Crime;
import com.example.criminalintentrefactoring.R;

import java.util.List;

/**
 * Created by 离子态狍子 on 2016/9/30.
 */

public class CrimeListFragment extends BaseFragment {

    /**
     * 常量
     */
    private static final int REQUEST_CRIME = 1;

    /**
     * 接口
     */
    private Callbacks mCallbacks;

    /**
     * 适配器
     */
    CrimeAdapter mAdapter;

    /**
     * 组件
     */
    private RecyclerView mCrimeRecyclerView;

    /**
     * 数据
     */
    private boolean mSubtitleVisible;
    List<Crime> crimes;




    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onCrimeSelected(Crime crime);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        initViews(inflater, container, savedInstanceState, view);
        loadData(inflater);
        return view;
    }

    @Override
    protected void initVariables() {
        setHasOptionsMenu(true);
        updateUI();
    }

    @Override
    protected void initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, View view){
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void loadData(LayoutInflater inflater) {
        updateUI();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI() {

        CrimeLab crimeLab = CrimeLab.get(getActivity());
        crimes = crimeLab.getCrimeList();

        if (mAdapter == null)
        {
            mAdapter = new CrimeAdapter(getActivity(), mCallbacks, crimes);

        }
        else {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }
        if (mCrimeRecyclerView != null)
        {
            mCrimeRecyclerView.setAdapter(mAdapter);
        }
        else {
            Log.d("CrimeListFragment", "mCrimeRecyclerView is null");
        }
        updateSubtitle();
    }

    /**
     * 更新副标题
     */
    private void updateSubtitle()
    {

        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimes.size();

        String subtitle = getString(R.string.subtitle_format, crimeCount);//拼字符串

        if (!mSubtitleVisible)
        {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible)
        {
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_item_new_crime:
                doCreateCrime();
                return true;

            case R.id.menu_item_show_subtitle:
                doChangeSubtitle();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 处理用户 按下新建按钮 后的业务
     */
    private void doCreateCrime() {
        Crime crime = new Crime();
        CrimeLab.get(getActivity()).addCrime(crime);
        updateUI();
        mCallbacks.onCrimeSelected(crime);
    }

    /**
     * 处理用户 按下副标题按钮 后的业务
     */
    private void doChangeSubtitle() {
        mSubtitleVisible = !mSubtitleVisible;
        getActivity().invalidateOptionsMenu();
        updateSubtitle();
    }

}
