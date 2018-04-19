package com.study.android.earlymap.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tatsuya on 2018/04/11.
 */

public class Routes {
    @SerializedName("legs")
    @Expose
    private List<Legs> legs;

    public List<Legs> getLegs() {
        return legs;
    }

    public void setLegs(List<Legs> legs) {
        this.legs = legs;
    }
}
