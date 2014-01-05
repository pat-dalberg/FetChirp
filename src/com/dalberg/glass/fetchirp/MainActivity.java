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
import android.content.Context;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dalberg.glass.fetchirp.adapter.TweetCardsAdapter;
import com.dalberg.glass.fetchirp.async.ProfileImageLoader;
import com.dalberg.glass.fetchirp.model.Tweet;
import com.dalberg.glass.fetchirp.model.Tweets;
import com.dalberg.glass.fetchirp.utility.AppConstants;
import com.dalberg.glass.fetchirp.utility.FileUtilities;
import com.dalberg.glass.fetchirp.utility.GeoTweetHelper;
import com.dalberg.glass.fetchirp.utility.StringUtilities;
import com.dalberg.glass.vitrumloc.R;
import com.google.android.glass.app.Card;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardScrollView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends Activity implements LocationListener{
	
	private static final String TAG = "MainActivity";
	
	private LocationManager mLocationManager;
	private TextView tvGettingLocation, tvTapToChange, tvTapToSet, tvMilesRadius, tvSwipeToSet;
	private ProgressBar pbWait;
	
	private boolean isSettingRadius = false;
	
	private String mAccessToken;
	
	private Location mLocation;
	
	private Future<JsonObject> loading;
	
	private ArrayList<Card> mCards;
	
	private ProfileImageLoader profileImageLoader;
	
	private FileUtilities fileUtilities;
	
	private GestureDetector mGestureDetector;
	
	private Resources res;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}
	
	
	@Override
	protected void onPause(){
		super.onPause();
		mLocationManager = null;
		mGestureDetector = null;
	}
	

	@Override
	public void onLocationChanged(Location location) {
		mLocation = location;
		loadNearbyTweets();
		
	}


	@Override
	public void onProviderDisabled(String provider) {		
		
	}


	@Override
	public void onProviderEnabled(String provider) {		
		
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {		
		
	}
	
	@Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            return mGestureDetector.onMotionEvent(event);
        }
        return false;
    }
	
	private void init(){
		initUi();
		initOps();
	}

	private void initOps(){
		res = this.getResources();
		mGestureDetector = createGestureDetector(this);
		profileImageLoader = new ProfileImageLoader(this);
		fileUtilities = new FileUtilities();
		fileUtilities.makeDir();
		fileUtilities.wipeFiles();
		mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String provider = mLocationManager.getBestProvider(criteria, true);
		mLocationManager.requestLocationUpdates(provider, 3000, 1000, this);
		mLocation = mLocationManager.getLastKnownLocation(provider);
		getCredentials();
	}
	
	private void initUi(){
		tvGettingLocation = (TextView)findViewById(R.id.tvGettingLocation);
		tvTapToChange = (TextView)findViewById(R.id.tvTapToChange);
		pbWait = (ProgressBar)findViewById(R.id.pbWait);
		tvTapToSet = (TextView)findViewById(R.id.tvTapToSet); 
		tvMilesRadius = (TextView)findViewById(R.id.tvMilesRadius);
		tvSwipeToSet = (TextView)findViewById(R.id.tvSwipeToSet);
	}
	
	private GestureDetector createGestureDetector(Context context){
		GestureDetector gestureDetector = new GestureDetector(context);
		gestureDetector.setBaseListener(new GestureDetector.BaseListener() {
			
			@Override
			public boolean onGesture(Gesture gesture) {
				if(gesture == Gesture.TAP){
					toggleSettingRadius();
					if(isSettingRadius){
						if(loading != null){
							loading.cancel(true);
						}
						setRadiusVisibility(true);
						setWaitVisibility(false);
						setRadiusMiles(GeoTweetHelper.getFetchRadius());
						
					}else{
						setRadiusVisibility(false);
						setWaitVisibility(true);
						loadNearbyTweets();
					}
					
					
					return true;
				}else if(gesture == Gesture.SWIPE_LEFT){
					//backward swipe ++
					int radius = GeoTweetHelper.getFetchRadius();
					radius++;
					setRadiusMiles(radius);
					GeoTweetHelper.setFetchRadius(radius);
					return true;
				}else if(gesture == Gesture.SWIPE_RIGHT){
					//forward swipe --
					int radius = GeoTweetHelper.getFetchRadius();
					if(radius > 1){
						radius--;
					}else{
						radius = 1;
					}
					setRadiusMiles(radius);
					GeoTweetHelper.setFetchRadius(radius);
					return true;
				}
				
				return false;
			}
		});
		
		
		
		return gestureDetector;
	}
	
	private void setWaitVisibility(boolean visibility){
		if(visibility){
			pbWait.setVisibility(View.VISIBLE);
			tvGettingLocation.setVisibility(View.VISIBLE);
			tvTapToChange.setVisibility(View.VISIBLE);
		}else{
			pbWait.setVisibility(View.INVISIBLE);
			tvGettingLocation.setVisibility(View.INVISIBLE);
			tvTapToChange.setVisibility(View.INVISIBLE);
		}
	}
	
	private void setRadiusVisibility(boolean isVis){
		if(isVis){
			tvTapToSet.setVisibility(View.VISIBLE);
			tvMilesRadius.setVisibility(View.VISIBLE);
			tvSwipeToSet.setVisibility(View.VISIBLE);
		}else{
			tvTapToSet.setVisibility(View.INVISIBLE);
			tvMilesRadius.setVisibility(View.INVISIBLE);
			tvSwipeToSet.setVisibility(View.INVISIBLE);
		}
	}
	
	private void setRadiusMiles(int miles){
		tvMilesRadius.setText(res.getString(R.string.n_miles_radius, miles));
	}
	
	private void toggleSettingRadius(){
		if(isSettingRadius){
			isSettingRadius = false;
		}else{
			isSettingRadius = true;
		}
	}
	
	private void getCredentials() {
        Ion.with(this, "https://api.twitter.com/oauth2/token")
        .basicAuthentication(AppConstants.CONSUMER_KEY, AppConstants.CONSUMER_SECRET)
        .setBodyParameter("grant_type", "client_credentials")
        .asJsonObject()
        .setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e != null) {
                	setWaitVisibility(false);
                    Toast.makeText(MainActivity.this, "Error loading tweets", Toast.LENGTH_LONG).show();
                    return;
                }
                mAccessToken = result.get("access_token").getAsString();
                
            }
        });
    }
	
	private void loadNearbyTweets(){
		if (loading != null && !loading.isDone() && !loading.isCancelled() )
            return;
		String url = GeoTweetHelper.searchUrl(mLocation);		
		loading = Ion.with(this, url)
		        .setHeader("Authorization", "Bearer " + mAccessToken)
		        .asJsonObject()
		        .setCallback(new FutureCallback<JsonObject>(){
		            @Override
		            public void onCompleted(Exception e, JsonObject result) {
		                if (e != null) {
		                    Toast.makeText(MainActivity.this, "Error loading tweets", Toast.LENGTH_LONG).show();
		                    return;
		                }
		                setWaitVisibility(false);
		                if(isSettingRadius){
		                	return;
		                }
		                createCards(result);
		            }
		        });
		
		
	}
	
	private void createCards(JsonObject jsonObject){
		Log.i(TAG, jsonObject.toString());
		mCards = new ArrayList<Card>();
		Gson gson = new Gson();
		Tweets tweets = gson.fromJson(jsonObject, Tweets.class); 
		for(Tweet tweet : tweets.statuses){
			Card card = new Card(this);
			card.setImageLayout(Card.ImageLayout.LEFT);
			card.setText(tweet.text);
			String footer = StringUtilities.formatFooter(tweet.user.screen_name, tweet.user.name, tweet.created_at);
			card.setFootnote(footer);
			String profileUrl = tweet.user.profile_image_url.replace("_normal", "");
			profileImageLoader.getProfileImage(profileUrl, card);
			mCards.add(card);
						
		}
		CardScrollView cardScrollView = new CardScrollView(this);
		TweetCardsAdapter adapter = new TweetCardsAdapter(mCards);
		cardScrollView.setAdapter(adapter);
		cardScrollView.activate();
		setContentView(cardScrollView);
	}
	
}
