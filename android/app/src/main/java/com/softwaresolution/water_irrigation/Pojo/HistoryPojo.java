package com.softwaresolution.water_irrigation.Pojo;

public class HistoryPojo {

    private Integer mainGate;
    private Integer gateA;
    private Integer gateB;
    private Integer gateC;
    private Float flL;
    private Float flRate;
    private Float flmL;
    private String waterStatus;
    private Integer waterLevel;
    private String title;
    private String description;
    private Integer waterLevelTrigger;
    private Long createdDate;

    public HistoryPojo() {
    }

    public HistoryPojo(Integer mainGate, Integer gateA, Integer gateB, Integer gateC, Float flL, Float flRate, Float flmL, String waterStatus, Integer waterLevel, String title, String description, Integer waterLevelTrigger, Long createdDate) {
        this.mainGate = mainGate;
        this.gateA = gateA;
        this.gateB = gateB;
        this.gateC = gateC;
        this.flL = flL;
        this.flRate = flRate;
        this.flmL = flmL;
        this.waterStatus = waterStatus;
        this.waterLevel = waterLevel;
        this.title = title;
        this.description = description;
        this.waterLevelTrigger = waterLevelTrigger;
        this.createdDate = createdDate;
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

    public String getWaterStatus() {
        return waterStatus;
    }

    public void setWaterStatus(String waterStatus) {
        this.waterStatus = waterStatus;
    }

    public Integer getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(Integer waterLevel) {
        this.waterLevel = waterLevel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWaterLevelTrigger() {
        return waterLevelTrigger;
    }

    public void setWaterLevelTrigger(Integer waterLevelTrigger) {
        this.waterLevelTrigger = waterLevelTrigger;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }
}
