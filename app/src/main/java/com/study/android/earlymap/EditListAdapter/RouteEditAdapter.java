package com.study.android.earlymap.EditListAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.study.android.earlymap.R;
import com.study.android.earlymap.SeeListAdapter.RouteItemView;

import java.util.List;

/**
 * Created by tatsuya on 2018/03/31.
 */

public class RouteEditAdapter extends RecyclerView.Adapter<RouteEditAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<RouteItemView> list;
    private View.OnLongClickListener onLongClickListener;

    public RouteEditAdapter(Context context, List<RouteItemView> list){
        layoutInflater=LayoutInflater.from(context);
        this.list=list;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener){
        this.onLongClickListener=onLongClickListener;
    }

    @NonNull
    @Override
    public RouteEditAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View inflate= layoutInflater.inflate(R.layout.edit_item_layout,parent,false);
        ViewHolder viewHolder=new ViewHolder(inflate);
        if(onLongClickListener!=null){
            inflate.setOnLongClickListener(onLongClickListener);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final RouteItemView listItem=list.get(position);
        holder.desName.setText(listItem.getDestination());

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView desName;
        public ViewHolder(View itemView){
            super(itemView);
            desName=itemView.findViewById(R.id.des_name_item);
        };
    }

    @Override
    public int getItemCount() {
        return list.size();  //必ず、list.size()と入れる
    }

    public void refreshItem(final List<RouteItemView> list){
        this.list=list;
        notifyDataSetChanged();
    }
}
