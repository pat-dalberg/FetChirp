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

package com.dalberg.glass.fetchirp.utility;

import java.io.File;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.dalberg.glass.fetchirp.R;
import com.dalberg.glass.fetchirp.async.ProfileImageLoader;
import com.dalberg.glass.fetchirp.model.UserProfile;
import com.google.android.glass.app.Card;

public class ProfileCardBuilder {
	
	private static final String TAG = "ProfileCardBuilder";
	
	private Context mContext;
	private FileUtilities mFileUtilities;
	private ProfileImageLoader mProfileImageLoader;

	public ProfileCardBuilder(Context context) {
		mContext = context;
		mFileUtilities = new FileUtilities();
		mProfileImageLoader = new ProfileImageLoader(mContext);
	}
	
	public Card buildCard(String picpath, UserProfile userProfile, boolean isAuthor){
		Card card = new Card(mContext);
		card.setImageLayout(Card.ImageLayout.LEFT);
		if(isAuthor){
			String filename = mFileUtilities.getFilename(picpath);
			File pic = mFileUtilities.getFileByName(filename);
			card.addImage(Uri.fromFile(pic));
		}else{
			card.addImage(R.drawable.profile_default);
			String profileUrl = userProfile.profile_image_url.replace("_normal", "");
			Log.i(TAG, "profileUrl = " + profileUrl);
			mProfileImageLoader.getProfileImage(profileUrl, card);
		}
		StringBuilder sb = new StringBuilder()
		.append(userProfile.name)
		.append(" ")
		.append(userProfile.location)
		.append(" ")
		.append(userProfile.description);
		card.setText(sb.toString());
		
		return card;
	}

}
