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
import org.apache.lucene.analysis.la.LatinNumberConvertFilter;


import static org.apache.lucene.analysis.VocabularyAssert.*;

/**
 * Simple tests for {@link LatinNumberConvertFilter}
 */
public class TestLatinNumberConvertFilter extends BaseTokenStreamTestCase {
	private Analyzer analyzerStrictTrue = new Analyzer() {
		@Override
	    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
			Tokenizer source = new MockTokenizer(reader, MockTokenizer.WHITESPACE, false);
			return new TokenStreamComponents(source, new LatinNumberConvertFilter(source, true));
	    }
	};
	
	private Analyzer analyzerStrictFalse = new Analyzer() {
		@Override
	    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
			Tokenizer source = new MockTokenizer(reader, MockTokenizer.WHITESPACE, false);
			return new TokenStreamComponents(source, new LatinNumberConvertFilter(source, false));
	    }
	};
  
	/** Test against a vocabulary from the reference impl - strictMode="false" */
	public void testVocabularyStrictTrue() throws IOException {
		assertVocabulary(analyzerStrictTrue, getDataFile("latinNumberTestData.zip"), "latinNumberTestDataStrictTrue.txt");
	}
	
	/** Test against a vocabulary from the reference impl - strictMode="false" */
	public void testVocabularyStrictFalse() throws IOException {
		assertVocabulary(analyzerStrictFalse, getDataFile("latinNumberTestData.zip"), "latinNumberTestDataStrictFalse.txt");
	}
}
