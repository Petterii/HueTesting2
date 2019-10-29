package com.tuononen.petteri.phuesensor;

public class Light {
    private HueState state;
    private HueSwUpdate swUpdate;
    private String type;
    private String name;
    private String modelId;
    private String manufacturername;
    // private Capabilities capabilities; // not needed yett
    private String uniqueid;
    private String swversion;

    public HueState getState() {
        return state;
    }

    public void setState(HueState state) {
        this.state = state;
    }

    public HueSwUpdate getSwUpdate() {
        return swUpdate;
    }

    public void setSwUpdate(HueSwUpdate swUpdate) {
        this.swUpdate = swUpdate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getManufacturername() {
        return manufacturername;
    }

    public void setManufacturername(String manufacturername) {
        this.manufacturername = manufacturername;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public String getSwversion() {
        return swversion;
    }

    public void setSwversion(String swversion) {
        this.swversion = swversion;
    }
}
