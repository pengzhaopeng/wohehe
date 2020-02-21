package com.messoft.gaoqin.wanyiyuan.utils;

import java.util.List;

/**
* List相关的工具方法
* @author 鹏鹏鹏先森
* created at 2018/10/17 15:54
*/

public class ListUtils {

    public static boolean isEmpty(List list){
        if (list == null){
            return true;
        }
        return list.size() == 0;
    }
}
