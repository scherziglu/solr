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

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Latin Stemmer.
 * based on http://snowball.tartarus.org/otherapps/schinke/intro.html
 * @author Markus Klose
 */
public class LatinStemmer {
	//TODO queList as txt file an property in schema.xml ???
	
	/** latin locale - no country specified */
	private static Locale locale = new Locale("la");
	
	/** list contains words ending with 'que' that should not be stemmed */
	private List<String> queList;
	
	/**
	 *	default constructor.
	 *
	 *	@author mk 
	 */
	public LatinStemmer() {
		// initialize the queList
		queList = Arrays.asList("atque", "quoque", "neque", "itaque", "absque", "apsque", "abusque", "adaeque", "adusque", "denique",
				"deque", "susque", "oblique", "peraeque", "plenisque", "quandoque", "quisque", "quaeque",
				"cuiusque", "cuique", "quemque", "quamque", "quaque", "quique", "quorumque", "quarumque",
				"quibusque", "quosque", "quasque", "quotusquisque", "quousque", "ubique", "undique", "usque",
				"uterque", "utique", "utroque", "utribique", "torque", "coque", "concoque", "contorque",
				"detorque", "decoque", "excoque", "extorque", "obtorque", "optorque", "retorque", "recoque",
				"attorque", "incoque", "intorque", "praetorque");
	}
	
	/**
	 * check if token ends with 'que' and if it should be stemmed
	 * @author mk
	 * 
	 * @param termBuffer
	 * 	term buffer containing token
	 * @param termLength
	 * 	length of the token
	 * @return 
	 * 	current termLength  (termLength - 3' if token ends with 'que'),<br/> if token should not be stemmed return -1
	 */
	public int stemQUE(char[] termBuffer, int termLength) {
		// buffer to token
		String currentToken = String.valueOf(termBuffer, 0, termLength).toLowerCase(locale);

		// check if token should be stemmed
		if (queList.contains(currentToken)) {
			// dont stem the token
			return -1;
		}
		
		// chekc if token ends with 'que'
		if (currentToken.endsWith("que")) {
			// cut of 'que'
			return termLength - 3;
		}
		return termLength;
	}


	/**
	 * removing known noun suffixe.<br/>
	 * changes to the snowball - additional suffixe: arum, erum, orum, ebus, uum, ium, ei, ui, im
	 * @author mk
	 * 
	 * @param termBuffer
	 * 	term buffer containing token
	 * @param termLength
	 * 	length of the token
	 * @return
	 * 	termLength after stemming
	 */
	public String stemAsNoun(char termBuffer[], int termLength) {
		// buffer to string
		String noun = String.valueOf(termBuffer, 0, termLength).toLowerCase(locale);

		// check longest suffix
        if ((noun.endsWith("ibus") || noun.endsWith("arum") || noun.endsWith("erum") || noun.endsWith("orum") || noun.endsWith("ebus")) && noun.length() >= 6) {
        	return String.valueOf(termBuffer, 0, termLength - 4);
        } else  if ((noun.endsWith("ius") || noun.endsWith("uum") || noun.endsWith("ium")) && noun.length() >= 5) {
        	return String.valueOf(termBuffer, 0, termLength - 3);
        } else  if ((noun.endsWith("ae") || noun.endsWith("am") || noun.endsWith("as") || noun.endsWith("em") || noun.endsWith("es")
        		|| noun.endsWith("ia") || noun.endsWith("is") || noun.endsWith("nt") || noun.endsWith("os") || noun.endsWith("ud")
        		|| noun.endsWith("um") || noun.endsWith("us") || noun.endsWith("ei") || noun.endsWith("ui") || noun.endsWith("im")) 
        		&& noun.length() >= 4) {
        	return String.valueOf(termBuffer, 0, termLength - 2);
        } else  if ((noun.endsWith("a") || noun.endsWith("e") || noun.endsWith("i") || noun.endsWith("o") || noun.endsWith("u")) && noun.length() >= 3) {
        	return String.valueOf(termBuffer, 0, termLength - 1);
        }

		// stem nothing
		return  String.valueOf(termBuffer, 0, termLength);
	}

