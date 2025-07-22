package com.example.aop;

/**
 * @ClassName UserContext
 * @Author sheng.lin
 * @Date 2025/7/22
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
public class UserContext {

    private static final ThreadLocal<Integer> localUserId = new ThreadLocal<>();



    /**
     * 设置
     * @param uid
     * @Author sheng.lin
     * @Date 2025/7/22
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    public static void setLocalUserId(Integer uid) {
        localUserId.set(uid);
    }


    /**
     * 获取
     * @param
     * @Author sheng.lin
     * @Date 2025/7/22
     * @return: java.lang.Integer
     * @Version  1.0
     * @修改记录
     */
    public static Integer getLocalUserId() {
        return localUserId.get();
    }


    /**
     * 移除
     * @param
     * @Author sheng.lin
     * @Date 2025/7/22
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    public static void clear() {
        localUserId.remove();
    }
}

