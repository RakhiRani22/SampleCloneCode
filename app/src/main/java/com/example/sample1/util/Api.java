package com.example.sample1.util;

import com.example.sample1.CommitInfo;
import com.example.sample1.model.CommitInstance;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    String BASE_URL = "https://simplifiedcoding.net/demos/";

    /**
     * The return type is important here
     * The class structure that you've defined in Call<T>
     * should exactly match with your json response
     * If you are not using another api, and using the same as mine
     * then no need to worry, but if you have your own API, make sure
     * you change the return type appropriately
     **/
    @GET("commits?")//getUsers(@Query("per_page") int pageSize,
    //@Query("page") int currentPage);
    Call<List<CommitInstance>> getCommitInformation(@Query("per_page") int pageSize, @Query("page") int currentPage);

    @GET("commits")//getUsers(@Query("per_page") int pageSize,
        //@Query("page") int currentPage);
    Call<List<CommitInstance>> getCommitAllInformation();
}
