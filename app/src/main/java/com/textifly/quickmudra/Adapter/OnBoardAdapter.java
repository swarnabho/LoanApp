package com.textifly.quickmudra.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.textifly.quickmudra.Model.OnBoardModel;
import com.textifly.quickmudra.databinding.RowOnboardBinding;

import java.util.List;

public class OnBoardAdapter extends SliderViewAdapter<OnBoardAdapter.HomeSliderVH>{
    List<OnBoardModel> modelList;
    Context context;

    public OnBoardAdapter(List<OnBoardModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @Override
    public HomeSliderVH onCreateViewHolder(ViewGroup parent) {
        return new HomeSliderVH(RowOnboardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(HomeSliderVH viewHolder, int position) {
        viewHolder.bind(position);
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    public class  HomeSliderVH extends SliderViewAdapter.ViewHolder {
        RowOnboardBinding rowOnboardBinding;
        public HomeSliderVH(RowOnboardBinding binding) {
            super(binding.getRoot());
            rowOnboardBinding = binding;
        }

        public void bind(int position) {

            Glide.with(itemView).load(modelList.get(position).getImage()).into(rowOnboardBinding.ivImage);
            rowOnboardBinding.tvDetails.setText(modelList.get(position).getDesc());
        }
    }
}
