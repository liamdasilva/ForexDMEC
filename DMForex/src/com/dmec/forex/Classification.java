package com.dmec.forex;

import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class Classification {

	public static Classifier createClassificationTree(String inputFileWithPath, String outputFileWithPath, String [] removeStringArray, ArrayList<Integer> movingAverages, ArrayList<Integer> trendPeriods, int pips, int columnNum) {
//		String filename = "data/EURUSD60edit2.csv";
//		String removeString = "";
		Preprocessor.startPreprocessing(inputFileWithPath, outputFileWithPath, movingAverages, trendPeriods, pips, columnNum);
		
	
		Instances dataset = Utilities.datasetFromFile(outputFileWithPath, removeStringArray);
//		 Build classifier from dataset
		Classifier classifier = Utilities.buildClassifier(dataset);

//		 Build evaluation of classifier
		Evaluation eval = Utilities.evaluateClassifier(classifier, dataset);
//		 System.out.println(eval.toSummaryString());
		String correct = Double.toString(Utilities.round(eval.pctCorrect(), 2));
		System.out.println(correct + "% correctly classified instances");

		// classify instances from file using classifier
//		Instances testset = Utilities.datasetFromFile("data/EURUSD60edit2Testset.csv", removeStringArray);
//		try {
//			Utilities.classifyInstances(classifier, testset);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return null;
	}

}
