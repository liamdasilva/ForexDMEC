package com.dmec.forex;

import java.util.ArrayList;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;

import weka.classifiers.Classifier;
import weka.core.Instances;

/**
 * Session Bean implementation class mySBSingleton
 */
@Singleton
@LocalBean
public class mySBSingleton implements mySBSingletonRemote {

	ClassifierMaster tempClassifierMaster;

    /**
     * Default constructor. 
     */
    public mySBSingleton() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
	public ClassifierMaster createClassificationTree(String inputFileWithPath, String outputFileWithPath,String arffOutputFileWithPath,
			String[] columnIndicesToRemoveArray, ArrayList<Integer> movingAverages, ArrayList<Integer> trendPeriods, int pips,
			int OLHC_ColumnNum, String baseCurr, String quoteCurr) {
		return Classification.createClassificationTree(inputFileWithPath, outputFileWithPath, arffOutputFileWithPath, columnIndicesToRemoveArray, movingAverages, trendPeriods, pips, OLHC_ColumnNum, false,baseCurr, quoteCurr);

		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String evaluateClassifier(Classifier classifier, String outputFileWithPath,String [] columnIndicesToRemoveArray){
		return Classification.evaluateClassifier(classifier, outputFileWithPath, columnIndicesToRemoveArray);
	}

	@Override
	public boolean saveClassifierMaster(String outputFileWithPath) {
		return Classification.saveClassifierMaster(this.tempClassifierMaster, outputFileWithPath);
	}

	@Override
	public ClassifierMaster getClassifierMaster(String classifierMasterName, String filePath) {
		return Classification.getClassifierMaster(classifierMasterName, filePath);
	}

	@Override
	public void temporarilyStoreClassifierMaster(ClassifierMaster classifierMaster) {
		this.tempClassifierMaster=classifierMaster;
	}
	
	@Override
	public ArrayList<ArrayList<String>> classifyData(Classifier classifier, String testInputFileWithPath, String testOutputFileWithPath,String arffOutputFileWithPath, String []columnIndicesToRemoveArray, ArrayList<Integer> movingAverages,ArrayList<Integer> trendPeriods,int pips, int OLHC_ColumnNum,Instances dataset, String baseCurr, String quoteCurr, String newLineStr){
		return Classification.classifyData(classifier, testInputFileWithPath, testOutputFileWithPath,arffOutputFileWithPath, columnIndicesToRemoveArray, movingAverages, trendPeriods, pips, OLHC_ColumnNum, dataset, baseCurr, quoteCurr,newLineStr);
	}

}
