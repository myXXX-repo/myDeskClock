package com.wh.mydeskclock.app.task;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.wh.mydeskclock.MyDialog;
import com.wh.mydeskclock.R;
import com.wh.mydeskclock.utils.UiUtils;

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
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mParent)
//                        .setTitle(holder.title+" "+holder.device)
//                        .setMessage(holder.tv_con.getText().toString())
//                        .setPositiveButton("Close",null)
//                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
//                            @Override
//                            public void onDismiss(DialogInterface dialogInterface) {
//                                UiUtils.setFullScreen(mParent.getWindow());
//                            }
//                        });
////                Window window = alertDialog.getWindow();
////                UiUtils.setFullScreen(window);
////                alertDialog.show();
//                MyDialog myDialog = new MyDialog(alertDialog);
//                myDialog.setFullScreen();
//                myDialog.show(mParent.getSupportFragmentManager(),"taskDetail");
//            }
//        });
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
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_con;
        CheckBox cb_done;
        String title,device;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_con = itemView.findViewById(R.id.tv_task_list_item_con);
            cb_done = itemView.findViewById(R.id.cb_task_list_item_done);
        }
    }
}
