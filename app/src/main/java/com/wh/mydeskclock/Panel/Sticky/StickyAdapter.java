package com.wh.mydeskclock.Panel.Sticky;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.wh.mydeskclock.DataBase.Sticky;
import com.wh.mydeskclock.R;

public class StickyAdapter extends ListAdapter<Sticky, StickyAdapter.MyViewHolder> {
    private StickyViewModel stickyViewModel;
    private String TAG = "WH_StickyAdapter";

    public StickyAdapter(StickyViewModel stickyViewModel) {
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
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View itemView;
        itemView = layoutInflater.inflate(R.layout.item_sticky_list, parent, false);
        final MyViewHolder holder = new MyViewHolder(itemView);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int id = holder.ID;
//                String title = holder.title;
//                String con = (String) holder.tv_con.getText();
//                String create_time = (String) holder.tv_createTime.getText();
//
//
//                // display con with a alert dialog
////                EditText title_sticky = itemView.findViewById();
////                EditText con_sticky = itemView.findViewById(R.id.et_sticky_con);
////                String title_ = title_sticky.getText().toString();
////                String con_ = con_sticky.getText().toString();
////
//                View dialogLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_show_one_sticky, null);
//                EditText et_title = dialogLayout.findViewById(R.id.et_sticky_title);
//                final EditText et_con = dialogLayout.findViewById(R.id.et_sticky_con);
//
//
//
//                AlertDialog.Builder stickyConDialog = new AlertDialog.Builder(parent.getContext());
//                stickyConDialog.setView(dialogLayout);
//                stickyConDialog.setPositiveButton("save", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        String con__ = con.getText().toString();
////                        String
////                        stickyViewModel.updateStickies();
//                    }
//                });
//                stickyConDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                stickyConDialog.show();
//
////                MyDialog myDialog = new MyDialog(stickyConDialog);
////                myDialog.setFullScreen();
////                myDialog.show(parent.);
//            }
//        });
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                AlertDialog.Builder deleteConFirmDialog = new AlertDialog.Builder(parent.getContext());
//                deleteConFirmDialog.setTitle("删除确认");
//                deleteConFirmDialog.setMessage("真的要删除这条Sticky吗？");
//                deleteConFirmDialog.setPositiveButton("是的", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        Log.d(TAG, String.valueOf(itemView.getId()));
//                    }
//                });
//                deleteConFirmDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                deleteConFirmDialog.show();
//                return true;
//            }
//        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Sticky sticky = (Sticky) holder.itemView.getTag(R.id.sticky_for_view_holder);
                sticky.setStickyDone(isChecked);
                stickyViewModel.updateStickies(sticky);
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
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView tv_createTime, tv_con;
        String title = null;
        int ID;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.sticky_list_item_cb);
            tv_createTime = itemView.findViewById(R.id.sticky_list_item_time);
            tv_con = itemView.findViewById(R.id.sticky_list_item_con);
        }
    }
}
