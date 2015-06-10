/*
 * Copyright (C) 2014 Pat Dalberg 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dalberg.glass.fetchirp.async;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.dalberg.glass.fetchirp.HashtagActivity;
import com.dalberg.glass.fetchirp.utility.AppConstants;
import com.dalberg.glass.fetchirp.utility.TweetCardBuilder;
import com.dalberg.glass.fetchirp.utility.GeoTweetHelper;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class HashtagLoader {
	
	private Context mContext;
	private String mAccessToken;
	private TweetCardBuilder cardBuilder;
	private int mCount;

	public HashtagLoader(HashtagActivity hashtagActivity, String accessToken) {
		mContext = hashtagActivity;
		mAccessToken = accessToken;
		cardBuilder = new TweetCardBuilder(hashtagActivity);
		SharedPreferences prefs = hashtagActivity.getSharedPreferences(AppConstants.PREFS, Context.MODE_PRIVATE);
		mCount = prefs.getInt(AppConstants.PREFS_COUNT_KEY, 30);
	}
	
	public void loadHashtagTweets(String hashtag){
		String url = GeoTweetHelper.searchHashtagUrl(hashtag, mCount);
		Ion.with(mContext)
		.load(url)
		.setHeader("Authorization", "Bearer " + mAccessToken)
		.asJsonObject()
		.setCallback(new FutureCallback<JsonObject>(){
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e != null) {
                    Toast.makeText(mContext, "Error loading tweets", Toast.LENGTH_LONG).show();
                    return;
                }
                
                cardBuilder.createCards(result);
            }
        });


	}
	
}
