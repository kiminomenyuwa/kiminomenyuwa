package com.scit45.kiminomenyuwa.service.recommendation;// SimilarityService.java

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class SimilarityService {

	/**
	 * 두 사용자의 코사인 유사도를 계산합니다.
	 *
	 * @param userVector1 사용자 1의 평점 벡터
	 * @param userVector2 사용자 2의 평점 벡터
	 * @return 코사인 유사도 값
	 */
	public double cosineSimilarity(Map<Integer, Integer> userVector1, Map<Integer, Integer> userVector2) {
		double dotProduct = 0.0;
		double normA = 0.0;
		double normB = 0.0;

		for (Integer key : userVector1.keySet()) {
			if (userVector2.containsKey(key)) {
				dotProduct += userVector1.get(key) * userVector2.get(key);
			}
			normA += Math.pow(userVector1.get(key), 2);
		}

		for (Integer key : userVector2.keySet()) {
			normB += Math.pow(userVector2.get(key), 2);
		}

		if (normA == 0.0 || normB == 0.0) {
			return 0.0;
		} else {
			return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
		}
	}
}
