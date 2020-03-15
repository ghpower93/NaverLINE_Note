package com.example.park.noteped;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by HAN on 2018. 1. 8..
 */class CustomAdapter_read extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();

    // 필수 생성자
    CustomAdapter_read(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }



    @Override
    public int getItemPosition(Object object) {

        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


    // List에 Fragment를 담을 함수
    public void addItem(Fragment fragment) {
        fragments.add(fragment);
    }


    public Fragment setItem(int position , Fragment fragment) {
        return fragments.set(position,fragment);
    }

    public void remove(  int index) {
        fragments.remove(index);
        }

    public void remove(Fragment fragment) {

        fragments.remove(fragment);
    }




     void clear() {
        fragments.clear();
    }


}