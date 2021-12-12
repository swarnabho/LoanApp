package com.textifly.quickmudra;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.textifly.quickmudra.Adapter.DrawerAdapter;
import com.textifly.quickmudra.Model.DrawerModel;
import com.textifly.quickmudra.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private NavOptions.Builder navBuilder;

    private ActionBarDrawerToggle mDrawerToggle;

    List<DrawerModel> modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.baseColor));
        }
        setDefaultView();
        setDrawerMenu();
        //setVersion();
    }

    private void setDrawerMenu() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.rvMenu.setLayoutManager(layoutManager);

        /*int goldenColor = Color.parseColor("#F7DC5F");
        int whiteColor = Color.parseColor("#FFFFFF");*/

        modelList = new ArrayList<>();

        modelList.add(new DrawerModel(R.drawable.money, "Get Cash"));
        modelList.add(new DrawerModel(R.drawable.stretching, "Activity"));
        modelList.add(new DrawerModel(R.drawable.coins, "QCoins"));
        modelList.add(new DrawerModel(R.drawable.ic_friends, "Friends"));
        modelList.add(new DrawerModel(R.drawable.ic_refer, "Refer & Earn"));
        modelList.add(new DrawerModel(R.drawable.ic_help, "Help"));

        DrawerAdapter drawerMenuAdapter = new DrawerAdapter(modelList, this);
        binding.rvMenu.setAdapter(drawerMenuAdapter);

        drawerMenuAdapter.setListenerDrawerMenu(new OnDrawerMenuListener() {
            @Override
            public void onDrawerMenuClick(int pos) {
                Bundle bundle = new Bundle();
                switch (pos) {
                    case 0:
                        Log.e("Pos", "= 0");
                            /*DrawerAdapter.DrawerViewHolder.ivMenu.setImageTintList(DrawerAdapter.selected_postion == pos ? ColorStateList.valueOf(goldenColor) : ColorStateList.valueOf(whiteColor));
                            DrawerAdapter.DrawerViewHolder.tvMenu.setTextColor(DrawerAdapter.selected_postion == pos ? getResources().getColor(R.color.goldenColor) : getResources().getColor(R.color.white));*/
                            /*drawerMenuAdapter.notifyItemChanged(DrawerAdapter.selected_postion);
                            DrawerAdapter.selected_postion = pos;*/
                        //drawerMenuAdapter.notifyItemChanged(DrawerAdapter.selected_postion);

                        navController.navigate(R.id.navigation_home, bundle, navBuilder.build());//This will open
                        openDrawer();
                        break;

                    case 1:
                        Log.e("Pos", "= 1");
                        navController.navigate(R.id.navigation_home, bundle, navBuilder.build());//This will open
                        openDrawer();
                        break;

                    case 2:
                        Log.e("Pos", "= 2");
                            /*DrawerAdapter.DrawerViewHolder.ivMenu.setImageTintList(DrawerAdapter.selected_postion == pos ? ColorStateList.valueOf(goldenColor) : ColorStateList.valueOf(whiteColor));
                            DrawerAdapter.DrawerViewHolder.tvMenu.setTextColor(DrawerAdapter.selected_postion == pos ? getResources().getColor(R.color.goldenColor) : getResources().getColor(R.color.white));*/
                            /*drawerMenuAdapter.notifyItemChanged(DrawerAdapter.selected_postion);
                            DrawerAdapter.selected_postion = pos;*/
                        //drawerMenuAdapter.notifyItemChanged(DrawerAdapter.selected_postion);

                        //navController.navigate(R.id.navigation_book, bundle, navBuilder.build());//This will open
                        openDrawer();
                        break;

                    case 3:
                        Log.e("Pos", "= 3");
                        //navController.navigate(R.id.navigation_helpDesk, bundle, navBuilder.build());//This will open
                        openDrawer();
                        break;

                    case 4:
                        Log.e("Pos", "= 4");
                        navController.navigate(R.id.navigation_refer, bundle, navBuilder.build() );//This will open
                        //startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        //overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                        //finish();
                        openDrawer();
                        break;
                }
            }
        });
    }

    public void openDrawer() {
        if (!binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            binding.drawerLayout.openDrawer(Gravity.LEFT);
        } else {
            binding.drawerLayout.closeDrawer(Gravity.LEFT);
        }
    }


    private void setDefaultView() {
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(binding.appBarMain.bottomNav, navController);
        NavigationUI.setupWithNavController(binding.navView, navController);
        navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.fade_in_animation)
                .setExitAnim(R.anim.fade_out_animation)
                .setPopEnterAnim(R.anim.fade_in_animation)
                .setPopExitAnim(R.anim.fade_out_animation);

        /*mDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout,
                R.drawable.ic_dashboard_black_24dp, R.string.title_home) {*/
        mDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout,
                R.drawable.ic_dashboard_black_24dp, R.string.title_home) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // Do whatever you want here
                Log.e("onDrawerClosed= ", "Closed");
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Do whatever you want here
                Log.e("onDrawerOpened= ", "Closed");
            }
        };
        binding.drawerLayout.addDrawerListener(mDrawerToggle);
    }

    public interface OnDrawerMenuListener {
        void onDrawerMenuClick(int pos);
    }


}