package ssu.pavkin.tp.services.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ssu.pavkin.tp.model.Post;
import ssu.pavkin.tp.services.SiteParser;

public class HabrahabrSiteParser implements SiteParser<Post> {

	private static final String HABRAHABR_POST_URL = "https://habrahabr.ru/post/%d/";

	private static final String POST_WRAPPER_CLASS = "post__wrapper";
	private static final String POST_TAG_LIST_CLASS = "post__tags-list";
	private static final String VOTING_WJT_POST_CLASS = "voting-wjt_post";
	private static final String HEADER_TAG = "header";
	private static final String A_TAG = "a";
	private static final String SPAN_TAG = "span";
	private static final String H1_TAG = "h1";
	private static final String LIST_TAG = "ul";
	private static final String LIST_ELEMENT = "li";

	public List<Post> parse(int startPage, int pageCount) {
		List<Post> posts = new ArrayList<>();
		for (int i = startPage, parsed = 0; parsed < pageCount; i++, parsed++) {
			try {
				String url = String.format(HABRAHABR_POST_URL, i);
				Document document = Jsoup.connect(url).get();
				Element postWrapper = document.getElementsByClass(POST_WRAPPER_CLASS).get(0);

				Element header = postWrapper.getElementsByTag(HEADER_TAG).get(0)
					.getElementsByTag(HEADER_TAG).get(0);

				String author = header.getElementsByTag(A_TAG).get(0)
					.getElementsByTag(SPAN_TAG).text();
				String date = header.getElementsByTag(SPAN_TAG).text();

				String title = postWrapper.getElementsByTag(H1_TAG).get(0)
					.getElementsByTag(SPAN_TAG).text();

				Elements tagList = postWrapper.getElementsByClass(POST_TAG_LIST_CLASS).get(0)
					.getElementsByTag(LIST_TAG).get(0)
					.getElementsByTag(LIST_ELEMENT);

				Iterator<Element> iterator = tagList.listIterator();
				List<String> tags = new ArrayList<>();
				iterator.forEachRemaining(element -> tags.add(getTextContentFromListElement(element)));

				Integer rating = Integer.valueOf(document.getElementsByClass(VOTING_WJT_POST_CLASS).get(0)
					.getElementsByTag(SPAN_TAG).get(1).text());

				posts.add(new Post(author, date, title, url, tags, rating));
			} catch (Exception e) {
				System.err.println("Error when trying to parse page " + i);
				parsed--;
			}

		}
		return posts;
	}

	private String getTextContentFromListElement(Element listElement) {
		return listElement.getElementsByTag(A_TAG).get(0)
			.text();
	}
}
