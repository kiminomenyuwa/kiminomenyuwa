package com.scit45.kiminomenyuwa.utils;

import java.time.LocalDate;
import java.time.Period;

/**
 * 연령대를 구분하기 위한 클래스
 */
public class AgeUtils {
	public static String getAgeGroup(LocalDate birthDate) {
		int age = Period.between(birthDate, LocalDate.now()).getYears();

		if (age >= 10 && age <= 19) {
			return "10대";
		} else if (age >= 20 && age <= 29) {
			return "20대";
		} else if (age >= 30 && age <= 39) {
			return "30대";
		} else if (age >= 40 && age <= 49) {
			return "40대";
		} else {
			return "어르신";
		}
	}

	// 연령대에 따른 시작 나이와 끝 나이를 반환하는 메서드
	public static int[] getAgeRange(String ageGroup) {
		switch (ageGroup) {
			case "10대":
				return new int[]{10, 19};
			case "20대":
				return new int[]{20, 29};
			case "30대":
				return new int[]{30, 39};
			case "40대":
				return new int[]{40, 49};
			default:
				return new int[]{50, 100}; // 예: 50대 이상
		}
	}
}
