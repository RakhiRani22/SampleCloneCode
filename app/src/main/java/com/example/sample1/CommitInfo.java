package com.example.sample1;

public class CommitInfo {
    String authorName;
    String commitHash;
    String commitMessage;

    public CommitInfo(){
        authorName = "Author: ";
        commitHash = "Commit#: ";
        commitMessage = "Commit Message";
    }

    public CommitInfo(int index) {
        this.authorName = authorName+index;
        commitHash = "Commit#: "+index;
        commitMessage = "Commit Message"+index;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = new StringBuilder("Message: ").append(commitMessage).toString();
    }

    public void setAuthorName(String authorName) {
        this.authorName = new StringBuilder("Author: ").append(authorName).toString();
    }

    public void setCommitHash(String commitHash) {
        this.commitHash = new StringBuilder("Commit#: ").append(commitHash).toString();;;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getCommitHash() {
        return commitHash;
    }

    public String getCommitMessage() {
        return commitMessage;
    }
}
