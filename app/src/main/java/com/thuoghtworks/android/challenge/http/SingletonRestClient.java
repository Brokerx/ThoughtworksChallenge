package com.thuoghtworks.android.challenge.http;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class SingletonRestClient {

	private static RestAdapter restAdapter = null;
		public static String BASE_API_URL = "http://starlord.hackerearth.com";

	private static void initRestAdapter(Context mContext) {
		if(restAdapter == null) {
			Gson gson = new GsonBuilder()
                .create();

			restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_API_URL)
                .setConverter(new GsonConverter(gson))
                .build();
		}
	}
	
	/* A private Constructor prevents any other 
    * class from instantiating.
    */
    private SingletonRestClient(){}

	public static <S> S createService(Class<S> serviceClass, Context mContext) {
		initRestAdapter(mContext);
		return restAdapter.create(serviceClass);
	}

}
