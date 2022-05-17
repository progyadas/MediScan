package com.example.h4i.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.h4i.Activity.DoctorActivity;
import com.example.h4i.ModelClass.DoctorModel;
import com.example.h4i.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.HospitalViewHolder> {

    List<DoctorModel> list;
    Context context;

    public DoctorAdapter(List<DoctorModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doctor_item, parent, false);
        return new HospitalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalViewHolder holder, int position) {
        DoctorModel model = list.get(position);
        holder.name.setText(model.getName());
        holder.place.setText(model.getPlace());
        int s = model.getSpecialist().size();
        String sp = "";
        for (int i = 0; i < s; i++) {
            sp = sp + model.getSpecialist().get(i) + "  ";
        }
        holder.specialist.setText(sp);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DoctorActivity.class);
                intent.putExtra("Doctor Details", model);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String inputString = charSequence.toString().toLowerCase().trim();
            List<DoctorModel> filteredList = new ArrayList<>();
            if (!inputString.isEmpty()) {
                for (DoctorModel Model : list) {
                    if (Model.getPlace().toLowerCase().trim().contains(inputString)) {
                        filteredList.add(Model);
                    }
                }

                if (filteredList.isEmpty())
                    Toast.makeText(context, "No Results found", Toast.LENGTH_SHORT).show();
            } else if (inputString.isEmpty())
                filteredList.addAll(list);
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((Collection<? extends DoctorModel>) filterResults.values);
            notifyDataSetChanged();

        }
    };

    public Filter getFilter() {
        return filter;
    }


    public class HospitalViewHolder extends RecyclerView.ViewHolder {
        TextView name, place, specialist;
        CardView card;

        public HospitalViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.doctor_name);
            place = itemView.findViewById(R.id.doctor_place);
            specialist = itemView.findViewById(R.id.specialist);
            card = itemView.findViewById(R.id.card);

        }
    }
}
