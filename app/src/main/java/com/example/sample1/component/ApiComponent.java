package com.example.sample1.component;

import com.example.sample1.module.ApiModule;
import com.example.sample1.module.AppModule;
import com.example.sample1.ui.MainActivity;
import com.example.sample1.ui.RepoCommitInfoActivity;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface ApiComponent {
    void inject(MainActivity activity);
    void inject(RepoCommitInfoActivity activity);
}

