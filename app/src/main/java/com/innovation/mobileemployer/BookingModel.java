package com.innovation.mobileemployer;
public class BookingModel {
    private String bookingId;
    private String seekerName;
    private String selectedTime;
    private String selectedDate;
    private String selectedLocation;
    private String bookingStatus;
    public String professionalId;

    // Default constructor required for Firebase
    public BookingModel() {
    }

    public BookingModel(String seekerName, String selectedTime, String selectedDate, String selectedLocation, String bookingStatus) {
        this.seekerName = seekerName;
        this.selectedTime = selectedTime;
        this.selectedDate = selectedDate;
        this.selectedLocation = selectedLocation;
        this.bookingStatus = bookingStatus;
    }

    // Getters and setters

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
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

    public String getProfessionalId() {
        return professionalId;
    }
}
