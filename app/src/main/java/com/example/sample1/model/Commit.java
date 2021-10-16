package com.example.sample1.model;

public class Commit {
    Author author;
    Committer committer;
    String message;

    /*public Commit(Author author, Committer committer) {
        this.author = author;
        this.committer = committer;
    }

    @Override
    public String toString() {
        return "Commit{" +
                "author=" + author +
                ", committer=" + committer +
                '}';
    }*/

    public Commit(Author author, Committer committer, String message) {
        this.author = author;
        this.committer = committer;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Commit{" +
                "author=" + author +
                ", committer=" + committer +
                ", message='" + message + '\'' +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Committer getCommitter() {
        return committer;
    }

    public void setCommitter(Committer committer) {
        this.committer = committer;
    }
}
