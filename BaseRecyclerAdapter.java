package com.lhave.yuebar.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gongjiangpeng on 15/10/14.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerAdapter.VirtualViewHolder> {

    List<T> mDatas;

    int mLayoutResID;

    public BaseRecyclerAdapter(int layoutResID){
        mLayoutResID = layoutResID;
    }

    public void addDatas(List<T> list, boolean clean){
        if( mDatas == null ) mDatas = new ArrayList<T>();

        if( clean ) mDatas.clear();

        mDatas.addAll(list);

        notifyDataSetChanged();
    }

    @Override
    public VirtualViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VirtualViewHolder<T>(LayoutInflater.from(parent.getContext()).inflate(mLayoutResID, parent, false), this);
    }

    @Override
    public void onBindViewHolder(VirtualViewHolder holder, int position) {
        holder.bind(mDatas.get(position), position);
    }

    abstract void onBindingViewHolder(View v, T data, int position);
    abstract void onInitViewHolder(VirtualViewHolder vh, View v);

    @Override
    public int getItemCount() {
        return mDatas==null?0:mDatas.size();
    }

    public static class VirtualViewHolder<T> extends RecyclerView.ViewHolder{

        BaseRecyclerAdapter mBindAdapter;
        T mData;
        View mItemView;

        public VirtualViewHolder(View itemView,BaseRecyclerAdapter adapter) {
            super(itemView);
            mBindAdapter = adapter;
            mItemView = itemView;

            init();
        }

        public void init(){
            mBindAdapter.onInitViewHolder(this, mItemView);
        }

        public void bind(T data, int position){
            mData = data;
            mBindAdapter.onBindingViewHolder(mItemView, data, position);
        }

        public T getData(){
            return  mData;
        }
    }
}
