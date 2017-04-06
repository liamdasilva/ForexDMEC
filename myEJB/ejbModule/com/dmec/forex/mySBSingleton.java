package com.dmec.forex;

import java.util.ArrayList;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;

import weka.classifiers.Classifier;

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
	public ClassifierMaster createClassificationTree(String inputFileWithPath, String outputFileWithPath,
			String[] columnIndicesToRemoveArray, ArrayList<Integer> movingAverages, ArrayList<Integer> trendPeriods, int pips,
			int OLHC_ColumnNum) {
		return Classification.createClassificationTree(inputFileWithPath, outputFileWithPath, columnIndicesToRemoveArray, movingAverages, trendPeriods, pips, OLHC_ColumnNum, false);

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

}
