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

import java.util.ArrayList;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.dalberg.glass.fetchirp.HashtagActivity;
import com.dalberg.glass.fetchirp.MainActivity;
import com.dalberg.glass.fetchirp.R;
import com.dalberg.glass.fetchirp.adapter.TweetCardsAdapter;
import com.dalberg.glass.fetchirp.async.ProfileImageLoader;
import com.dalberg.glass.fetchirp.model.Hashtag;
import com.dalberg.glass.fetchirp.model.Tweet;
import com.dalberg.glass.fetchirp.model.TweetCard;
import com.dalberg.glass.fetchirp.model.Tweets;
import com.dalberg.glass.fetchirp.model.Url;
import com.dalberg.glass.fetchirp.model.UserMention;
//import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class TweetCardBuilder {
	
	private static final String TAG = "CardBuilder";

	private MainActivity mainActivity = null;
	
	private HashtagActivity hashtagActivity = null;
	
	private ArrayList<CardBuilder> mCards;
	
	private ProfileImageLoader mProfileImageLoader;
	
	private CardScrollView mCardScrollView;
	
	private Tweets tweets;
	
	private FileUtilities mFileUtilities;
	
	public TweetCardBuilder(MainActivity ma) {
		mainActivity = ma;
		mProfileImageLoader = new ProfileImageLoader(mainActivity);
		mFileUtilities = new FileUtilities();
	}
	
	public TweetCardBuilder(HashtagActivity ha) {
		hashtagActivity = ha;
		mProfileImageLoader = new ProfileImageLoader(hashtagActivity);
		mFileUtilities = new FileUtilities();
	}

	
	public void createCards(JsonObject jsonObject){
		mCards = new ArrayList<CardBuilder>();
		Gson gson = new Gson();
		tweets = gson.fromJson(jsonObject, Tweets.class); 
		for(Tweet tweet : tweets.statuses){
			CardBuilder card;
			String footer = StringUtilities.formatFooter(tweet.user.screen_name, tweet.user.name, tweet.created_at);
			String profileUrl = tweet.user.profile_image_url.replace("_normal", "");
			if(mainActivity != null){
				card = new CardBuilder(mainActivity.getApplicationContext(), CardBuilder.Layout.COLUMNS)
				.setText(tweet.text)
				.addImage(R.drawable.profile_default)
				.setFootnote(footer);
				mProfileImageLoader.getProfileImage(profileUrl, card);

			}else{
				card = new CardBuilder(hashtagActivity.getApplicationContext(), CardBuilder.Layout.COLUMNS)
				.setText(tweet.text)
				.addImage(R.drawable.profile_default)
				.setFootnote(footer);
				mProfileImageLoader.getProfileImage(profileUrl, card);
			}
			//card.setImageLayout(Card.ImageLayout.LEFT);
//			card.setText(tweet.text);
//			card.addImage(R.drawable.profile_default);
//			card.setFootnote(footer);
//
//			mProfileImageLoader.getProfileImage(profileUrl, card);
			mCards.add(card);
						
		}
		if(mainActivity != null){
			mCardScrollView = new CardScrollView(mainActivity);		
		}else{
			mCardScrollView = new CardScrollView(hashtagActivity);
		}
		TweetCardsAdapter adapter = new TweetCardsAdapter(mCards);
		mCardScrollView.setAdapter(adapter);
		mCardScrollView.activate();
//		mCardScrollView.setOnItemClickListener(tapListener);
		if(mainActivity != null){
			mainActivity.setContentView(mCardScrollView);
		}else{
			hashtagActivity.setContentView(mCardScrollView);
		}
	}
	
	
//	OnItemClickListener tapListener = new OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> adapter, View view, int position,
//				long id) {
//			Tweet tweet = mCards.get(position);
//			View card = mCards.get(position).getView();
//			ArrayList<String> mentions = new ArrayList<String>();
//			ArrayList<String> hashtags = new ArrayList<String>();
//			ArrayList<String> urls = new ArrayList<String>();
//			mentions.add(tweet.user.screen_name);
//			for(UserMention mention : tweet.entities.user_mentions){
//				 mentions.add(mention.screen_name);
//			}
//			for(Hashtag hashtag : tweet.entities.hashtags){
//				hashtags.add(hashtag.text);
//			}
//			for(Url url : tweet.entities.urls){
//				urls.add(url.expanded_url);
//			}
//			if(mainActivity != null){
//				mainActivity.setTweetMenu(mentions, hashtags, urls);
//				String filename = mFileUtilities.getFilename(card.getImage(0).getPath());
//				StringBuilder path = new StringBuilder(AppConstants.FILEPATH)
//				.append("/")
//				.append(filename);
//				mainActivity.setProfileImagePath(path.toString());
//				mainActivity.openOptionsMenu();
//			}else{
//				hashtagActivity.setTweetMenu(mentions, hashtags, urls);
//				String filename = mFileUtilities.getFilename(card.getImage(0).getPath());
//				StringBuilder path = new StringBuilder(AppConstants.FILEPATH)
//				.append("/")
//				.append(filename);
//				hashtagActivity.setProfileImagePath(path.toString());
//				hashtagActivity.openOptionsMenu();
//			}
//
//		}
//	};


}
