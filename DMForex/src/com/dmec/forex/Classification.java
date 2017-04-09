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

	public static ClassifierMaster createClassificationTree(String inputFileWithPath, String outputFileWithPath,String arffOutputFileWithPath,
			String[] columnIndicesToRemoveArray, ArrayList<Integer> movingAverages, ArrayList<Integer> trendPeriods, int pips,
			int OLHC_ColumnNum, boolean testDataFlag, String baseCurr, String quoteCurr) {

		Preprocessor.startPreprocessing(inputFileWithPath, outputFileWithPath, movingAverages, trendPeriods, pips,
				OLHC_ColumnNum, testDataFlag, baseCurr, quoteCurr);
		String [] copyOfColumnIndicesToRemoveArray=new String[columnIndicesToRemoveArray.length];
		System.arraycopy( columnIndicesToRemoveArray, 0, copyOfColumnIndicesToRemoveArray, 0, columnIndicesToRemoveArray.length );
		Utilities.buildARFF(outputFileWithPath, arffOutputFileWithPath, movingAverages, trendPeriods, pips);
		Instances dataset = Utilities.datasetFromFile(arffOutputFileWithPath, copyOfColumnIndicesToRemoveArray);
//		Instances dataset = Utilities.datasetFromFile(outputFileWithPath, copyOfColumnIndicesToRemoveArray);
//		Utilities.saveToARFF(dataset, "data/TRAININGSETARFF.arff");

		// Build classifier from dataset

		Classifier classifier = Utilities.buildClassifier(dataset);
		// clear out entries in instances
//		while (dataset.numInstances() != 0) {
//			dataset.remove(dataset.numInstances() - 1);
//		}
//		System.out.println(dataset);
		ClassifierMaster cmObject = new ClassifierMaster(classifier, columnIndicesToRemoveArray, movingAverages, trendPeriods,
				pips, OLHC_ColumnNum, dataset, baseCurr,quoteCurr);
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
//		System.out.println("here is the array:"+Arrays.toString(columnIndicesToRemoveArray));
		return correct + "% correctly classified instances";
	}

	public static ArrayList<ArrayList<String>> classifyData(Classifier classifier, String testInputFileWithPath, String testOutputFileWithPath, String arffOutputFileWithPath, String [] columnIndicesToRemoveArray, ArrayList<Integer> movingAverages, ArrayList<Integer> trendPeriods, int pips,
			int OLHC_ColumnNum,Instances dataset, String baseCurr, String quoteCurr, String newLineStr) {
		// classify instances from file using classifier
//		System.out.println("here are the pips"+cmObject.getPips());
		Preprocessor.startPreprocessing(testInputFileWithPath, testOutputFileWithPath, movingAverages,trendPeriods,
				pips,OLHC_ColumnNum,true, baseCurr, quoteCurr);
		
		String [] copyOfColumnIndicesToRemoveArray=new String[columnIndicesToRemoveArray.length];
		System.arraycopy( columnIndicesToRemoveArray, 0, copyOfColumnIndicesToRemoveArray, 0, columnIndicesToRemoveArray.length );


		
//		Instances testset = Utilities.datasetFromFile(testOutputFileWithPath, copyOfColumnIndicesToRemoveArray);
		Utilities.buildARFF(testOutputFileWithPath, arffOutputFileWithPath, movingAverages, trendPeriods, pips);
		Instances testset = Utilities.datasetFromFile(arffOutputFileWithPath, copyOfColumnIndicesToRemoveArray);

//		Instances dataset = new Instances(cmObject.getInstances());
//		for(int i=0;i<testset.numInstances();i++){
//			dataset.add(testset.get(i));
//		}
//		Utilities.saveToARFF(dataset, "data/TESTSETARFF.arff");
		// Utilities.saveToARFF(testOutputFileWithPath, outfile)
		ArrayList<ArrayList<String>>results = new ArrayList<ArrayList<String>>();
		try {
			results = Utilities.classifyInstances(classifier, testset, newLineStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
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
