package com.tuononen.petteri.phuesensor;

public class Bridge {

    private String id;
    private String internalipaddress;
    private String macaddress;
    private String name;
    private String key;

    public Bridge(String internalipaddress) {
        this.internalipaddress = internalipaddress;
    }
    public Bridge(String internalipaddress, String key) {
        this.internalipaddress = internalipaddress;
        this.key = key;
    }


    public String getInternalipaddress() {
        return internalipaddress;
    }

    public void setUserKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
