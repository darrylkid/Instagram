package com.codepath.instagram;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("cxChIE2SJ1dHEE7Ire1JUFunlY6vivpNK2xjF8Ta")
                .clientKey("ZNMdqB9rfuIoNWgWASwZRmWfdJtUzeWQvreUMnYA")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
