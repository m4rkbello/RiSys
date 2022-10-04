package com.softwaresolution.water_irrigation.Pojo;

public class Device {

    private Integer mainGate;
    private Integer gateA;
    private Integer gateB;
    private Integer gateC;
    private Float flL;
    private Float flRate;
    private Float flmL;
    private String waterStatus;
    private Integer waterLevel;

    public Device() {
    }

    public Device(Integer mainGate, Integer gateA, Integer gateB, Integer gateC, Float flL,
                  Float flRate, Float flmL,  String  waterStatus,Integer waterLevel) {
        super();
        this.mainGate = mainGate;
        this.gateA = gateA;
        this.gateB = gateB;
        this.gateC = gateC;
        this.flL = flL;
        this.flRate = flRate;
        this.flmL = flmL;
        this.waterStatus = waterStatus;
        this.waterLevel = waterLevel;
    }

    public Integer getMainGate() {
        return mainGate;
    }

    public void setMainGate(Integer mainGate) {
        this.mainGate = mainGate;
    }

    public Integer getGateA() {
        return gateA;
    }

    public void setGateA(Integer gateA) {
        this.gateA = gateA;
    }

    public Integer getGateB() {
        return gateB;
    }

    public void setGateB(Integer gateB) {
        this.gateB = gateB;
    }

    public Integer getGateC() {
        return gateC;
    }

    public void setGateC(Integer gateC) {
        this.gateC = gateC;
    }

    public Float getFlL() {
        return flL;
    }

    public void setFlL(Float flL) {
        this.flL = flL;
    }

    public Float getFlRate() {
        return flRate;
    }

    public void setFlRate(Float flRate) {
        this.flRate = flRate;
    }

    public Float getFlmL() {
        return flmL;
    }

    public void setFlmL(Float flmL) {
        this.flmL = flmL;
    }

    public Integer getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(Integer waterLevel) {
        this.waterLevel = waterLevel;
    }

    public String getWaterStatus() {
        return waterStatus;
    }

    public void setWaterStatus(String waterStatus) {
        this.waterStatus = waterStatus;
    }

}