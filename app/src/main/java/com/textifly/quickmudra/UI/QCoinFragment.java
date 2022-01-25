package com.textifly.quickmudra.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.textifly.quickmudra.R;
import com.textifly.quickmudra.databinding.FragmentQCoinBinding;

public class QCoinFragment extends Fragment {
    FragmentQCoinBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentQCoinBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }
}