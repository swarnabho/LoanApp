package com.textifly.quickmudra.UI;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.textifly.quickmudra.Activity.DetailsListActivity;
import com.textifly.quickmudra.MainActivity;
import com.textifly.quickmudra.ManageSharedPreferenceData.YoDB;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Constants;
import com.textifly.quickmudra.databinding.FragmentReferBinding;

public class ReferFragment extends Fragment implements View.OnClickListener{
    FragmentReferBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReferBinding.inflate(inflater,container,false);
        if(YoDB.getPref().read(Constants.isFullyDocumented,"").equals("false")){
            startActivity(new Intent(getActivity(), DetailsListActivity.class));
            getActivity().overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
            getActivity().finish();
        }
        BtnClick();
        return binding.getRoot();
    }

    private void BtnClick() {
        binding.llMenu.setOnClickListener(this);
        binding.tvShare.setOnClickListener(this);
        binding.tvReferalCode.setText(YoDB.getPref().read(Constants.REFERRAL_CODE,""));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.llMenu:
                ((MainActivity)getActivity()).openDrawer();
                break;
            case R.id.tvShare:
                startSharing();
                break;
        }
    }

    private void startSharing(){
        String msg = "Referral Code: "+binding.tvReferalCode.getText().toString()+"\nhttp://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Share");
        startActivity(shareIntent);
    }
}