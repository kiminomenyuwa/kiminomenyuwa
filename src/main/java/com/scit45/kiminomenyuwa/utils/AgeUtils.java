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
}
