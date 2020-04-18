package com.example.reminderapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminderapp.Database.RemDatabase;
import com.example.reminderapp.Model.Reminder;
import com.example.reminderapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RemAdapter extends RecyclerView.Adapter<RemAdapter.RemViewHolder>{

    Context context;
    ArrayList<Reminder> reminderArrayList;

    public RemAdapter(Context context, ArrayList<Reminder> remArrayList)
    {
        this.context = context;
        this.reminderArrayList = remArrayList;
    }
    @NonNull
    @Override
    public RemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.reminder_card,null,false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new RemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RemViewHolder holder, final int position) {
        Reminder r1 = reminderArrayList.get(position);
        final Reminder rem = r1;
        final RemViewHolder r_holder = holder;
        holder.title.setText(r1.getTitle());
        holder.desc.setText(r1.getDescription());
        holder.date.setText("Date: " + r1.getDate());
        holder.time.setText("Time: " + r1.getTime());
        holder.rep_mode.setText(r1.getRepeatMode());
        holder.can_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemDatabase rdb = new RemDatabase(context);
                rdb.deleteReminder(rem.getTitle());
                reminderArrayList.remove(r_holder.getAdapterPosition());
                notifyDataSetChanged();
                Toast.makeText(context, "Reminder Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminderArrayList.size();
    }

    public class RemViewHolder extends RecyclerView.ViewHolder{

        TextView title,desc,date,time,rep_mode;
        FloatingActionButton can_but;
        public RemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_c);
            desc = itemView.findViewById(R.id.desc_c);
            date = itemView.findViewById(R.id.date_c);
            time = itemView.findViewById(R.id.time_c);
            rep_mode = itemView.findViewById(R.id.rep_mode_c);
            can_but = itemView.findViewById(R.id.cancel_button_c);
        }
    }
}
