package com.dmec.forex;

import java.util.ArrayList;

import javax.ejb.Remote;

import weka.classifiers.Classifier;

@Remote
public interface mySBStatefulRemote {
	public Classifier createClassificationTree(String inputFileWithPath, String outputFileWithPath, String [] removeStringArray, ArrayList<Integer> movingAverages, ArrayList<Integer> trendPeriods, int pips, int columnNum);
	
	public String evaluateClassifier(Classifier classifier, String outputFileWithPath,String [] removeStringArray);
	public boolean saveClassifier(String outputFileWithPath);
	public Classifier getClassifier(String classifierName, String filePath);
	public void temporarilyStoreClassifier(Classifier classifier);
		
	
}
