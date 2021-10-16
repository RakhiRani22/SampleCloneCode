package com.example.sample1.model.repoinfo;

public class RepoInstance {
    String name = null;

    public RepoInstance(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RepoInstance{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
