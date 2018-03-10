package ssu.pavkin.tp.services;

import java.util.List;
import java.util.Map;
import ssu.pavkin.tp.model.Post;

public interface RankingService {

	double getNDCG(Map<Post, Integer> relevanceMap);

	Map<Post, Integer> getRelevanceMap(List<Post> data, Post request);

	Map<Post, Integer> getRelevanceMap(List<Post> data, Post request,
		int fullRelStart, int fullRelEnd, int partialRelStart, int partialRelEnd, int littleRelStart, int littleRelEnd);
}
