package com.example.sample1.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

        private static RetrofitClient instance = null;
        private Api myApi;

        private RetrofitClient() {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(Utility.getUriBuilder("Rakhirani22","AlbumsDataList", "1", 0))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            myApi = retrofit.create(Api.class);
        }

        public static synchronized RetrofitClient getInstance() {
            if (instance == null) {
                instance = new RetrofitClient();
            }
            return instance;
        }

        public Api getMyApi() {
            return myApi;
        }

}
