package com.study.android.earlymap.SeeListAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.study.android.earlymap.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tatsuya on 2018/03/31.
 */

public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.ViewHolder> {

    private List<RouteItemView>  list;
    private View.OnClickListener onListClickListener;
    private LayoutInflater layoutInflater;

    public RouteListAdapter(Context context,List<RouteItemView> list){
        this.list=list;
        layoutInflater=LayoutInflater.from(context);
    }

    public void setListClickListener(View.OnClickListener onClickListener){
        onListClickListener=onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate= layoutInflater.inflate(R.layout.route_item_layout,parent,false);
        if(onListClickListener!=null){
            inflate.setOnClickListener(onListClickListener);
        }
        ViewHolder viewHolder=new ViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final RouteItemView listItem=list.get(position);
        holder.desText.setText(listItem.getDestination());
        holder.timeText.setText(listItem.getTime());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView desText;
        TextView timeText;
        public ViewHolder(View itemView){
            super(itemView);
            desText=itemView.findViewById(R.id.des_name);
            timeText=itemView.findViewById(R.id.take_time);
        }
    }

    public void refreshItem(ArrayList<RouteItemView> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public RouteItemView getItem(int position){
        return list.get(position);
    }
}
