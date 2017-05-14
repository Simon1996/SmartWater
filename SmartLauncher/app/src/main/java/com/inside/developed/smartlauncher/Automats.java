package com.inside.developed.smartlauncher;

/**
 * Created by Simon on 14.05.2017.
 */

public class Automats {
    private String locationx;
    private String locationy;
    private String waterlevel;
    private String lastupdate;
    private String usercount;
    private String status;
    public Automats(String locationx, String locationy, String waterlevel, String lastupdate, String usercount, String status) {
        this.locationx = locationx;
        this.locationy = locationy;
        this.waterlevel = waterlevel;
        this.lastupdate = lastupdate;
        this.usercount = usercount;
        this.status = status;
    }

    public String getLocationx() {
        return locationx;
    }

    public void setLocationx(String locationx) {
        this.locationx = locationx;
    }

    public String getLocationy() {
        return locationy;
    }

    public void setLocationy(String locationy) {
        this.locationy = locationy;
    }

    public String getWaterlevel() {
        return waterlevel;
    }

    public void setWaterlevel(String waterlevel) {
        this.waterlevel = waterlevel;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    public String getUsercount() {
        return usercount;
    }

    public void setUsercount(String usercount) {
        this.usercount = usercount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
