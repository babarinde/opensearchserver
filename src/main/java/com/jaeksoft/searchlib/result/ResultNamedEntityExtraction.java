/**   
 * License Agreement for OpenSearchServer
 *
 * Copyright (C) 2012-2013 Emmanuel Keller / Jaeksoft
 * 
 * http://www.open-search-server.com
 * 
 * This file is part of OpenSearchServer.
 *
 * OpenSearchServer is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 * OpenSearchServer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with OpenSearchServer. 
 *  If not, see <http://www.gnu.org/licenses/>.
 **/

package com.jaeksoft.searchlib.result;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.jaeksoft.searchlib.SearchLibException;
import com.jaeksoft.searchlib.render.Render;
import com.jaeksoft.searchlib.request.AbstractRequest;
import com.jaeksoft.searchlib.request.NamedEntityExtractionRequest;
import com.jaeksoft.searchlib.result.collector.DocIdInterface;
import com.jaeksoft.searchlib.schema.FieldValueOriginEnum;
import com.jaeksoft.searchlib.util.Timer;
import com.jaeksoft.searchlib.webservice.query.document.DocumentResult.Position;

public class ResultNamedEntityExtraction extends
		AbstractResult<AbstractRequest> implements
		ResultDocumentsInterface<AbstractRequest> {

	final private Map<Integer, ResultDocument> docMap;
	final private List<ResultDocument> docList;
	final private String text;

	public ResultNamedEntityExtraction(NamedEntityExtractionRequest request) {
		super(request);
		this.docMap = new TreeMap<Integer, ResultDocument>();
		this.docList = new ArrayList<ResultDocument>(0);
		this.text = request.getText();
	}

	public void addFieldValue(Integer docId, String field, String value) {
		ResultDocument doc = docMap.get(docId);
		if (doc == null) {
			doc = new ResultDocument(docId);
			docMap.put(docId, doc);
			docList.add(doc);
		}
		doc.addReturnedField(FieldValueOriginEnum.ENTITY_EXTRACTION, field,
				value);
		// Locate position of the entity in the original text
		// First using exact string
		int i = 0;
		for (;;) {
			i = StringUtils.indexOf(text, value, i);
			if (i == StringUtils.INDEX_NOT_FOUND)
				break;
			doc.addPosition(new Position(i, i + value.length()));
			i++;
		}
	}

	@Override
	public ResultDocument getDocument(int pos, Timer timer)
			throws SearchLibException {
		if (docList == null || pos < 0 || pos > docList.size())
			return null;
		return docList.get(pos);
	}

	@Override
	public float getScore(int pos) {
		return 0;
	}

	@Override
	public int getCollapseCount(int pos) {
		return 0;
	}

	@Override
	public int getNumFound() {
		if (docList == null)
			return 0;
		return docList.size();
	}

	@Override
	protected Render getRenderXml() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Render getRenderCsv() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Render getRenderJson(boolean indent) {
		return null;
	}

	@Override
	public Iterator<ResultDocument> iterator() {
		return new ResultDocumentIterator(this, null);
	}

	@Override
	public int getDocumentCount() {
		return docList.size();
	}

	@Override
	public int getRequestStart() {
		return 0;
	}

	@Override
	public int getRequestRows() {
		return docList.size();
	}

	@Override
	public DocIdInterface getDocs() {
		return null;
	}

	@Override
	public float getMaxScore() {
		return 0;
	}

	@Override
	public int getCollapsedDocCount() {
		return 0;
	}

}