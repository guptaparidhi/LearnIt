package com.paridhigupta.qless.Model;

public class BookingInformation {
    private String customerName, customerPhone, time, machineId, machineNumb, floorId, floorNumb, machineIdentity;
    private  Long slot;

    public BookingInformation() {
    }

    public BookingInformation(String customerName, String customerPhone, String time, String machineId, String machineNumb, String floorId, String floorNumb, String machineIdentity, Long slot) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.time = time;
        this.machineId = machineId;
        this.machineNumb = machineNumb;
        this.floorId = floorId;
        this.floorNumb = floorNumb;
        this.machineIdentity = machineIdentity;
        this.slot = slot;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getMachineNumb() {
        return machineNumb;
    }

    public void setMachineNumb(String machineNumb) {
        this.machineNumb = machineNumb;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getFloorNumb() {
        return floorNumb;
    }

    public void setFloorNumb(String floorNumb) {
        this.floorNumb = floorNumb;
    }

    public String getMachineIdentity() {
        return machineIdentity;
    }

    public void setMachineIdentity(String machineIdentity) {
        this.machineIdentity = machineIdentity;
    }

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }
}
