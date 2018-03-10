package ssu.pavkin.tp.services;

import java.util.List;

public interface SiteParser<T> {

	List<T> parse(int startPage, int pageCount);
}
