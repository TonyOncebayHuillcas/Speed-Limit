package com.connecttix.speedlimit.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.connecttix.speedlimit.R;
import com.connecttix.speedlimit.adapters.HistoryAdapter;
import com.connecttix.speedlimit.database.SqliteClass;
import com.connecttix.speedlimit.models.RouteHistoryModel;
import com.connecttix.speedlimit.views.activitys.LoginActivity;
import com.connecttix.speedlimit.views.activitys.MainActivity;
import com.connecttix.speedlimit.views.dialog.CommonDialog;

import java.util.ArrayList;

public class HistoryRouteFragment extends Fragment {
    ArrayList<RouteHistoryModel> routeHistoryModels ;
    RecyclerView recyclerView;
    LayoutInflater inflater;
    public HistoryRouteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_history_route, container, false);

        ((MainActivity) getActivity()).setTitle("Historial de Rutas");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        routeHistoryModels = SqliteClass.getInstance(getActivity()).databasehelp.appRouteHistory.getAllRouteHistorys();

        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("login_preferences",Context.MODE_PRIVATE);

        View v = inflater.inflate(R.layout.alert_confirm_pass, null);
        EditText et_password = (EditText) v.findViewById(R.id.et_password_confirm);
        Button btnFire = (Button)v.findViewById(R.id.btn_ok_confrm);
        Button btnCancel = (Button)v.findViewById(R.id.btn_cancel_confirm);
        builder.setView(v);
        alertDialog = builder.create();
        // Add action buttons
        btnFire.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(et_password.getText().toString().equals(sharedPref.getString("password","@Josepe20@"))){
                            getList(routeHistoryModels);
                            Toast.makeText(getActivity(),"Contraseña valida",Toast.LENGTH_LONG).show();
                            alertDialog.dismiss();


                        }else {
                            Toast.makeText(getActivity(),"Contraseña invalida",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            getActivity().startActivity(intent);
                        }

                    }
                }
        );
        btnCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        getActivity().startActivity(intent);
                        alertDialog.dismiss();
                    }
                }
        );
        alertDialog.show();


        return view;
    }

    public void getList(ArrayList<RouteHistoryModel> list){
        if(list.size()>0){
            //tv_empty.setVisibility(recyclerView.GONE);
            HistoryAdapter adapter = new HistoryAdapter(getActivity(), list);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }else {
            //tv_empty.setVisibility(recyclerView.VISIBLE);
        }

    }

}
