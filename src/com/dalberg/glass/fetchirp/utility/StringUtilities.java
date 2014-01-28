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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Patterns;

public class StringUtilities {
	
	private static final int FLAGS = Pattern.CASE_INSENSITIVE + Pattern.MULTILINE;
	
	private static Pattern patHashtag = Pattern.compile("(#+\\w+)", FLAGS);
	private static Pattern patMention = Pattern.compile("(@+\\w+)", FLAGS);


	private static String formatCreatedStamp(String created){
		String[] expanded = created.split(" ");
		StringBuffer time = new StringBuffer();
		time.append(expanded[0])
		.append(" ")
		.append(expanded[1])
		.append(" ")
		.append(expanded[2]);
		return time.toString();
	}
	
	public static String formatFooter(String screenName, String user, String time){
		StringBuffer footer = new StringBuffer();
		footer.append(user)
		.append(" ")
		.append("@")
		.append(screenName)
		.append(" ")
		.append(formatCreatedStamp(time));
		return footer.toString();
	}
	
	public static ArrayList<String> findHashtags(String text){
		ArrayList<String> hashtags = new ArrayList<String>();
		Matcher matcher = patHashtag.matcher(text);
		if(matcher.find()){
			if(matcher.groupCount() > 1){
				for(int i = 0;i < matcher.groupCount();i++){
					hashtags.add(matcher.group(i));
				}
			}else{
				hashtags.add(matcher.group(0));
			}
		}
		return hashtags;
	}
	
	public static String findUrl(String text){
		String url;
		Matcher matcher = Patterns.WEB_URL.matcher(text);
		url = matcher.group(0);
		return url;
	}
	
	public static ArrayList<String> findMentions(String text){
		ArrayList<String> mentions = new ArrayList<String>();
		Matcher matcher = patMention.matcher(text);
		if(matcher.find()){
			if(matcher.groupCount() > 1){
				for(int i = 0;i < matcher.groupCount();i++){
					mentions.add(matcher.group(i));
				}
			}else{
				mentions.add(matcher.group(0));
			}
		}
		return mentions;
	}
	
	public static String attifyScreenname(String screenname){
		StringBuilder sb = new StringBuilder("@")
		.append(screenname);
		return sb.toString();
	}
	
	public static String deAttifyMention(String mention){
		return mention.substring(mention.indexOf("@") + 1, mention.indexOf("'"));
	}

	
	public static String extractUrl(String urlTitle){
		return urlTitle.substring(urlTitle.indexOf("http"));
	}
	
	public static String extractHashtag(String htTitle){
		return htTitle.substring(htTitle.indexOf("#") + 1);
	}
}
