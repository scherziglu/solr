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

import java.io.StringReader;
import java.util.HashMap;

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.la.LatinStemFilterFactory;

/**
 * Simple tests to ensure the Latin stem factory is working.
 */
public class TestLatinStemFilterFactory extends BaseTokenStreamTestCase {
 
	public void testLatinStemFilterFactory() throws Exception {
		MockTokenizer mockTokenizer = new MockTokenizer(MockTokenizer.WHITESPACE, false);
		mockTokenizer.setReader(new StringReader("adultero filius filivs FILIVS filii atque"));
		LatinStemFilterFactory factory = new LatinStemFilterFactory(new HashMap<String, String>());
		TokenStream stream = factory.create(mockTokenizer);
		assertTokenStreamContents(stream, new String[] {"adulter", "adulteri", "fil", "filiu", "fil", "filiu", "FIL", "FILIU", "fili", "filii", "atque", "atque"});
	}
}
