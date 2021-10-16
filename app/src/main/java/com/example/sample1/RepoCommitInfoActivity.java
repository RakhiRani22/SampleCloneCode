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
import com.example.sample1.model.Author;
import com.example.sample1.model.Commit;
import com.example.sample1.model.CommitInstance;
import com.example.sample1.model.Committer;
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

public class RepoCommitInfoActivity extends AppCompatActivity {
    private static final String HTTPS = "https";
    private static final String GIT_HUB_API = "api.github.com";
    private static final String REPOS_ENDPOINT = "repos";
    private static final String COMMITS_ENDPOINT = "commits";
    private static final String TAG = "RepoCommitInfoActivity";
    private static final String PER_PAGE = "per_page";
    private static final String PAGE_NUMBER = "page";
    private static final int PER_PAGE_SIZE = 1;
    private ArrayList<CommitInfo> commitInfoList = new ArrayList<>();
    private ArrayList<CommitInstance> commitInformationList = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private int pageNumber = 1;
    boolean isLoading = false;
    private String username;
    private String repositoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_commit_info);

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
        initScrollListener(recyclerView);

        adapter = new CommitInfoAdapter(this, commitInformationList);
        recyclerView.setAdapter(adapter);
        getCommitInformationInPages(pageNumber);
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

            isLoading = false;
            if (!TextUtils.isEmpty(s)) {
                decodeJson(s);
            } else {
                Log.e(TAG, "RAR:: isLoading = FALSE");
                isLoading = false;
                Toast.makeText(RepoCommitInfoActivity.this, "Error fetching file structure!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void decodeJson(String responseBody) {

        if (TextUtils.isEmpty(responseBody)) {
            Log.e(TAG, "RAR:: isLoading = FALSE");
            isLoading = false;
            Toast.makeText(this, "JSON response error: " + responseBody, Toast.LENGTH_SHORT).show();
        }

        //commitInfoList.clear();

        try {
            Log.i(TAG, "RAR:: responseBody:" + responseBody+" char:"+responseBody.charAt(0));
            if (responseBody.charAt(0) == '{') {
                Log.e(TAG, "RAR:: isLoading = FALSE");
                isLoading = false;
                String jsonRoot1 = new JSONObject(responseBody).getString("message");
                if(jsonRoot1.equalsIgnoreCase("Git Repository is empty.")) {
                    Toast.makeText(this, "No commit for " + repositoryName, Toast.LENGTH_SHORT).show();
                }
            } else {
                JSONArray jsonRoot = new JSONArray(responseBody);
                Log.i(TAG, "RAR:: jsonRoot:" + jsonRoot.toString());


                if(jsonRoot.length() > 0) {
                    Log.e(TAG, "RAR:: isLoading = FALSE");
                    isLoading = false;
                    for (int i = 0; i < jsonRoot.length(); i++) {
                        JSONObject child = jsonRoot.optJSONObject(i);
                        String sha = child.optString("sha");
                        String commit = child.optString("commit");
                        CommitInfo commitInfoObj = new CommitInfo();
                        commitInfoObj.setCommitHash(sha);
                        if (!commit.isEmpty()) {
                            JSONObject commitInfo = new JSONObject(commit);
                            String authorName = new JSONObject(commitInfo.optString("author")).optString("name");
                            String message = commitInfo.optString("message");
                            commitInfoObj.setAuthorName(authorName);
                            commitInfoObj.setCommitMessage(message);
                            Log.i(TAG, "RAR:: Commit Hash:" + sha);
                            Log.i(TAG, "RAR:: Author:" + authorName);
                            Log.i(TAG, "RAR:: Message:" + message);
                            Log.i(TAG, "RAR:: -----------");
                        }
                        commitInfoList.add(commitInfoObj);
                    }
                    for (int i = 0; i < 25; i++) {
                        commitInfoList.add(new CommitInfo(((pageNumber - 1) * 25) + i));
                    }
                }
                else{
                    Log.e(TAG, "RAR:: isLoading = TRUE");
                    isLoading = true;
                }

                if (!commitInfoList.isEmpty()) {
                    Log.i(TAG, "RAR:: ********Notify DataSetChanged:"+commitInfoList.size());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "No files returned from JSON!", Toast.LENGTH_SHORT).show();
                }
            }
            } catch(JSONException e){
                Log.e("parseJSON", "Cannot parse JSON", e);
                finish();
            }
    }

    private void requestToLoadData(String username, String repositoryName, String perPage, int pageNumber){
        Log.i(TAG,"RAR:: requestToLoadData:"+perPage+" pagenumber:"+pageNumber);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(HTTPS)
                .authority(GIT_HUB_API)
                .appendPath(REPOS_ENDPOINT)
                .appendPath(username)
                .appendPath(repositoryName)
                .appendPath(COMMITS_ENDPOINT);
        builder.appendQueryParameter(PER_PAGE, perPage);
        this.pageNumber = pageNumber + 1;
        builder.appendQueryParameter(PAGE_NUMBER, String.valueOf(this.pageNumber));
        new NetworkWork().execute(builder.build().toString(), null);
        Log.i(TAG,"RAR:: builder value********:"+builder.build().toString());
    }

    private void initScrollListener(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                Log.i(TAG,"RAR:: isLoading"+isLoading);
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == commitInformationList.size() - 1) {
                        //requestToLoadData(username, repositoryName, String.valueOf(PER_PAGE_SIZE), pageNumber);
                        getCommitInformationInPages(nextPageNumber());
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void getCommitInformationInPages(int pageNumber) {
        Log.d(TAG, "RAR:: ********NEW REQUEST************");
        Log.d(TAG, "RAR:: getCommitInformationInPages"+pageNumber);
        Call<List<CommitInstance>> call = RetrofitClient.getInstance().getMyApi().getCommitInformation(PER_PAGE_SIZE, pageNumber);
        call.enqueue(new Callback<List<CommitInstance>>() {
            @Override
            public void onResponse(Call<List<CommitInstance>> call, retrofit2.Response<List<CommitInstance>> response) {
                List<CommitInstance> commitInstanceList = response.body();
                if(commitInstanceList.size() > 0){
                    isLoading = false;
                }
                commitInformationList.addAll(commitInstanceList);

                Log.i(TAG, "RAR:: **********response.body():"+response.body());
                //looping through all the heroes and inserting the names inside the string array
                for (int index = 1; index < 25; index++) {
                    String position = String.valueOf(((pageNumber - 1) * 25) + index);
                    Author author = new Author("Author:"+position, null, null);
                    Committer committer = new Committer("Committer:"+position, null, null);
                    String message = "Message: ******** "+position+" ******";
                    commitInformationList.add(new CommitInstance(position, new Commit(author, committer, message)));
                    Log.i(TAG, "RAR:: **********Commit Message:"+message);
                }
                adapter.notifyDataSetChanged();

                //displaying the string array into listview
                //listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, commitInstanceList));
            }

            @Override
            public void onFailure(Call<List<CommitInstance>> call, Throwable t) {
                Log.i(TAG, "RAR:: Error:"+t.getMessage());
                isLoading = false;
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int nextPageNumber(){
        pageNumber = pageNumber + 1;
        return pageNumber;
    }
}