	/**
	 * removing / changing known verb suffixe.<br/>
	 * @author mk
	 * 
	 * @param termBuffer
	 * 	term buffer containing token
	 * @param termLength
	 * 	length of the token
	 * @return
	 * 	termLength after stemming
	 */
	public String stemAsVerb(char termBuffer[], int termLength) {
		// buffer to string
		String verb = String.valueOf(termBuffer, 0, termLength).toLowerCase(locale);
		
		// check suffixe
		if (verb.endsWith("iuntur") || verb.endsWith("erunt") || verb.endsWith("untur") || verb.endsWith("iunt") || verb.endsWith("unt")) {
			// 'iuntur' 'erunt' 'untur' 'iunt' 'unt' -> 'i'
			return this.verbSuffixToI(termBuffer, termLength);
	    } else  if (verb.endsWith("beris") || verb.endsWith("bor") || verb.endsWith("bo")) {
	    	// 'beris' 'bor' 'bo' -> 'bi'
	    	return this.verbSuffixToBI(termBuffer, termLength);
	    } else  if (verb.endsWith("ero") && termLength >= 5) {
	    	// 'ero' -> 'eri'
	    	termBuffer[termLength -1] = 'i';
	    	return String.valueOf(termBuffer, 0, termLength);
	    } else  if ((verb.endsWith("mini") || verb.endsWith("ntur") || verb.endsWith("stis")) && termLength >= 6) {
	    	// 'mini' 'ntur' 'stis' -> delete
	    	return String.valueOf(termBuffer, 0, termLength  - 4);
	    } else  if ((verb.endsWith("mus") || verb.endsWith("mur") || verb.endsWith("ris") || verb.endsWith("sti") || verb.endsWith("tis") || verb.endsWith("tur")) && termLength >= 5) {
	    	// 'mus' 'ris' 'sti' 'tis' 'tur' -> delete
	    	return String.valueOf(termBuffer, 0, termLength  - 3);
	    } else  if ((verb.endsWith("ns") || verb.endsWith("nt") || verb.endsWith("ri")) && termLength >= 4) {
	    	// 'ns' 'nt' 'ri' -> delete
	    	return String.valueOf(termBuffer, 0, termLength  - 2);
	    } else  if ((verb.endsWith("m") || verb.endsWith("r") || verb.endsWith("s") || verb.endsWith("t")) && termLength >= 3) {
	    	// 'm' 'r' 's' 't' -> delete
	    	return String.valueOf(termBuffer, 0, termLength  - 1);
	    }
		
		// stem nothing
		return String.valueOf(termBuffer, 0, termLength);
	}	
	/**
	 * general verb suffixe
	 * praesens indikativ aktiv -> o, s, t, mus, tis, (u)nt, is, it, imus, itis
	 * praesens konjunktiv aktiv -> am, as, at, amus, atis, ant, iam, ias, iat, iamus, iatis, iant
	 *
	 * imperfekt indikativ aktiv -> bam,bas,bat,bamus,batis,bant,   ebam,ebas,ebat,ebamus,ebatis,ebant
	 * imperfekt konjunktiv aktiv -> rem,res,ret,remus,retis,rent,   erem,eres,eret,eremus,eretis,erent
	 *	  
	 * futur 1 indikativ aktiv -> bo,bis,bit,bimus,bitis,bunt,   am,es,et,emus,etis,ent,   iam,ies,iet,iemus,ietis,ient
	 * futur 2 indikativ aktiv ->
	 *	  
	 * perfekt indikativ aktiv -> i,isti,it,imus,istis,erunt,
	 * perfekt konjunktiv aktiv -> erim,eris,erit,erimus,eritis,erint
	 *	  
	 * plusquamperfekt indikativ aktiv -> eram,eras,erat,eramus,eratis,erant
	 * plusquamperfekt konjunktiv aktiv -> issem,isses,isset,issemus,issetis,issent
	 */
	
	// helper methods
	/**
	 * replacing suffix with 'i'
	 * @param termBuffer
	 * 	term buffer containing token
	 * @param termLength
	 * 	length of the token
	 * @return
	 * 	stemmed verb
	 */
	private String verbSuffixToI(char termBuffer[], int termLength) {
		String verb = String.valueOf(termBuffer, 0, termLength).toLowerCase(locale);
		// 'iuntur' 'erunt' 'untur' 'iunt' 'unt' -> 'i'
		if (verb.endsWith("iuntur") && termLength >= 8) {
			return String.valueOf(termBuffer, 0, termLength - 5);
		} else if ((verb.endsWith("erunt") || verb.endsWith("untur")) && termLength >= 7) {
			termBuffer[termLength - 5] = 'i';
			return String.valueOf(termBuffer, 0, termLength - 4);
		} else if (verb.endsWith("iunt") && termLength >= 6) {;
			return String.valueOf(termBuffer, 0, termLength - 3);
		} else if (verb.endsWith("unt") && termLength >= 5) {
			termBuffer[termLength - 3] = 'i';
			return String.valueOf(termBuffer, 0, termLength - 2);
		} 
		return String.valueOf(termBuffer, 0, termLength);
	}
	
	/**
	 * replacing suffix with 'bi'
	 * @param termBuffer
	 * 	term buffer containing token
	 * @param termLength
	 * 	length of the token
	 * @return
	 * 	stemmed verb
	 */
	private String verbSuffixToBI(char termBuffer[], int termLength) {
		String verb = String.valueOf(termBuffer, 0, termLength).toLowerCase(locale);
		// 'beris' 'bor' 'bo' -> 'bi'
		if (verb.endsWith("beris") && termLength >= 7) {
			termBuffer[termLength - 4] = 'i';
			return String.valueOf(termBuffer, 0, termLength - 3);
		} else if (verb.endsWith("bor") && termLength >= 5) {
			termBuffer[termLength - 2] = 'i';
			return String.valueOf(termBuffer, 0, termLength - 1);
		} else if (verb.endsWith("bo") && termLength >= 4) {;
			termBuffer[termLength - 1] = 'i';
			return String.valueOf(termBuffer, 0, termLength);
		}
		return String.valueOf(termBuffer, 0, termLength);
	}
}
