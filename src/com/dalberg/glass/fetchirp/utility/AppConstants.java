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

import android.os.Environment;

public class AppConstants {

	public static final String CONSUMER_KEY = "A4S8660ox8Jxzmf47JJNg";
	public static final String CONSUMER_SECRET = "2iQciltRLUSyo1idoVOnHKXNWk0hWizyJ5XIg8CAXkU";
	
	public static final String FILEPATH = Environment.getExternalStorageDirectory().getPath() + "/FetChirp";
	
	public static final String CREDENTIALS_ERROR_MSG = "error getting credentials";
	public static final String TWEETS_ERROR_MSG = "error getting tweets";
	public static final String PROFILE_ERROR_MSG = "error getting profile";
	
	public static final String PREFS = "com.dalberg.glass.fetchirp";
	public static final String PREFS_COUNT_KEY = "com.dalberg.glass.fetchirp.count";
	public static final String PREFS_RADIUS_KEY = "com.dalberg.glass.fetchirp.radius";
	

}
