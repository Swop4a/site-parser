package ssu.pavkin.tp.services.impl;

import static ssu.pavkin.tp.model.Fields.AUTHOR;
import static ssu.pavkin.tp.model.Fields.RATING;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.junit.Test;
import ssu.pavkin.tp.model.Post;
import ssu.pavkin.tp.services.LuceneService;
import ssu.pavkin.tp.services.RankingService;

public class RankingServiceImplTest {

	private RankingService rankingService = new RankingServiceImpl();
	private LuceneService<Post> luceneService = new LuceneServiceImpl();
	private NumberFormat formatter = new DecimalFormat("#0.000000000000");

	@Test
	public void testGetRelevance1() {
//		Post request1 = new Post("deniskin", "deniskin 24 октября 2007 в 00:01", "40000",
//			"https://habrahabr.ru/post/15343/",
//			Collections.singletonList("Хабрахабр"), 45);
//
//		Post request2 = new Post("deniskin", "deniskin 24 октября 2007 в 00:01", "40000",
//			"https://habrahabr.ru/post/15343/",
//			Collections.singletonList("Хабрахабр"), 45);
//		Post request2 = new Post("deniskin", "14 июля 2006 в 01:13", "Мы знаем много недоделок на сайте… но!",
//			"url", Arrays.asList("tag1", "tag2"), null);
//		Post request3 = new Post("deniskin", "14 июля 2006 в 17:59", "Чтение RSS-потоков",
//			"url", Arrays.asList("tag1", "tag2"), null);

		TermQuery termQuery = luceneService.createTermQuery(AUTHOR.getName(), "deniskin");
		Query rangeQuery = IntPoint.newRangeQuery(RATING.getName(), 0, 45);
		BooleanQuery booleanQuery = luceneService.createBooleanQuery(termQuery, rangeQuery);
		IndexSearcher searcher = luceneService.createSearcher();
		List<Post> posts = luceneService.search(booleanQuery, searcher, 10);

		posts.forEach(System.out::println);

		Post post1 = new Post("deniskin", "deniskin 24 октября 2007 в 00:01", "40000",
			"https://habrahabr.ru/post/15343/", Collections.singletonList("Хабрахабр"), 45);
		Map<Post, Integer> relevanceMap1 = new HashMap<Post, Integer>() {{
			put(new Post("deniskin", "deniskin 24 октября 2007 в 00:01", "40000", "https://habrahabr.ru/post/15343/", Collections.singletonList("Хабрахабр"), 45), 3);
			put(new Post("deniskin", "deniskin 25 сентября 2007 в 00:57", "Как и прежде, хабралюди любят Firefox", "https://habrahabr.ru/post/14396/", Arrays.asList("Firefox", "Хабрахабр", "статистика"), 44), 3);
			put(new Post("deniskin", "deniskin 28 марта 2007 в 06:30", "Рейтинги", "https://habrahabr.ru/post/5878/", Arrays.asList("рейтинг", "Хабрахабр", "комментарий", "хабратопик", "блог"), 40), 3);
			put(new Post("deniskin", "deniskin 1 ноября 2006 в 01:28", "О рейтингах и вообще, буквально несколько слов", "https://habrahabr.ru/post/518/", Arrays.asList("Хабрахабр", "рейтинг", "пользователей", "карма", "ограничения", "функционал"), 29), 3);
			put(new Post("deniskin", "deniskin 29 января 2007 в 06:12", "Хабрареволюция!", "https://habrahabr.ru/post/2621/", Arrays.asList("Хабрахабр", "революция", "Счастливые", "Хабралюди"), 26), 3);
			put(new Post("deniskin", "deniskin 10 января 2007 в 02:52", "Мы продолжаем любить rss!", "https://habrahabr.ru/post/1112/", Arrays.asList("Хабрахабр", "RSS", "синдикация", "поток", "фид"), 23), 3);
			put(new Post("deniskin", "deniskin 27 ноября 2006 в 15:51", "О выплатах", "https://habrahabr.ru/post/731/", Arrays.asList("контекстная", "реклама", "Директ", "Бегун", "Адсенс", "Хабрахабр", "авторы", "отч"), 20), 2);
			put(new Post("deniskin", "deniskin 3 ноября 2006 в 04:38", "Рекомендации — релиз (альфа)", "https://habrahabr.ru/post/545/", Arrays.asList("Хабрахабр", "рекомендации", "компании"), 19), 2);
			put(new Post("deniskin", "deniskin 23 ноября 2006 в 01:27", "Хабратеги", "https://habrahabr.ru/post/706/", Arrays.asList("Хабрахабр", "хабратеги"), 19), 2);
			put(new Post("deniskin", "deniskin 1 декабря 2006 в 03:55", "Я умный", "https://habrahabr.ru/post/762/", Arrays.asList("Хабрахабр", "Я", "умный", "блогосфера", "карма"), 18), 1);
		}};

		Map<Post, Integer> relevanceMap1_L = rankingService.getRelevanceMap(posts, post1);

		Post post2 = new Post("deniskin", "deniskin 25 сентября 2007 в 00:57", "Как и прежде, хабралюди любят Firefox",
			"https://habrahabr.ru/post/14396/", Arrays.asList("Firefox", "Хабрахабр", "статистика"), 44);
		Map<Post, Integer> relevanceMap2 = new HashMap<Post, Integer>() {{
			put(new Post("deniskin", "deniskin 24 октября 2007 в 00:01", "40000", "https://habrahabr.ru/post/15343/", Collections.singletonList("Хабрахабр"), 45), 2);
			put(new Post("deniskin", "deniskin 25 сентября 2007 в 00:57", "Как и прежде, хабралюди любят Firefox", "https://habrahabr.ru/post/14396/", Arrays.asList("Firefox", "Хабрахабр", "статистика"), 44), 3);
			put(new Post("deniskin", "deniskin 28 марта 2007 в 06:30", "Рейтинги", "https://habrahabr.ru/post/5878/", Arrays.asList("рейтинг", "Хабрахабр", "комментарий", "хабратопик", "блог"), 40), 3);
			put(new Post("deniskin", "deniskin 1 ноября 2006 в 01:28", "О рейтингах и вообще, буквально несколько слов", "https://habrahabr.ru/post/518/", Arrays.asList("Хабрахабр", "рейтинг", "пользователей", "карма", "ограничения", "функционал"), 29), 3);
			put(new Post("deniskin", "deniskin 29 января 2007 в 06:12", "Хабрареволюция!", "https://habrahabr.ru/post/2621/", Arrays.asList("Хабрахабр", "революция", "Счастливые", "Хабралюди"), 26), 3);
			put(new Post("deniskin", "deniskin 10 января 2007 в 02:52", "Мы продолжаем любить rss!", "https://habrahabr.ru/post/1112/", Arrays.asList("Хабрахабр", "RSS", "синдикация", "поток", "фид"), 23), 3);
			put(new Post("deniskin", "deniskin 27 ноября 2006 в 15:51", "О выплатах", "https://habrahabr.ru/post/731/", Arrays.asList("контекстная", "реклама", "Директ", "Бегун", "Адсенс", "Хабрахабр", "авторы", "отч"), 20), 2);
			put(new Post("deniskin", "deniskin 3 ноября 2006 в 04:38", "Рекомендации — релиз (альфа)", "https://habrahabr.ru/post/545/", Arrays.asList("Хабрахабр", "рекомендации", "компании"), 19), 2);
			put(new Post("deniskin", "deniskin 23 ноября 2006 в 01:27", "Хабратеги", "https://habrahabr.ru/post/706/", Arrays.asList("Хабрахабр", "хабратеги"), 19), 2);
			put(new Post("deniskin", "deniskin 1 декабря 2006 в 03:55", "Я умный", "https://habrahabr.ru/post/762/", Arrays.asList("Хабрахабр", "Я", "умный", "блогосфера", "карма"), 18), 1);
		}};

		Map<Post, Integer> relevanceMap2_L = rankingService.getRelevanceMap(posts, post2);

		Post post3 = new Post("deniskin", "deniskin 28 марта 2007 в 06:30", "Рейтинги",
			"https://habrahabr.ru/post/5878/",
			Arrays.asList("рейтинг", "Хабрахабр", "комментарий", "хабратопик", "блог"), 40);
		Map<Post, Integer> relevanceMap3 = new HashMap<Post, Integer>() {{
			put(new Post("deniskin", "deniskin 24 октября 2007 в 00:01", "40000", "https://habrahabr.ru/post/15343/", Collections.singletonList("Хабрахабр"), 45), 2);
			put(new Post("deniskin", "deniskin 25 сентября 2007 в 00:57", "Как и прежде, хабралюди любят Firefox", "https://habrahabr.ru/post/14396/", Arrays.asList("Firefox", "Хабрахабр", "статистика"), 44), 2);
			put(new Post("deniskin", "deniskin 28 марта 2007 в 06:30", "Рейтинги", "https://habrahabr.ru/post/5878/", Arrays.asList("рейтинг", "Хабрахабр", "комментарий", "хабратопик", "блог"), 40), 3);
			put(new Post("deniskin", "deniskin 1 ноября 2006 в 01:28", "О рейтингах и вообще, буквально несколько слов", "https://habrahabr.ru/post/518/", Arrays.asList("Хабрахабр", "рейтинг", "пользователей", "карма", "ограничения", "функционал"), 29), 3);
			put(new Post("deniskin", "deniskin 29 января 2007 в 06:12", "Хабрареволюция!", "https://habrahabr.ru/post/2621/", Arrays.asList("Хабрахабр", "революция", "Счастливые", "Хабралюди"), 26), 3);
			put(new Post("deniskin", "deniskin 10 января 2007 в 02:52", "Мы продолжаем любить rss!", "https://habrahabr.ru/post/1112/", Arrays.asList("Хабрахабр", "RSS", "синдикация", "поток", "фид"), 23), 3);
			put(new Post("deniskin", "deniskin 27 ноября 2006 в 15:51", "О выплатах", "https://habrahabr.ru/post/731/", Arrays.asList("контекстная", "реклама", "Директ", "Бегун", "Адсенс", "Хабрахабр", "авторы", "отч"), 20), 2);
			put(new Post("deniskin", "deniskin 3 ноября 2006 в 04:38", "Рекомендации — релиз (альфа)", "https://habrahabr.ru/post/545/", Arrays.asList("Хабрахабр", "рекомендации", "компании"), 19), 3);
			put(new Post("deniskin", "deniskin 23 ноября 2006 в 01:27", "Хабратеги", "https://habrahabr.ru/post/706/", Arrays.asList("Хабрахабр", "хабратеги"), 19), 3);
			put(new Post("deniskin", "deniskin 1 декабря 2006 в 03:55", "Я умный", "https://habrahabr.ru/post/762/", Arrays.asList("Хабрахабр", "Я", "умный", "блогосфера", "карма"), 18), 1);
		}};

		Map<Post, Integer> relevanceMap3_L = rankingService.getRelevanceMap(posts, post3);

		Post post4 = new Post("deniskin", "deniskin 1 ноября 2006 в 01:28",
			"О рейтингах и вообще, буквально несколько слов", "https://habrahabr.ru/post/518/",
			Arrays.asList("Хабрахабр", "рейтинг", "пользователей", "карма", "ограничения", "функционал"), 29);
		Map<Post, Integer> relevanceMap4 = new HashMap<Post, Integer>() {{
			put(new Post("deniskin", "deniskin 24 октября 2007 в 00:01", "40000", "https://habrahabr.ru/post/15343/", Collections.singletonList("Хабрахабр"), 45), 1);
			put(new Post("deniskin", "deniskin 25 сентября 2007 в 00:57", "Как и прежде, хабралюди любят Firefox", "https://habrahabr.ru/post/14396/", Arrays.asList("Firefox", "Хабрахабр", "статистика"), 44), 2);
			put(new Post("deniskin", "deniskin 28 марта 2007 в 06:30", "Рейтинги", "https://habrahabr.ru/post/5878/", Arrays.asList("рейтинг", "Хабрахабр", "комментарий", "хабратопик", "блог"), 40), 2);
			put(new Post("deniskin", "deniskin 1 ноября 2006 в 01:28", "О рейтингах и вообще, буквально несколько слов", "https://habrahabr.ru/post/518/", Arrays.asList("Хабрахабр", "рейтинг", "пользователей", "карма", "ограничения", "функционал"), 29), 3);
			put(new Post("deniskin", "deniskin 29 января 2007 в 06:12", "Хабрареволюция!", "https://habrahabr.ru/post/2621/", Arrays.asList("Хабрахабр", "революция", "Счастливые", "Хабралюди"), 26), 3);
			put(new Post("deniskin", "deniskin 10 января 2007 в 02:52", "Мы продолжаем любить rss!", "https://habrahabr.ru/post/1112/", Arrays.asList("Хабрахабр", "RSS", "синдикация", "поток", "фид"), 23), 3);
			put(new Post("deniskin", "deniskin 27 ноября 2006 в 15:51", "О выплатах", "https://habrahabr.ru/post/731/", Arrays.asList("контекстная", "реклама", "Директ", "Бегун", "Адсенс", "Хабрахабр", "авторы", "отч"), 20), 2);
			put(new Post("deniskin", "deniskin 3 ноября 2006 в 04:38", "Рекомендации — релиз (альфа)", "https://habrahabr.ru/post/545/", Arrays.asList("Хабрахабр", "рекомендации", "компании"), 19), 3);
			put(new Post("deniskin", "deniskin 23 ноября 2006 в 01:27", "Хабратеги", "https://habrahabr.ru/post/706/", Arrays.asList("Хабрахабр", "хабратеги"), 19), 3);
			put(new Post("deniskin", "deniskin 1 декабря 2006 в 03:55", "Я умный", "https://habrahabr.ru/post/762/", Arrays.asList("Хабрахабр", "Я", "умный", "блогосфера", "карма"), 18), 3);
		}};

		Map<Post, Integer> relevanceMap4_L = rankingService.getRelevanceMap(posts, post4);

		Post post5 = new Post("deniskin", "deniskin 29 января 2007 в 06:12", "Хабрареволюция!",
			"https://habrahabr.ru/post/2621/", Arrays.asList("Хабрахабр", "революция", "Счастливые", "Хабралюди"), 26);
		Map<Post, Integer> relevanceMap5 = new HashMap<Post, Integer>() {{
			put(new Post("deniskin", "deniskin 24 октября 2007 в 00:01", "40000", "https://habrahabr.ru/post/15343/", Collections.singletonList("Хабрахабр"), 45), 1);
			put(new Post("deniskin", "deniskin 25 сентября 2007 в 00:57", "Как и прежде, хабралюди любят Firefox", "https://habrahabr.ru/post/14396/", Arrays.asList("Firefox", "Хабрахабр", "статистика"), 44), 1);
			put(new Post("deniskin", "deniskin 28 марта 2007 в 06:30", "Рейтинги", "https://habrahabr.ru/post/5878/", Arrays.asList("рейтинг", "Хабрахабр", "комментарий", "хабратопик", "блог"), 40), 2);
			put(new Post("deniskin", "deniskin 1 ноября 2006 в 01:28", "О рейтингах и вообще, буквально несколько слов", "https://habrahabr.ru/post/518/", Arrays.asList("Хабрахабр", "рейтинг", "пользователей", "карма", "ограничения", "функционал"), 29), 2);
			put(new Post("deniskin", "deniskin 29 января 2007 в 06:12", "Хабрареволюция!", "https://habrahabr.ru/post/2621/", Arrays.asList("Хабрахабр", "революция", "Счастливые", "Хабралюди"), 26), 3);
			put(new Post("deniskin", "deniskin 10 января 2007 в 02:52", "Мы продолжаем любить rss!", "https://habrahabr.ru/post/1112/", Arrays.asList("Хабрахабр", "RSS", "синдикация", "поток", "фид"), 23), 3);
			put(new Post("deniskin", "deniskin 27 ноября 2006 в 15:51", "О выплатах", "https://habrahabr.ru/post/731/", Arrays.asList("контекстная", "реклама", "Директ", "Бегун", "Адсенс", "Хабрахабр", "авторы", "отч"), 20), 2);
			put(new Post("deniskin", "deniskin 3 ноября 2006 в 04:38", "Рекомендации — релиз (альфа)", "https://habrahabr.ru/post/545/", Arrays.asList("Хабрахабр", "рекомендации", "компании"), 19), 3);
			put(new Post("deniskin", "deniskin 23 ноября 2006 в 01:27", "Хабратеги", "https://habrahabr.ru/post/706/", Arrays.asList("Хабрахабр", "хабратеги"), 19), 3);
			put(new Post("deniskin", "deniskin 1 декабря 2006 в 03:55", "Я умный", "https://habrahabr.ru/post/762/", Arrays.asList("Хабрахабр", "Я", "умный", "блогосфера", "карма"), 18), 3);
		}};

		Map<Post, Integer> relevanceMap5_L = rankingService.getRelevanceMap(posts, post5);

//		Map<Post, Integer> relevanceMap1 = rankingService.getRelevanceMap(posts, null, 1, 13, 13, 16, 16, 20);
//		Map<Post, Integer> relevanceMap2 = rankingService.getRelevanceMap(posts, null, 1, 13, 13, 16, 16, 20);
//		Map<Post, Integer> relevanceMap3 = rankingService.getRelevanceMap(posts, null, 5, 17, 0, 5, 17, 20);
//		Map<Post, Integer> relevanceMap4 = rankingService.getRelevanceMap(posts, null, 3, 13, 13, 16, 16, 20);
//		Map<Post, Integer> relevanceMap5 = rankingService.getRelevanceMap(posts, null, 2, 12, 12, 17, 17, 20);

		double ndcg1 = rankingService.getNDCG(relevanceMap1);
		double ndcg2 = rankingService.getNDCG(relevanceMap2);
		double ndcg3 = rankingService.getNDCG(relevanceMap3);
		double ndcg4 = rankingService.getNDCG(relevanceMap4);
		double ndcg5 = rankingService.getNDCG(relevanceMap5);

		double ndcg1_l = rankingService.getNDCG(relevanceMap1_L);
		double ndcg2_l = rankingService.getNDCG(relevanceMap2_L);
		double ndcg3_l = rankingService.getNDCG(relevanceMap3_L);
		double ndcg4_l = rankingService.getNDCG(relevanceMap4_L);
		double ndcg5_l = rankingService.getNDCG(relevanceMap5_L);

//		System.out.println(ndcg5 + " " + ndcg5);

		System.out.println(formatter.format((ndcg1 + ndcg2 + ndcg3 + ndcg4 + ndcg5) / 5.0));
		System.out.println(formatter.format((ndcg1_l + ndcg2_l + ndcg3_l + ndcg4_l + ndcg5_l) / 5.0));
//		System.out.println(ndcg1);
//		System.out.println(ndcg2);
//		System.out.println(ndcg3);
//		System.out.println(ndcg4);
//		System.out.println(ndcg5);
	}
}
