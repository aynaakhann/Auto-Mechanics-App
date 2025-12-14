package com.example.map;

public class Model {
    private String id,cust_name;
    private String ratings;
    private String distance;
//    private String time_travel;
//    private  id;

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

//    public String getRatings() {
//        return ratings;
//    }
//
//    public void setRatings(String ratings) {
//        this.ratings = ratings;
//    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
//
//    public String getTime_travel() {
//        return time_travel;
//    }
//
//    public void setTime_travel(String time_travel) {
//        this.time_travel = time_travel;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
