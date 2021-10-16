package com.example.sample1;

import android.app.Application;
import android.widget.Toast;
import com.example.sample1.component.ApiComponent;
import com.example.sample1.component.DaggerApiComponent;
import com.example.sample1.module.ApiModule;
import com.example.sample1.module.AppModule;
import com.example.sample1.network.Api;

public class MyApplication extends Application {

    private ApiComponent apiComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        apiComponent = DaggerApiComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule(Api.BASE_URL))
                .build();
    }

    public ApiComponent getNetComponent() {
        return apiComponent;
    }

    public void displayToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

