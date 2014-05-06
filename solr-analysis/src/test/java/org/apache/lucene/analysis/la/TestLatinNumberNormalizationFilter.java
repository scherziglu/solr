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
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.Tokenizer;

public class TestLatinNumberNormalizationFilter extends BaseTokenStreamTestCase {

	private Analyzer analyzer = new Analyzer() {
		@Override
	    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
			Tokenizer source = new MockTokenizer(reader, MockTokenizer.WHITESPACE, false);
			return new TokenStreamComponents(source, new LatinNumberNormalizationFilter(source));
	    }
	};
	
	public void testApostrophus() throws IOException {
		checkOneTerm(analyzer, "CCCIↃↃↃ", "\u2188");
		checkOneTerm(analyzer, "IↃↃↃ", "\u2187");
		checkOneTerm(analyzer, "CCIↃↃ", "\u2182");
		checkOneTerm(analyzer, "IↃↃ", "\u2181");
		checkOneTerm(analyzer, "CIↃ", "M");
		checkOneTerm(analyzer, "IↃ", "D");
		checkOneTerm(analyzer, "CCCIↃↃↃIↃↃↃCCIↃↃIↃↃCIↃIↃ", "\u2188\u2187\u2182\u2181MD");
	}
	
	public void testUpperCase() throws Exception {
		checkOneTerm(analyzer, "Ⅰ", "I");
		checkOneTerm(analyzer, "\u2160", "I");
		checkOneTerm(analyzer, "Ⅱ", "II");
		checkOneTerm(analyzer, "\u2161", "II");
		checkOneTerm(analyzer, "Ⅲ", "III");
		checkOneTerm(analyzer, "\u2162", "III");
		checkOneTerm(analyzer, "Ⅳ", "IV");
		checkOneTerm(analyzer, "\u2163", "IV");
		checkOneTerm(analyzer, "Ⅴ", "V");
		checkOneTerm(analyzer, "\u2164", "V");
		checkOneTerm(analyzer, "Ⅵ", "VI");
		checkOneTerm(analyzer, "\u2165", "VI");
		checkOneTerm(analyzer, "Ⅶ", "VII");
		checkOneTerm(analyzer, "\u2166", "VII");
		checkOneTerm(analyzer, "Ⅷ", "VIII");
		checkOneTerm(analyzer, "\u2167", "VIII");
		checkOneTerm(analyzer, "Ⅸ", "IX");
		checkOneTerm(analyzer, "\u2168", "IX");
		checkOneTerm(analyzer, "Ⅹ", "X");
		checkOneTerm(analyzer, "\u2169", "X");
		checkOneTerm(analyzer, "Ⅺ", "XI");
		checkOneTerm(analyzer, "\u216A", "XI");
		checkOneTerm(analyzer, "Ⅻ", "XII");
		checkOneTerm(analyzer, "\u216B", "XII");
		checkOneTerm(analyzer, "Ⅼ", "L");
		checkOneTerm(analyzer, "\u216C", "L");
		checkOneTerm(analyzer, "Ⅽ", "C");
		checkOneTerm(analyzer, "\u216D", "C");
		checkOneTerm(analyzer, "Ⅾ", "D");
		checkOneTerm(analyzer, "\u216E", "D");
		checkOneTerm(analyzer, "Ⅿ", "M");
		checkOneTerm(analyzer, "\u216F", "M");
		checkOneTerm(analyzer, "Ⅰ\u2160Ⅱ\u2161Ⅲ\u2162Ⅳ\u2163Ⅴ\u2164Ⅵ\u2165Ⅶ\u2166Ⅷ\u2167Ⅸ\u2168Ⅹ\u2169Ⅺ\u216AⅫ\u216BⅬ\u216CⅭ\u216DⅮ\u216EⅯ\u216F", "IIIIIIIIIIIIIVIVVVVIVIVIIVIIVIIIVIIIIXIXXXXIXIXIIXIILLCCDDMM");
	}
	
	public void testLowerCase() throws Exception {
		checkOneTerm(analyzer, "ⅰ", "i");
		checkOneTerm(analyzer, "\u2170", "i");
		checkOneTerm(analyzer, "ⅱ", "ii");
		checkOneTerm(analyzer, "\u2171", "ii");
		checkOneTerm(analyzer, "ⅲ", "iii");
		checkOneTerm(analyzer, "\u2172", "iii");
		checkOneTerm(analyzer, "ⅳ", "iv");
		checkOneTerm(analyzer, "\u2173", "iv");
		checkOneTerm(analyzer, "ⅴ", "v");
		checkOneTerm(analyzer, "\u2174", "v");
		checkOneTerm(analyzer, "ⅵ", "vi");
		checkOneTerm(analyzer, "\u2175", "vi");
		checkOneTerm(analyzer, "ⅶ", "vii");
		checkOneTerm(analyzer, "\u2176", "vii");
		checkOneTerm(analyzer, "ⅷ", "viii");
		checkOneTerm(analyzer, "\u2177", "viii");
		checkOneTerm(analyzer, "ⅸ", "ix");
		checkOneTerm(analyzer, "\u2178", "ix");
		checkOneTerm(analyzer, "ⅹ", "x");
		checkOneTerm(analyzer, "\u2179", "x");
		checkOneTerm(analyzer, "ⅺ", "xi");
		checkOneTerm(analyzer, "\u217A", "xi");
		checkOneTerm(analyzer, "ⅻ", "xii");
		checkOneTerm(analyzer, "\u217B", "xii");
		checkOneTerm(analyzer, "ⅼ", "l");
		checkOneTerm(analyzer, "\u217C", "l");
		checkOneTerm(analyzer, "ⅽ", "c");
		checkOneTerm(analyzer, "\u217D", "c");
		checkOneTerm(analyzer, "ⅾ", "d");
		checkOneTerm(analyzer, "\u217E", "d");
		checkOneTerm(analyzer, "ⅿ", "m");
		checkOneTerm(analyzer, "\u217F", "m");
		checkOneTerm(analyzer, "ⅰ\u2170ⅱ\u2171ⅲ\u2172ⅳ\u2173ⅴ\u2174ⅵ\u2175ⅶ\u2176ⅷ\u2177ⅸ\u2178ⅹ\u2179ⅺ\u217Aⅻ\u217Bⅼ\u217Cⅽ\u217Dⅾ\u217Eⅿ\u217F", "iiiiiiiiiiiiivivvvviviviiviiviiiviiiixixxxxixixiixiillccddmm");
	}

	public void testSpecials() throws Exception {
		checkOneTerm(analyzer, "Ↄ", "C");
		checkOneTerm(analyzer, "\u2183", "C");
		checkOneTerm(analyzer, "ↄ", "c");
		checkOneTerm(analyzer, "\u2184", "c");
		checkOneTerm(analyzer, "\u2185", "VI");
		checkOneTerm(analyzer, "\u2186", "L");
	}
}
