/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package cn.vlabs.clb.api;

import java.util.ArrayList;

public class SupportedFileFormatForOnLineViewer {
	private static ArrayList<String> supported=new ArrayList<String>();
	static{
		supported.add("doc");
		supported.add("docx");
		supported.add("ppt");
		supported.add("pptx");
		supported.add("xls");
		supported.add("xlsx");
		supported.add("rtf");
	}
	
	public static boolean isSupported(String fileformat){
		if(null==fileformat || "".equals(fileformat))
			return false;
		return supported.contains(fileformat);
	}
}
