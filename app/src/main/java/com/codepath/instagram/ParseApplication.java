package com.codepath.instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register the Post class as a parse model.
        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("cxChIE2SJ1dHEE7Ire1JUFunlY6vivpNK2xjF8Ta")
                .clientKey("ZNMdqB9rfuIoNWgWASwZRmWfdJtUzeWQvreUMnYA")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
