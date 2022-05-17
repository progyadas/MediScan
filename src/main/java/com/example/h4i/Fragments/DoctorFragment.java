package com.example.h4i.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.h4i.Adapters.DoctorAdapter;
import com.example.h4i.Interface.Service;
import com.example.h4i.ModelClass.DoctorModel;
import com.example.h4i.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DoctorFragment extends Fragment {

    private DoctorAdapter adapter;
    private RecyclerView recyclerView;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor, container, false);
        recyclerView = view.findViewById(R.id.doctor_rv);
        searchView = view.findViewById(R.id.search_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getList();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!s.isEmpty())
                    adapter.getFilter().filter(s);
                else
                    getList();
                return true;
            }
        });
        return view;
    }

    private void getList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://hack4il.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Service service = retrofit.create(Service.class);

        service.getDoctorsList().enqueue(new Callback<List<DoctorModel>>() {
            @Override
            public void onResponse(Call<List<DoctorModel>> call, Response<List<DoctorModel>> response) {

                List<DoctorModel> list = response.body();
                adapter = new DoctorAdapter(list, getActivity());
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<DoctorModel>> call, Throwable t) {
            }
        });
    }
}