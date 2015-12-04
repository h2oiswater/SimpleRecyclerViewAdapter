package com.lhave.yuebar.ui.adapter;

import android.content.Context;
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

    Context fakerContext;

    public BaseRecyclerAdapter(int layoutResID){
        mLayoutResID = layoutResID;
    }

    public void addDatas(List<T> list, boolean clean){
        if( mDatas == null ) mDatas = new ArrayList<>();

        if( clean ) mDatas.clear();

        mDatas.addAll(list);

        notifyDataSetChanged();
    }
    public void setDatas(List<T> list){
        mDatas = list;
        notifyDataSetChanged();
    }

    public List<T> getDatas(){
        return mDatas;
    }

    @Override
    public VirtualViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        fakerContext = parent.getContext();
        return new VirtualViewHolder<T>(LayoutInflater.from(parent.getContext()).inflate(getLayoutByType(viewType), parent, false), this, viewType);
    }

    @Override
    public void onBindViewHolder(VirtualViewHolder holder, int position) {
        holder.bind(mDatas.get(position), position);
    }

    abstract void onBindingViewHolder(View v, T data, int position, int type);
    abstract void onInitViewHolder(VirtualViewHolder vh, View v);
    protected void onItemClicked(T data, int pos, RecyclerView.Adapter adapter){}
    protected int getLayoutByType(int position){
        return mLayoutResID;
    }

    @Override
    public int getItemCount() {
        return mDatas==null?0:mDatas.size();
    }

    public static class VirtualViewHolder<T> extends RecyclerView.ViewHolder{

        private BaseRecyclerAdapter mBindAdapter;
        private T mData;
        private View mItemView;
        private int mPosition;
        private int mType;

        public VirtualViewHolder(View itemView, BaseRecyclerAdapter adapter, int viewType) {
            super(itemView);
            mBindAdapter = adapter;
            mItemView = itemView;
            mType = viewType;
            init();
        }

        public void init(){
            mBindAdapter.onInitViewHolder(this, mItemView);
        }

        public void bind(T data, int position){
            mData = data;
            mBindAdapter.onBindingViewHolder(mItemView, data, position, mType);
            mPosition = position;
            mItemView.setOnClickListener(new OnItemClickListener(mData, position, mBindAdapter));
        }

        public T getData(){
            return  mData;
        }
        public View getItemView(){
            return  mItemView;
        }

        public int getItemPosition(){
            return  mPosition;
        }
        public int getItemType(){
            return  mType;
        }
    }

    private static class OnItemClickListener<T> implements View.OnClickListener{

        T mData;
        int mPosition;
        BaseRecyclerAdapter mAdapter;

        public OnItemClickListener(T data,int pos,BaseRecyclerAdapter adapter){
            this.mData = data;
            this.mPosition = pos;
            this.mAdapter = adapter;
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemClicked(mData, mPosition, mAdapter);
        }
    }

    protected void deleteItem(int itemPosition) {
        mDatas.remove(itemPosition);
        notifyDataSetChanged();
    }
}