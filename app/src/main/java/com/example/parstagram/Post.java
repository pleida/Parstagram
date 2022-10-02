package com.example.parstagram;

import androidx.annotation.Nullable;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_KEY = "createdAt";
    public static final String KEY_NBERLIKE = "likeNumber";
    public static final String KEY_LISTLIKE = "listLike";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public int getLIkeNumber() {
        return getInt(KEY_NBERLIKE);
    }

    public void setLikeNumber(int likeNumber){
        put(KEY_NBERLIKE,likeNumber);
    }


    public JSONArray getListLike() {
        return getJSONArray(KEY_LISTLIKE);
    }

    public void setListLike(ParseUser parseUser){
        add(KEY_LISTLIKE,parseUser);
    }

    public void removeListLike(List<String> listLikes){
        remove(KEY_LISTLIKE);
        put(KEY_LISTLIKE,listLikes);
    }

    public static ArrayList<String> fromJsonArray(JSONArray jsonArray) throws JSONException {
        ArrayList<String> lists = new ArrayList<String>();
        try {
            for (int i = 0; i< jsonArray.length(); i++){
                lists.add(jsonArray.getJSONObject(i).getString("objectId"));
            }

        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return lists;
    }
}


