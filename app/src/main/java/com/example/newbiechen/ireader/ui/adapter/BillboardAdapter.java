package com.example.newbiechen.ireader.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BillboardBean;
import com.example.newbiechen.ireader.utils.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by newbiechen on 17-4-23.
 */

public class BillboardAdapter extends BaseExpandableListAdapter {

    private List<BillboardBean> mGroups = new ArrayList<>();
    private List<BillboardBean> mChildren = new ArrayList<>();

    private Animation mOpenAnim;
    private Animation mCloseAnim;

    private Context mContext;
    private boolean isOpen = false;


    public BillboardAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == mGroups.size() - 1){
            return mChildren.size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        //只有最后一个groups才有child
        if (groupPosition == mGroups.size() - 1){
            return mChildren.get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;

        if (convertView == null){
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_billboard_group,parent,false);
            holder.ivSymbol = ButterKnife.findById(convertView,R.id.billboard_group_iv_symbol);
            holder.tvName = ButterKnife.findById(convertView,R.id.billboard_group_tv_name);
            holder.ivArrow = ButterKnife.findById(convertView,R.id.billboard_group_iv_arrow);
            convertView.setTag(holder);
        }
        else {
            holder = (GroupViewHolder) convertView.getTag();
        }

        BillboardBean bean = (BillboardBean) getGroup(groupPosition);

        if (bean.getCover() != null){
            Glide.with(parent.getContext())
                    .load(Constant.IMG_BASE_URL+bean.getCover())
                    .placeholder(R.drawable.ic_loadding)
                    .error(R.drawable.ic_load_error)
                    .fitCenter()
                    .into(holder.ivSymbol);
        }
        else {
            holder.ivSymbol.setImageResource(R.drawable.ic_billboard_collapse);
        }

        holder.tvName.setText(bean.getTitle());

        if (groupPosition == mGroups.size() - 1){
            holder.ivArrow.setVisibility(View.VISIBLE);
            //设置动画
            if (mOpenAnim == null || mCloseAnim == null){
                mOpenAnim = AnimationUtils.loadAnimation(mContext,R.anim.rotate_0_to_180);
                mCloseAnim = AnimationUtils.loadAnimation(mContext,R.anim.rotate_180_to_360);
                mOpenAnim.setFillAfter(true);
                mCloseAnim.setFillAfter(true);
            }
            GroupViewHolder finalHolder = holder;
            convertView.setOnTouchListener(
                    (v, event) -> {
                        if (!isOpen){
                            finalHolder.ivArrow.startAnimation(mOpenAnim);
                            isOpen = true;
                        }
                        else {
                            finalHolder.ivArrow.startAnimation(mCloseAnim);
                            isOpen = false;
                        }
                        return false;
                    }
            );
        }
        else {
            holder.ivArrow.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void setExpandAnimation(View view){

    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null){
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_billborad_child,parent,false);
            holder.tvName = ButterKnife.findById(convertView,R.id.billboard_child_tv_name);
            convertView.setTag(holder);
        }
        else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        holder.tvName.setText(mChildren.get(childPosition).getTitle());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void addGroups(List<BillboardBean> beans){
        mGroups.addAll(beans);
        notifyDataSetChanged();
    }

    public void addGroup(BillboardBean bean){
        mGroups.add(bean);
        notifyDataSetChanged();
    }

    public void addChildren(List<BillboardBean> beans){
        mChildren.addAll(beans);
        notifyDataSetChanged();
    }

    public void addChild(BillboardBean bean){
        mChildren.add(bean);
        notifyDataSetChanged();
    }

    public List<BillboardBean> getGroups(){
        return Collections.unmodifiableList(mGroups);
    }

    public List<BillboardBean> getChildren(){
        return Collections.unmodifiableList(mChildren);
    }

    private class GroupViewHolder {
        ImageView ivSymbol;
        TextView tvName;
        ImageView ivArrow;
    }

    private class ChildViewHolder{
        TextView tvName;
    }
}
