package com.biit.abcd.core.drools.utils;

import java.util.List;

public class RulesOperators {

	/**
	 * Calculates the maximum of the variables array
	 * 
	 * @param variables
	 * @return
	 */
	public static Double calculateMaxValueFunction(List<Double> variables) {
		Double max = 0.;
		if (!variables.isEmpty()) {
			for (Double variable : variables) {
				max = Math.max(max, variable);
			}
		} else {
			return null;
		}
		return max;
	}

	/**
	 * Calculates the minimum of the variables array
	 * 
	 * @param variables
	 * @return
	 */
	public static Double calculateMinValueFunction(List<Double> variables) {
		if (!variables.isEmpty()) {
			Double min = 10000000.;
			for (Double variable : variables) {
				min = Math.min(min, variable);
			}
			return min;
		} else {
			return null;
		}
	}

	/**
	 * Calculates the average of the variables array
	 * 
	 * @param variables
	 * @return
	 */
	public static Double calculateAvgValueFunction(List<Double> variables) {
		Double avg = 0.;
		if (!variables.isEmpty()) {
			for (Double variable : variables) {
				avg += variable;
			}
		} else {
			return null;
		}
		return (avg / (double) variables.size());
	}

	/**
	 * Calculates the total sum of the variables array
	 * 
	 * @param variables
	 * @return
	 */
	public static Double calculateSumValueFunction(List<Double> variables) {
		Double sum = 0.;
		if (!variables.isEmpty()) {
			for (Double variable : variables) {
				sum += variable;
			}
		} else {
			return null;
		}
		return sum;
	}

	/**
	 * Calculates how much your monthly payment would be on a loan based on an
	 * interest rate and a constant payment schedule.
	 * 
	 * @param variables
	 * @return
	 */
	public static Double calculatePmtValueFunction(List<Double> variables) {
		Double pmtValue = 0.0;
		if (!variables.isEmpty()) {
			if (variables.size() == 3) {
				double rate = variables.get(0);
				double term = variables.get(1);
				double amount = variables.get(2);

				double v = 1 + rate;
				double t = -term;
				pmtValue = (amount * rate) / (1 - Math.pow(v, t));
			}
		} else {
			return null;
		}
		return pmtValue;
	}
}
