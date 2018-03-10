package ssu.pavkin.tp.services;

import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

public interface LuceneService<T> {

	void index(List<T> data, Analyzer analyzer);

	IndexSearcher createSearcher();

	TermQuery createTermQuery(String field, String q);

	List<T> search(Query query, IndexSearcher searcher, int limit);

	Query createSimpleQuery(String fieldName, String query, Analyzer analyzer);

	BooleanQuery createBooleanQuery(Query... queries);
}
