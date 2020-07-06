package com.wh.mydeskclock.TaskNode;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.wh.mydeskclock.R;

public class TaskListAdapter extends ListAdapter<Task, TaskListAdapter.MyViewHolder> {
    private TaskListViewModel taskListViewModel;
    AppCompatActivity mParent;

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
                        && oldItem.isReadDone()==newItem.isReadDone());
            }
        });
        this.taskListViewModel = taskListViewModel;
        this.mParent = mParent;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View ItemView = layoutInflater.inflate(R.layout.item_task_list,parent,false);
        final MyViewHolder holder = new MyViewHolder(ItemView);
        holder.cb_done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Task task = (Task) holder.itemView.getTag(R.id.task_tag);
                task.setReadDone(b);
                taskListViewModel.update(task);
                if(b){
                    holder.tv_con.setPaintFlags(holder.tv_con.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }else {
                    holder.tv_con.setPaintFlags(holder.tv_con.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Task task = getItem(position);
        holder.itemView.setTag(R.id.task_tag,task);
        holder.tv_con.setText(task.getCon());
        holder.cb_done.setChecked(task.isReadDone());
        holder.id = task.getId();
        holder.con = task.getCon();
        holder.title = task.getTitle();
        holder.devName = task.getDeviceName();
        holder.createTime = task.getCreateTime();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_con;
        CheckBox cb_done;
        int id;
        String con,title,devName,createTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_con = itemView.findViewById(R.id.tv_task_list_item_con);
            cb_done = itemView.findViewById(R.id.cb_task_list_item_done);
        }
    }
}
