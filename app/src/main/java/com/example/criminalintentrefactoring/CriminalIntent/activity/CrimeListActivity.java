package com.example.criminalintentrefactoring.CriminalIntent.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.criminalintentrefactoring.CriminalIntent.entity.Crime;
import com.example.criminalintentrefactoring.CriminalIntent.fragment.CrimeFragment;
import com.example.criminalintentrefactoring.CriminalIntent.fragment.CrimeListFragment;
import com.example.criminalintentrefactoring.R;

/**
 * Created by 离子态狍子 on 2016/9/30.
 */

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null)
        {
            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
