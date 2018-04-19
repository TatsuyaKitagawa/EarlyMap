package com.study.android.earlymap.Service;

import com.study.android.earlymap.Model.MapModel;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by tatsuya on 2018/04/11.
 */

public interface MapService {
    @GET("maps/api/directions/json?")
    Observable<MapModel> getTakeTime(
            @Query("key")String key,
            @Query("origin")String origin,
            @Query("destination")String  destination,
            @Query("mode")String mode

    );
}
