/*
 * Copyright 2019 Andre601
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.andre601.javabotblockapi.requests;

import com.andre601.javabotblockapi.exceptions.RatelimitedException;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestHandler{
    
    private final String BASE_URL = "https://botblock.org/api/";
    private final OkHttpClient CLIENT = new OkHttpClient();
    
    private Cache<String, JSONObject> botCache = Caffeine.newBuilder()
            .expireAfterWrite(2, TimeUnit.MINUTES)
            .build();
    private Cache<String, JSONObject> listCache = Caffeine.newBuilder()
            .expireAfterWrite(2, TimeUnit.MINUTES)
            .build();
    
    JSONObject performGetBot(@NotNull String id, boolean disableCache){
        String url = BASE_URL + "bots/" + id;
        
        if(!disableCache)
                return botCache.get(id, k -> {
                    try{
                        return performGET(url);
                    }catch(IOException | RatelimitedException ex){
                        ex.printStackTrace();
                        return null;
                    }
                });
        
        try{
            return performGET(url);
        }catch(IOException | RatelimitedException ex){
            ex.printStackTrace();
            return null;
        }
    }
    
    JSONObject performGetList(@NotNull String id, boolean disableCache){
        String url = BASE_URL + "lists";
    
        if(!disableCache)
            return listCache.get(id, k -> {
                try{
                    return performGET(url);
                }catch(IOException | RatelimitedException ex){
                    ex.printStackTrace();
                    return null;
                }
            });
    
        try{
            return performGET(url);
        }catch(IOException | RatelimitedException ex){
            ex.printStackTrace();
            return null;
        }
    }
    
    private JSONObject performGET(@NotNull String url) throws IOException, RatelimitedException{
        Request request = new Request.Builder()
                .url(url)
                .build();
    
        try(Response response = CLIENT.newCall(request).execute()){
            ResponseBody body = response.body();
            if(body == null)
                throw new NullPointerException("Received empty response body.");
        
            if(body.string().isEmpty())
                throw new NullPointerException("Received empty response body.");
        
            if(!response.isSuccessful()){
                if(response.code() == 429)
                    throw new RatelimitedException(body.string());
            
                throw new IOException(String.format(
                        "Could not retrieve information. The server responded with error code %d (%s).",
                        response.code(),
                        response.message()
                ));
            }
        
            return new JSONObject(body.string());
        }
    }
    
    void performPOST(@NotNull JSONObject json) throws IOException, RatelimitedException{
        String url = BASE_URL + "count";
    
        RequestBody body = RequestBody.create(json.toString(), null);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
        
        try(Response response = CLIENT.newCall(request).execute()){
            ResponseBody responseBody = response.body();
            if(responseBody == null)
                throw new NullPointerException("Received empty response body.");
            
            if(responseBody.string().isEmpty())
                throw new NullPointerException("Received empty response body.");
            
            if(!response.isSuccessful()){
                if(response.code() == 429)
                    throw new RatelimitedException(responseBody.string());
                
                throw new IOException(String.format(
                        "Could not post Guild count. The server responded with error code %d (%s)",
                        response.code(),
                        response.message()
                ));
            }
            
            JSONObject responseJson = new JSONObject(responseBody);
            if(responseJson.has("failure")){
                JSONObject failure = responseJson.getJSONObject("failure");
    
                List<String> sites = new ArrayList<>();
                for(String key : failure.keySet()){
                    try{
                        JSONArray array = failure.getJSONArray(key);
                        sites.add(String.format(
                                "Site: %s, Code: %d, Message: %s",
                                key,
                                array.getInt(0),
                                array.getString(1)
                        ));
                    }catch(JSONException ex){
                        Map<String, Object> notFound = failure.toMap();
                        sites.add("Errors: " + notFound.toString());
                    }
                }
                
                throw new IOException(String.format(
                        "One or multiple post requests failed! Response(s): %s",
                        String.join(", ", sites)
                ));
            }
        }
    }
}