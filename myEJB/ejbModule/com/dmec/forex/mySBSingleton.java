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

	Classifier tempClassifier;

    /**
     * Default constructor. 
     */
    public mySBSingleton() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
	public Classifier createClassificationTree(String inputFileWithPath, String outputFileWithPath,
			String[] removeStringArray, ArrayList<Integer> movingAverages, ArrayList<Integer> trendPeriods, int pips,
			int columnNum) {
		return Classification.createClassificationTree(inputFileWithPath, outputFileWithPath, removeStringArray, movingAverages, trendPeriods, pips, columnNum);

		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String evaluateClassifier(Classifier classifier, String outputFileWithPath,String [] removeStringArray){
		return Classification.evaluateClassifier(classifier, outputFileWithPath, removeStringArray);
	}

	@Override
	public boolean saveClassifier(String outputFileWithPath) {
		return Classification.saveClassifier(this.tempClassifier, outputFileWithPath);
	}

	@Override
	public Classifier getClassifier(String classifierName, String filePath) {
		return Classification.getClassifier(classifierName, filePath);
	}

	@Override
	public void temporarilyStoreClassifier(Classifier classifier) {
		this.tempClassifier=classifier;
	}

}
