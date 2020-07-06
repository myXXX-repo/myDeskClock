package com.wh.mydeskclock.NotifyNode;

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

public class NotifyListAdapter extends ListAdapter<Notify,NotifyListAdapter.NotifyViewHolder> {
    private NotifyListViewModel notifyListViewModel;
    AppCompatActivity mParent;

    public NotifyListAdapter(NotifyListViewModel notifyListViewModel, AppCompatActivity mParent) {
        super(new DiffUtil.ItemCallback<Notify>() {
            @Override
            public boolean areItemsTheSame(@NonNull Notify oldItem, @NonNull Notify newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Notify oldItem, @NonNull Notify newItem) {
                return (oldItem.getNotifyCon().equals(newItem.getNotifyCon())
                        && oldItem.getId() == newItem.getId()
                        && oldItem.getNotifyCreateTime().equals(newItem.getNotifyCreateTime())
                        && oldItem.getNotifyTitle().equals(newItem.getNotifyTitle()));
            }
        });
        this.notifyListViewModel = notifyListViewModel;
        this.mParent = mParent;
    }

    @NonNull
    @Override
    public NotifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View ItemView = layoutInflater.inflate(R.layout.item_notify_list,parent,false);
        NotifyViewHolder holder = new NotifyViewHolder(ItemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyViewHolder holder, int position) {
        Notify notify = getItem(position);
        holder.tv_con.setText(notify.getNotifyCon());
    }

    static class NotifyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_con;

        public NotifyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_con = itemView.findViewById(R.id.tv_notify_list_item_con);
        }
    }
}
