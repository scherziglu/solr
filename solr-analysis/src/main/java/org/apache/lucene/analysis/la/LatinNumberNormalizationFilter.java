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

public class LatinNumberNormalizationFilter extends TokenFilter {
	/** converter */
	private final LatinNumberNormalizer numberNormalizer;
	  
	/** attributes */
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
		  
	/** default constructor */
	public LatinNumberNormalizationFilter(TokenStream input) {
		super(input);
		this.numberNormalizer = new LatinNumberNormalizer();
	}
	  
	
	@Override
	public final boolean incrementToken() throws IOException {
		if (input.incrementToken()) {
			final String normalizedNumber = numberNormalizer.format(termAtt.buffer(), termAtt.length());
	        //change CharTermAttribute if not null
	        if (normalizedNumber != null) {
	        	termAtt.setEmpty().append(normalizedNumber);
	        	termAtt.setLength(normalizedNumber.length());
	        }
	        return true;
		} else {
			return false;
	    }
	}

}
