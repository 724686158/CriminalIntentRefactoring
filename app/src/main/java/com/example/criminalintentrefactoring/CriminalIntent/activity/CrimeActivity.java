package com.example.criminalintentrefactoring.CriminalIntent.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.criminalintentrefactoring.CriminalIntent.fragment.CrimeFragment;

import java.util.UUID;

/**
 * Created by 离子态狍子 on 2016/9/30.
 */

public class CrimeActivity extends SingleFragmentActivity {
    public static final String EXTRA_CRIME_ID = "com.example.criminalintentrefactoring.crime_id";


    public static Intent newIntent(Context packageContext, UUID crime_id)
    {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crime_id);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }
}
