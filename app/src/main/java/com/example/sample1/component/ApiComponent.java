package com.example.sample1.component;

import com.example.sample1.ApiModule;
import com.example.sample1.AppModule;
import com.example.sample1.MainActivity;
import com.example.sample1.RepoCommitInfoActivity;
import com.example.sample1.util.Utility;
import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface ApiComponent {
    void inject(MainActivity activity);
    void inject(RepoCommitInfoActivity activity);
}

