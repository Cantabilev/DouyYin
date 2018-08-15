package org.cantabile.douyin.adapter.wrapper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.cantabile.douyin.R;
import org.cantabile.douyin.adapter.CommMusicAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 常规 RecyclerView 适配器
 * @author Created by You
 * @email 1269859055@qq.com
 * @data 2018/8/14
 **/
public abstract class CommRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private Context context;
    private List<T> list;

    public CommRecyclerAdapter(Context context) {
        this.context = context;
    }

    public CommRecyclerAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(context, LayoutInflater.from(context).inflate(getLayoutId(), parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        T t = list.get(position);
        convertView(holder, t, position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public Context getContext() {
        return context;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void add(T t){
        if (null == this.list)
            this.list = new ArrayList<>();
        list.add(t);
        notifyItemInserted(list.indexOf(t));
    }
    public void addAll(List<T> list){
        if (null == this.list)
            this.list = list;
        else
            this.list.addAll(list);
        notifyItemRangeInserted(this.list.size() - list.size(), list.size());
    }
    public void remove(T t){
        if (null == this.list)
            return;
        int pos = list.indexOf(t);
        if(-1 == pos)
            return;
        this.list.remove(pos);
        notifyItemRemoved(pos);
    }
    public void remove(int index){
        if (null == this.list || this.list.size()-1 > index)
            throw new RuntimeException("there target list is null or index out of bounds");
        this.list.remove(index);
        notifyItemRemoved(index);
    }

    /**
     * 获取资源视图
     * @return
     */
    public abstract int getLayoutId();

    public abstract void convertView(ViewHolder holder, T t, int position);
}
