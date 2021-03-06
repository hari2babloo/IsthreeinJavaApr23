package com.example.hari.isthreeinjava.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by b on 10/3/18.
 */

public class modelmyorders {

    @SerializedName("jobid")
    @Expose
    private String jobid;
    @SerializedName("invoiceId")
    @Expose
    private String invoiceId;
    @SerializedName("customerId")
    @Expose
    private String customerId;
    @SerializedName("shift")
    @Expose
    private Object shift;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("category")
    @Expose
    private List<String> category = null;
    @SerializedName("price")
    @Expose
    private List<String> price = null;
    @SerializedName("quantity")
    @Expose
    private List<String> quantity = null;
    @SerializedName("subTotal")
    @Expose
    private List<String> subTotal = null;
    @SerializedName("GSTPercentage")
    @Expose
    private List<String> gSTPercentage = null;
    @SerializedName("grandTotal")
    @Expose
    private String grandTotal;
    @SerializedName("deliveryStatus")
    @Expose
    private String deliveryStatus;
    @SerializedName("expressDelivery")
    @Expose
    private String expressDelivery;
    @SerializedName("expressDeliveryCharge")
    @Expose
    private String expressDeliveryCharge;

    @SerializedName("serviceName")
    @Expose
    private String serviceName;


    @SerializedName("washServiceCharge")
    @Expose
    private String washServiceCharge;

    public String getWashServiceCharge() {
        return washServiceCharge;
    }

    public void setWashServiceCharge(String washServiceCharge) {
        this.washServiceCharge = washServiceCharge;
    }

    public String getDeliverOnHanger() {
        return deliverOnHanger;
    }

    public void setDeliverOnHanger(String deliverOnHanger) {
        this.deliverOnHanger = deliverOnHanger;
    }

    public String getWashQuantity() {
        return washQuantity;
    }

    public void setWashQuantity(String washQuantity) {
        this.washQuantity = washQuantity;
    }

    @SerializedName("deliverOnHanger")
    @Expose
    private String deliverOnHanger;
    @SerializedName("washQuantity")
    @Expose
    private String washQuantity;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<String> getgSTPercentage() {
        return gSTPercentage;
    }

    public void setgSTPercentage(List<String> gSTPercentage) {
        this.gSTPercentage = gSTPercentage;
    }

    public String getExpressDelivery() {
        return expressDelivery;
    }

    public void setExpressDelivery(String expressDelivery) {
        this.expressDelivery = expressDelivery;
    }

    public String getExpressDeliveryCharge() {
        return expressDeliveryCharge;
    }

    public void setExpressDeliveryCharge(String expressDeliveryCharge) {
        this.expressDeliveryCharge = expressDeliveryCharge;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Object getShift() {
        return shift;
    }

    public void setShift(Object shift) {
        this.shift = shift;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public List<String> getPrice() {
        return price;
    }

    public void setPrice(List<String> price) {
        this.price = price;
    }

    public List<String> getQuantity() {
        return quantity;
    }

    public void setQuantity(List<String> quantity) {
        this.quantity = quantity;
    }

    public List<String> getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(List<String> subTotal) {
        this.subTotal = subTotal;
    }

    public List<String> getGSTPercentage() {
        return gSTPercentage;
    }

    public void setGSTPercentage(List<String> gSTPercentage) {
        this.gSTPercentage = gSTPercentage;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

}
