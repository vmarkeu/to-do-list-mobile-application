package com.example.assignment1;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.function.Function;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList task_id, task_title, task_desc, task_cat, task_status;
    private Function remoteUpdate;

    String UID, id, status;

    CustomAdapter(Activity activity, Context context, ArrayList task_id, ArrayList task_title, ArrayList task_desc, ArrayList task_cat, ArrayList task_status) {
        this.activity = activity;
        this.context = context;
        this.task_id = task_id;
        this.task_title = task_title;
        this.task_desc = task_desc;
        this.task_cat = task_cat;
        this.task_status = task_status;
    }

    CustomAdapter(Activity activity, Context context, ArrayList task_id, ArrayList task_title, ArrayList task_desc, ArrayList task_cat, ArrayList task_status, Function f) {
        this.activity = activity;
        this.context = context;
        this.task_id = task_id;
        this.task_title = task_title;
        this.task_desc = task_desc;
        this.task_cat = task_cat;
        this.task_status = task_status;
        this.remoteUpdate = f;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.task_id_txt.setText(String.valueOf(task_id.get(position)));
        holder.task_title_txt.setText(String.valueOf(task_title.get(position)));
        holder.task_desc_txt.setText(String.valueOf(task_desc.get(position)));
        holder.task_cat_txt.setText(String.valueOf(task_cat.get(position)));
        holder.task_status_txt.setText(String.valueOf(task_status.get(position)));

        //Recyclerview onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(task_id.get(position)));
                intent.putExtra("title", String.valueOf(task_title.get(position)));
                intent.putExtra("desc", String.valueOf(task_desc.get(position)));
                intent.putExtra("cat", String.valueOf(task_cat.get(position)));
                intent.putExtra("status", String.valueOf(task_status.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });

        // set category colour
        if (holder.task_cat_txt.getText().toString().equals("Keep in View (KIV)")) {
            holder.task_cat_txt.setTextColor(0xFF1C5411);
        } else if (holder.task_cat_txt.getText().toString().equals("General")) {
            holder.task_cat_txt.setTextColor(0xFF00308F);
        } else if (holder.task_cat_txt.getText().toString().equals("Important")) {
            holder.task_cat_txt.setTextColor(0xFFE6AC00);
        } else {
            holder.task_cat_txt.setTextColor(0xFFFF0000);
        }
    }

    @Override
    public int getItemCount() {
        return task_id.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {

        TextView task_id_txt, task_title_txt, task_desc_txt, task_cat_txt, task_status_txt;
        ImageView task_status_options;
        LinearLayout mainLayout;

        @RequiresApi(api = Build.VERSION_CODES.M)
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            task_id_txt = itemView.findViewById(R.id.task_id_txt);
            task_title_txt = itemView.findViewById(R.id.task_title_txt);
            task_desc_txt = itemView.findViewById(R.id.task_desc_txt);
            task_cat_txt = itemView.findViewById(R.id.task_cat_txt);
            task_status_txt = itemView.findViewById(R.id.task_status_txt);
            task_status_options = itemView.findViewById(R.id.task_status_options);
            mainLayout = itemView.findViewById(R.id.mainLayout);

            mainLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public boolean onLongClick(View view) {
                    confirmDialog(task_id_txt.getText().toString(), task_title_txt.getText().toString());
                    return false;
                }
            });

            task_status_options.setOnClickListener(view -> {
                showPopupMenu(view, task_id_txt.getText().toString());
                UID = task_id_txt.getText().toString();
            });

            //Animate Recyclerview
            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void confirmDialog(String idToDelete, String titleToDelete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete " + titleToDelete + " ?");
            builder.setMessage("Are you sure you want to delete " + titleToDelete + " ?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                MyDatabaseHelper myDB = new MyDatabaseHelper(activity.getApplicationContext());
                myDB.deleteOneRow(idToDelete);
                remoteUpdate.apply(null);
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.create().show();
        }

        private void showPopupMenu(View view, String ID){
            PopupMenu status_options = new PopupMenu(view.getContext(), view);
            status_options.inflate(R.menu.status_menu);
            status_options.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    MyDatabaseHelper myDB = new MyDatabaseHelper(activity.getApplicationContext());
                    // set status
                    switch (item.getItemId()) {
                        case R.id.menu_pending:
                            task_status_txt.setText("PENDING");
                            break;
                        case R.id.menu_in_progress:
                            task_status_txt.setText("IN PROGRESS");
                            break;
                        case R.id.menu_complete:
                            task_status_txt.setText("COMPLETED");
                            break;
                    }
                    id = ID;
                    status = task_status_txt.getText().toString();
                    myDB.updateStatus(id, status);
                    return false;
                }
            });
            status_options.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            MyDatabaseHelper myDB = new MyDatabaseHelper(activity.getApplicationContext());
            switch (itemView.getId()) {
                case R.id.menu_pending:
                    task_status_txt.setText("PENDING");
                    break;
                case R.id.menu_in_progress:
                    task_status_txt.setText("IN PROGRESS");
                    break;
                case R.id.menu_complete:
                    task_status_txt.setText("COMPLETED");
                    break;
            }
            id = UID;
            status = task_status_txt.getText().toString();
            myDB.updateStatus(id, status);
            return false;
        }
    }
}