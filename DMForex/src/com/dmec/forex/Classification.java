package com.dmec.forex;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
		
		return classifier;
	}
	
	public static String evaluateClassifier(Classifier classifier, String outputFileWithPath,String [] removeStringArray){
//		String []copyOfRemoveStringArray=new String[]{removeStringArray};
		Instances dataset = Utilities.datasetFromFile(outputFileWithPath, removeStringArray);


//		 Build evaluation of classifier
		Evaluation eval = Utilities.evaluateClassifier(classifier, dataset);
//		 System.out.println(eval.toSummaryString());
		String correct = Double.toString(Utilities.round(eval.pctCorrect(), 2));
		return correct + "% correctly classified instances";
	}
	
	public static boolean saveClassifier(Classifier classifier, String outputFileWithPath){
		try{
			ObjectOutputStream os=new ObjectOutputStream(new FileOutputStream(outputFileWithPath));
			os.writeObject(classifier);
			os.close();
			System.out.println("Completed writing object to file");
			return true;
		}catch(FileNotFoundException e){
			e.printStackTrace();
			return false;
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static Classifier getClassifier(String classifierName, String filePath){
		Classifier classifier=null;
		try{
			ObjectInputStream is=new ObjectInputStream(new FileInputStream(filePath+classifierName));
			classifier=(Classifier)is.readObject();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		return classifier;
	}

}
