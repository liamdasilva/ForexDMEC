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
		String inputFileWithPath="data/liamEURUSD15.csv";
		String outputFileWithPath="data/output/output.csv";
		String testInputFileWithPath="data/liamEURUSD15.csv";
		String testOutputFileWithPath="data/output/preprocessed_liamEURUSD15.csvEURUSD60edit2Testset.csv";
		ArrayList<Integer> movingAverages=new ArrayList<Integer>();
		movingAverages.add(20);

		movingAverages.add(50);
		movingAverages.add(100);

		ArrayList<Integer> trendPeriods=new ArrayList<Integer>();
//		trendPeriods.add(100);
//		trendPeriods.add(200);
		Integer pips=10;
		Integer columnNum=5;		
		
		ClassifierMaster cmObject=Classification.createClassificationTree(inputFileWithPath, outputFileWithPath, new String[]{"-R","1,3-7"}, movingAverages, trendPeriods, pips, columnNum,false,"EUR","EUR");
//		System.out.println(Arrays.toString(removeStringArray));
//		System.out.println(Classification.evaluateClassifier(cmObject,outputFileWithPath, new String[]{"-R","1,3-7"}));
		System.out.println(Classification.classifyData(cmObject.getClassifier(), testInputFileWithPath, testOutputFileWithPath, columnIndicesToRemoveArray,movingAverages, trendPeriods, pips,
				columnNum,cmObject.getInstances(),"EUR","EUR","\n"));
		
		ClassifierMaster classifierMaster=Classification.getClassifierMaster("temp.bin", "/Users/brandonstanley/Documents/enterprise/wildfly-10.1.0.Final/standalone/deployments/myEAR.ear/DMWeb.war/WEB-INF/output/");
		String path="/Users/brandonstanley/Documents/enterprise/wildfly-10.1.0.Final/standalone/deployments/myEAR.ear/DMWeb.war/WEB-INF/";
		String fileName="EURUSD60.csv";
		
		Classifier classifier=classifierMaster.getClassifier();
		testInputFileWithPath=path+"/input/"+fileName;
		testOutputFileWithPath=path+"/output/"+"test_"+fileName;
		columnIndicesToRemoveArray=new String[]{"-R","1,3-7"};
		movingAverages=classifierMaster.getMovingAverages();
		trendPeriods=classifierMaster.getTrendPeriods();
		pips=classifierMaster.getPips();
		int OLHC_ColumnNum=classifierMaster.getOLHC_ColumnNum();
		Instances dataset=classifierMaster.getInstances();
		String baseCurr=classifierMaster.getBaseCurr();
		String quoteCurr=classifierMaster.getQuoteCurr();
		System.out.println(Arrays.toString(columnIndicesToRemoveArray));
//		String result=Classification.classifyData(classifier, testInputFileWithPath, testOutputFileWithPath, columnIndicesToRemoveArray, movingAverages, trendPeriods, pips, OLHC_ColumnNum, dataset, baseCurr, quoteCurr,"\n");
//		System.out.println(result);

		

	}

}
