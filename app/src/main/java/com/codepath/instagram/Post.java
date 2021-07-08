package com.codepath.instagram;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Post.java allows users to persist post details (user, image and description)
 * into the Parse Database using the Back4App database manager.
 */

@ParseClassName("Post")
public class Post extends ParseObject {
    // The assigned values to these constants must be exactly
    // the same as in the Back4App database manager.
    private static final String KEY_USER = "user";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_DESCRIPTION = "description";

    // These are not normal getter methods. They access the database using
    // the keys defined above.
    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    // Likewise for the setter methods. They put data into the database.
    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }
}
