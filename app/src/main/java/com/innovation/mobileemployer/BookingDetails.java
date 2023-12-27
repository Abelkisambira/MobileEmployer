package com.innovation.mobileemployer;

public class BookingDetails {
    private String seekerName;
    private String selectedTime;
    private String selectedDate;
    private String selectedLocation;
    private String bookingStatus;
    private String professionaID;

    public BookingDetails() {
    }

    public BookingDetails(String seekerName, String selectedTime, String selectedDate, String selectedLocation, String bookingStatus) {
        this.seekerName = seekerName;
        this.selectedTime = selectedTime;
        this.selectedDate = selectedDate;
        this.selectedLocation = selectedLocation;
        this.bookingStatus = bookingStatus;
    }

    public String getSeekerName() {
        return seekerName;
    }

    public void setSeekerName(String seekerName) {
        this.seekerName = seekerName;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(String selectedLocation) {
        this.selectedLocation = selectedLocation;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getProfessionaID() {
        return professionaID;
    }

    public void setProfessionaID(String professionaID) {
        this.professionaID = professionaID;
    }
}
