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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.location.Location;

public class GeoTweetHelper {
	
	private static final String BASE_URL = "https://api.twitter.com/1.1/search/tweets.json?"; 
	
	public static final String OAUTH_URL = "https://api.twitter.com/oauth2/token";
	
	private static final String PROFILE_URL = "https://api.twitter.com/1.1/users/show.json?screen_name=";
	
	private static String mRadius = "%dmi";

	public GeoTweetHelper() {
		
	}
	
	public static String searchUrl(Location location, int radius, int count){
		String near = String.format(mRadius, radius);
		StringBuilder url = new StringBuilder();
		url.append(BASE_URL);
		url.append("q=&");
		url.append(String.format("geocode=%f,%f,%s", location.getLatitude(),location.getLongitude(),near));
		url.append(String.format("&result_type=recent&count=%d", count));		
		return url.toString();
	}
	
	public static String searchHashtagUrl(String hashtag, int count){
		String ht = null;
		try {
			ht = URLEncoder.encode(hashtag.substring(hashtag.indexOf("#") + 1), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuilder url = new StringBuilder()
		.append(BASE_URL)
		.append("q=")
		.append(ht)
		.append(String.format("&result_type=recent&count=%d", count));
		return url.toString();
	}
	
	public static String profileUrl(String screenname){
		StringBuilder profile = new StringBuilder(PROFILE_URL)
		.append(screenname);
		return profile.toString();
	}

}
