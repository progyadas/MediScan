package com.example.h4i.Interface;

import com.example.h4i.ModelClass.DoctorModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {
    @GET("doctors")
    Call<List<DoctorModel>> getDoctorsList();
}
