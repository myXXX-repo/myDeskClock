package com.wh.mydeskclock.app.task;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.wh.mydeskclock.BaseApp;
import com.wh.mydeskclock.widget.MyDialog;
import com.wh.mydeskclock.R;
import com.wh.mydeskclock.utils.TimeUtils;

public class TaskListAdapter extends ListAdapter<Task, TaskListAdapter.MyViewHolder> {
    AppCompatActivity mParent;
    private TaskListViewModel taskListViewModel;

    public TaskListAdapter(TaskListViewModel taskListViewModel, AppCompatActivity mParent) {
        super(new DiffUtil.ItemCallback<Task>() {
            @Override
            public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
                return (oldItem.getCon().equals(newItem.getCon())
                        && oldItem.getId() == newItem.getId()
                        && oldItem.getCreateTime().equals(newItem.getCreateTime())
                        && oldItem.getTitle().equals(newItem.getTitle())
                        && oldItem.isReadDone() == newItem.isReadDone());
            }
        });
        this.taskListViewModel = taskListViewModel;
        this.mParent = mParent;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View ItemView = layoutInflater.inflate(R.layout.item_task_list, parent, false);
        final MyViewHolder holder = new MyViewHolder(ItemView);
        holder.cb_done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Task task = (Task) holder.itemView.getTag(R.id.task_tag);
                task.setReadDone(b);
                taskListViewModel.update(task);
                if (b) {
                    holder.tv_con.setPaintFlags(holder.tv_con.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.tv_con.setPaintFlags(holder.tv_con.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // taskDetail 弹框的布局文件
                View v_task_item_dialog = LayoutInflater.from(mParent).inflate(R.layout.item_task_list_item_dialog, null, false);

                // task title
                TextView tv_task_title = v_task_item_dialog.findViewById(R.id.tv_task_item_title);
                tv_task_title.setText(holder.title);

                // task device
                TextView tv_task_dev = v_task_item_dialog.findViewById(R.id.tv_task_item_device);
                tv_task_dev.setText(holder.device);

                // task createTime
                TextView tv_task_createTime = v_task_item_dialog.findViewById(R.id.tv_task_item_create_time);
                tv_task_createTime.setText(TimeUtils.getFormattedTime(Long.parseLong(holder.createTime)));

                // task con
                TextView tv_task_con = v_task_item_dialog.findViewById(R.id.tv_task_item_task);
                tv_task_con.setText(holder.task);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mParent)
                        .setView(v_task_item_dialog)
                        .setPositiveButton(holder.isDone ? "setUndone" : "setDone", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Task task = (Task) holder.itemView.getTag(R.id.task_tag);
                                task.setReadDone(!holder.isDone);
                                BaseApp.taskRepository.update(task);
                            }
                        });

                MyDialog myDialog = new MyDialog(alertDialog);
                myDialog.setFullScreen();
                myDialog.show(mParent.getSupportFragmentManager(), "myDeskClock_task_list_item");
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Task task = getItem(position);
        holder.itemView.setTag(R.id.task_tag, task);
        holder.tv_con.setText(task.getCon());
        holder.cb_done.setChecked(task.isReadDone());
        holder.title = task.getTitle();
        holder.device = task.getDeviceName();
        holder.createTime = task.getCreateTime();
        holder.task = task.getCon();
        holder.isDone = task.isReadDone();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_con;
        CheckBox cb_done;
        String title, device, createTime, task;
        boolean isDone;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_con = itemView.findViewById(R.id.tv_task_list_item_con);
            cb_done = itemView.findViewById(R.id.cb_task_list_item_done);
        }
    }
}
