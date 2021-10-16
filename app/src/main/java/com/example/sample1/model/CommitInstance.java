package com.example.sample1.model;

import android.view.textclassifier.ConversationActions;

public class CommitInstance {
    String sha;
    Commit commit;
    //String message;
    //Committer committer;

    public CommitInstance(String sha, Commit commit) {
        this.sha = sha;
        this.commit = commit;
    }

    @Override
    public String toString() {
        return "CommitInstance{" +
                "sha='" + sha + '\'' +
                ", commit=" + commit +
                '}';
    }

    /*@Override
    public String toString() {
        return "CommitInstance{" +
                "sha='" + sha + '\'' +
                ", commit=" + commit +
                '}';
    }*/

   /* public CommitInstance(String sha, Commit commit) {
        this.sha = sha;
        this.commit = commit;
    }*/


    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }
}
