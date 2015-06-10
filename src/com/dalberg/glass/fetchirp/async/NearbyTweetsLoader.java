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
import android.location.Location;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dalberg.glass.fetchirp.MainActivity;
import com.dalberg.glass.fetchirp.utility.AppConstants;
import com.dalberg.glass.fetchirp.utility.TweetCardBuilder;
import com.dalberg.glass.fetchirp.utility.GeoTweetHelper;
import com.dalberg.glass.fetchirp.utility.UiHelper;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class NearbyTweetsLoader {
	
	private Context mContext;
	private String mAccessToken;
	private Future<JsonObject> mLoading;
	private Location mLocation;
	private boolean isSettingRadius = false;
	
	private UiHelper uiHelper;
	private TweetCardBuilder cardBuilder;
	
	private int mCount, mRadius;

	public NearbyTweetsLoader(Context context, MainActivity mainActivity, String accessToken, Future<JsonObject> loading, Location location, ProgressBar pb,
			TextView tv) {
		mContext = context;
		mAccessToken = accessToken;
		mLoading = loading;
		mLocation = location;
		uiHelper = new UiHelper(pb, tv);
		cardBuilder = new TweetCardBuilder(mainActivity);
		SharedPreferences prefs = mainActivity.getSharedPreferences(AppConstants.PREFS, Context.MODE_PRIVATE);
		mCount = prefs.getInt(AppConstants.PREFS_COUNT_KEY, 30);
		mRadius = prefs.getInt(AppConstants.PREFS_RADIUS_KEY, 5);
	}

	
	public void loadNearbyTweets(){
		if (mLoading != null && !mLoading.isDone() && !mLoading.isCancelled() )
            return;
		String url = GeoTweetHelper.searchUrl(mLocation, mRadius, mCount);		
		mLoading = Ion.with(mContext)
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
		                uiHelper.setWaitVisibility(false);
		                if(isSettingRadius){
		                	return;
		                }
		                cardBuilder.createCards(result);
		            }
		        });
		
		
	}
	
	public void setAccessToken(String accessToken){
		mAccessToken = accessToken;
	}
	
	public void cancelLoadingTweets(){
		mLoading.cancel(true);
	}
	
}
