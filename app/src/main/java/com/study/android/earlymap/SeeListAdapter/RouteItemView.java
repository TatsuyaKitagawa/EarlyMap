package com.study.android.earlymap.SeeListAdapter;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by tatsuya on 2018/03/31.
 */

public class RouteItemView extends RealmObject implements Serializable{
   private String destination;
    private String time;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
