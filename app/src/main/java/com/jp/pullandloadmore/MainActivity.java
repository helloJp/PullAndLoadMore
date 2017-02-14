package com.jp.pullandloadmore;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.ViewGroup;

import com.jp.pullandloadmore.dao.Person;
import com.jp.pullandloadmore.utils.Util;
import com.jp.pullandloadmore.viewholder.PersonViewHolder;
import com.jp.pullandloadmorelibrary.recyclerview.XRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class MainActivity extends AppCompatActivity implements RecyclerArrayAdapter.OnLoadMoreListener {
    private XRecyclerView mRecyclerView;
    private RecyclerArrayAdapter<Person> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    protected void initView() {
        mRecyclerView = (XRecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(this, 0.5f), Util.dip2px(this, 72), 0);
        itemDecoration.setDrawLastItem(false);
        mRecyclerView.addItemDecoration(itemDecoration);
//        mRecyclerView.getPtrLayout().setLastUpdateTimeRelateObject(this);

        //参数2 每页最大item数量 默认（不填） 为10
        mRecyclerView.setAdapterWithProgress(adapter = new RecyclerArrayAdapter<Person>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new PersonViewHolder(parent);
            }
        }, 5);

        //空态页面
        mRecyclerView.setEmptyView(R.layout.view_empty);

        //加载更多
        adapter.setMore(R.layout.view_more, this);
        adapter.setNoMore(R.layout.view_nomore);
        adapter.setOnItemLongClickListener(new RecyclerArrayAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(int position) {
                adapter.remove(position);
                return true;
            }
        });
        adapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                adapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                adapter.resumeMore();
            }
        });

        //下拉刷新
        mRecyclerView.setRefreshListener(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                onRefresh(frame);
            }
        });

        mRecyclerView.autoRefresh();

    }

    private void onRefresh(PtrFrameLayout frame) {
        page = 1;
        adapter.clear();
        List<Person> list = DataProvider.getPersonList(page);
        while (list.size() > 4) list.remove(0);

        adapter.addAll(list);
        page++;
    }

    private int page = 1;

    @Override
    public void onLoadMore() {
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.addAll(DataProvider.getPersonList(page));
                page++;
            }
        }, 2000);
    }

    private void log(String msg) {
        if (msg == null) return;
        Log.d(MainActivity.this.getClass().getName(), msg);
    }

}
