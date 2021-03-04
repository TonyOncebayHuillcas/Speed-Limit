package com.connecttix.speedlimit.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.connecttix.speedlimit.R;
import com.connecttix.speedlimit.models.RouteHistoryModel;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    Context context;
    List<RouteHistoryModel> items;

    public HistoryAdapter(Context context, List<RouteHistoryModel> items) {
        this.context = context;
        this.items = items;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.row_history_route, parent, false);
        HistoryAdapter.ViewHolder viewHolder = new HistoryAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));

        if(items.get(position).getIs_point().equals("si")){
          holder.view_is_point.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }
        //  root.setBackgroundColor(getResources().getColor(android.R.color.red));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_route,tv_category,tv_strech,tv_fecha,tv_vel_permitida,tv_vel_falta;
        LinearLayout view_is_point;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_category =  (TextView) itemView.findViewById(R.id.tv_category_route);
            tv_route =  (TextView) itemView.findViewById(R.id.tv_route);
            tv_strech =  (TextView) itemView.findViewById(R.id.tv_strech);
            tv_fecha =  (TextView) itemView.findViewById(R.id.tv_fecha);
            tv_vel_permitida =  (TextView) itemView.findViewById(R.id.tv_vel_permitida);
            tv_vel_falta =  (TextView) itemView.findViewById(R.id.tv_vel_falta);
            view_is_point = (LinearLayout) itemView.findViewById(R.id.lnyt_type);

        }

        public void bind(RouteHistoryModel model){
            tv_category.setText(model.getId_category_route());
            tv_route.setText(model.getId_route());
            tv_strech.setText(model.getId_strech());
            tv_fecha.setText(model.getDate());
            tv_vel_permitida.setText(model.getVel_strech()+"KM/H");
            tv_vel_falta.setText(model.getVel_falta()+"KM/H");
        }
    }

}

