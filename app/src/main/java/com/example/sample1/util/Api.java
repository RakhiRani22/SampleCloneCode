package com.example.sample1.util;

import com.example.sample1.CommitInfo;
import com.example.sample1.model.CommitInstance;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    String BASE_URL = "https://api.github.com/";

    /**
     * The return type is important here
     * The class structure that you've defined in Call<T>
     * should exactly match with your json response
     * If you are not using another api, and using the same as mine
     * then no need to worry, but if you have your own API, make sure
     * you change the return type appropriately
     **/
    @GET("commits?")
    Call<List<CommitInstance>> getCommitInformation(@Query("per_page") int pageSize, @Query("page") int currentPage);

    @GET("commits")
    Call<List<CommitInstance>> getCommitAllInformation();
//repos/RakhiRani22/AlbumsDataList/commits
    @GET("/repos/{user}/{repo}/commits?")
    Call<List<CommitInstance>> getCommitInformationForRepos(@Path ("user") String user, @Path("repo") String repo, @Query("per_page") int pageSize, @Query("page") int currentPage);
}
