package com.study.android.earlymap.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tatsuya on 2018/04/11.
 */

public class Legs {
    @SerializedName("duration")
    @Expose
    private  Duration duration;

    public Duration getDuration() {
        return duration;
    }
}
