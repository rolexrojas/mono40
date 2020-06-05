package com.mono40.movil.util;

import com.google.gson.annotations.SerializedName;
import com.mono40.movil.ServiceInformation.CarServiceInformation;
import com.mono40.movil.ServiceInformation.Maintenance;

import java.util.ArrayList;
import java.util.Arrays;

public class QrJwtMnt {
    private final char ZERO = '1';
    @SerializedName("sub")
    private String sub;
    @SerializedName("iss")
    private String iss;

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getSub() {
        return sub;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public Maintenance getMaintenanceFromSub(){

        Maintenance maintenance = new Maintenance();
        maintenance.setOilChange((getSub().charAt(0) == ZERO));
        maintenance.setOilFilterChange((getSub().charAt(1) == ZERO));
        maintenance.setTransmissionFluidChange((getSub().charAt(2) == ZERO));
        maintenance.setBrakeFluidChange((getSub().charAt(3) == ZERO));
        maintenance.setSteeringFluidChange((getSub().charAt(4) == ZERO));
        maintenance.setCoolantFluidChange((getSub().charAt(5) == ZERO));
        maintenance.setWipeWaterCheck((getSub().charAt(6) == ZERO));
        maintenance.setBatteryWaterChange((getSub().charAt(7) == ZERO));
        maintenance.setRadiatorHosesCheck((getSub().charAt(8) == ZERO));
        maintenance.setHeaterHosesCheck((getSub().charAt(9) == ZERO));
        maintenance.setAirCondHosesCheck((getSub().charAt(10) == ZERO));
        maintenance.setAirFilterChange((getSub().charAt(11) == ZERO));
        maintenance.setTirePressureCheck((getSub().charAt(12) == ZERO));
        maintenance.setTireWearCheck((getSub().charAt(13) == ZERO));
        maintenance.setWipersCheck((getSub().charAt(14) == ZERO));
        maintenance.setHeadLampAlignmentCheck((getSub().charAt(15) == ZERO));
        maintenance.setSeatBeltCheck((getSub().charAt(16) == ZERO));
        maintenance.setParkingBreakCheck((getSub().charAt(17) == ZERO));

        return maintenance;
    }

    public CarServiceInformation getCarServiceInformationFromIss(){
        CarServiceInformation carServiceInformation = new CarServiceInformation();
        ArrayList<String> carList = new ArrayList<>(Arrays.asList(getIss().split(",")));
        carServiceInformation.setInsuranceNo(carList.get(0));
        carServiceInformation.setMake(carList.get(1));
        carServiceInformation.setModel(carList.get(2));
        carServiceInformation.setYear(carList.get(3));
        carServiceInformation.setMiles(carList.get(4));

        return carServiceInformation;
    }
}

