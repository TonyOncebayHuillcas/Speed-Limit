package com.connecttix.speedlimit.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.connecttix.speedlimit.R;
import com.connecttix.speedlimit.database.SqliteClass;
import com.connecttix.speedlimit.models.CategoryModel;
import com.connecttix.speedlimit.models.RouteModel;
import com.connecttix.speedlimit.views.activitys.RouteActivity;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context context;
    List<CategoryModel> items;

    public CategoryAdapter(Context context, List<CategoryModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.row_category, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_category;
        public CardView desplegable;
        public RecyclerView rcv_routes;
        public ImageView img_detail;

        ArrayList<RouteModel>list;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_category= (TextView) itemView.findViewById(R.id.tv_category);
            rcv_routes = (RecyclerView) itemView.findViewById(R.id.recycler_view_route);
            desplegable = (CardView) itemView.findViewById(R.id.ideaCard__2);
            img_detail =(ImageView) itemView.findViewById(R.id.img_detail);
            desplegable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(rcv_routes.getVisibility()==View.VISIBLE){
                        rcv_routes.setVisibility(View.GONE);
                        img_detail.setImageResource(R.drawable.ic_show);
                    } else {
                        rcv_routes.setVisibility(View.VISIBLE);
                        img_detail.setImageResource(R.drawable.ic_not_show);
                    }
                }
            });

        }

        public void bind(CategoryModel model){
            list = SqliteClass.getInstance(context).databasehelp.appRouteSql.getAllRoutesByCategory(String.valueOf(model.getId_category()));
            tv_category.setText(model.getName());

            getList(list);

        }

        public void getList(ArrayList<RouteModel> list){
            if(list.size()>0){
                //tv_empty.setVisibility(recyclerView.GONE);
                RouteAdapter adapter = new RouteAdapter(context, list);
                rcv_routes.setAdapter(adapter);
                rcv_routes.setLayoutManager(new LinearLayoutManager(context));
            }

        }
    }

}
