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
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.dalberg.glass.fetchirp.utility.AppConstants;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

public class SettingsActivity extends Activity {

	private static final String TAG = "SettingsActivity";
	
	private int mCount = 30;
	
	private int mRadius = 5;
	
	private boolean isSettingRadius = false;
	
	private boolean isSettingCount = false;
	
	private SharedPreferences mPrefs;
	
	private GestureDetector mGestureDetector;
	
	private TextView tvInstructions, tvChangedCount, tvChangedInstructions;
	
	private Resources mRes; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);
		init();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_settings, menu);
		return true;
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		tvInstructions.setVisibility(View.INVISIBLE);
		tvChangedCount.setVisibility(View.VISIBLE);
		tvChangedInstructions.setVisibility(View.VISIBLE);
		if(item.getTitle().toString().contentEquals("change radius")){
			// radius
			isSettingRadius = true;
			isSettingCount = false;
			tvChangedCount.setText(mRes.getString(R.string.n_miles_radius, mRadius));
			tvChangedInstructions.setText(R.string.change_radius_instructions);
		}else if(item.getTitle().toString().contentEquals("change count")){
			// count
			isSettingRadius = false;
			isSettingCount = true;
			tvChangedCount.setText(mRes.getString(R.string.n_tweets, mCount));
			tvChangedInstructions.setText(R.string.change_count_instructions);
		}
		
		return true;
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
	
	private void initUi(){
		tvInstructions = (TextView) findViewById(R.id.tvInstructions);
		tvChangedCount = (TextView) findViewById(R.id.tvChangedCount); 
		tvChangedInstructions = (TextView) findViewById(R.id.tvChangedInstructions);
	}
	
	private void initOps(){
		mRes = this.getResources();
		mGestureDetector = createGestureDetector(this);
		mPrefs = this.getSharedPreferences(AppConstants.PREFS, Context.MODE_PRIVATE);
		mCount = mPrefs.getInt(AppConstants.PREFS_COUNT_KEY, 30);
		mRadius = mPrefs.getInt(AppConstants.PREFS_RADIUS_KEY, 5);

	}
	
	private GestureDetector createGestureDetector(Context context){
		GestureDetector gestureDetector = new GestureDetector(context);
		gestureDetector.setBaseListener(new GestureDetector.BaseListener() {
			
			@Override
			public boolean onGesture(Gesture gesture) {
				if(gesture == Gesture.TAP){
					if(isSettingRadius){
						setRadiusMiles(mRadius);						
					}else if(isSettingCount){
						setTweetsCount(mCount);
					}else{
						Intent intent = new Intent(getApplicationContext(), MainActivity.class);
						startActivity(intent);
					}
					return true;
				}else if(gesture == Gesture.TWO_TAP){
					openOptionsMenu();
					return true;
				}else if(gesture == Gesture.SWIPE_LEFT){
					//backward swipe ++
					if(isSettingRadius){
						mRadius++;
						tvChangedCount.setText(mRes.getString(R.string.n_tweets, mRadius));
					}else if(isSettingCount){
						mCount += 5;
						if(mCount > 100){
							mCount = 100;
						}
						tvChangedCount.setText(mRes.getString(R.string.n_tweets, mCount));
					}
					return true;
				}else if(gesture == Gesture.SWIPE_RIGHT){
					//forward swipe --
					if(isSettingRadius){
						mRadius--;
						if(mRadius <= 0){
							mRadius = 1;
						}
						tvChangedCount.setText(mRes.getString(R.string.n_tweets, mRadius));
					}else if(isSettingCount){
						mCount -= 5;
						if(mCount <= 5){
							mCount = 5;
						}
						tvChangedCount.setText(mRes.getString(R.string.n_tweets, mCount));
					}
					return true;
				}
				return false;
			}
		});		
		return gestureDetector;
	}
	
	private void setRadiusMiles(int miles){
		mPrefs.edit().putInt(AppConstants.PREFS_RADIUS_KEY, miles).commit();
		isSettingRadius = false;
		tvChangedCount.setVisibility(View.INVISIBLE);
		tvChangedInstructions.setVisibility(View.INVISIBLE);
		tvInstructions.setVisibility(View.VISIBLE);
	}
	
	private void setTweetsCount(int count){
		mPrefs.edit().putInt(AppConstants.PREFS_COUNT_KEY, count).commit();
		isSettingCount = false;
		tvChangedCount.setVisibility(View.INVISIBLE);
		tvChangedInstructions.setVisibility(View.INVISIBLE);
		tvInstructions.setVisibility(View.VISIBLE);
	}
	

}
