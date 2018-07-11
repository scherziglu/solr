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

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.TokenStream;

import rosa.lucene.la.LatinNumberConvertFilterFactory;

/**
 * Simple tests to ensure the Latin stem factory is working.
 */
public class TestLatinNumberConvertFilterFactory extends BaseTokenStreamTestCase {
	public void testNumberFormatStrictDefault() throws Exception {
		// init Filter
		MockTokenizer mockTokenizer = new MockTokenizer(MockTokenizer.WHITESPACE, false);
		mockTokenizer.setReader(new StringReader("IC VC IL S I i V IV VI VII X XA XIX MXM"));
		LatinNumberConvertFilterFactory factory = new LatinNumberConvertFilterFactory(new HashMap<String, String>());
		
		// test
		TokenStream stream = factory.create(mockTokenizer);
		assertTokenStreamContents(stream, new String[] {"99", "VC", "49", "S", "1", "1", "5", "4", "6", "7", "10", "XA", "19", "1990"});
	}
  
	public void testNumberFormatStrictFalse() throws Exception {
		// init Filter
		Map<String, String> args = new HashMap<String, String>();
		args.put("strictMode", "false");
		MockTokenizer mockTokenizer = new MockTokenizer(MockTokenizer.WHITESPACE, false);
		mockTokenizer.setReader(new StringReader("IC VC IL S I i V IV VI VII X XA XIX MXM"));
		LatinNumberConvertFilterFactory factory = new LatinNumberConvertFilterFactory(args);
    
		// test
		TokenStream stream = factory.create(mockTokenizer);
		assertTokenStreamContents(stream, new String[] {"99", "VC", "49", "S", "1", "1", "5", "4", "6", "7", "10", "XA", "19", "1990" });
	}
	
	public void testNumberFormatStrictTrue() throws Exception {
		// init Filter
		Map<String, String> args = new HashMap<String, String>();
		args.put("strictMode", "true");
		MockTokenizer mockTokenizer = new MockTokenizer(MockTokenizer.WHITESPACE, false);
		mockTokenizer.setReader(new StringReader("IC VC IL S I i V IV VI VII X XA XIX MXM"));
		LatinNumberConvertFilterFactory factory = new LatinNumberConvertFilterFactory(args);
	    
		// test
		TokenStream stream = factory.create(mockTokenizer);
		assertTokenStreamContents(stream, new String[] {"IC", "VC", "IL", "S", "1", "1", "5", "4", "6", "7", "10", "XA", "19", "MXM" });
	}

	public void test1() throws Exception {
		MockTokenizer mockTokenizer = new MockTokenizer(MockTokenizer.WHITESPACE, false);
		mockTokenizer.setReader(new StringReader("I i"));
		// number convert
	    LatinNumberConvertFilterFactory factory = new LatinNumberConvertFilterFactory(new HashMap<String, String>());
	    TokenStream stream = factory.create(mockTokenizer);
		assertTokenStreamContents(stream, new String[] {"1", "1"});
	}	
	
	public void test5() throws Exception {
		MockTokenizer mockTokenizer = new MockTokenizer(MockTokenizer.WHITESPACE, false);
		mockTokenizer.setReader(new StringReader("V v"));
		// number convert
	    LatinNumberConvertFilterFactory factory = new LatinNumberConvertFilterFactory(new HashMap<String, String>());
	    TokenStream stream = factory.create(mockTokenizer);
		assertTokenStreamContents(stream, new String[] {"5", "5"});
	}	
	
