package com.example.agendarechovot.ModelsAdapter;

public class VisitModel {
    String CPF, DATA, SEAPROVADO;
    VisitModel() {

    }
    public VisitModel(String CPF, String DATA, String SEAPROVADO) {
        this.CPF = CPF;
        this.DATA = DATA;
        this.SEAPROVADO = SEAPROVADO;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getDATA() {
        return DATA;
    }

    public void setDATA(String DATA) {
        this.DATA = DATA;
    }

    public String getSEAPROVADO() {
        return SEAPROVADO;
    }

    public void setSEAPROVADO(String SEAPROVADO) {
        this.SEAPROVADO = SEAPROVADO;
    }
}