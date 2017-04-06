package com.dmec.forex;

import java.util.ArrayList;
import java.util.Arrays;

import weka.classifiers.Classifier;
import weka.core.Instances;


public class WekaTest {

	public static void main(String[] args) throws Exception{
//		if (args.length%2==0){
//			
//		}
		
		String filename = "data/EURUSD60.csv";
		String []columnIndicesToRemoveArray = new String[]{"-R","1,3-7"};
//		String []removeStringArray2 = new String[]{"-R","1,3-7"};

		
//		//Utilities.saveToARFF(filename, "EURUSD60edit2.arff");
//		
//		//build dataset from CSV
//		Instances dataset = Utilities.datasetFromFile(filename, removeStringArray);
////		System.out.println(dataset.toSummaryString());
////		int numclasses = dataset.numClasses();
////		for (int i = 0; i<numclasses;i++){
////			String classvalue = dataset.classAttribute().value(i);
////			System.out.println("Class value "+i+" is "+classvalue);
////		}
//		
//		//Build classifier from dataset
//		Classifier classifier = Utilities.buildClassifier(dataset);
//		
//		//Build evaluation of classifier
//		Evaluation eval = Utilities.evaluateClassifier(classifier, dataset);
////		System.out.println(eval.toSummaryString());
//		String correct = Double.toString(Utilities.round(eval.pctCorrect(), 2));
//		System.out.println(correct+"% correctly classified instances");
//		
//		
//		//classify instances from file using classifier
//		Instances testset = Utilities.datasetFromFile("data/EURUSD60edit2Testset.csv", removeStringArray);
//		Utilities.classifyInstances(classifier, testset);
//		
//		//new function callClassifier
////		removeString = "-R 3-7";
		String inputFileWithPath="data/EURUSD60.csv";
		String outputFileWithPath="data/output/output.csv";
		String testInputFileWithPath="data/EURUSD60edit2Testset.csv";
		String testOutputFileWithPath="data/output/EURUSD60edit2Testset.csv";
		ArrayList<Integer> movingAverages=new ArrayList<Integer>();
		movingAverages.add(20);

		movingAverages.add(50);
		movingAverages.add(100);

		ArrayList<Integer> trendPeriods=new ArrayList<Integer>();
//		trendPeriods.add(100);
//		trendPeriods.add(200);
		Integer pips=10;
		Integer columnNum=5;		
		
		ClassifierMaster cmObject=Classification.createClassificationTree(inputFileWithPath, outputFileWithPath, new String[]{"-R","1,3-7"}, movingAverages, trendPeriods, pips, columnNum,false);
//		System.out.println(Arrays.toString(removeStringArray));
//		System.out.println(Classification.evaluateClassifier(cmObject,outputFileWithPath, new String[]{"-R","1,3-7"}));
		System.out.println(Classification.classifyData(cmObject.getClassifier(), testInputFileWithPath, testOutputFileWithPath, columnIndicesToRemoveArray,movingAverages, trendPeriods, pips,
				columnNum,cmObject.getInstances()));
	

		

	}

}
