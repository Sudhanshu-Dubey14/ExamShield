package com.example.android.examshield;
/**
 * Interface defining a callable to be used as callback when fetching server data
 */
public interface FetchDataCallbackInterface {
    // method called when server's data get fetched
    public void fetchDataCallback (String result);
}
