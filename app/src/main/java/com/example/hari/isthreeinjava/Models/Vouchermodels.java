package com.example.hari.isthreeinjava.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hari on 9/5/18.
 */

public class Vouchermodels {

    @SerializedName("voucherID")
    @Expose
    private String voucherID;
    @SerializedName("voucherType")
    @Expose
    private String voucherType;
    @SerializedName("voucherSerialNumber")
    @Expose
    private String voucherSerialNumber;
    @SerializedName("voucherCode")
    @Expose
    private String voucherCode;
    @SerializedName("voucherValue")
    @Expose
    private String voucherValue;
    @SerializedName("voucherStartDate")
    @Expose
    private String voucherStartDate;
    @SerializedName("voucherExpiryDate")
    @Expose
    private String voucherExpiryDate;
    @SerializedName("voucherUserName")
    @Expose
    private String voucherUserName;
    @SerializedName("voucherUserEmail")
    @Expose
    private String voucherUserEmail;
    @SerializedName("voucherUserMobile")
    @Expose
    private String voucherUserMobile;
    @SerializedName("voucherStatus")
    @Expose
    private String voucherStatus;

    public String getVoucherID() {
        return voucherID;
    }

    public void setVoucherID(String voucherID) {
        this.voucherID = voucherID;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getVoucherSerialNumber() {
        return voucherSerialNumber;
    }

    public void setVoucherSerialNumber(String voucherSerialNumber) {
        this.voucherSerialNumber = voucherSerialNumber;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getVoucherValue() {
        return voucherValue;
    }

    public void setVoucherValue(String voucherValue) {
        this.voucherValue = voucherValue;
    }

    public String getVoucherStartDate() {
        return voucherStartDate;
    }

    public void setVoucherStartDate(String voucherStartDate) {
        this.voucherStartDate = voucherStartDate;
    }

    public String getVoucherExpiryDate() {
        return voucherExpiryDate;
    }

    public void setVoucherExpiryDate(String voucherExpiryDate) {
        this.voucherExpiryDate = voucherExpiryDate;
    }

    public String getVoucherUserName() {
        return voucherUserName;
    }

    public void setVoucherUserName(String voucherUserName) {
        this.voucherUserName = voucherUserName;
    }

    public String getVoucherUserEmail() {
        return voucherUserEmail;
    }

    public void setVoucherUserEmail(String voucherUserEmail) {
        this.voucherUserEmail = voucherUserEmail;
    }

    public String getVoucherUserMobile() {
        return voucherUserMobile;
    }

    public void setVoucherUserMobile(String voucherUserMobile) {
        this.voucherUserMobile = voucherUserMobile;
    }

    public String getVoucherStatus() {
        return voucherStatus;
    }

    public void setVoucherStatus(String voucherStatus) {
        this.voucherStatus = voucherStatus;
    }
}
