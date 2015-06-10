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

import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.widget.Toast;

import com.dalberg.glass.fetchirp.model.UserProfile;
import com.dalberg.glass.fetchirp.utility.AppConstants;
import com.dalberg.glass.fetchirp.utility.GeoTweetHelper;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.ion.Ion;

public class UserProfileLoader {
	
	private static final String TAG = "UserProfileLoader";
	
	private Context mContext;
	private String mAccessToken;

	public UserProfileLoader(Context context, String accessToken) {
		mContext = context;
		mAccessToken = accessToken;
	}
	
	public UserProfile loadUserProfile(String screenname){
		String url = GeoTweetHelper.profileUrl(screenname);
		UserProfile userProfile = null;
		try {
			userProfile = Ion.with(mContext)
			.load(url)
			.setHeader("Authorization", "Bearer " + mAccessToken)
			.as(new TypeToken<UserProfile>(){})				
			.get();			
		} catch (InterruptedException e) {
			Toast.makeText(mContext, AppConstants.PROFILE_ERROR_MSG, Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (ExecutionException e) {
			Toast.makeText(mContext, AppConstants.PROFILE_ERROR_MSG, Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		return userProfile;
	}

}
