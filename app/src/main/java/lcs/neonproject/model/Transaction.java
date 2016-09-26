package lcs.neonproject.model;

import com.google.gson.annotations.SerializedName;

import lcs.neonproject.contacts.ContactsManager;

/**
 * Created by Leandro on 9/25/2016.
 */

public class Transaction implements Model{


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Transaction(String clientId, String date, String id, String token, String valor) {
        this.clientId = clientId;
        this.date = date;
        this.id = id;
        this.token = token;
        this.valor = valor;
    }

    @SerializedName("ClienteId") private String clientId;
    @SerializedName("Data") private String date;
    @SerializedName("Id") private String id;
    @SerializedName("Token") private String token;
    @SerializedName("Valor") private String valor;

    private Contact contact;
    public Contact getContact() {
        if (contact == null) {
            contact = ContactsManager.getInstance().searchForContact(Integer.parseInt(clientId));
            if (contact == null)
                contact = new Contact("0","Default","999999999",0);
        }
        return contact;
    }
}
