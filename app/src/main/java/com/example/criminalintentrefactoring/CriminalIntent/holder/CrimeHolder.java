package com.example.criminalintentrefactoring.CriminalIntent.holder;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.criminalintentrefactoring.CriminalIntent.entity.Crime;
import com.example.criminalintentrefactoring.CriminalIntent.fragment.CrimeListFragment;
import com.example.criminalintentrefactoring.R;

/**
 * Created by 离子态狍子 on 2016/10/1.
 */

public class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    /**
     * 接口
     */
    private CrimeListFragment.Callbacks mCallbacks;

    /**
     * 组件
     */
    private TextView mTitleTextView;
    private TextView mDateTextView;
    private CheckBox mSolvedCheckBox;
    /**
     * 数据
     */
    private Crime mCrime;



    public CrimeHolder(View itemView, CrimeListFragment.Callbacks callbacks) {
        super(itemView);
        itemView.setOnClickListener(this);
        mCallbacks = callbacks;
        mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
        mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
        mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
    }

    public void bindCrime(Crime crime)
    {
        mCrime = crime;
        mTitleTextView.setText(mCrime.getTitle());
        mDateTextView.setText((DateFormat.format("yyyy-MM-dd HH:mm:ss", mCrime.getDate())));
        mSolvedCheckBox.setChecked(mCrime.isSolved());

    }

    @Override
    public void onClick(View v) {
        mCallbacks.onCrimeSelected(mCrime);
    }
}