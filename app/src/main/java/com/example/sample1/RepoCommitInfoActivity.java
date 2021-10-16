package com.example.sample1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sample1.model.commitinfo.CommitInstance;
import com.example.sample1.network.RetrofitClient;
import com.example.sample1.util.Utility;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;

public class RepoCommitInfoActivity extends AppCompatActivity {
    private static final String TAG = "RepoCommitInfoActivity";
    private static final int PER_PAGE_SIZE = 1;
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
        username = intent.getStringExtra(Utility.USERNAME);
        repositoryName = intent.getStringExtra(Utility.REPOSITORY_NAME);

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
                Log.i(TAG,"RAR:: isLoading"+isLoading);
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
        Log.d(TAG, "RAR:: ********NEW REQUEST************");
        Log.d(TAG, "RAR:: getCommitInformationInPages"+pageNumber);
        Call<List<CommitInstance>> call = RetrofitClient.getInstance().getMyApi().getCommitInformationForRepos(username, repositoryName, PER_PAGE_SIZE, pageNumber);
        call.enqueue(new Callback<List<CommitInstance>>() {
            @Override
            public void onResponse(Call<List<CommitInstance>> call, retrofit2.Response<List<CommitInstance>> response) {
                Log.i(TAG, "RAR:: **********response.body():" + response.body());
                List<CommitInstance> commitInstanceList = response.body();
                if(commitInstanceList != null) {
                    isLoading = false;

                    commitInformationList.addAll(commitInstanceList);

                    Log.i(TAG, "RAR:: **********response.body():" + response.body());

                    //To test
                    /*for (int index = 1; index < 25; index++) {
                        String position = String.valueOf(((pageNumber - 1) * 25) + index);
                        Author author = new Author("Author:" + position, null, null);
                        Committer committer = new Committer("Committer:" + position, null, null);
                        String message = "Message: ******** " + position + " ******";
                        commitInformationList.add(new CommitInstance(position, new Commit(author, committer, message)));
                        Log.i(TAG, "RAR:: **********Commit Message:" + message);
                    }*/
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No commit done for this repository!", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

            @Override
            public void onFailure(Call<List<CommitInstance>> call, Throwable t) {
                Log.i(TAG, "RAR:: Error:"+t.getMessage());
                isLoading = false;
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getNextPageNumber(){
        pageNumber = pageNumber + 1;
        return pageNumber;
    }
}
