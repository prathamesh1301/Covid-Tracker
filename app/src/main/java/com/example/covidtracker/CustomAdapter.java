package com.example.covidtracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.covidtracker.apis.CountryData;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{
    private Context context;
    private List<CountryData> list;

    public CustomAdapter(Context context, List<CountryData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CountryData data=list.get(position);
        holder.countryName.setText(data.getCountry());
        holder.countryCases.setText(NumberFormat.getInstance().format(Integer.parseInt(data.getCases())));
        holder.sno.setText(String.valueOf(position+1));
        Map<String,String> map=data.getCountryInfo();
        Glide.with(context).load(map.get("flag")).into(holder.countryFlag);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,MainActivity.class);
                intent.putExtra("country",data.getCountry());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filter(List<CountryData> filterList){
        list=filterList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView sno,countryName,countryCases;
        ImageView countryFlag;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sno=itemView.findViewById(R.id.sno);
            countryCases=itemView.findViewById(R.id.countryCases);
            countryFlag=itemView.findViewById(R.id.countryFlag);
            countryName=itemView.findViewById(R.id.countryName);
        }
    }
}
