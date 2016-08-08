package cn.ucai.fulicenter.activity.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.adapter.ExpandableAdapter;
import cn.ucai.fulicenter.activity.bean.CategoryChildBean;
import cn.ucai.fulicenter.activity.bean.CategoryGroupBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.super_activity.BaseActivity;
import cn.ucai.fulicenter.utils.F;
import cn.ucai.fulicenter.utils.Utils;

public class CategoryFragment3 extends Fragment {
    public String TAG = CategoryFragment3.class.getSimpleName();
    View mView;
    Context mContext;
    ExpandableListView mExpandable;
    SwipeRefreshLayout mSwipe;
    ArrayList<CategoryGroupBean> mMaxList;//大类型
    ArrayList<ArrayList<CategoryChildBean>> mMinList;//小类型
    ExpandableAdapter mAdapter;


    @Override

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        mSwipe = (SwipeRefreshLayout) mView.findViewById(R.id.swipeCategory);
        initView();
        setListener();
        initData();
    }

    Timer mTimer;
    int i = 0;

    private void initData() {
        final OkHttpUtils2<CategoryGroupBean[]> utils = new OkHttpUtils2<CategoryGroupBean[]>();
        utils.setRequestUrl(F.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroupBean[].class)
                .execute(new OkHttpUtils2.OnCompleteListener<CategoryGroupBean[]>() {
                    @Override
                    public void onSuccess(CategoryGroupBean[] group) {
                        Log.i("main", "胡邦茂=" + group[1]);
                        if (group.length == 0) {
                            return;
                        }
                        mMaxList = utils.array2List(group);
                        mAdapter.updateMax(mMaxList);
                        mTimer = new Timer();
                        mTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                downChildData(mMaxList.get(i).getId());
                                i++;
                            }
                        }, 0, 1500);
                        Log.i("main", "线程2已开启=");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mTimer.cancel();
                            }
                        }, 12000);

                    }

                    @Override
                    public void onError(String error) {

                    }
                });

    }


    private void downChildData(int maxId) {
        final OkHttpUtils2<CategoryChildBean[]> utils1 = new OkHttpUtils2<CategoryChildBean[]>();
        utils1.setRequestUrl(F.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(F.CategoryChild.PARENT_ID, maxId + "")
                .addParam(F.PAGE_ID, 1 + "")
                .addParam(F.PAGE_SIZE, +F.PAGE_SIZE_DEFAULT + "")
                .targetClass(CategoryChildBean[].class)
                .execute(new OkHttpUtils2.OnCompleteListener<CategoryChildBean[]>() {
                    @Override
                    public void onSuccess(CategoryChildBean[] child) {
                        if (child.length == 0) {
                            return;
                        }
                        mAdapter.updateMin(utils1.array2List(child));
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private void initView() {
        mMaxList = new ArrayList<CategoryGroupBean>();
        mMinList = new ArrayList<ArrayList<CategoryChildBean>>();
        mExpandable = (ExpandableListView) mView.findViewById(R.id.expandableCategory);
        mAdapter = new ExpandableAdapter(mContext, mMaxList, mMinList);
        mExpandable.setAdapter(mAdapter);
        mExpandable.setGroupIndicator(null);

    }

    private void setListener() {
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.listClear();
                initData();
                mSwipe.setRefreshing(false);

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return mView = inflater.inflate(R.layout.category_fragment3, container, false);
    }

}