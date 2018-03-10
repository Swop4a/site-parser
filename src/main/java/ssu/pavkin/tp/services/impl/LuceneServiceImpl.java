package ssu.pavkin.tp.services.impl;

import static org.apache.lucene.document.Field.Store.YES;
import static ssu.pavkin.tp.model.Fields.AUTHOR;
import static ssu.pavkin.tp.model.Fields.DATE;
import static ssu.pavkin.tp.model.Fields.RATING;
import static ssu.pavkin.tp.model.Fields.TAGS;
import static ssu.pavkin.tp.model.Fields.TITLE;
import static ssu.pavkin.tp.model.Fields.URL;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.SortedNumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.SortedNumericSortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import ssu.pavkin.tp.model.Post;
import ssu.pavkin.tp.services.LuceneService;

public class LuceneServiceImpl implements LuceneService<Post> {

	private static final String IDX_DIR = "index";
	private static final String EMPTY_STRING = "";

	@Override
	public void index(List<Post> data, Analyzer analyzer) {
		try (IndexWriter writer = createIndexWriter(IDX_DIR, analyzer)) {
			writer.addDocuments(data.stream()
				.map(this::createDocument)
				.collect(Collectors.toList()));
			writer.commit();
			writer.close();
		} catch (IOException e) {
			System.err.println("Oops, went something wrong...");
		}
	}

	private IndexWriter createIndexWriter(String indexDir, Analyzer analyzer) throws IOException {
		FSDirectory directory = FSDirectory.open(Paths.get(indexDir));
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		return new IndexWriter(directory, config);
	}

	private Document createDocument(Post object) {
		Document document = new Document();
		document.add(new StringField(AUTHOR.getName(), object.getAuthor(), YES));
		document.add(new TextField(DATE.getName(), object.getDate(), YES));
		document.add(new StringField(TITLE.getName(), object.getTitle(), YES));
		document.add(new StringField(URL.getName(), object.getUrl(), YES));
		document.add(new TextField(TAGS.getName(), castListOfStringsToString(object.getTags()), YES));
		document.add(new IntPoint(RATING.getName(), object.getRating()));
		document.add(new StoredField(RATING.getName(), object.getRating()));
		document.add(new SortedNumericDocValuesField(RATING.getName(), object.getRating()));
		return document;
	}

	private static String castListOfStringsToString(List<String> list) {
		return list.toString()
			.replace("[", EMPTY_STRING)
			.replace("]", EMPTY_STRING)
			.replace(",", EMPTY_STRING);
	}

	@Override
	public IndexSearcher createSearcher() {
		Directory dir;
		IndexReader reader = null;
		try {
			dir = FSDirectory.open(Paths.get(IDX_DIR));
			reader = DirectoryReader.open(dir);
		} catch (IOException e) {
			System.err.println("");
		}
		return new IndexSearcher(reader);
	}

	@Override
	public TermQuery createTermQuery(String field, String q) {
		return new TermQuery(new Term(field, q.toLowerCase()));
	}

	@Override
	public List<Post> search(Query query, IndexSearcher searcher, int limit) {
		List<Post> result = new ArrayList<>();
		try {
			TopDocs docs = searcher
				.search(query, limit, new Sort(new SortedNumericSortField(RATING.getName(), Type.INT, true)));
			for (ScoreDoc scoreDoc : docs.scoreDocs) {
				Document doc = searcher.doc(scoreDoc.doc);
				result.add(new Post(doc.get(AUTHOR.getName()), doc.get(DATE.getName()), doc.get(TITLE.getName()),
					doc.get(URL.getName()), Arrays.asList(doc.get(TAGS.getName()).split(" ")),
					Integer.valueOf(doc.get(RATING.getName()))));
			}
		} catch (IOException e) {
			System.err.println("Cannot perform search");
		}
		return result;
	}

	@Override
	public Query createSimpleQuery(String fieldName, String query, Analyzer analyzer) {
		QueryParser parser = new QueryParser(fieldName, analyzer);
		try {
			return parser.parse(query);
		} catch (ParseException e) {
			System.err.println(String.format("Error execute search FIELD: %s, QUERY: %s ", fieldName, query));
			throw new RuntimeException();
		}
	}

	@Override
	public BooleanQuery createBooleanQuery(Query... queries) {
		Builder builder = new Builder();
		Arrays.stream(queries).forEach(query -> builder.add(query, Occur.MUST));
		return builder.build();
	}
}
