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
import android.content.Intent;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dalberg.glass.fetchirp.async.CredentialLoader;
import com.dalberg.glass.fetchirp.async.NearbyTweetsLoader;
import com.dalberg.glass.fetchirp.utility.FileUtilities;
import com.dalberg.glass.fetchirp.utility.StringUtilities;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;

public class MainActivity extends Activity implements LocationListener{
	
	private static final String TAG = "MainActivity";
	
	private LocationManager mLocationManager;
	private TextView tvGettingLocation;
	private ProgressBar pbWait;
	
	private String mAccessToken;
	
	private Location mLocation;
	
	private Future<JsonObject> loading;
	
	private FileUtilities fileUtilities;
	
	private GestureDetector mGestureDetector;
	
	private Resources res;
	
	private CredentialLoader mCredentialLoader;
	
	private NearbyTweetsLoader mNearbyTweetsLoader;
	
	private String mProfileImagePath;
	
	private ArrayList<String> mMentions, mHashtags, mUrls = new ArrayList<String>();
	
	private static final int MENU_PROFILE_GROUP = 1;
	private static final int MENU_HASHTAG_GROUP = 2;
	private static final int MENU_URL_GROUP = 3;
	private static final int AUTHOR = 1;
	

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
	public boolean onPrepareOptionsMenu(Menu menu){
		if(mMentions == null && mHashtags == null && mUrls == null){
			return true;
		}
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
				Intent intent = new Intent(getApplicationContext(), ProfileCardActivity.class);
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
				Intent htIntent = new Intent(this, HashtagActivity.class);
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
	

	@Override
	public void onLocationChanged(Location location) {
		if(location != null){
			mNearbyTweetsLoader = new NearbyTweetsLoader(this, mAccessToken, loading, location, pbWait, tvGettingLocation);
			mNearbyTweetsLoader.setAccessToken(mCredentialLoader.getAccessToken());
			mNearbyTweetsLoader.loadNearbyTweets();
		}
		
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
		// TODO remove for production build
		//android.os.Debug.waitForDebugger();
		res = this.getResources();
		mGestureDetector = createGestureDetector(this);
		fileUtilities = new FileUtilities();
		fileUtilities.makeDir();
		fileUtilities.wipeFiles();
		mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String provider = mLocationManager.getBestProvider(criteria, true);
		mLocationManager.requestLocationUpdates(provider, 3000, 1000, this);
		mLocation = mLocationManager.getLastKnownLocation(provider);
		mCredentialLoader = new CredentialLoader(getApplicationContext());
		mCredentialLoader.getCredentials();
		mAccessToken = mCredentialLoader.getAccessToken();
		if(mLocation != null){
			mNearbyTweetsLoader = new NearbyTweetsLoader(this, mAccessToken, loading, mLocation, pbWait, tvGettingLocation);
			mNearbyTweetsLoader.setAccessToken(mCredentialLoader.getAccessToken());
			mNearbyTweetsLoader.loadNearbyTweets();
		}
	}
	
	private void initUi(){
		tvGettingLocation = (TextView)findViewById(R.id.tvGettingLocation);
		pbWait = (ProgressBar)findViewById(R.id.pbWait);
	}
	
	private GestureDetector createGestureDetector(Context context){
		GestureDetector gestureDetector = new GestureDetector(context);
		gestureDetector.setBaseListener(new GestureDetector.BaseListener() {
			
			@Override
			public boolean onGesture(Gesture gesture) {
				if(gesture == Gesture.TAP){
					openOptionsMenu();																
					return true;
				}
		
				return false;
			}
		});		
		return gestureDetector;
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
