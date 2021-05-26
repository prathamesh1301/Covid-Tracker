package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.example.covidtracker.apis.APIUtilities;
import com.example.covidtracker.apis.CountryData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryActivity extends AppCompatActivity {
    private RecyclerView countryRecyclerView;
    private CustomAdapter adapter;
    private List<CountryData> list;
    ProgressDialog dialog;
    EditText searchBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        countryRecyclerView=findViewById(R.id.countryRecyclerView);
        list=new ArrayList<>();
        adapter=new CustomAdapter(CountryActivity.this,list);
        dialog=new ProgressDialog(this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.show();
        countryRecyclerView.setHasFixedSize(true);
        countryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        countryRecyclerView.setAdapter(adapter);
        searchBar=findViewById(R.id.search_bar);


        APIUtilities.getAPIInterface().getCountryData().enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                list.addAll(response.body());
                dialog.dismiss();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<CountryData>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(CountryActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void filter(String s) {
        List<CountryData> filterList=new ArrayList<>();
        for(CountryData data:list){
            if(data.getCountry().toLowerCase().contains(s.toLowerCase())){
                filterList.add(data);
            }
        }
        adapter.filter(filterList);
    }
}