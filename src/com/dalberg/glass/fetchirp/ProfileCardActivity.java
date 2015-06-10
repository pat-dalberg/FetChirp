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

package com.dalberg.glass.fetchirp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dalberg.glass.fetchirp.async.UserProfileLoader;
import com.dalberg.glass.fetchirp.model.UserProfile;
import com.dalberg.glass.fetchirp.utility.ProfileCardBuilder;
import com.dalberg.glass.fetchirp.utility.UiHelper;
import com.google.android.glass.app.Card;

public class ProfileCardActivity extends Activity {
	
	private final static String TAG = "ProfileCardActivity";

	private ProgressBar pbProfileWait;
	private TextView tvProfileWait;
	
	private Context mContext;
	
	private UserProfileLoader mUserProfileLoader;
	
	private ProfileCardBuilder mProfileCardBuilder;
	
	private UiHelper mUiHelper;
	
	private String mScreenname;
	
	private String mPicpath;
	
	private boolean mIsAuthor = false;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_card);
		init();
	}
	
	private void init(){
		initUi();
		initOps();
	}
	
	private void initUi(){
		
		pbProfileWait = (ProgressBar)findViewById(R.id.pbHashtagWait);
		tvProfileWait = (TextView)findViewById(R.id.tvHashtagWait);
	}
	
	private void initOps(){
		UserProfile userProfile;
		mContext = getApplicationContext();
		Intent intent = getIntent();
		mIsAuthor = intent.getBooleanExtra("isauthor", false);
		mScreenname = intent.getStringExtra("screenname");
		mPicpath = intent.getStringExtra("profileimagepath");
		String accessToken = intent.getStringExtra("accesstoken");
		mUserProfileLoader = new UserProfileLoader(mContext, accessToken);
		userProfile = mUserProfileLoader.loadUserProfile(mScreenname);
		mUiHelper = new UiHelper(pbProfileWait, tvProfileWait);
		mProfileCardBuilder = new ProfileCardBuilder(mContext);
		if(userProfile != null){
			mUiHelper.setWaitVisibility(false);
			Card card = mProfileCardBuilder.buildCard(mPicpath, userProfile, mIsAuthor);
			setContentView(card.getView());
		}
	}

}
