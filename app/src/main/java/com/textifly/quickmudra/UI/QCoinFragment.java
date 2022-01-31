package com.textifly.quickmudra.UI;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.textifly.quickmudra.Activity.DetailsListActivity;
import com.textifly.quickmudra.ManageSharedPreferenceData.YoDB;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Constants;
import com.textifly.quickmudra.databinding.FragmentQCoinBinding;

public class QCoinFragment extends Fragment {
    FragmentQCoinBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentQCoinBinding.inflate(inflater,container,false);
        if(YoDB.getPref().read(Constants.isFullyDocumented,"").equals("false")){
            startActivity(new Intent(getActivity(), DetailsListActivity.class));
            getActivity().overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
            getActivity().finish();
        }
        if(YoDB.getPref().read(Constants.QCOIN,"").equals("null")){
            binding.tvTotalCoin.setText("Total Coins: 0");
        }else{
            binding.tvTotalCoin.setText("Total Coins: "+YoDB.getPref().read(Constants.QCOIN,""));
        }
        return binding.getRoot();
    }
}