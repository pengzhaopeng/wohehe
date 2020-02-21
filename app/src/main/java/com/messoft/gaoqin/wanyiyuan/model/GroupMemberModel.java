package com.messoft.gaoqin.wanyiyuan.model;

import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.AgentMemberInfoDetail;
import com.messoft.gaoqin.wanyiyuan.bean.MemberInfoList;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;

import org.json.JSONObject;

import java.util.List;

public class GroupMemberModel {

    /**
     * 团队列表查询
     */
    public void getMemberInfoList(BaseActivity context,
                                              String recommenderMobile,
                                              String pageType,
                                              int pageNo,
                                              int pageSize, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (recommenderMobile != null) {
                jsonObject.put("recommenderMobile", recommenderMobile);
            }
            if (pageType != null) {
                jsonObject.put("pageType", pageType);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getMemberInfoList(StringUtils.toURLEncoderUTF8(jsonObject.toString()), pageNo, pageSize)
//        HttpClient.Builder.getWYYServer().getMemberInfoList("", pageNo, pageSize)
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<MemberInfoList>>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(List<MemberInfoList> login) {
                        if (login != null) {
                            listener.loadSuccess(login);
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 代理商详情查询
     */
    public void getAgentMemberInfoDetail(BaseActivity context,
                                  String memberId,
                                         final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (memberId != null) {
                jsonObject.put("memberId", memberId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getAgentMemberInfoDetail(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
//        HttpClient.Builder.getWYYServer().getMemberInfoList("", pageNo, pageSize)
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<AgentMemberInfoDetail>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(AgentMemberInfoDetail login) {
                        if (login != null) {
                            listener.loadSuccess(login);
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }
}
