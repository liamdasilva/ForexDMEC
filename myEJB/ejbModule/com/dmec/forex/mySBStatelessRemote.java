package com.dmec.forex;

import java.util.ArrayList;

import javax.ejb.Remote;

import weka.classifiers.Classifier;

@Remote
public interface mySBStatelessRemote {

	public ClassifierMaster createClassificationTree(String inputFileWithPath, String outputFileWithPath, String [] columnIndicesToRemoveArray, ArrayList<Integer> movingAverages, ArrayList<Integer> trendPeriods, int pips, int OLHC_ColumnNum);
	public String evaluateClassifier(Classifier classifier, String outputFileWithPath,String [] columnIndicesToRemoveArray);
	public boolean saveClassifierMaster(String outputFileWithPath);
	public ClassifierMaster getClassifierMaster(String classifierName, String filePath);
	public void temporarilyStoreClassifierMaster(ClassifierMaster classifierMaster);
}
