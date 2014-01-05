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

import android.location.Location;

public class GeoTweetHelper {
	
	private static final String BASE_URL = "https://api.twitter.com/1.1/search/tweets.json?"; 
	
	private static String mRadius = "%dmi";
	private static int mFetchRadius = 5;

	public GeoTweetHelper() {
		
	}
	
	public static void setFetchRadius(int radius){
		mFetchRadius = radius;
	}
	
	public static int getFetchRadius(){
		return mFetchRadius;
	}
	
	public static String searchUrl(Location location){
		String near = String.format(mRadius, mFetchRadius);
		StringBuffer url = new StringBuffer();
		url.append(BASE_URL);
		url.append("q=&");
		String geo = String.format("geocode=%f,%f,%s", location.getLatitude(),location.getLongitude(),near);
		url.append(geo);
		url.append("&result_type=recent");		
		return url.toString();
	}

}
