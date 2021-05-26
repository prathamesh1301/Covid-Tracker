package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidtracker.apis.APIUtilities;
import com.example.covidtracker.apis.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView totalConfirmed,totalRecovered,totalActive,totalDeaths,totalTests;
    private TextView todayConfirmed,todayRecovered,todayDeaths;
    private List<CountryData> list;
    private PieChart pieChart;
    private TextView dateText,countryName;
    private String country="India";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list=new ArrayList<>();
        init();
        countryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,CountryActivity.class));
            }
        });
        countryName.setText(country);

        if(getIntent().getStringExtra("country")!=null)
            country=getIntent().getStringExtra("country");

        APIUtilities.getAPIInterface().getCountryData().enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                list=response.body();
                for(CountryData data:list){
                    if(data.getCountry().equals(country)){
                        int totalActiveCases=Integer.parseInt(data.getActive());
                        int totalConfirmedCases=Integer.parseInt(data.getCases());
                        int totalRecoverdCases=Integer.parseInt(data.getRecovered());
                        int totalDeathsCases=Integer.parseInt(data.getDeaths());
                        int totalTestCases=Integer.parseInt(data.getTests());

                        int todayConfirmedCases=Integer.parseInt(data.getTodayCases());
                        int todayRecoveredCases=Integer.parseInt(data.getTodayRecovered());
                        int todayDeathsCases=Integer.parseInt(data.getTodayDeaths());

                        pieChart.addPieSlice(new PieModel("Confirmed",totalConfirmedCases,getResources().getColor(R.color.yellow)));
                        pieChart.addPieSlice(new PieModel("Recovered",totalRecoverdCases,getResources().getColor(R.color.black)));
                        pieChart.addPieSlice(new PieModel("Deaths",totalActiveCases,getResources().getColor(R.color.red)));
                        pieChart.addPieSlice(new PieModel("Active",totalActiveCases,getResources().getColor(R.color.purple_200)));


                        updateDate(data.getUpdated());
                        countryName.setText(data.getCountry());

                        totalActive.setText(NumberFormat.getInstance().format(totalActiveCases));
                        totalRecovered.setText(NumberFormat.getInstance().format(totalRecoverdCases));
                        totalDeaths.setText(NumberFormat.getInstance().format(totalDeathsCases));
                        totalConfirmed.setText(NumberFormat.getInstance().format(totalConfirmedCases));
                        todayConfirmed.setText("(+"+NumberFormat.getInstance().format(todayConfirmedCases)+")");
                        todayDeaths.setText("(+"+NumberFormat.getInstance().format(todayDeathsCases)+")");
                        todayRecovered.setText("(+"+NumberFormat.getInstance().format(todayRecoveredCases)+")");
                        totalTests.setText(NumberFormat.getInstance().format(totalTestCases));

                    }
                }
            }

            @Override
            public void onFailure(Call<List<CountryData>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void init(){
        todayConfirmed=findViewById(R.id.todayConfirmed);
        totalConfirmed=findViewById(R.id.totalConfirmed);
        totalRecovered=findViewById(R.id.totalRecovered);
        todayRecovered=findViewById(R.id.todayRecovered);
        totalDeaths=findViewById(R.id.totalDeaths);
        todayDeaths=findViewById(R.id.todayDeaths);
        totalActive=findViewById(R.id.totalActive);
        totalTests=findViewById(R.id.totalTests);
        pieChart=findViewById(R.id.piechart);
        dateText=findViewById(R.id.dateText);
        countryName=findViewById(R.id.countryName);

    }
    private void updateDate(String update){
        DateFormat dateFormat=new SimpleDateFormat("MMM dd, yyyy");
        long millisecs=Long.parseLong(update);
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(millisecs);
        dateText.setText("Updated at "+ dateFormat.format(calendar.getTime()));

    }
}