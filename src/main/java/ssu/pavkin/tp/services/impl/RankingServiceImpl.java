package ssu.pavkin.tp.services.impl;

import static java.lang.Math.log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import ssu.pavkin.tp.model.Post;
import ssu.pavkin.tp.services.RankingService;

public class RankingServiceImpl implements RankingService {

	/**
	 * Measure of ranking quality - показатель качества ранжирования
	 *
	 * @return DCG / IDCG
	 */
	@Override
	public double getNDCG(Map<Post, Integer> relevanceMap) {
		return getDCG(relevanceMap) / getIDCG(relevanceMap);
	}

	private double getDCG(Map<Post, Integer> relevanceMap) {
		int i = 1;
		double dcgValue = 0;
		for (Map.Entry<Post, Integer> relevance : relevanceMap.entrySet()) {
			dcgValue += calculateDCGFunction(i++, relevance.getValue());
		}
		return dcgValue;
	}

	/**
	 * @return I[DEAL]DCG
	 */
	private double getIDCG(Map<Post, Integer> relevanceMap) {
		ArrayList<Integer> relevance = new ArrayList<>(relevanceMap.values());
		relevance.sort(Comparator.reverseOrder());

		double dcgValue = 0;
		for (int i = 0; i < relevance.size(); i++) {
			dcgValue += calculateDCGFunction(i + 1, relevance.get(i));
		}
		return dcgValue;
	}

	/**
	 * @return relevance / log2(i + 1)
	 */
	private double calculateDCGFunction(int i, int relevance) {
		return relevance / log(i + 1) / log(2);
	}

	/**
	 * Describe how relevance result of query to object
	 */
	@Override
	public Map<Post, Integer> getRelevanceMap(List<Post> data, Post request) {
		Map<Post, Integer> relevanceMap = new LinkedHashMap<>();
		data.forEach(post -> {
			int count = 0;
			if (eqAuthor(post, request) && closeRating(post, request)) {
				count = 3;
			} else if (eqAuthor(post, request) || closeRating(post, request)) {
				count = 2;
			} else {
				count = 1;
			}
			relevanceMap.put(post, count);
		});
		return relevanceMap;
	}

	@Override
	public Map<Post, Integer> getRelevanceMap(List<Post> data, Post request, int fullRelStart, int fullRelEnd,
		int partialRelStart, int partialRelEnd, int littleRelStart, int littleRelEnd) {
		Map<Post, Integer> relevanceMap = new LinkedHashMap<>();
		for (int i = 0; i < data.size(); i++) {
			if (i >= fullRelStart && i < fullRelEnd) {
				relevanceMap.put(data.get(i), 3);
			}
			if (i >= partialRelStart && i < partialRelEnd) {
				relevanceMap.put(data.get(i), 2);
			}
			if (i >= littleRelStart && i < littleRelEnd) {
				relevanceMap.put(data.get(i), 1);
			}
		}
		return relevanceMap;
	}

	private boolean eqAuthor(Post o1, Post o2) {
		return o1.getAuthor().equals(o2.getAuthor());
	}

	private boolean eqDate(Post o1, Post o2) {
		return o1.getDate().equals(o2.getDate());
	}

	private boolean closeRating(Post o1, Post o2) {
		return o1.getRating() >= o2.getRating() - 5 && o1.getRating() <= o2.getRating() + 5;
	}
}
