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

package com.dalberg.glass.fetchirp.async;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.dalberg.glass.fetchirp.utility.AppConstants;
import com.dalberg.glass.fetchirp.utility.FileUtilities;
import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardBuilder;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.concurrent.Future;

public class ProfileImageLoader {

	private Context mContext;
		
	private FileUtilities fileUtilities; 

	public ProfileImageLoader(Context context) {
		mContext = context;
		fileUtilities = new FileUtilities();
	}
	
	public void getProfileImage(String url, final CardBuilder card) {
		final String filename = AppConstants.FILEPATH + "/" + fileUtilities.getFilename(url);
		Future<File> file = Ion.with(mContext)
        .load(url)
		.write(new File(filename))
		.setCallback(new FutureCallback<File>() {
			@Override
			public void onCompleted(Exception e, File file){
				if(e != null){
					e.printStackTrace();
					Log.e("ProfileImageLoader", e.getMessage());
				}else{
					card.clearImages();
                    Bitmap bitmap = BitmapFactory.decodeFile(filename);
					card.addImage(bitmap);
				}
			}
		});

	}

}
