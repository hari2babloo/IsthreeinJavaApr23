package com.example.hari.isthreeinjava.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hari on 29/3/18.
 */

public class Currentorder {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("jobid")
    @Expose
    private String jobid;
    @SerializedName("customerId")
    @Expose
    private String customerId;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("invoiceId")
    @Expose
    private String invoiceId;
    @SerializedName("shift")
    @Expose
    private Object shift;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("category")
    @Expose
    private List<String> category = null;
    @SerializedName("price")
    @Expose
    private List<String> price = null;
    @SerializedName("qty")
    @Expose
    private List<String> qty = null;
    @SerializedName("subTotal")
    @Expose
    private List<String> subTotal = null;
    @SerializedName("GSTPercentage")
    @Expose
    private String gSTPercentage;
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("grandTotal")
    @Expose
    private String grandTotal;
    @SerializedName("payableAmount")
    @Expose
    private String payableAmount;
    @SerializedName("amountPaid")
    @Expose
    private Object amountPaid;
    @SerializedName("balanceAmountToPay")
    @Expose
    private Object balanceAmountToPay;
    @SerializedName("paymentMode")
    @Expose
    private Object paymentMode;
    @SerializedName("pickupScheduledAt")
    @Expose
    private String pickupScheduledAt;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("pic")
    @Expose
    private String pic;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("landMark")
    @Expose
    private String landMark;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("longi")
    @Expose
    private String longi;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @SerializedName("serviceName")
    @Expose
    private String serviceName;

    @SerializedName("deliverOnHanger")
    @Expose
    private String deliverOnHanger;
    @SerializedName("washQuantity")
    @Expose
    private String washQuantity;

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

    public String getWashServiceCharge() {
        return washServiceCharge;
    }

    public void setWashServiceCharge(String washServiceCharge) {
        this.washServiceCharge = washServiceCharge;
    }

    @SerializedName("washServiceCharge")
    @Expose
    private String washServiceCharge;

    @SerializedName("expressDelivery")
    @Expose
    private String expressDelivery;

    public String getExpressDeliveryCharge() {
        return expressDeliveryCharge;
    }

    public void setExpressDeliveryCharge(String expressDeliveryCharge) {
        this.expressDeliveryCharge = expressDeliveryCharge;
    }

    @SerializedName("expressDeliveryCharge")
    @Expose
    private String expressDeliveryCharge;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getgSTPercentage() {
        return gSTPercentage;
    }

    public void setgSTPercentage(String gSTPercentage) {
        this.gSTPercentage = gSTPercentage;
    }

    public String getExpressDelivery() {
        return expressDelivery;
    }

    public void setExpressDelivery(String expressDelivery) {
        this.expressDelivery = expressDelivery;
    }
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
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

    public List<String> getQty() {
        return qty;
    }

    public void setQty(List<String> qty) {
        this.qty = qty;
    }

    public List<String> getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(List<String> subTotal) {
        this.subTotal = subTotal;
    }

    public String getGSTPercentage() {
        return gSTPercentage;
    }

    public void setGSTPercentage(String gSTPercentage) {
        this.gSTPercentage = gSTPercentage;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(String payableAmount) {
        this.payableAmount = payableAmount;
    }

    public Object getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Object amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Object getBalanceAmountToPay() {
        return balanceAmountToPay;
    }

    public void setBalanceAmountToPay(Object balanceAmountToPay) {
        this.balanceAmountToPay = balanceAmountToPay;
    }

    public Object getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Object paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPickupScheduledAt() {
        return pickupScheduledAt;
    }

    public void setPickupScheduledAt(String pickupScheduledAt) {
        this.pickupScheduledAt = pickupScheduledAt;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandMark() {
        return landMark;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }



}

