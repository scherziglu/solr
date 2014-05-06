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

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.junit.Before;

public class TestLatinNumberNormalizationFilterFactory extends BaseTokenStreamTestCase {

	LatinNumberNormalizationFilterFactory factory;
	
	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.factory = new LatinNumberNormalizationFilterFactory(new HashMap<String, String>());
	}

	public void testApostrophus() throws Exception {
		//Reader reader = new StringReader("CCCCIↃↃↃↃ IↃↃↃↃ CCCIↃↃↃ IↃↃↃ CCIↃↃ IↃↃ CIↃ IↃ");
		Reader reader = new StringReader("CCCIↃↃↃ IↃↃↃ CCIↃↃ IↃↃ CIↃ IↃ");
		TokenStream stream = this.factory.create(new MockTokenizer(reader, MockTokenizer.WHITESPACE, false));
		assertTokenStreamContents(stream, new String[] {"\u2188","\u2187", "\u2182", "\u2181", "M", "D"});
	}

	public void testUpperCase() throws Exception {
		Reader reader = new StringReader("Ⅰ \u2160 Ⅱ \u2161 Ⅲ \u2162 Ⅳ \u2163 Ⅴ \u2164 Ⅵ \u2165 Ⅶ \u2166 Ⅷ \u2167 Ⅸ \u2168 Ⅹ \u2169 Ⅺ \u216A Ⅻ \u216B Ⅼ \u216C Ⅽ \u216D Ⅾ \u216E Ⅿ \u216F");		
		TokenStream stream = this.factory.create(new MockTokenizer(reader, MockTokenizer.WHITESPACE, false));
		assertTokenStreamContents(stream, new String[] {"I", "I", "II", "II", "III", "III", "IV", "IV", "V", "V", "VI", "VI", "VII", "VII", "VIII", "VIII", "IX", "IX", "X", "X", "XI", "XI", "XII", "XII", "L", "L", "C", "C", "D", "D", "M", "M"});
	}
	
	public void testLowerCase() throws Exception {
		Reader reader = new StringReader("ⅰ \u2170 ⅱ \u2171 ⅲ \u2172 ⅳ \u2173 ⅴ \u2174 ⅵ \u2175 ⅶ \u2176 ⅷ \u2177 ⅸ \u2178 ⅹ \u2179 ⅺ \u217A ⅻ \u217B ⅼ \u217C ⅽ \u217D ⅾ \u217E ⅿ \u217F");		
		TokenStream stream = this.factory.create(new MockTokenizer(reader, MockTokenizer.WHITESPACE, false));
		assertTokenStreamContents(stream, new String[] {"i", "i", "ii", "ii", "iii", "iii", "iv", "iv", "v", "v", "vi", "vi", "vii", "vii", "viii", "viii", "ix", "ix", "x", "x", "xi", "xi", "xii", "xii", "l", "l", "c", "c", "d", "d", "m", "m"});
	}

	public void testSpecials() throws Exception {
		Reader reader = new StringReader("Ↄ \u2183 ↄ \u2184 \u2185 \u2186");
		TokenStream stream = this.factory.create(new MockTokenizer(reader, MockTokenizer.WHITESPACE, false));
		assertTokenStreamContents(stream, new String[] {"C","C" , "c", "c", "VI", "L"});
	}
	
	public void testCombinations() throws Exception {
		Reader reader = new StringReader("CCCIↃↃↃⅫ");
		TokenStream stream = this.factory.create(new MockTokenizer(reader, MockTokenizer.WHITESPACE, false));
		assertTokenStreamContents(stream, new String[] {"\u2188XII"});
	}
}
