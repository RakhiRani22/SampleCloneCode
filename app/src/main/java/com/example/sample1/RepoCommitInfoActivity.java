package com.example.sample1;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sample1.model.commitinfo.CommitInstance;
import com.example.sample1.network.Api;
import com.example.sample1.util.Utility;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class RepoCommitInfoActivity extends AppCompatActivity {
    private static final String TAG = "RepoCommitInfoActivity";
    private static final int PER_PAGE_SIZE = 25;
    private ArrayList<CommitInstance> commitInformationList;
    private RecyclerView.Adapter adapter;
    private int pageNumber = 1;
    boolean isLoading = false;
    private String username;
    private String repositoryName;

    @Inject
    Retrofit retrofit;
    Application applicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_commit_info);

        ((MyApplication) getApplication()).getNetComponent().inject(this);
        Intent intent = getIntent();
        username = intent.getStringExtra(Utility.USERNAME);
        repositoryName = intent.getStringExtra(Utility.REPOSITORY_NAME);
        commitInformationList = new ArrayList<>();

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
                Log.i(TAG,"isLoading"+isLoading);
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == commitInformationList.size() - 1) {
                        getCommitInformationInPages(getNextPageNumber());
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void getCommitInformationInPages(int pageNumber) {
        Log.d(TAG, "PageNumber to be requested:"+pageNumber);
        if(Utility.isNetworkConnected(this)) {
            Api api = retrofit.create(Api.class);
            Call<List<CommitInstance>> call = api.getCommitInformationForRepos(username, repositoryName, PER_PAGE_SIZE, pageNumber);
            call.enqueue(new Callback<List<CommitInstance>>() {
                @Override
                public void onResponse(Call<List<CommitInstance>> call, retrofit2.Response<List<CommitInstance>> response) {
                    Log.i(TAG, "Response:" + response.body());
                    List<CommitInstance> commitInstanceList = response.body();
                    if (commitInstanceList != null) { //No commit found
                        if (!commitInstanceList.isEmpty()) { //Reached end of server data, no more request needed as last response was empty
                            isLoading = false;
                            commitInformationList.addAll(commitInstanceList);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        displayMessage("No commit done for this repository!");
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<List<CommitInstance>> call, Throwable t) {
                    Log.i(TAG, "RAR:: Error:" + t.getMessage());
                    isLoading = false;
                    displayMessage(t.getMessage());
                }
            });
        }
        else
        {
            displayMessage("No internet! Please check your internet connection.");
        }
    }

    private int getNextPageNumber(){
        pageNumber = pageNumber + 1;
        return pageNumber;
    }

    private void displayMessage(String message){
        Toast.makeText(RepoCommitInfoActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
