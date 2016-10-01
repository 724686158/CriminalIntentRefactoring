package com.example.criminalintentrefactoring.CriminalIntent.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.example.criminalintentrefactoring.AndroidLib.activity.BaseActivity;
import com.example.criminalintentrefactoring.CriminalIntent.entity.Crime;
import com.example.criminalintentrefactoring.CriminalIntent.fragment.CrimeFragment;
import com.example.criminalintentrefactoring.CriminalIntent.fragment.CrimeListFragment;
import com.example.criminalintentrefactoring.CriminalIntent.holder.CrimeHolder;
import com.example.criminalintentrefactoring.R;
import java.util.List;

/**
 * Created by 离子态狍子 on 2016/9/30.
 */

public class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

    private List<Crime> mCrimes;
    private CrimeListFragment.Callbacks mCallbacks;
    private FragmentActivity mFragmentActivity;

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public void setCrimes(List<Crime> crimes) {
        mCrimes = crimes;
    }

    public CrimeListFragment.Callbacks getCallbacks() {
        return mCallbacks;
    }

    public void setCallbacks(CrimeListFragment.Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    public FragmentActivity getFragmentActivity() {
        return mFragmentActivity;
    }

    public void setFragmentActivity(FragmentActivity fragmentActivity) {
        mFragmentActivity = fragmentActivity;
    }

    public CrimeAdapter(FragmentActivity fragmentActivity, CrimeListFragment.Callbacks callbacks, List<Crime> crimes) {
        mFragmentActivity = fragmentActivity;
        mCallbacks = callbacks;
        mCrimes = crimes;
    }

    @Override
    public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mFragmentActivity);
        View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
        return new CrimeHolder(view, mCallbacks);
    }

    @Override
    public void onBindViewHolder(CrimeHolder holder, int position) {
        Crime crime = mCrimes.get(position);

        holder.bindCrime(crime);

    }

    @Override
    public int getItemCount() {
        return mCrimes.size();
    }

    public void setCrimeList(List<Crime> crimeList)
    {
        mCrimes = crimeList;
    }
}
