package com.softwaresolution.water_irrigation.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//{
//"title":"",
//"description":"",
//"timestamp":11111111111111111111111111,
//"mainGate":0,
//"gateA":0,
//"gateB":0,
//"gateC":0
//}
public class SchedulePojo {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("timestamp")
    @Expose
    private Long timestamp;
    @SerializedName("mainGate")
    @Expose
    private Integer mainGate;
    @SerializedName("gateA")
    @Expose
    private Integer gateA;
    @SerializedName("gateB")
    @Expose
    private Integer gateB;
    @SerializedName("gateC")
    @Expose
    private Integer gateC;
    @SerializedName("isExecuted")
    @Expose
    private Boolean isExecuted;

    /**
     * No args constructor for use in serialization
     *
     */
    public SchedulePojo() {
    }

    /**
     *
     * @param gateC
     * @param description
     * @param title
     * @param mainGate
     * @param gateB
     * @param timestamp
     * @param gateA
     */
    public SchedulePojo(String title, String description, Long timestamp,
                        Integer mainGate, Integer gateA,
                        Integer gateB, Integer gateC ) {
        super();
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.mainGate = mainGate;
        this.gateA = gateA;
        this.gateB = gateB;
        this.gateC = gateC;
        this.isExecuted = false;
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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
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

    public Boolean getIsExecuted() {
        return isExecuted;
    }

    public void setIsExecuted(Boolean isExecuted) {
        this.isExecuted = isExecuted;
    }

}