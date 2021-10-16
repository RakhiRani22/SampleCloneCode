package com.example.sample1.util;

import android.net.Uri;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sample1.CommitInfo;
import com.example.sample1.RepoCommitInfoActivity;

import java.util.ArrayList;

public class Utility {
    private static final String TAG = "Utility";
    private static final String HTTPS = "https";
    private static final String GIT_HUB_API = "api.github.com";
    private static final String REPOS_ENDPOINT = "repos";
    private static final String COMMITS_ENDPOINT = "commits";
    private static final String PER_PAGE = "per_page";
    private static final String PAGE_NUMBER = "page";
    private static int pageNumber = 0;

    public static String getUriBuilder(String username, String repositoryName, String perPage, int pagenumber){
            Log.i(TAG,"RAR:: requestToLoadData:"+perPage+" pagenumber:"+pageNumber);
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(HTTPS)
                    .authority(GIT_HUB_API)
                    .appendPath(REPOS_ENDPOINT)
                    .appendPath(username)
                    .appendPath(repositoryName).appendPath("/");
                    //.appendPath(COMMITS_ENDPOINT);
            //builder.appendQueryParameter(PER_PAGE, perPage);
            pageNumber = pagenumber + 1;
            //builder.appendQueryParameter(PAGE_NUMBER, String.valueOf(pageNumber));
        StringBuilder uri = new StringBuilder(HTTPS).append("://").append(GIT_HUB_API).append("/").append(REPOS_ENDPOINT).append("/").append(username).append("/").append(repositoryName).append("/");

            Log.i(TAG,"RAR:: builder value********:"+uri.toString());//builder.build().toString());
            return uri.toString();//builder.build().toString();
    }
}
