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

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.dalberg.glass.fetchirp.async.HashtagLoader;
import com.dalberg.glass.fetchirp.utility.StringUtilities;

public class HashtagActivity extends Activity {
	
	private static final String TAG = "HashtagActivity";
	
	private TextView tvHashtagWait;
	
	private String mHashtag;
	private String mAccessToken;
	private String mProfileImagePath;
	
	private Resources res; 
	
	private ArrayList<String> mMentions, mHashtags, mUrls = new ArrayList<String>();
	
	private static final int MENU_PROFILE_GROUP = 1;
	private static final int MENU_HASHTAG_GROUP = 2;
	private static final int MENU_URL_GROUP = 3;
	private static final int AUTHOR = 1;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hashtag_card);
		init();
	}


	@Override
	public boolean onPrepareOptionsMenu(Menu menu){
		menu.clear();		
		if(mMentions.size() > 0){
			int order = 1;
			for(String mention : mMentions){
				if(order == 1){
					menu.add(MENU_PROFILE_GROUP, AUTHOR, Menu.NONE,res.getString(R.string.view_profile, StringUtilities.attifyScreenname(mention)));
				}else{
					menu.add(MENU_PROFILE_GROUP, Menu.NONE, Menu.NONE,res.getString(R.string.view_profile, StringUtilities.attifyScreenname(mention)));
				}
				order++;
			}
		}
		if(mHashtags.size() > 0){
			for(String hashtag : mHashtags){
				menu.add(MENU_HASHTAG_GROUP, Menu.NONE, Menu.NONE,res.getString(R.string.search_hashtag, hashtag));
			}
		}
		if(mUrls.size() > 0){
			for(String url : mUrls){
				menu.add(MENU_URL_GROUP, Menu.NONE, Menu.NONE,res.getString(R.string.view_website, url));
			}
		}
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getGroupId()){
			case MENU_PROFILE_GROUP:
				Intent intent = new Intent(this, ProfileCardActivity.class);
				if(item.getItemId() == AUTHOR){
					intent.putExtra("isauthor", true);
				}else{
					intent.putExtra("isauthor", false);
				}
				intent.putExtra("accesstoken", mAccessToken);
				intent.putExtra("screenname", StringUtilities.deAttifyMention(item.getTitle().toString()));
				intent.putExtra("profileimagepath", mProfileImagePath);
				startActivity(intent);
				break;
			case MENU_HASHTAG_GROUP:
				Intent htIntent = new Intent(getApplicationContext(), HashtagActivity.class);
				htIntent.putExtra("accesstoken", mAccessToken);
				htIntent.putExtra("hashtag", item.getTitle().toString());
				startActivity(htIntent);
				break;
			case MENU_URL_GROUP:
				browseUrl(item.getTitle().toString());
				break;
		}
		
		return true;
	}	
	
	private void init(){
		initUi();
		initOps();
	}
	
	private void initUi(){
		tvHashtagWait = (TextView)findViewById(R.id.tvHashtagWait);
	}
	
	private void initOps(){
		res = getResources();
		Intent intent = getIntent();
		mHashtag = intent.getStringExtra("hashtag");
		mAccessToken = intent.getStringExtra("accesstoken");
		HashtagLoader hashtagLoader = new HashtagLoader(this, mAccessToken);
		hashtagLoader.loadHashtagTweets(mHashtag);
		tvHashtagWait.setText(res.getString(R.string.searching_for_hashtag, mHashtag));
	}
	
	public void setTweetMenu(ArrayList<String> mentions, ArrayList<String> hashtags, ArrayList<String> urls){
		mMentions = mentions;
		mHashtags = hashtags;
		mUrls = urls;
	}
	
	public void setProfileImagePath(String path){
		mProfileImagePath = path;
	}
	
	private void browseUrl(String url){
		Uri uri = Uri.parse(StringUtilities.extractUrl(url));		
		startActivity(new Intent(Intent.ACTION_VIEW, uri));
	}	

}
