package com.dmec.forex;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.enterprise.inject.Instance;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class Classification {

	public static ClassifierMaster createClassificationTree(String inputFileWithPath, String outputFileWithPath,
			String[] columnIndicesToRemoveArray, ArrayList<Integer> movingAverages, ArrayList<Integer> trendPeriods, int pips,
			int OLHC_ColumnNum, boolean testDataFlag) {

		Preprocessor.startPreprocessing(inputFileWithPath, outputFileWithPath, movingAverages, trendPeriods, pips,
				OLHC_ColumnNum, testDataFlag);
		String [] copyOfColumnIndicesToRemoveArray=new String[columnIndicesToRemoveArray.length];
		System.arraycopy( columnIndicesToRemoveArray, 0, copyOfColumnIndicesToRemoveArray, 0, columnIndicesToRemoveArray.length );

		Instances dataset = Utilities.datasetFromFile(outputFileWithPath, copyOfColumnIndicesToRemoveArray);
//		Utilities.saveToARFF(dataset, "data/TRAININGSETARFF.arff");

		// Build classifier from dataset

		Classifier classifier = Utilities.buildClassifier(dataset);
		// clear out entries in instances
		while (dataset.numInstances() != 0) {
			dataset.remove(dataset.numInstances() - 1);
		}
//		System.out.println(dataset);
		ClassifierMaster cmObject = new ClassifierMaster(classifier, copyOfColumnIndicesToRemoveArray, movingAverages, trendPeriods,
				pips, OLHC_ColumnNum, dataset);

		return cmObject;
	}

	public static String evaluateClassifier(Classifier classifier, String outputFileWithPath,
			String[] columnIndicesToRemoveArray) {
		
		String [] copyOfColumnIndicesToRemoveArray=new String[columnIndicesToRemoveArray.length];
		System.arraycopy( columnIndicesToRemoveArray, 0, copyOfColumnIndicesToRemoveArray, 0, columnIndicesToRemoveArray.length );

		Instances dataset = Utilities.datasetFromFile(outputFileWithPath, copyOfColumnIndicesToRemoveArray);

//		Classifier classifier = cmObject.getClassifier();
		// Build evaluation of classifier
		Evaluation eval = Utilities.evaluateClassifier(classifier, dataset);
		// System.out.println(eval.toSummaryString());
		String correct = Double.toString(Utilities.round(eval.pctCorrect(), 2));
		return correct + "% correctly classified instances";
	}

	public static String classifyData(Classifier classifier, String testInputFileWithPath, String testOutputFileWithPath, String [] columnIndicesToRemoveArray, ArrayList<Integer> movingAverages, ArrayList<Integer> trendPeriods, int pips,
			int OLHC_ColumnNum,Instances dataset) {
		// classify instances from file using classifier
//		System.out.println("here are the pips"+cmObject.getPips());
		Preprocessor.startPreprocessing(testInputFileWithPath, testOutputFileWithPath, movingAverages,trendPeriods,
				pips,OLHC_ColumnNum,true);
		
		String [] copyOfColumnIndicesToRemoveArray=new String[columnIndicesToRemoveArray.length];
		System.arraycopy( columnIndicesToRemoveArray, 0, copyOfColumnIndicesToRemoveArray, 0, columnIndicesToRemoveArray.length );


		Instances testset = Utilities.datasetFromFile(testOutputFileWithPath, copyOfColumnIndicesToRemoveArray);
//		Instances dataset = new Instances(cmObject.getInstances());
		for(int i=0;i<testset.numInstances();i++){
			dataset.add(testset.get(i));
		}
//		Utilities.saveToARFF(dataset, "data/TESTSETARFF.arff");
		// Utilities.saveToARFF(testOutputFileWithPath, outfile)
		String result = "";
		try {
			result = Utilities.classifyInstances(classifier, dataset);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static boolean saveClassifierMaster(ClassifierMaster cmObject, String outputFileWithPath) {
		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(outputFileWithPath));
			os.writeObject(cmObject);
			os.close();
			System.out.println("Completed writing object to file");
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static ClassifierMaster getClassifierMaster(String classifierMasterName, String filePath) {
		ClassifierMaster cmObject = null;
		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(filePath + classifierMasterName));
			cmObject = (ClassifierMaster) is.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return cmObject;
	}

}
