package com.example.h4i.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.h4i.ModelClass.DoctorModel;
import com.example.h4i.R;

import java.io.Serializable;

public class DoctorActivity extends AppCompatActivity {

    TextView name, place, qualification, reg_no, specialist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        name = findViewById(R.id.doctor_id_name);
        place = findViewById(R.id.id_place);
        qualification = findViewById(R.id.id_qualification);
        reg_no = findViewById(R.id.id_reg_no);
        specialist = findViewById(R.id.id_specialist);
        DoctorModel doctorModel = (DoctorModel) getIntent().getSerializableExtra("Doctor Details");
        name.setText(doctorModel.getName());
        place.setText(doctorModel.getPlace());
        qualification.setText(doctorModel.getQualification());
        reg_no.setText(doctorModel.getReg_no());
        int s = doctorModel.getSpecialist().size();
        String sp = "";
        for (int i = 0; i < s; i++) {
            sp = sp + doctorModel.getSpecialist().get(i) + "\n";
        }
        specialist.setText(sp);
    }

    public void backToMain(View view) {
        onBackPressed();
    }
}