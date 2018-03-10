package ssu.pavkin.tp;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import ssu.pavkin.tp.model.Post;
import ssu.pavkin.tp.search.SynonymAnalyzer;
import ssu.pavkin.tp.services.LuceneService;
import ssu.pavkin.tp.services.SiteParser;
import ssu.pavkin.tp.services.impl.HabrahabrSiteParser;
import ssu.pavkin.tp.services.impl.LuceneServiceImpl;

public class Application {

	private static final int ALL_POSTS = 11000;

	/*
		WARNING! DON'T CHANGE BOOLEAN PARAMS, IF IT'S NOT NECESSARY!!!
	 */
	public static void main(String[] args) throws IOException {
		new Application().run(false, false);
	}

	/**
	 * @param createIndex is needed to create new index
	 * @param writeToFile is needed to write parsed values into file
	 */
	private void run(boolean createIndex, boolean writeToFile) throws IOException {
		LuceneService<Post> luceneService = new LuceneServiceImpl();
		SiteParser<Post> siteParser = new HabrahabrSiteParser();
		File file = new File("src/main/resources/site/habrahabr_with_rating.json");

		if (writeToFile) {
			new ObjectMapper().writer().writeValue(file, siteParser.parse(0, ALL_POSTS));
		}

		if (createIndex) {
			MappingIterator<Post> objectMappingIterator = new ObjectMapper().readerFor(Post.class).readValues(file);
			luceneService.index(objectMappingIterator.readAll(), new SynonymAnalyzer());
		}
	}
}