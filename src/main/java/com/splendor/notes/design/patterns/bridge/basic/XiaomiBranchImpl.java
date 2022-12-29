package com.splendor.notes.design.patterns.bridge.basic;

/**
 * @author splendor.s
 * @create 2022/12/28 下午4:39
 * @description 小米品牌
 */
public class XiaomiBranchImpl implements AbstractBranch {
    @Override
    public void open() {
        System.out.println("小米手机开机");
    }

    @Override
    public void call() {
        System.out.println("小米手机打电话");
    }

    @Override
    public void close() {
        System.out.println("小米手机关机");
    }
}
