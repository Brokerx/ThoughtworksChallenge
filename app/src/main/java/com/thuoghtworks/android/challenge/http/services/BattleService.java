package com.thuoghtworks.android.challenge.http.services;

import com.thuoghtworks.android.challenge.db.Battle;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Govind on 08-Jan-17.
 */

public interface BattleService {
    @GET("/gotjson")
    void getAllBattles(Callback<List<Battle>> cb);
}
