package com.example.criminalintentrefactoring.CriminalIntent.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.criminalintentrefactoring.AndroidLib.Fragment.BaseFragment;
import com.example.criminalintentrefactoring.CriminalIntent.engine.CrimeLab;
import com.example.criminalintentrefactoring.CriminalIntent.entity.Crime;
import com.example.criminalintentrefactoring.CriminalIntent.utils.PictureUtils;
import com.example.criminalintentrefactoring.R;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by 离子态狍子 on 2016/9/30.
 */

public class CrimeFragment extends BaseFragment {

    /**
     * 常量
     */
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHOTO = 2;
    private static final Intent PICK_CONTACT= new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
    private static final Intent CAPTURE_IMAGE = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    /**
     * 接口
     */
    private Callbacks mCallbacks;

    /**
     * 组件
     */
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mSuspectButton;
    private Button mReportButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    /**
     * 数据
     */
    private Crime mCrime;
    private File mPhotoFile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        initViews(inflater, container, savedInstanceState, view);
        loadData(inflater);
        return view;
    }

    @Override
    protected void initVariables() {
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
    }

    @Override
    protected void initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, View v) {
        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_camera);
        mPhotoView = (ImageView) v.findViewById(R.id.crime_photo);


        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doReport(v);
            }
        });
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                doTitleTextChange(s,start,before,count);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doChooseDate(v);
            }
        });

        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                doCheckedChanged(buttonView, isChecked);
            }
        });
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFindSuspect(v);
            }
        });
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTakePhoto(v);
            }
        });
    }
    /**
     * 处理结果
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
        {
            return;
        }else {
            if (requestCode == REQUEST_DATE)
            {
                doDateResult(data);
            } else if (requestCode == REQUEST_CONTACT) {
                doContactResult(data);

            } else if (requestCode == REQUEST_PHOTO) {
                doPhotoResult(data);
            }
        }
    }

    @Override
    protected void loadData(LayoutInflater inflater) {
        updateTitle();
        updateIsSolved();
        updateSuspect();
        updateDate();
    }



    /**
     * 处理 用户按下拍照按钮 后的业务
     */
    private void doTakePhoto(View v) {
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(PICK_CONTACT, packageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }
        boolean canTakePhoto = false;
        if (mPhotoFile != null && CAPTURE_IMAGE.resolveActivity(packageManager) != null)
        {
            canTakePhoto = true;
        }
        mPhotoButton.setEnabled(canTakePhoto);
        if (canTakePhoto)
        {
            Uri uri = Uri.fromFile(mPhotoFile);
            CAPTURE_IMAGE.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        startActivityForResult(CAPTURE_IMAGE, REQUEST_PHOTO);
    }

    /**
     * 处理 用户按下CHOOSE SUSPECT 后的业务
     */
    private void doFindSuspect(View v) {
        startActivityForResult(PICK_CONTACT, REQUEST_CONTACT);
    }

    /**
     * 处理 修改“是否完成”选择框 后的业务
     * @param view
     * @param checked
     */
    private void doCheckedChanged(CompoundButton view, boolean checked) {
        mCrime.setSolved(checked);
        updateCrime();
        //updateIsSolved();
    }

    /**
     * 处理 用户按下日期按钮 后的业务
     */
    private void doChooseDate(View v) {
        FragmentManager manager = getFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
        dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
        dialog.show(manager, DIALOG_DATE);
        updateCrime();
        updateDate();
    }

    /**
     * 处理 用户修改标题内容 后的业务
     * @param s
     * @param start
     * @param before
     * @param count
     */
    private void doTitleTextChange(CharSequence s, int start, int before, int count) {
        mCrime.setTitle(s.toString());
        updateCrime();
    }

    /**
     * 处理用户 按下SEND CRIME REPORT 后的业务
     */
    private void doReport(View v) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
        i = Intent.createChooser(i, getString(R.string.send_report));
        startActivity(i);
    }

    /**
     * 处理照片的结果
     */
    private void doPhotoResult(Intent data) {
        updateCrime();//更新Crime信息
        updatePhotoView();
    }

    /**
     * 处理联系人的结果
     */
    private void doContactResult(Intent data) {
        Uri contactUri = data.getData();
        String[] queryFields = new String[]{
                ContactsContract.Contacts.DISPLAY_NAME
        };

        Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
        try {
            if (c.getCount() == 0) {
                return;
            }

            c.moveToFirst();
            String suspect = c.getString(0);
            mCrime.setSuspect(suspect);
            mSuspectButton.setText(suspect);

        } finally {
            c.close();
        }

        updateCrime();
        updateSuspect();
    }

    /**
     * 处理日期的结果
     */
    private void doDateResult(Intent data) {
        Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
        mCrime.setDate(date);

        updateCrime();
        updateDate();
    }



    /**
     * 回调接口
     */
    public interface Callbacks {
        void onCrimeUpdated(Crime crime);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * 获取一个本对象的实例
     * @param crimeId
     * @return
     */
    public static CrimeFragment newInstance(UUID crimeId)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }





    /**
     * 向CrimeLab单例中更新Crime信息
     */
    private void updateCrime() {
        CrimeLab.get(getActivity()).updateCrime(mCrime);
        mCallbacks.onCrimeUpdated(mCrime);
    }


    /**
     *
     */
    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists())
        {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    /**
     * 返回报告
     * @return
     */
    private String getCrimeReport()
    {
        String solvedString = null;
        if (mCrime.isSolved())
        {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect = mCrime.getSuspect();
        if (suspect == null)
        {
            suspect = getString(R.string.crime_report_no_suspect);

        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);
        return report;
    }


    /**
     * 更新日期按钮上显示的信息
     */
    private void updateDate() {
        mDateButton.setText(DateFormat.format("yyyy-MM-dd", mCrime.getDate()));
    }

    /**
     * 更新嫌疑人按钮上显示的信息
     */
    private void updateSuspect() {
        if (mCrime.getSuspect() != null)
        {
            mSuspectButton.setText(mCrime.getSuspect());
        }
    }

    /**
     * 更新解决选择框上显示的信息
     */
    private void updateIsSolved() {
        mSolvedCheckBox.setChecked(mCrime.isSolved());
    }

    /**
     * 更新标题输入栏上显示的信息
     */
    private void updateTitle() {
        if (mCrime.getTitle() != null)
        {
            mTitleField.setText(mCrime.getTitle());
        }
    }
}
