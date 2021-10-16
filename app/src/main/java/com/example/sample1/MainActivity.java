package com.example.sample1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sample1.model.commitinfo.Author;
import com.example.sample1.util.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    private static final String HTTPS = "https";
    private static final String GIT_HUB_API = "api.github.com";
    private static final String USERS_ENDPOINT = "users";
    private static final String REPOS_ENDPOINT = "repos";
    private static final String ERROR = "ERROR";
    private static final String ERROR_2FA = "ERROR_2FA";
    private static final String ERROR_RETRIES_EXCEEDED = "ERROR_RETRIES_EXCEEDED";
    private static final String TAG = "MainActivity";
    private String username;
    private String repositoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("TAG", "Sample code on create");
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
                    submitRequest(username, repositoryName);
                }
            }
        });
    }

    private void submitRequest(String username, String repositoryName){
        this.username = username;
        this.repositoryName = repositoryName;

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(HTTPS);
        builder.authority(GIT_HUB_API)
                    .appendPath(USERS_ENDPOINT)
                    .appendPath(username)
                    .appendPath(REPOS_ENDPOINT);
        new NetworkCall().execute(builder.build().toString());
    }

    class NetworkCall extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            if (url == null || url[0] == null) return ERROR;

            Log.i(TAG, "Fetching " + url[0]);

            OkHttpClient client = new OkHttpClient();
            Log.i(TAG, "RAR:: Uri length: " + url.length);
            Request.Builder builder = new Request.Builder().url(url[0]);
            if (url.length > 1) {
                Log.i(TAG, "RAR:: url 2:"+url[1]);
                builder.addHeader("Authorization", url[1]);
            }

            Log.i(TAG, "RAR:: Builder::"+builder.toString());
            try (Response response = client.newCall(builder.build()).execute()) {
                //can only access response once
                String responseString = response.body().string();
                Log.i(TAG, "RAR:: responseString::"+responseString);
                if (response.code() == 401) {
                    return ERROR_2FA;
                } else if (response.code() == 403) {
                    return ERROR_RETRIES_EXCEEDED;
                } else if (!response.isSuccessful()) {
                    return ERROR;
                }

                return responseString;
            } catch (IOException e) {
                e.printStackTrace();
                return ERROR;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (TextUtils.isEmpty(response))
                Toast.makeText(MainActivity.this, "Empty response!", Toast.LENGTH_SHORT).show();

            switch (response) {
                case ERROR_2FA:
                    Toast.makeText(MainActivity.this, "Two-factor authentication is active, please enter code.", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_RETRIES_EXCEEDED:
                    Toast.makeText(MainActivity.this, "Maximum number of login attempts exceeded. Please try again later.", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    Toast.makeText(MainActivity.this, "Cannot fetch data from GitHub! Please verify the username", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    {
                        decodeResponse(response);
                    }
                    break;
            }
        }

        private void decodeResponse(String response) {
            if (TextUtils.isEmpty(response))
                Toast.makeText(MainActivity.this, "Empty response! No public repository for this user fetched.", Toast.LENGTH_SHORT).show();

            ArrayList<String> repos = new ArrayList<>();

            try {
                JSONArray jsonRoot = new JSONArray(response);
                for (int i = 0; i < jsonRoot.length(); i++) {
                    JSONObject child = jsonRoot.optJSONObject(i);
                    String repoName = child.optString("name");
                    if (!TextUtils.isEmpty(repoName)) repos.add(repoName);
                }

                if (!repos.isEmpty() && repos.contains(repositoryName)) {
                    Log.i(TAG," Access successful!");
                    Intent intent = new Intent(MainActivity.this, RepoCommitInfoActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("repositoryName", repositoryName);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "The repository is not found!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, "Cannot decode JSON!", Toast.LENGTH_SHORT).show();
            }
        }
    }
/*
    private void getCommitInformationInPages(int pageNumber) {
        Log.d(TAG, "RAR:: ********NEW REQUEST************");
        Log.d(TAG, "RAR:: getCommitInformationInPages"+pageNumber);
        Call<List<Author.CommitInstance>> call = RetrofitClient.getInstance().getMyApi().getCommitInformationForRepos(username, repositoryName, PER_PAGE_SIZE, pageNumber);
        call.enqueue(new Callback<List<Author.CommitInstance>>() {
            @Override
            public void onResponse(Call<List<Author.CommitInstance>> call, retrofit2.Response<List<Author.CommitInstance>> response) {
                Log.i(TAG, "RAR:: **********response.body():" + response.body());
                List<Author.CommitInstance> commitInstanceList = response.body();
                if(commitInstanceList != null) {
                    isLoading = false;

                    commitInformationList.addAll(commitInstanceList);

                    Log.i(TAG, "RAR:: **********response.body():" + response.body());

                    //To test
                    *//*for (int index = 1; index < 25; index++) {
                        String position = String.valueOf(((pageNumber - 1) * 25) + index);
                        Author author = new Author("Author:" + position, null, null);
                        Committer committer = new Committer("Committer:" + position, null, null);
                        String message = "Message: ******** " + position + " ******";
                        commitInformationList.add(new CommitInstance(position, new Commit(author, committer, message)));
                        Log.i(TAG, "RAR:: **********Commit Message:" + message);
                    }*//*
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No commit done for this repository!", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

            @Override
            public void onFailure(Call<List<Author.CommitInstance>> call, Throwable t) {
                Log.i(TAG, "RAR:: Error:"+t.getMessage());
                isLoading = false;
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }*/
}
