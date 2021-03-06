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

import java.io.File;


public class FileUtilities {
	
	private File mFile;

	public FileUtilities() {
		mFile = new File(AppConstants.FILEPATH);
	}
	
	
	public boolean makeDir(){ 		
		return mFile.mkdir();
	}
	
	public String getFilename(String url){
		String filename = url.substring(url.lastIndexOf("/") + 1);
		return filename;
	}
	
	public void wipeFiles(){		
		for(File file : mFile.listFiles()){
			file.delete();
		}
	}
	
	public File getFileByName(String name){
		File files[] = mFile.listFiles();
		for(File file : files){
			if(name.contentEquals(file.getName())){
				return file;
			}
		}
		return null;
	}




}
