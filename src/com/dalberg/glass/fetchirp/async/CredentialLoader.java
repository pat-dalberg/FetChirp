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

import com.dalberg.glass.fetchirp.utility.AppConstants;
import com.dalberg.glass.fetchirp.utility.GeoTweetHelper;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

public class CredentialLoader {
	
	
	private Context mContext;
	private String mAccessToken;

	
	public CredentialLoader(Context context){
		mContext = context;
	}


	
	public void getCredentials() {
        JsonObject result;
		try {
			result = Ion.with(mContext, GeoTweetHelper.OAUTH_URL)
			.basicAuthentication(AppConstants.CONSUMER_KEY, AppConstants.CONSUMER_SECRET)
			.setBodyParameter("grant_type", "client_credentials")
			.asJsonObject()
			.get();
			mAccessToken = result.get("access_token").getAsString();
		} catch (InterruptedException e) {
			e.printStackTrace();
			Toast.makeText(mContext, AppConstants.CREDENTIALS_ERROR_MSG, Toast.LENGTH_SHORT).show();
			//mUiHelper.setWaitVisibility(false);
			return;
		} catch (ExecutionException e) {
			e.printStackTrace();
			Toast.makeText(mContext, AppConstants.CREDENTIALS_ERROR_MSG, Toast.LENGTH_SHORT).show();
			//mUiHelper.setWaitVisibility(false);
			return;
		}
        

    }
	
	
	public String getAccessToken(){
		return mAccessToken;
	}

}
