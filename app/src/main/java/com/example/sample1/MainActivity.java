package com.example.sample1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.sample1.model.repoinfo.RepoInstance;
import com.example.sample1.network.RetrofitClient;
import com.example.sample1.util.Utility;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ArrayList<RepoInstance> repoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("TAG", "code on create");
        final EditText usernameText = findViewById(R.id.username);
        final EditText repoNameText = findViewById(R.id.repo_name);
        final Button submitButton = findViewById(R.id.submit_btn);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameText.getText().toString().toLowerCase();
                String repositoryName = repoNameText.getText().toString();
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(MainActivity.this, "Enter a valid username!", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(repositoryName)){
                    Toast.makeText(MainActivity.this, "Enter a valid repository name!", Toast.LENGTH_SHORT).show();
                }
                else{
                    getRepoInformationForUser(username, repositoryName);
                }
            }
        });
    }

    private void getRepoInformationForUser(String username, String repositoryName) {
        Log.d(TAG, "RAR:: ********NEW REQUEST************");
        Log.d(TAG, "RAR:: getRepoInformationForUser");
        if(Utility.isNetworkConnected(this)) {
            Call<List<RepoInstance>> call = RetrofitClient.getInstance().getMyApi().getRepoInformationForUser(username);
            call.enqueue(new Callback<List<RepoInstance>>() {
                @Override
                public void onResponse(Call<List<RepoInstance>> call, retrofit2.Response<List<RepoInstance>> response) {
                    Log.i(TAG, "RAR:: **********response.body():" + response.body());

                    repoList.clear();

                    if (response.code() == 401) {
                        Toast.makeText(MainActivity.this, "Two-factor authentication is active, please enter code.", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 403) {
                        Toast.makeText(MainActivity.this, "Maximum number of login attempts exceeded. Please try again later.", Toast.LENGTH_SHORT).show();

                    } else if (!response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Cannot fetch data from GitHub! Please verify the username", Toast.LENGTH_SHORT).show();
                    } else {
                        if (response.body() != null) {
                            if (TextUtils.isEmpty(response.body().toString())) {
                                Toast.makeText(MainActivity.this, "Empty response! No public repository for this user fetched.", Toast.LENGTH_SHORT).show();
                            }
                            List<RepoInstance> repositoryList = response.body();
                            repoList.addAll(repositoryList);
                            validateResponse(username, repositoryName);
                        } else {
                            Toast.makeText(MainActivity.this, "Response is null! No public repository for this user fetched.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<RepoInstance>> call, Throwable t) {
                    Log.i(TAG, "RAR:: Error:" + t.getMessage());
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No internet! Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private void validateResponse(String username, String repositoryName){
        Log.d(TAG,"RAR:: ******repoList:"+repoList.toString());
        if (!repoList.isEmpty()) {
            for (RepoInstance repositoryInstance : repoList) {
                Log.d(TAG,"RAR:: repository name:"+repositoryInstance.getName());
                if(repositoryInstance.getName().equalsIgnoreCase(repositoryName)) {
                    Log.i(TAG, " Access successful!");
                    Intent intent = new Intent(MainActivity.this, RepoCommitInfoActivity.class);
                    intent.putExtra(Utility.USERNAME, username);
                    intent.putExtra(Utility.REPOSITORY_NAME, repositoryName);
                    startActivity(intent);
                    return;
                }
            }
            Toast.makeText(MainActivity.this, "The repository is not found!", Toast.LENGTH_SHORT).show();
        }
    }
}
