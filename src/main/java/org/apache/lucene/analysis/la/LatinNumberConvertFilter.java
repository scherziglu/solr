package org.apache.lucene.analysis.la;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;

/**
 * A {@link TokenFilter} that applies {@link LatinNumberConverter} to convert latin numbers.
 * @author Markus Klose, Waldemar Erhardt
 */
public class LatinNumberConvertFilter extends TokenFilter {
	/** converter */
	private final LatinNumberConverter numberFormatter;
	  
	/** attributes */
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final KeywordAttribute keywordAttr = addAttribute(KeywordAttribute.class);
	  
	/** default constructor */
	public LatinNumberConvertFilter(TokenStream input, boolean strictMode) {
		super(input);
		this.numberFormatter = new LatinNumberConverter(strictMode);
	}
	  
	@Override
	public final boolean incrementToken() throws IOException {
		if (input.incrementToken()) {
	    	
			// token is secured by KeywordMarkerFilter -> dont stem
			if (keywordAttr.isKeyword()) {
				return true;
			}
			
	    	
	        final String arabicNumber = numberFormatter.format(termAtt.buffer(), termAtt.length());
	        //change CharTermAttribute if not null
	        if (arabicNumber != null) {
	        	termAtt.setEmpty().append(arabicNumber);
	        	termAtt.setLength(arabicNumber.length());
	        }
	        return true;
		} else {
			return false;
	    }
	}
}
