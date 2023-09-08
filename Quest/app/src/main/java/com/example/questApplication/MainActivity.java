package com.example.questApplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.questApplication.Adapter.ViewPageAdapter;
import com.example.questApplication.fragment.DailyFragment;
import com.example.questApplication.fragment.DungeonFragment;
import com.example.questApplication.fragment.KnapsackFragment;
import com.example.questApplication.fragment.VillageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ViewPager2 viewPager2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager2 = findViewById(R.id.viewpager2_bottom);
        bottomNavigationView = findViewById(R.id.bottom_nav2);
        ViewPageAdapter myViewPager2BottomAdapter =
                new ViewPageAdapter(this, initFragmentList());
        viewPager2.setAdapter(myViewPager2BottomAdapter);
        viewPager2.setCurrentItem(1);
        bottomNavigationView.setSelectedItemId(R.id.daily);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            switch (itemId) {
                case R.id.dungeon:
                    viewPager2.setCurrentItem(0);
                    break;
                case R.id.daily:
                    viewPager2.setCurrentItem(1);
                    break;
                case R.id.village:
                    viewPager2.setCurrentItem(2);
                    break;
                case R.id.knapsack:
                    viewPager2.setCurrentItem(3);
                    break;
            }
            return true;
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.dungeon);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.daily);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.village);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.knapsack);
                        break;
                }
            }
        });
    }

    private List<Fragment> initFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new DungeonFragment());
        fragmentList.add(new DailyFragment());
        fragmentList.add(new VillageFragment());
        fragmentList.add(new KnapsackFragment());
        return fragmentList;
    }
}