package com.messoft.gaoqin.wanyiyuan.ui.market;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.RelativeLayout;

import com.messoft.gaoqin.wanyiyuan.R;


public class MenuItemView {

    private View mView;
    private RelativeLayout mCreateGroupLl;
    private RelativeLayout mAddFriendLl;
    private RelativeLayout mSendMsgLl;

    public MenuItemView(View view) {
        this.mView = view;
    }

    @SuppressLint("CutPasteId")
    public void initModule() {
        mCreateGroupLl = mView.findViewById(R.id.rl_sm);
        mAddFriendLl = mView.findViewById(R.id.ll_cg);
        mSendMsgLl = mView.findViewById(R.id.ll_gs);

    }

    public void setListeners(View.OnClickListener listener) {
        mCreateGroupLl.setOnClickListener(listener);
        mAddFriendLl.setOnClickListener(listener);
        mSendMsgLl.setOnClickListener(listener);
    }

    public void showAddFriendDirect() {
        mAddFriendLl.setVisibility(View.GONE);
    }

    public void showAddFriend() {
        mAddFriendLl.setVisibility(View.VISIBLE);
    }

    public void setColor() {

    }
}
