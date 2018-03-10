package ssu.pavkin.tp.services.impl;

import static ssu.pavkin.tp.model.Fields.AUTHOR;
import static ssu.pavkin.tp.model.Fields.RATING;
import static ssu.pavkin.tp.model.Fields.TAGS;

import java.util.List;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.junit.Test;
import ssu.pavkin.tp.model.Post;
import ssu.pavkin.tp.services.LuceneService;

public class LuceneServiceImplTest {

	private LuceneService<Post> luceneService = new LuceneServiceImpl();

	private static final int ALL_POSTS = 11000;

	@Test
	public void testSearch1() {
		IndexSearcher searcher = luceneService.createSearcher();
		Query query = luceneService.createSimpleQuery(AUTHOR.getName(), "deniskin", new StandardAnalyzer());
		luceneService.search(query, searcher, ALL_POSTS).forEach(System.out::println);
	}

	@Test
	public void testSearch2() {
		IndexSearcher searcher = luceneService.createSearcher();
		Query query = luceneService.createSimpleQuery(AUTHOR.getName(), "skazala", new StandardAnalyzer());
		luceneService.search(query, searcher, ALL_POSTS).forEach(System.out::println);
	}

	@Test
	public void testSearch3() {
		IndexSearcher searcher = luceneService.createSearcher();
		Query query = luceneService.createSimpleQuery(AUTHOR.getName(), "(d*in) OR (s?azala)", new StandardAnalyzer());
		luceneService.search(query, searcher, ALL_POSTS).forEach(System.out::println);
	}

	@Test
	public void testSearch4() {
		IndexSearcher searcher = luceneService.createSearcher();
		Query query = luceneService.createSimpleQuery(AUTHOR.getName(), "(*:*)", new StandardAnalyzer());
		List<Post> posts = luceneService.search(query, searcher, ALL_POSTS);
		posts.forEach(System.out::println);
		System.out.println("Number of records: " + posts.size());
	}

	@Test
	public void testSearch5() {
		IndexSearcher searcher = luceneService.createSearcher();
		BooleanQuery query = luceneService.createBooleanQuery(luceneService.createTermQuery(TAGS.getName(), "правла"));
		luceneService.search(query, searcher, ALL_POSTS).forEach(System.out::println);
	}

	@Test
	public void testSearch6() {
		IndexSearcher searcher = luceneService.createSearcher();
		BooleanQuery query = luceneService.createBooleanQuery(luceneService.createTermQuery(TAGS.getName(), "Хабрахабр"));
		luceneService.search(query, searcher, ALL_POSTS).forEach(System.out::println);
	}

	@Test
	public void testSearch7() {
		IndexSearcher searcher = luceneService.createSearcher();
		BooleanQuery query = luceneService.createBooleanQuery(luceneService.createTermQuery(TAGS.getName(), "поисковик"));
		luceneService.search(query, searcher, ALL_POSTS).forEach(System.out::println);
	}

	@Test
	public void testSearch8() {
		IndexSearcher searcher = luceneService.createSearcher();
		BooleanQuery query = luceneService.createBooleanQuery(luceneService.createTermQuery(RATING.getName(),
			String.valueOf(0)));
		luceneService.search(query, searcher, ALL_POSTS).forEach(System.out::println);
	}
}
