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

public class LatinNumberNormalizer {

	/**
	 * entry point for normalizing latin numbers.</br>
	 * replace latin numbers in apostrophus writing into single letter</br> 
	 * uni code characters will be replaced, e.g. 'VI' (one character) is replaced with 'VI' (two characters) 
	 * see http://de.wikipedia.org/wiki/R%C3%B6mische_Zahlschrift#Schreibweise_mit_Apostrophus
	 * @author Markus Klose
	 * 
	 * @param termBuffer
	 * 	term buffer containing token
	 * @param termLength
	 * 	length of the token
	 * @return
	 * 	normalized number
	 */
	public String format(char termBuffer[], int termLength) {
		String currentToken = String.valueOf(termBuffer, 0, termLength);
		
		// step 1 - replace Apostrophus writings
		currentToken = this.replaceApostrophus(currentToken);
		
		// step 2 - replace further latin unicode chars
		currentToken = this.replaceLatinUnicode(currentToken);
		
		// return normalized number
		return currentToken;
	}

	/**
	 * replaced latin numbers in apostrophus writing into single letter. 
	 * @param currentToken
	 * 	current token
	 * @return
	 * 	string without apostrophus
	 */
	private String replaceApostrophus(String currentToken) {
		//TODO lowercase?
		String removedApostrophus = currentToken;
	
		// 100.000
		removedApostrophus = removedApostrophus.replaceAll("CCCIↃↃↃ", String.valueOf('\u2188'));
		// 50.000
		removedApostrophus = removedApostrophus.replaceAll("IↃↃↃ", String.valueOf('\u2187'));
		// 10.000
		removedApostrophus = removedApostrophus.replaceAll("CCIↃↃ", String.valueOf('\u2182'));
		// 5.000
		removedApostrophus = removedApostrophus.replaceAll("IↃↃ", String.valueOf('\u2181'));
		// 1.000
		removedApostrophus = removedApostrophus.replaceAll("CIↃ", "M");
		removedApostrophus = removedApostrophus.replaceAll("ↀ", "M");
		// 500
		removedApostrophus = removedApostrophus.replaceAll("IↃ", "D");
		
		return removedApostrophus;	
	}
	
	/**
	 * replacing unicode letters with corresponding letter combination. 
	 * @param chars
	 * 	array of chars
	 * @param length
	 * 	length of the token
	 * @return
	 * 	string withg replaced chars
	 */
	private String replaceLatinUnicode(String currentToken) {
		String normalizedNumber = "";
		// normalize chars
		for (int i = 0; i < currentToken.length(); i++) {
		    switch(currentToken.charAt(i)) {
		    	// upper case
		      	case '\u2160': normalizedNumber += "I"; break;
		      	case '\u2161': normalizedNumber += "II"; break;
		      	case '\u2162': normalizedNumber += "III"; break;
		      	case '\u2163': normalizedNumber += "IV"; break;
		      	case '\u2164': normalizedNumber += "V"; break;
		      	case '\u2165': normalizedNumber += "VI"; break;
		      	case '\u2166': normalizedNumber += "VII"; break;
		      	case '\u2167': normalizedNumber += "VIII"; break;
		      	case '\u2168': normalizedNumber += "IX"; break;
		      	case '\u2169': normalizedNumber += "X"; break;
		      	case '\u216A': normalizedNumber += "XI"; break;
		      	case '\u216B': normalizedNumber += "XII"; break;
		      	case '\u216C': normalizedNumber += "L"; break;
		      	case '\u216D': normalizedNumber += "C"; break;
		      	case '\u216E': normalizedNumber += "D"; break;
		      	case '\u216F': normalizedNumber += "M"; break;
		      	// lower case
		      	case '\u2170': normalizedNumber += "i"; break;
		      	case '\u2171': normalizedNumber += "ii"; break;
		      	case '\u2172': normalizedNumber += "iii"; break;
		      	case '\u2173': normalizedNumber += "iv"; break;
		      	case '\u2174': normalizedNumber += "v"; break;
		      	case '\u2175': normalizedNumber += "vi"; break;
		      	case '\u2176': normalizedNumber += "vii"; break;
		      	case '\u2177': normalizedNumber += "viii"; break;
		      	case '\u2178': normalizedNumber += "ix"; break;
		      	case '\u2179': normalizedNumber += "x"; break;
		      	case '\u217A': normalizedNumber += "xi"; break;
		      	case '\u217B': normalizedNumber += "xii"; break;
		      	case '\u217C': normalizedNumber += "l"; break;
		      	case '\u217D': normalizedNumber += "c"; break;
		      	case '\u217E': normalizedNumber += "d"; break;
		      	case '\u217F': normalizedNumber += "m"; break;
		      	// reversed 100 (Ã¢â€ Æ’,Ã¢â€ â€ž)
		      	case '\u2183': normalizedNumber += "C"; break;
		      	case '\u2184': normalizedNumber += "c"; break;
		      	// 6 late form
		      	case '\u2185': normalizedNumber += "VI"; break;
		      	// 50 early form
		      	case '\u2186': normalizedNumber += "L"; break;
		      	// default
		      	default: normalizedNumber += currentToken.charAt(i);
		    }
		}
		return normalizedNumber;
	}
}
