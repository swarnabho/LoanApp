package com.textifly.quickmudra.UI.ActivityPage.LoanRequest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.textifly.quickmudra.R;
import com.textifly.quickmudra.databinding.FragmentLoanRequestBinding;

public class LoanRequestFragment extends Fragment {
    FragmentLoanRequestBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoanRequestBinding.inflate(inflater,container,false);
        loadLoanRequest();
        return binding.getRoot();
    }

    private void loadLoanRequest() {

    }
}