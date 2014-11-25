package com.biit.abcd.core.drools.utils;

import java.util.List;

public class RulesOperators {

	public static Double calculateMaxValueFunction(List<Double> variables) {
		Double max = 0.;
		for (Double variable : variables) {
			max = Math.max(max, variable);
		}
		return max;
	}

	public static Double calculateMinValueFunction(List<Double> variables) {
		Double min = 10000000.;
		for (Double variable : variables) {
			min = Math.min(min, variable);
		}
		return min;
	}

	public static Double calculateAvgValueFunction(List<Double> variables) {
		Double avg = 0.;
		for (Double variable : variables) {
			avg += variable;
		}
		return (avg / (double) variables.size());
	}

	public static Double calculateSumValueFunction(List<Double> variables) {
		Double sum = 0.;
		for (Double variable : variables) {
			sum += variable;
		}
		return sum;
	}

	public static Double calculatePmtValueFunction(List<Double> variables) {
		Double pmtValue = 0.0;
		if (variables.size() == 3) {
			double rate = variables.get(0);
			double term = variables.get(1);
			double amount = variables.get(2);

			double v = 1 + rate;
			double t = -term;
			pmtValue = (amount * rate) / (1 - Math.pow(v, t));
		}
		return pmtValue;
	}
}
