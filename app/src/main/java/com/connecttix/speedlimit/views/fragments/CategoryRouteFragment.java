package com.connecttix.speedlimit.views.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.connecttix.speedlimit.R;
import com.connecttix.speedlimit.adapters.CategoryAdapter;
import com.connecttix.speedlimit.database.SqliteClass;
import com.connecttix.speedlimit.models.CategoryModel;
import com.connecttix.speedlimit.views.activitys.MainActivity;
import com.connecttix.speedlimit.views.activitys.RouteActivity;
import com.connecttix.speedlimit.views.dialog.CommonDialog;

import java.util.ArrayList;

public class CategoryRouteFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<CategoryModel> arrayList;
    TextView tv_empty;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_category_route, container, false);
        /** Cambio de titulo de fragment en vista **/
        ((MainActivity) getActivity()).setTitle("Categoría de Rutas");

        arrayList = SqliteClass.getInstance(getActivity()).databasehelp.appCategorySql.getAllLocations();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_route);
        tv_empty = (TextView) view.findViewById(R.id.tv_empty);
        getList(arrayList);
        /*
         Intent intent = new Intent(getActivity(), RouteActivity.class);
         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
         startActivity(intent);
         */

        return view;
    }

    public void getList(ArrayList<CategoryModel> list){
        if(list.size()>0){
            //tv_empty.setVisibility(recyclerView.GONE);
            CategoryAdapter adapter = new CategoryAdapter(getActivity(), list);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }else {
            //tv_empty.setVisibility(recyclerView.VISIBLE);
        }

    }

}