	public void test10() throws Exception {
		MockTokenizer mockTokenizer = new MockTokenizer(MockTokenizer.WHITESPACE, false);
		mockTokenizer.setReader(new StringReader("X x"));
		// number convert
	    LatinNumberConvertFilterFactory factory = new LatinNumberConvertFilterFactory(new HashMap<String, String>());
	    TokenStream stream = factory.create(mockTokenizer);
		assertTokenStreamContents(stream, new String[] {"10", "10"});
	}
	public void test50() throws Exception {
		MockTokenizer mockTokenizer = new MockTokenizer(MockTokenizer.WHITESPACE, false);
		mockTokenizer.setReader(new StringReader("L l"));
		// number convert
	    LatinNumberConvertFilterFactory factory = new LatinNumberConvertFilterFactory(new HashMap<String, String>());
	    TokenStream stream = factory.create(mockTokenizer);
		assertTokenStreamContents(stream, new String[] {"50", "50"});
	}
	
	public void test100() throws Exception {
		MockTokenizer mockTokenizer = new MockTokenizer(MockTokenizer.WHITESPACE, false);
		mockTokenizer.setReader(new StringReader("C c"));
		// number convert
	    LatinNumberConvertFilterFactory factory = new LatinNumberConvertFilterFactory(new HashMap<String, String>());
	    TokenStream stream = factory.create(mockTokenizer);
		assertTokenStreamContents(stream, new String[] {"100", "100"});
	}
	
	public void test500() throws Exception {
		MockTokenizer mockTokenizer = new MockTokenizer(MockTokenizer.WHITESPACE, false);
		mockTokenizer.setReader(new StringReader("D d"));
		// number convert
	    LatinNumberConvertFilterFactory factory = new LatinNumberConvertFilterFactory(new HashMap<String, String>());
	    TokenStream stream = factory.create(mockTokenizer);
		assertTokenStreamContents(stream, new String[] {"500", "500"});
	}
	
	public void test1000() throws Exception {
		MockTokenizer mockTokenizer = new MockTokenizer(MockTokenizer.WHITESPACE, false);
		mockTokenizer.setReader(new StringReader("M m"));
		// number convert
	    LatinNumberConvertFilterFactory factory = new LatinNumberConvertFilterFactory(new HashMap<String, String>());
	    TokenStream stream = factory.create(mockTokenizer);
		assertTokenStreamContents(stream, new String[] {"1000", "1000"});
	}
	
	public void test5000() throws Exception {
		MockTokenizer mockTokenizer = new MockTokenizer(MockTokenizer.WHITESPACE, false);
		mockTokenizer.setReader(new StringReader("\u2181"));
		// number convert
	    LatinNumberConvertFilterFactory factory = new LatinNumberConvertFilterFactory(new HashMap<String, String>());
	    TokenStream stream = factory.create(mockTokenizer);
		assertTokenStreamContents(stream, new String[] {"5000"});
	}
	
	public void test10000() throws Exception {
		MockTokenizer mockTokenizer = new MockTokenizer(MockTokenizer.WHITESPACE, false);
		mockTokenizer.setReader(new StringReader("\u2182"));
	    // number convert
	    LatinNumberConvertFilterFactory factory = new LatinNumberConvertFilterFactory(new HashMap<String, String>());
	    TokenStream stream = factory.create(mockTokenizer);
		assertTokenStreamContents(stream, new String[] {"10000"});
	}
	 
	public void test50000() throws Exception {
		MockTokenizer mockTokenizer = new MockTokenizer(MockTokenizer.WHITESPACE, false);
		mockTokenizer.setReader(new StringReader("\u2187"));
		// number convert
	    LatinNumberConvertFilterFactory factory = new LatinNumberConvertFilterFactory(new HashMap<String, String>());
	    TokenStream stream = factory.create(mockTokenizer);
		assertTokenStreamContents(stream, new String[] {"50000"});
	}
	  
	 public void test100000() throws Exception {
		 MockTokenizer mockTokenizer = new MockTokenizer(MockTokenizer.WHITESPACE, false);
		mockTokenizer.setReader( new StringReader("\u2188"));
	    // number convert
	    LatinNumberConvertFilterFactory factory = new LatinNumberConvertFilterFactory(new HashMap<String, String>());
	    TokenStream stream = factory.create(mockTokenizer);
		assertTokenStreamContents(stream, new String[] {"100000"});
	 }
}
