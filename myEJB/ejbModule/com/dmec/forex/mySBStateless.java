package com.dmec.forex;

import java.util.ArrayList;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import weka.classifiers.Classifier;

/**
 * Session Bean implementation class mySBStateless
 */
@Stateless
@LocalBean
public class mySBStateless implements mySBStatelessRemote {

    /**
     * Default constructor. 
     */
    public mySBStateless() {
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

}