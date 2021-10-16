package com.example.sample1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.sample1.model.repoinfo.RepoInstance;
import com.example.sample1.network.Api;
import com.example.sample1.util.DataValidator;
import com.example.sample1.util.Utility;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Inject
    Retrofit retrofit;

    private ArrayList<RepoInstance> repoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MyApplication) getApplication()).getNetComponent().inject(this);
        Log.i("TAG", "code on create");
        final EditText usernameText = findViewById(R.id.username);
        final EditText repoNameText = findViewById(R.id.repo_name);
        final Button submitButton = findViewById(R.id.submit_btn);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameText.getText().toString();
                String repositoryName = repoNameText.getText().toString();
                if(DataValidator.isUserInputValid(username) == false) {
                    displayToast("Enter a valid username!");
                }
                else if(DataValidator.isUserInputValid(repositoryName) == false) {
                    displayToast("Enter a valid repository name!");
                }
                else {
                    getRepoInformationForUsername(username, repositoryName);
                }
            }
        });
    }

    private void getRepoInformationForUsername(String username, String repositoryName) {
        Log.d(TAG, "Make network request for User:"+username+" Repo:"+repositoryName);
        if(Utility.isNetworkConnected(this)) {
            Api api = retrofit.create(Api.class);
            Call<List<RepoInstance>> call = api.getRepoInformationForUser(username);
            call.enqueue(new Callback<List<RepoInstance>>() {
                @Override
                public void onResponse(Call<List<RepoInstance>> call, Response<List<RepoInstance>> response) {
                    Log.i(TAG, "Response received:" + response.body());

                    repoList.clear();
                    Response<List<RepoInstance>> responseObj = response;
                    String errorMessage = DataValidator.isValidResponse(responseObj);

                    if(errorMessage != null){
                        displayToast(errorMessage);
                    }
                    else {
                        Log.i(TAG, "Response received:" + response.body());
                        List<RepoInstance> repositoryList = response.body();
                        if(repositoryList!= null && !repositoryList.isEmpty()) {
                            repoList.addAll(repositoryList);
                            handleReposResponse(username, repositoryName);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<RepoInstance>> call, Throwable t) {
                    Log.i(TAG, "The request has failed: " + t.getMessage());
                    displayToast(t.getMessage());
                }
            });
        }
        else
        {
            displayToast("No internet! Please check your internet connection.");
        }
    }

    private void handleReposResponse(String username, String repositoryName){
        Log.i(TAG, "Response received");
        if(DataValidator.isRepositoryFound(repoList, repositoryName)){
            Intent intent = new Intent(MainActivity.this, RepoCommitInfoActivity.class);
            intent.putExtra(Utility.USERNAME, username);
            intent.putExtra(Utility.REPOSITORY_NAME, repositoryName);
            startActivity(intent);
        }
        else {
            displayToast("The repository is not found!");
        }
    }

    private void displayToast(String message){
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
