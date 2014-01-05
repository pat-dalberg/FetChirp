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

package com.dalberg.glass.fetchirp.adapter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;

public class TweetCardsAdapter extends CardScrollAdapter {

	private ArrayList<Card> mCards;
	
	public TweetCardsAdapter() {

	}
	
	public TweetCardsAdapter(ArrayList<Card> cards){
		mCards = cards;
	}

	@Override
	public int findIdPosition(Object id) {		
		return -1;
	}

	@Override
	public int findItemPosition(Object item) {
		return mCards.indexOf(item);
	}

	@Override
	public int getCount() {
		return mCards.size();
	}

	@Override
	public Object getItem(int position) {
		return mCards.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return mCards.get(position).toView();
	}

}
