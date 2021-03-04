package com.connecttix.speedlimit.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.connecttix.speedlimit.R;
import com.connecttix.speedlimit.database.SqliteClass;
import com.connecttix.speedlimit.models.CategoryModel;
import com.connecttix.speedlimit.models.RouteModel;
import com.connecttix.speedlimit.views.activitys.RouteActivity;

import java.util.ArrayList;
import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {
    Context context;
    List<RouteModel> items;

    public RouteAdapter(Context context, List<RouteModel> items) {
        this.context = context;
        this.items = items;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.row_route, parent, false);
        RouteAdapter.ViewHolder viewHolder = new RouteAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // se usa en route activity
                SharedPreferences sharedPref = context.getSharedPreferences("values", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("id_route",String.valueOf(items.get(position).getId_route())); // uso en Route Actituvy
                editor.putString("name_route",String.valueOf(items.get(position).getName())); // uso en Route Actituvy

                CategoryModel categoryModel = SqliteClass.getInstance(context).databasehelp.appCategorySql.getCategoryById(String.valueOf(items.get(position).getId_fk_category()));

                editor.putString("name_category",String.valueOf(categoryModel.getName())); // uso en Route Actituvy
                editor.apply();

                Intent intent = new Intent(context, RouteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_route;
        FrameLayout frmt_route;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_route =  (TextView) itemView.findViewById(R.id.tv_route);
            frmt_route = (FrameLayout) itemView.findViewById(R.id.frmlt_route);

        }

        public void bind(RouteModel model){
            tv_route.setText(model.getName());
        }
    }

}
