package com.example.sample1.model.commitinfo;

public class Author {
    String name;
    String email;
    String date;

    public Author(String name, String email, String date) {
        this.name = name;
        this.email = email;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static class CommitInstance {
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
}
