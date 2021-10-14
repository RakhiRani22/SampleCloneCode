package com.example.sample1;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RepoCommitInfoActivity extends AppCompatActivity {
    private static final String HTTPS = "https";
    private static final String GIT_HUB_API = "api.github.com";
    private static final String REPOS_ENDPOINT = "repos";
    private static final String COMMITS_ENDPOINT = "commits";
    private static final String TAG = "RepoCommitInfoActivity";
    private ArrayList<CommitInfo> commitInfoList = new ArrayList<>();
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_commit_info);

        String username;
        String repositoryName;

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        repositoryName = intent.getStringExtra("repositoryName");

        if (repositoryName == null) finish();

        setTitle("Repos for " + username);

        RecyclerView recyclerView = findViewById(R.id.commit_info_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView. addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration. VERTICAL));

        adapter = new CommitInfoAdapter(this, commitInfoList);
        recyclerView.setAdapter(adapter);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(HTTPS)
                .authority(GIT_HUB_API)
                .appendPath(REPOS_ENDPOINT)
                .appendPath(username)
                .appendPath(repositoryName)
                .appendPath(COMMITS_ENDPOINT);
        new NetworkWork().execute(builder.build().toString(), null);
    }

    class NetworkWork extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            if (url == null) return null;

            Log.i(TAG, "RAR:: Fetching " + url[0]);

            OkHttpClient client = new OkHttpClient();

            Request.Builder builder = new Request.Builder().url(url[0]);
            if (url.length > 1 && !TextUtils.isEmpty(url[1])) {
                Log.i(TAG, "RAR:: Fetching url [1]:" + url[1]);
                builder.addHeader("Authorization", url[1]);
            }

            try (Response response = client.newCall(builder.build()).execute()) {
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (!TextUtils.isEmpty(s)) {
                decodeJson(s);
            } else {
                Toast.makeText(RepoCommitInfoActivity.this, "Error fetching file structure!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void decodeJson(String responseBody) {
        if (TextUtils.isEmpty(responseBody) || responseBody.length() < 50) {
            Toast.makeText(this, "JSON response error: " + responseBody, Toast.LENGTH_SHORT).show();
        }

        commitInfoList.clear();

        try {
            JSONArray jsonRoot = new JSONArray(responseBody);
            Log.i(TAG, "RAR:: jsonRoot:"+jsonRoot.toString());

            for (int i = 0; i < jsonRoot.length(); i++) {
                JSONObject child = jsonRoot.optJSONObject(i);
                String sha = child.optString("sha");
                String commit = child.optString("commit");
                CommitInfo commitInfoObj = new CommitInfo();
                commitInfoObj.setCommitHash(sha);
                if(!commit.isEmpty())
                {
                    JSONObject commitInfo = new JSONObject(commit);
                    String authorName = new JSONObject(commitInfo.optString("author")).optString("name");
                    String message = commitInfo.optString("message");
                    commitInfoObj.setAuthorName(authorName);
                    commitInfoObj.setCommitMessage(message);
                    Log.i(TAG, "RAR:: Commit Hash:"+sha);
                    Log.i(TAG, "RAR:: Author:"+authorName);
                    Log.i(TAG,"RAR:: Message:"+message);
                    Log.i(TAG,"RAR:: -----------");
                }
                commitInfoList.add(commitInfoObj);
            }

            if (!commitInfoList.isEmpty()) {
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "No files returned from JSON!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e("parseJSON", "Cannot parse JSON", e);
            finish();
        }
    }
}
