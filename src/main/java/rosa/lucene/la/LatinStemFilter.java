package rosa.lucene.la;

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
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/**
 * A {@link TokenFilter} that applies {@link LatinStemmer} to stem Latin words.
 * @author Markus Klose
 */
public final class LatinStemFilter extends TokenFilter {
	/** stemmer */   
	private final LatinStemmer stemmer = new LatinStemmer();
  
	/** attributes */
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
	private final PositionIncrementAttribute posIncAtt = addAttribute(PositionIncrementAttribute.class);
	private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
	private final KeywordAttribute keywordAttr = addAttribute(KeywordAttribute.class);
  
	/** flag that indicates if input should be incremented */
	private boolean stemAsNoun = true;
  
	/** token types */
	public static final String TYPE_NOUN = "LATIN_NOUN";
	public static final String TYPE_VERB = "LATIN_VERB";
  
	/** current token information */
	private char[] currentTokenBuffer;
	private int currentTokenLength;
	private int currentTokenStart;
	private int currentTokenEnd;
	private int currentTokenPosition;

	/** default constructor */
	public LatinStemFilter(TokenStream input) {
		super(input);
	}
  
	/**
	 * Entry point for latin stemming.<br/>
	 * step 1 - replace 'v' with 'u' and 'j' with 'i'<br/>
	 * step 2 - check for tokens ending with 'que' <br/>
	 * step 3 - stem nouns or verb 
	 * 
	 * @author markus klose
	 * 
	 * @see org.apache.lucene.analysis.TokenStream#incrementToken()
	 */
	@Override
	public final boolean incrementToken() throws IOException {
		if (currentTokenBuffer == null) {
			if (!input.incrementToken()) {
				return false;
			} else {
				// token is secured by KeywordMarkerFilter -> dont stem
				if (keywordAttr.isKeyword()) {
					return true;
				}
				
				// buffer current input
				currentTokenBuffer = termAtt.buffer().clone();
				currentTokenLength = termAtt.length();
				currentTokenStart = offsetAtt.startOffset();
		        currentTokenEnd = offsetAtt.endOffset();
		        currentTokenPosition = posIncAtt.getPositionIncrement();
			}
		}	

		// reset token attributes
		clearAttributes();
		
		/** step 1 - replace 'v' and 'j' (case sensitive) */
		this.replaceVJ(currentTokenBuffer, currentTokenLength);

		String stemmedToken;
		/** step 2 - check for words to stem ending with 'que' */
		int termLength = stemmer.stemQUE(currentTokenBuffer, currentTokenLength);
		if (termLength == -1) {
			// write original buffer as noun and verb	
			stemmedToken = String.valueOf(currentTokenBuffer, 0,  currentTokenLength);
		} else {
			/** step 3 - stem as noun or verb */
			if (stemAsNoun) {
				stemmedToken = stemmer.stemAsNoun(currentTokenBuffer, termLength);
			} else {
				stemmedToken = stemmer.stemAsVerb(currentTokenBuffer, termLength);
			}
		}
    	
		// switch from noun to verb or vice versa
		String tokenType;
		if(stemAsNoun) {
			stemAsNoun = false;
			tokenType = TYPE_NOUN;
			posIncAtt.setPositionIncrement(currentTokenPosition);
		} else {
			stemAsNoun = true;
			tokenType = TYPE_VERB;
			// reset buffer
			currentTokenBuffer = null;
			currentTokenLength = -1;
			posIncAtt.setPositionIncrement(0);
		}
		
		// create output token
		termAtt.setEmpty().append(stemmedToken);
		termAtt.setLength(stemmedToken.length());
		offsetAtt.setOffset(currentTokenStart, currentTokenEnd);
		typeAtt.setType(tokenType);
		
		return true;
	}

	/**
	 * Replace replace 'v' with 'u' and 'j' with 'i' (case sensitive).
	 * 
	 * @author markus klose
	 * 
	 * @param termBuffer
	 * 	term buffer containing token
	 * @param termLength
	 * 	length of the token
	 */
	private void replaceVJ(char termBuffer[], int termLength) {
		for (int i = 0; i < termLength; i++) {
			switch(termBuffer[i]) {
		  		case 'V': termBuffer[i] = 'U'; break;
		  		case 'v': termBuffer[i] = 'u'; break;
		  		case 'J': termBuffer[i] = 'I'; break;
		  		case 'j': termBuffer[i] = 'i'; break;
			}
		}
	}
}
