package com.lhave.yuebar.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by gongjiangpeng on 15/10/14.
 */
public abstract class BaseDuoItemRecyclerAdapter extends RecyclerView.Adapter<BaseDuoItemRecyclerAdapter.VirtualViewHolder> {

    public BaseDuoItemRecyclerAdapter(){

    }

    public interface OnRecyclerViewItemClickedListener{
        void onItemClicked(Object data, int pos, RecyclerView.Adapter adapter);
    }

    private OnRecyclerViewItemClickedListener mOnRecyclerViewItemClickedListener;
    public void setOnItemClickListener(OnRecyclerViewItemClickedListener listener){
        mOnRecyclerViewItemClickedListener = listener;
    }

    @Override
    public VirtualViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VirtualViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutByType(viewType), parent, false), this, viewType);
    }

    @Override
    public void onBindViewHolder(VirtualViewHolder holder, int position) {
        holder.bind(getDataByPosition(position), position);
    }

    protected abstract void onBindingViewHolder(View v, Object data, int position, int type);
    protected abstract void onInitViewHolder(VirtualViewHolder vh, View v);
    protected void onItemClicked(Object data, int pos, RecyclerView.Adapter adapter){
        if(mOnRecyclerViewItemClickedListener!=null)
            mOnRecyclerViewItemClickedListener.onItemClicked(data,pos,adapter);
    }
    protected abstract int getLayoutByType(int viewType);
    protected abstract Object getDataByPosition(int position);

    public static class VirtualViewHolder extends RecyclerView.ViewHolder{

        private BaseDuoItemRecyclerAdapter mBindAdapter;
        private Object mData;
        private View mItemView;
        private int mPosition;
        private int mType;

        public VirtualViewHolder(View itemView, BaseDuoItemRecyclerAdapter adapter, int viewType) {
            super(itemView);
            mBindAdapter = adapter;
            mItemView = itemView;
            mType = viewType;
            init();
        }

        public void init(){
            mBindAdapter.onInitViewHolder(this, mItemView);
        }

        public void bind(Object data, int position){
            mData = data;
            mBindAdapter.onBindingViewHolder(mItemView, data, position, mType);
            mPosition = position;
            mItemView.setOnClickListener(new OnItemClickListener(mData, position, mBindAdapter));
        }

        public Object getData(){
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

    private static class OnItemClickListener implements View.OnClickListener{

        Object mData;
        int mPosition;
        BaseDuoItemRecyclerAdapter mAdapter;

        public OnItemClickListener(Object data,int pos,BaseDuoItemRecyclerAdapter adapter){
            this.mData = data;
            this.mPosition = pos;
            this.mAdapter = adapter;
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemClicked(mData, mPosition, mAdapter);
        }
    }
}