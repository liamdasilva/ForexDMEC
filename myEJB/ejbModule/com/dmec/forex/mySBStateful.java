package com.dmec.forex;

import java.util.ArrayList;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;

import weka.classifiers.Classifier;

/**
 * Session Bean implementation class mySBStateful
 */
@Stateful
@LocalBean
public class mySBStateful implements mySBStatefulRemote {
	
	Classifier tempClassifier;
	
    /**
     * Default constructor. 
     */
    public mySBStateful() {
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
