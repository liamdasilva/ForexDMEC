package com.dmec.forex;

import java.io.File;
import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.core.converters.CSVLoader;

public class WekaTest {

	public static void main(String[] args) throws Exception{
//		if (args.length%2==0){
//			
//		}
		
		String filename = "data/EURUSD60.csv";
		String []removeStringArray = new String[]{"-R","1,3-7"};
		
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
		ArrayList<Integer> movingAverages=new ArrayList<Integer>();
		movingAverages.add(10);
		movingAverages.add(20);

		ArrayList<Integer> trendPeriods=new ArrayList<Integer>();
		trendPeriods.add(10);
		trendPeriods.add(20);
		Integer pips=10;
		Integer columnNum=5;		
		
		Classification.createClassificationTree(inputFileWithPath, outputFileWithPath, removeStringArray, movingAverages, trendPeriods, pips, columnNum);
		
	}

}
