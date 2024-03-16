package com.example.agendarechovot.ModelsAdapter;

import android.net.Uri;

public class UserModel {

    private String nomeCompleto, email, telephone, endereco, cep, cpf, senha, key, sinceDate;
    private Uri foto;

    public UserModel() {
    }

    public UserModel(String key) {
        this.key = key;
    }

    public Uri getFoto() {
        return foto;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setFoto(Uri foto) {
        this.foto = foto;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSinceDate() { return sinceDate;}

    public void setSinceDate(String sinceDate) { this.sinceDate = sinceDate;}
}