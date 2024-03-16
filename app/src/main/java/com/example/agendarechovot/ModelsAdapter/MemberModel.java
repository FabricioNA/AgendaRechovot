package com.example.agendarechovot.ModelsAdapter;

public class MemberModel {

    String USERNAME, SINCEDATE;

    MemberModel() {
    }

    public MemberModel(String USERNAME, String SINCEDATE) {
        this.USERNAME = USERNAME;
        this.SINCEDATE = SINCEDATE;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getSINCEDATE() {
        return SINCEDATE;
    }

    public void setSINCEDATE(String SINCEDATE) {
        this.SINCEDATE = SINCEDATE;
    }
}
