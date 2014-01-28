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

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UiHelper {
	
	private ProgressBar pbWait;
	private TextView tvGettingLocation;

	public UiHelper(ProgressBar pb, TextView tvLocation) {
		pbWait = pb;
		tvGettingLocation = tvLocation;
	}
	
	
	public void setWaitVisibility(boolean visibility){
		if(visibility){
			pbWait.setVisibility(View.VISIBLE);
			tvGettingLocation.setVisibility(View.VISIBLE);

		}else{
			pbWait.setVisibility(View.INVISIBLE);
			tvGettingLocation.setVisibility(View.INVISIBLE);
		}
	}
	

}
