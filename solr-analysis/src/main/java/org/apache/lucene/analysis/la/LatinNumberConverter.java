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

import java.util.HashMap;

/**
 * Latin Number Converter.
 * @author Markus Klose, Waldemar Erhardt
 * http://www.utf8-zeichentabelle.de/unicode-utf8-table.pl?start=8192&number=1024&unicodeinhtml=hex&htmlent=1&view=3
 * http://de.wikipedia.org/wiki/R%C3%B6mische_Zahlen#Gro.C3.9Fe_Zahlen
 */
public class LatinNumberConverter {

	private boolean strict = false;
	
	// create a map with the latin letters and their corresponding values
	private static final HashMap<Character, Integer> latinArabicMap = new HashMap<Character, Integer>();

	static {
		latinArabicMap.put('\u2188', 100000);
		latinArabicMap.put('\u2187', 50000);
		latinArabicMap.put('\u2182', 10000);
		latinArabicMap.put('\u2181', 5000);
		latinArabicMap.put('m', 1000);
		latinArabicMap.put('d', 500);
		latinArabicMap.put('c', 100);
		latinArabicMap.put('l', 50);
		latinArabicMap.put('x', 10);
		latinArabicMap.put('v', 5);
		latinArabicMap.put('i', 1);
	}
	
	/** default constructor */
	public LatinNumberConverter(boolean strict) {
		this.strict = strict;
	}
	
	/**
	 * entry point for converting latin number to arabic one.
	 * 
	 * @author Markus Klose
	 * 
	 * @param termBuffer
	 * 	term buffer containing token
	 * @param termLength
	 * 	length of the token
	 * @return
	 * 	arabic value of latin number; null if token is not a number
	 */
	public String format(char termBuffer[], int termLength) {   
		return this.latinToArabic(termBuffer, termLength);
	}
	  
	/**
	 * doing the conversion.
	 * 
	 * @author Markus Klose, Waldemar Erhardt
	 * 
	 * @param termBuffer
	 * 	term buffer containing token
	 * @param termLength
	 * 	length of the token
	 * @return
	 * 	arabic value of latin number; null if token is not a number
	 * 
	 */
	private String latinToArabic(char termBuffer[], int termLength) {
		// check if term is a valid latin number 
		if (this.validate(termBuffer, termLength)) {
			
			String latin = String.valueOf(termBuffer, 0, termLength);
			String arabic = this.convertLatinToArabic(latin, strict);
			if(latin.equals(arabic)) {
				return null;
			}
			return arabic;
			
		} else {
			return null;
		}
	}
	
	/**
	 * Converts the latin letter into an arabic value.
	 * @param latin The latin letter.
	 * @return The arabic value or <code>0</code>.
	 * @author Waldemar Erhardt
	 */
	private int convertLatinToArabic(char latin) {
		
		Integer arabic = latinArabicMap.get(Character.toLowerCase(latin));
		if(arabic != null) {
			return arabic.intValue();
		}
		return 0;
	}

	/**
	 * Check if the given latin letter is a main char. Main chars are 'M', 'C', 'X' and 'I'.
	 * @param latin The latin letter to check.
	 * @return <code>true</code> if the letter is a main char, else <code>false</code>
	 * @author Waldemar Erhardt
	 */
	private boolean isMainChar(char latin) {
		latin = Character.toLowerCase(latin);
		return latin == 'm' || latin == 'c' || latin == 'x' || latin == 'i' || latin == '\u2182' || latin == '\u2188';

	}

	private static final int HELP_CHAR = 0;
	private static final int MAIN_CHAR = 1;
	private static final int SUBTRACTION = 2;
	
	/**
	 * Converts the given latin letters into an arabic value. <br/>
	 * (Based on http://www.diaware.de/html/roemzahl.html)
	 * @param latin The latin value to convert.
	 * @param strict Whether stricter validation should be used or not.
	 * @return The arabic value of the latin value, or if some validation 
	 * was violated the original latin value.
	 * @author Waldemar Erhardt
	 */
	private String convertLatinToArabic(String latin, boolean strict) {

        int maxValue = convertLatinToArabic('\u2188') + 1;
        int oldValue = convertLatinToArabic('\u2188');
        int currentArabicValue = 0;
        int charCounter = 0;
        int charType = HELP_CHAR; // 1 = main char; 0 = help char; 2 = subtraction
        int arabicValue = 0;
        
        for(int i = 0; i < latin.length(); i++) {
			// check if its a subtraction
			if ((i + 1) < latin.length() && convertLatinToArabic(latin.charAt(i)) < convertLatinToArabic(latin.charAt(i + 1))) {
				// a subtraction needs always a main char before another char
				if (isMainChar(latin.charAt(i)) == false) {
					// syntax error: help char is not allowed to be the first char
					return latin;
				}
				// it is an subtraction
				currentArabicValue = convertLatinToArabic(latin.charAt(i + 1)) - convertLatinToArabic(latin.charAt(i));
				charType = SUBTRACTION;
			} else {
				currentArabicValue = convertLatinToArabic(latin.charAt(i));
				charType = (isMainChar(latin.charAt(i)) == true) ? MAIN_CHAR : HELP_CHAR;
			}

			if (oldValue < currentArabicValue) {
				// syntax error: the chars are not ordered with decreasing valency from left to right
				return latin;
			}

			// if strict mode is activated, do further validation
			if (strict) {
				if (charType != SUBTRACTION && maxValue < convertLatinToArabic(latin.charAt(i))) {
					// syntax error: subtraction rule violated
					return latin;
				}
				if (charType == SUBTRACTION && maxValue < convertLatinToArabic(latin.charAt(i + 1))) {
					// syntax error: subtraction rule violated
					return latin;
				}
				if (charType == SUBTRACTION && (convertLatinToArabic(latin.charAt(i + 1)) / convertLatinToArabic(latin.charAt(i))) > 10) {
					// syntax error: subtraction rule violated
					return latin;
				}
			}

			if (i > 0 && charType != SUBTRACTION && oldValue == currentArabicValue) {
				charCounter++;
				if (charType == MAIN_CHAR && charCounter == 3) {
					// syntax error: it is not allowed to have more than 3 main char in succession
					return latin;
				}
				if (charType == HELP_CHAR && charCounter == 1) {
					// syntax error: it is not allowed to have the same help char in succession
					return latin;
				}
			} else {
				charCounter = 0;
			}

			// prepare for next iteration
			arabicValue += currentArabicValue;
			oldValue = currentArabicValue;
			if (charType == SUBTRACTION) {
				maxValue = convertLatinToArabic(latin.charAt(i));
			}
			if (charType == SUBTRACTION) {
				i++;
			}
		}
		return String.valueOf(arabicValue);
	}
	
	
	/**
	 * validating the token.
	 * 
	 * @author Markus Klose
	 * 
	 * @param termBuffer
	 * 	term buffer containing token
	 * @param termLength
	 * 	length of the token
	 * @return
	 * 	if there are non valid chars return false, else true
	 */
	private boolean validate(char termBuffer[], int termLength) {
		char toValidate;
		
		for (int i = 0; i < termLength; i++) {
			toValidate = Character.toLowerCase(termBuffer[i]);
			if (toValidate != 'i' && toValidate != 'v' && toValidate != 'x' && toValidate != 'l'
					&& toValidate != 'c' && toValidate != 'd' && toValidate != 'm' 
					&& toValidate != '\u2181' && toValidate != '\u2182' && toValidate != '\u2187' && toValidate != '\u2188')
				return false;
		}
		//TODO
		return true;
	}
	
}