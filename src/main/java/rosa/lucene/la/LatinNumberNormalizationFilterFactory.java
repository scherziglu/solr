package rosa.lucene.la;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

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

/**
 * Factory for  {@link LatinNumberNormalizationFilter}.
 * <pre class="prettyprint" >
 * &lt;fieldType name="text_latin" class="solr.TextField" positionIncrementGap="100"&gt;
 *   &lt;analyzer&gt;
 *     &lt;tokenizer class="solr.StandardTokenizerFactory"/&gt;
 *     &lt;filter class="solr.LatinNumberNormalizationFilterFactory"/&gt;
 *     &lt;filter class="solr.LatinNumberConvertFilterFactory" strictMode="true"/&gt;
 *   &lt;/analyzer&gt;
 * &lt;/fieldType&gt;</pre> 
 *
 */
public class LatinNumberNormalizationFilterFactory extends TokenFilterFactory {

	/**
	 * default constructor.
	 * @param args
	 * 	arguments from schema.xml
	 */
	public LatinNumberNormalizationFilterFactory(Map<String,String> args) {
		super(args);
	}
	  
	@Override
	public TokenStream create(TokenStream input) {
		return new LatinNumberNormalizationFilter(input);
	}  
}
