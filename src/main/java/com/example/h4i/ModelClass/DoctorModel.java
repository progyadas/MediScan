package com.example.h4i.ModelClass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class DoctorModel implements Serializable {

    @SerializedName("name")
    public String name;

    @SerializedName("reg_no")
    public String reg_no;

    @SerializedName("qualification")
    public String qualification;

    @SerializedName("place")
    public String place;

    @SerializedName("specialist")
    public ArrayList<String> specialist;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public ArrayList<String> getSpecialist() {
        return specialist;
    }

    public DoctorModel() {
    }

    public DoctorModel(String name, String reg_no, String qualification, String place, ArrayList<String> specialist) {
        this.name = name;
        this.reg_no = reg_no;
        this.qualification = qualification;
        this.place = place;
        this.specialist = specialist;
    }

    public void setSpecialist(ArrayList<String> specialist) {
        this.specialist = specialist;
    }
}
