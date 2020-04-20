package com.wh.mydeskclock.Panel;

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

    StickyAdapter(StickyViewModel stickyViewModel) {
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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView;
        itemView = layoutInflater.inflate(R.layout.sticky_list_item, parent, false);
        final MyViewHolder holder = new MyViewHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // display con with a alert dialog
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO {}
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
        holder.itemView.setTag(R.id.sticky_for_view_holder,sticky);
        holder.checkBox.setChecked(sticky.isStickyDone());
        holder.tv_createTime.setText(sticky.getStickyCreateTime());
        holder.tv_con.setText(sticky.getStickyCon());
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView tv_createTime, tv_con;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.sticky_list_item_cb);
            tv_createTime = itemView.findViewById(R.id.sticky_list_item_time);
            tv_con = itemView.findViewById(R.id.sticky_list_item_con);

        }
    }
}
