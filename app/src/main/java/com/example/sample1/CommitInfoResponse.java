package com.example.sample1;

import com.example.sample1.model.commitinfo.CommitInstance;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;

public class CommitInfoResponse {
    List<CommitInstance> commitResponse;

    // public constructor is necessary for collections
    public CommitInfoResponse() {
        commitResponse = new ArrayList<CommitInstance>();
    }

    public static CommitInfoResponse parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        CommitInfoResponse commitInfoResponse = gson.fromJson(response, CommitInfoResponse.class);
        return commitInfoResponse;
    }
}
