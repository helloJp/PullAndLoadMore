package com.jp.pullandloadmorelibrary.header;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.jp.pullandloadmorelibrary.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import in.srain.cube.views.ptr.indicator.PtrTensionIndicator;

/**
 * Created by jiangp on 14/02/2017.
 * Desc:下拉刷新头部动画 布局
 */

public class RefreshHeaderView extends FrameLayout implements PtrUIHandler {

    private ImageView mSeparateIv;
    private ImageView mLoopIv;

    private PtrFrameLayout mPtrFrameLayout;
    private PtrTensionIndicator mPtrTensionIndicator;

    public RefreshHeaderView(Context context) {
        super(context);
        initView();
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setUp(PtrFrameLayout ptrFrameLayout) {
        mPtrFrameLayout = ptrFrameLayout;
        mPtrTensionIndicator = new PtrTensionIndicator();
        mPtrFrameLayout.setPtrIndicator(mPtrTensionIndicator);
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.layout_refresh_header, this);
        mSeparateIv = (ImageView) view.findViewById(R.id.separate_iv);
        mLoopIv = (ImageView) view.findViewById(R.id.loop_iv);
        initLoopAnimation(mLoopIv);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {

    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        switch (status) {
            default:
            case PtrFrameLayout.PTR_STATUS_INIT:
                setLoadingStatus(false);
                mSeparateIv.setImageDrawable(getResources().getDrawable(R.mipmap.refresh_1));
                break;
            case PtrFrameLayout.PTR_STATUS_COMPLETE:
                calculateMergeProgress(ptrIndicator);
                break;
            case PtrFrameLayout.PTR_STATUS_PREPARE:
                calculateSeparateProgress(ptrIndicator);
                break;
            case PtrFrameLayout.PTR_STATUS_LOADING:
                setLoadingStatus(true);
                break;
        }

    }


    /**
     * 计算分离的进度
     *
     * @param ptrIndicator
     */
    private void calculateSeparateProgress(PtrIndicator ptrIndicator) {

        float percent = ptrIndicator.getCurrentPercent();
        int offset = ptrIndicator.getCurrentPosY();

        setLoadingStatus(false);

        if (percent > 0.75) {
            //拉倒头布局只剩1/4的时候才触发 动画正好出去一半布局的下面一半处

            float ping = getMeasuredHeight() / 20;
            float height = offset - (getMeasuredHeight() * 3) / 4;

            int times = (int) (height / ping);
            //log(" offset:" + offset + " percent:" + percent + " times:" + times);
            switch (times) {
                case 0:
                    mSeparateIv.setImageResource(R.mipmap.refresh_1);
                    break;
                case 1:
                    mSeparateIv.setImageResource(R.mipmap.refresh_2);
                    break;
                case 2:
                    mSeparateIv.setImageResource(R.mipmap.refresh_3);
                    break;
                case 3:
                    mSeparateIv.setImageResource(R.mipmap.refresh_4);
                    break;
                case 4:
                    mSeparateIv.setImageResource(R.mipmap.refresh_5);
                    break;
                case 6:
                    mSeparateIv.setImageResource(R.mipmap.refresh_6);
                    break;
            }
        }
    }

    /**
     * 计算合并的进度
     *
     * @param ptrIndicator
     */
    private void calculateMergeProgress(PtrIndicator ptrIndicator) {

        float percent = ptrIndicator.getCurrentPercent();
        int offset = ptrIndicator.getCurrentPosY();

        setLoadingStatus(false);

        if (percent <= 0.75) {
            //拉倒头布局只剩1/4的时候才触发 动画正好出去一半布局的下面一半处

            float ping = getMeasuredHeight() / 20;
            float height = offset - (getMeasuredHeight() * 3) / 4;

            int times = Math.abs((int) (height / ping));
            switch (times) {
                case 0:
                    mSeparateIv.setImageResource(R.mipmap.refresh_7);
                    break;
                case 1:
                    mSeparateIv.setImageResource(R.mipmap.refresh_5);
                    break;
                case 2:
                    mSeparateIv.setImageResource(R.mipmap.refresh_3);
                    break;
                case 3:
                    mSeparateIv.setImageResource(R.mipmap.refresh_2);
                    break;
                case 4:
                    mSeparateIv.setImageResource(R.mipmap.refresh_1);
                    break;
            }
        }
    }


    private void setLoadingStatus(boolean isLoading) {
        if (isLoading) {
            mSeparateIv.setVisibility(GONE);
            mLoopIv.setVisibility(VISIBLE);
            AnimationDrawable anim = (AnimationDrawable) mLoopIv.getDrawable();
            if (!anim.isRunning()) anim.start();
        } else {
            mLoopIv.setVisibility(GONE);
            mSeparateIv.setVisibility(VISIBLE);
        }
    }

    //为了适配6.0以上和6.0以下系统
    private void initLoopAnimation(ImageView imageView) {
        AnimationDrawable animDrawable = new AnimationDrawable();
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_8), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_9), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_10), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_11), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_12), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_13), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_14), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_15), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_16), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_17), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_18), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_19), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_20), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_21), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_22), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_23), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_24), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_1), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_2), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_3), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_4), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_5), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_6), 50);
        animDrawable.addFrame(getContext().getResources().getDrawable(R.mipmap.refresh_7), 50);
        animDrawable.setOneShot(false);
        imageView.setImageDrawable(animDrawable);
    }


}
