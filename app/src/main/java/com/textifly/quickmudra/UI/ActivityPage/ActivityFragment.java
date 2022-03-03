package com.textifly.quickmudra.UI.ActivityPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.textifly.quickmudra.MainActivity;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.UI.ActivityPage.Adapter.ViewPagerAdapter;
import com.textifly.quickmudra.UI.ActivityPage.LoanRequest.LoanRequestFragment;
import com.textifly.quickmudra.UI.ActivityPage.OpenLoan.OpenLoanFragment;
import com.textifly.quickmudra.UI.ActivityPage.RepaidLoan.RepaidLoanFragment;
import com.textifly.quickmudra.databinding.FragmentActivityBinding;

public class ActivityFragment extends Fragment implements View.OnClickListener{
    FragmentActivityBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentActivityBinding.inflate(inflater,container,false);
        setViewPager();
        binding.llMenu.setOnClickListener(this);
        return binding.getRoot();

    }

    private void setViewPager() {
        binding.tabLayout.setupWithViewPager(binding.viewPager);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        // add your fragments
        adapter.addFrag(new OpenLoanFragment(), "Open Loan");
        adapter.addFrag(new RepaidLoanFragment(), "Repaid Loan");
        adapter.addFrag(new LoanRequestFragment(), "Loan Request");

        binding.viewPager.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llMenu:
                ((MainActivity)getActivity()).openDrawer();
                break;
        }
    }
}