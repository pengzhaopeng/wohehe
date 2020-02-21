package com.messoft.gaoqin.wanyiyuan.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2018/5/15 0015.
 */

public class MyFragmentPagerAdapterTwo extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragments;
    private List<String> mTitleList;

    public MyFragmentPagerAdapterTwo(FragmentManager fm, ArrayList<Fragment> fragments, List<String> mTitleList) {
        super(fm);
        this.mFragments = fragments;
        this.mTitleList = mTitleList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    public void setFragments(FragmentManager fm, ArrayList<Fragment> fragments, List<String> mTitles) {
        this.mTitleList = mTitles;
        if (this.mFragments != null) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : this.mFragments) {
                ft.remove(f);
            }
//            ft.commit();
            //报错
            ft.commitAllowingStateLoss();
            ft = null;
            fm.executePendingTransactions();
        }
        this.mFragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return !isNullOrEmpty(mTitleList) ? mTitleList.get(position) : "";
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    /**
     * 判断集合是否为null或者0个元素
     *
     * @param c
     * @return
     */
    public static boolean isNullOrEmpty(Collection c) {
        if (null == c || c.isEmpty()) {
            return true;
        }
        return false;
    }
}
