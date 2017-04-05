package com.dmec.forex;

import java.util.ArrayList;

import javax.ejb.Remote;

import weka.classifiers.Classifier;

@Remote
public interface mySBStatelessRemote {
	public Classifier createClassificationTree(String inputFileWithPath, String outputFileWithPath, String [] removeStringArray, ArrayList<Integer> movingAverages, ArrayList<Integer> trendPeriods, int pips, int columnNum);

	public String evaluateClassifier(Classifier classifier, String outputFileWithPath,String [] removeStringArray);

}