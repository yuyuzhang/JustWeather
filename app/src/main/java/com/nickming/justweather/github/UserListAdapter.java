package com.nickming.justweather.github;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nickming.justweather.R;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 *
 * @author:nickming date:2016/4/20
 * time: 10:39
 * e-mail：962570483@qq.com
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder>{

    protected List<UserBean> mdatas=new ArrayList<>();
    protected UserClickCallback clickCallback;

    public UserListAdapter(UserClickCallback clickCallback) {
        this.clickCallback = clickCallback;
    }

    public void addUser(UserBean bean)
    {
        mdatas.add(bean);
        notifyItemInserted(mdatas.size()-1);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.view_user_list_item,parent,false);
        return new UserViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, final int position) {
        holder.mHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCallback!=null)
                    clickCallback.onItemClicked(mdatas.get(position).avatar_url);
            }
        });
        holder.bindTo(mdatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mdatas.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder
    {

        TextView mName;
        ImageView mHead;

        public UserViewHolder(View itemView) {
            super(itemView);
            mName= (TextView) itemView.findViewById(R.id.tv_user_name);
            mHead= (ImageView) itemView.findViewById(R.id.iv_user_head);
        }

        public void bindTo(UserBean bean)
        {
            mName.setText(bean.name);
            Glide.with(mHead.getContext()).load(bean.avatar_url).into(mHead);
        }
    }

    // 点击的回调
    public interface UserClickCallback {
        void onItemClicked(String name);
    }

}
