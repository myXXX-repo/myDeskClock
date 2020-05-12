package com.wh.mydeskclock.Panel.Sticky;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.wh.mydeskclock.DataBase.Sticky;
import com.wh.mydeskclock.MainActivity;
import com.wh.mydeskclock.R;
import com.wh.mydeskclock.Widget.MyDialog;

public class StickyAdapter extends ListAdapter<Sticky, StickyAdapter.MyViewHolder> {
    private StickyViewModel stickyViewModel;
    private String TAG = "WH_StickyAdapter";
    private MainActivity mParent;

    StickyAdapter(StickyViewModel stickyViewModel, MainActivity mParent) {
        super(new DiffUtil.ItemCallback<Sticky>() {
            @Override
            public boolean areItemsTheSame(@NonNull Sticky oldItem, @NonNull Sticky newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Sticky oldItem, @NonNull Sticky newItem) {
                return (oldItem.getStickyCon().equals(newItem.getStickyCon())
                        && oldItem.getId() == newItem.getId()
                        && oldItem.getStickyCreateTime().equals(newItem.getStickyCreateTime())
                        && oldItem.getStickyTitle().equals(newItem.getStickyTitle()));
            }
        });
        this.stickyViewModel = stickyViewModel;
        this.mParent = mParent;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View itemView;
        itemView = layoutInflater.inflate(R.layout.item_sticky_list, parent, false);
        final MyViewHolder holder = new MyViewHolder(itemView);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Sticky sticky = (Sticky) holder.itemView.getTag(R.id.sticky_for_view_holder);
                sticky.setStickyDone(isChecked);
                stickyViewModel.updateStickies(sticky);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(parent.getContext(),String.valueOf(holder.ID),Toast.LENGTH_SHORT).show();
                AlertDialog.Builder stickyDetailDialog = new AlertDialog.Builder(mParent)
                        .setTitle(holder.title)
                        .setMessage(holder.con)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                MyDialog myDialog = new MyDialog(stickyDetailDialog);
                myDialog.setFullScreen();
                myDialog.show(mParent.getSupportFragmentManager(),"show sticky detail");
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder confirmDialog = new AlertDialog.Builder(mParent)
                        .setTitle("删除确认")
                        .setMessage("真的要删除这一条Sticky吗")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Sticky sticky = new Sticky();
                                sticky.setId(holder.ID);
                                stickyViewModel.deleteStickies(sticky);
                            }
                        });


                MyDialog myDialog = new MyDialog(confirmDialog);
                myDialog.setFullScreen();
                myDialog.show(mParent.getSupportFragmentManager(),"delete sticky");

                return true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Sticky sticky = getItem(position);
        holder.itemView.setTag(R.id.sticky_for_view_holder, sticky);
        holder.checkBox.setChecked(sticky.isStickyDone());
        holder.tv_createTime.setText(sticky.getStickyCreateTime());
        holder.tv_con.setText(sticky.getStickyCon());
        holder.title = sticky.getStickyTitle();
        holder.ID = sticky.getId();
        holder.con = sticky.getStickyCon();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView tv_createTime, tv_con;
        String title = null;
        String con = null;
        int ID;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.sticky_list_item_cb);
            tv_createTime = itemView.findViewById(R.id.sticky_list_item_time);
            tv_con = itemView.findViewById(R.id.sticky_list_item_con);
        }
    }
}
