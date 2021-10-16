package com.example.sample1.util;

import android.text.TextUtils;
import android.util.Log;
import com.example.sample1.model.repoinfo.RepoInstance;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Response;

public class DataValidator {
    private static final String TAG = "DataHandler";

    public static boolean isUserInputValid(String userInput){
        if(TextUtils.isEmpty(userInput) == true) {
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean isRepositoryFound(ArrayList<RepoInstance> repositoryList, String repositoryName) {
        for (RepoInstance repositoryInstance : repositoryList) {
            if (repositoryInstance.getName().equalsIgnoreCase(repositoryName)) {
                Log.i(TAG, " Repository found successfully!");
                return true;
            }
        }
        return false;
    }

    public static String isValidResponse(Response<List<RepoInstance>> response) {
        String errorMessage = null;

        if (response != null) {
            if (response.code() == 401) {
                errorMessage = "Two-factor authentication is active, please enter code.";
            } else if (response.code() == 403) {
                errorMessage = "Maximum number of login attempts exceeded. Please try again later.";
            } else if (!response.isSuccessful()) {
                errorMessage = "Cannot fetch data from GitHub! Please verify the username";
            } else if (response.body().toString().isEmpty()) {
                errorMessage = "Empty response! No public repository for this user fetched.";
            } else {
                //Valid response
            }
        } else {
            errorMessage = "Response is null!";
        }

        Log.i(TAG, "Error Message:"+errorMessage);
        return errorMessage;
    }
}
