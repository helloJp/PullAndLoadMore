package com.jp.pullandloadmorelibrary.recyclerview;

import android.support.v7.widget.RecyclerView;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

public class EasyDataObserver extends RecyclerView.AdapterDataObserver {
    private XRecyclerView recyclerView;
    private RecyclerArrayAdapter adapter;
    private final int DEFAULT_EACH_PAGE_ITEM_COUNT = 10;//默认每页加载条数为10
    private int mEachPageItemCount = DEFAULT_EACH_PAGE_ITEM_COUNT;

    public EasyDataObserver(XRecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        if (recyclerView.getAdapter() instanceof RecyclerArrayAdapter) {
            adapter = (RecyclerArrayAdapter) recyclerView.getAdapter();
        }
    }

    public EasyDataObserver(XRecyclerView recyclerView, int eachPageItemCount) {
        this(recyclerView);
        mEachPageItemCount = eachPageItemCount;
    }

    private boolean isHeaderFooter(int position) {
        return adapter != null && (position < adapter.getHeaderCount() || position >= adapter.getHeaderCount() + adapter.getCount());
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        super.onItemRangeChanged(positionStart, itemCount);
        if (!isHeaderFooter(positionStart)) {
            update();
        }
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        if (!isHeaderFooter(positionStart)) {
            update();
        }
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        super.onItemRangeRemoved(positionStart, itemCount);
        if (!isHeaderFooter(positionStart)) {
            update();
        }
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount);
        update();//header&footer不会有移动操作
    }

    @Override
    public void onChanged() {
        super.onChanged();
        update();//header&footer不会引起changed
    }


    //自动更改Container的样式
    private void update() {
        int count;
        if (recyclerView.getAdapter() instanceof RecyclerArrayAdapter) {
            count = ((RecyclerArrayAdapter) recyclerView.getAdapter()).getCount();
            //加载不是EACH_PAGE_COUNT整数倍,显示加载完成
            if (count % mEachPageItemCount != 0)
                ((RecyclerArrayAdapter) recyclerView.getAdapter()).stopMore();
        } else {
            count = recyclerView.getAdapter().getItemCount();
        }
        if (count == 0) {
            recyclerView.showEmpty();
        } else {
            recyclerView.showRecycler();
        }
    }
}
