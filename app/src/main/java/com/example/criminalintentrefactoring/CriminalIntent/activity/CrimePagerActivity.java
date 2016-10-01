package com.example.criminalintentrefactoring.CriminalIntent.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import com.example.criminalintentrefactoring.AndroidLib.activity.BaseActivity;
import com.example.criminalintentrefactoring.CriminalIntent.engine.CrimeLab;
import com.example.criminalintentrefactoring.CriminalIntent.entity.Crime;
import com.example.criminalintentrefactoring.CriminalIntent.fragment.CrimeFragment;
import com.example.criminalintentrefactoring.R;

import java.util.List;
import java.util.UUID;

/**
 * Created by 离子态狍子 on 2016/9/30.
 */

public class CrimePagerActivity extends BaseActivity implements CrimeFragment.Callbacks{

    /**
     * 常量
     */
    private static final String EXTRA_CRIME_ID = "com.example.criminalintentrefactoring.crime_id";

    /**
     * 组件
     */
    private ViewPager mViewPager;

    /**
     * 数据
     */
    private List<Crime> mCrimeList;



    @Override
    protected void initVariables() {
        mCrimeList = CrimeLab.get(this).getCrimeList();
    }
    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_crime_pager);
        mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return onClickItem(position);
            }
            @Override
            public int getCount() {
                return  mCrimeList.size();
            }
        });
    }

    private Fragment onClickItem(int position) {
        Crime crime = mCrimeList.get(position);
        return CrimeFragment.newInstance(crime.getId());
    }

    @Override
    protected void loadData() {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        for (int i = 0; i < mCrimeList.size(); i++)
        {
            if (mCrimeList.get(i) != null)
            {
                if (mCrimeList.get(i).getId().equals(crimeId))
                {
                    mViewPager.setCurrentItem(i);
                    break;
                }
            }
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {

    }

    public static Intent newIntent(Context packageContext, UUID crimeId)
    {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }
}
