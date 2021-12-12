package com.textifly.quickmudra.UI;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.textifly.quickmudra.MainActivity;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements View.OnClickListener {
    FragmentHomeBinding binding;
    private boolean backPress = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        BtnClick();
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
        return binding.getRoot();
    }

    @Override
    public void onStop() {
        super.onStop();
        callback.setEnabled(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        callback.setEnabled(true);
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            Log.e("Home Fragment== ", "Called");
            if (backPress){
                //ManageLoginData.clearLoginData(); // Logout(Temporary)
                getActivity().finish();
            }else {
                Toast.makeText(getActivity(), "Press again to exit", Toast.LENGTH_SHORT).show();
            }
            pressTimer();
            backPress = true;
        }
    };

    private void pressTimer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPress = false;
            }
        }, 2000);
    }

    private void BtnClick() {
        binding.llMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llMenu:
                ((MainActivity) getActivity()).openDrawer();
                break;
        }
    }
}