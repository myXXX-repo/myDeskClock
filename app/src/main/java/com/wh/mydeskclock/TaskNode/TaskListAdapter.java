package com.wh.mydeskclock.TaskNode;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
                        && oldItem.getTitle().equals(newItem.getTitle()));
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
        MyViewHolder holder = new MyViewHolder(ItemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Task task = getItem(position);
        holder.tv_con.setText(task.getCon());
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_con;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_con = itemView.findViewById(R.id.tv_task_list_item_con);
        }
    }
}
