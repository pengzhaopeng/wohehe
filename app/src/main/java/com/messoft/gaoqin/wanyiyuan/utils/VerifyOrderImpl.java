package com.messoft.gaoqin.wanyiyuan.utils;

public interface VerifyOrderImpl {
    void verifyResult(boolean reuslt);
    void loadFailed(int errorCode, String errorMessage);
}
