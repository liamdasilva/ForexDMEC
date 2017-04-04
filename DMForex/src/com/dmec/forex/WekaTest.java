package com.dmec.forex;

import java.io.File;

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
		
		String filename = "data/EURUSD60edit2.csv";
		String removeString = "";
		
		//Utilities.saveToARFF(filename, "EURUSD60edit2.arff");
		
		//build dataset from CSV
		Instances dataset = Utilities.datasetFromFile(filename, removeString);
//		System.out.println(dataset.toSummaryString());
//		int numclasses = dataset.numClasses();
//		for (int i = 0; i<numclasses;i++){
//			String classvalue = dataset.classAttribute().value(i);
//			System.out.println("Class value "+i+" is "+classvalue);
//		}
		
		//Build classifier from dataset
		Classifier classifier = Utilities.buildClassifier(dataset);
		
		//Build evaluation of classifier
		Evaluation eval = Utilities.evaluateClassifier(classifier, dataset);
//		System.out.println(eval.toSummaryString());
		String correct = Double.toString(Utilities.round(eval.pctCorrect(), 2));
		System.out.println(correct+"% correctly classified instances");
		
		
		//classify instances from file using classifier
		Instances testset = Utilities.datasetFromFile("data/EURUSD60edit2Testset.csv", "");
		Utilities.classifyInstances(classifier, testset);
		
	}

}
