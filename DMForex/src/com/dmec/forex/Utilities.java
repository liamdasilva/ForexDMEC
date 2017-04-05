package com.dmec.forex;

import java.io.File;
import java.io.IOException;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomTree;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.Filter;

/**
 * @author Liam Da Silva
 * 
 * Contains various data mining functions to aid in foreign exchange analysis
 *
 */
public class Utilities {
	
	/**
	 * @param filename
	 * 	the path of the csv or arff file to read
	 * @param removeStringArray
	 * 	the columns to remove, should be of the form: "-R,1,7,9-12"
	 * @return the (Instances) dataset created from the file
	 * 
	 */
	public static Instances datasetFromFile(String filename, String []removeStringArray){
		CSVLoader loader = new CSVLoader();
		try {
			loader.setSource(new File(filename)); //example of filename: "eurusd60edit2.csv"
			Instances dataset = loader.getDataSet();
			
			//remove columns here
			if (removeStringArray.length!=0){
//				String[] opts = new String[]{"-R","3-7"}; //example of removeString: "-R 1,7,9-12"
				Remove remove = new Remove();
				remove.setOptions(removeStringArray);
				remove.setInputFormat(dataset);
				Instances newdataset = Filter.useFilter(dataset, remove);
				newdataset.setClassIndex(newdataset.numAttributes()-1);
				dataset=newdataset;
			}

			dataset.setClassIndex(dataset.numAttributes()-1);
			return dataset;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @param dataset
	 * 	the (Instances) dataset to classify
	 * @return the (Classifier) classifier created from the dataset
	 * 
	 */
	public static Classifier buildClassifier(Instances dataset){
		//J48
//		J48 classifier = new J48();
		
		//RandomTree
		RandomTree classifier = new RandomTree();
		
		try {
			classifier.buildClassifier(dataset);
//			System.out.println(classifier.graph());
			return classifier;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @param classifier
	 * 	the classifier to evaluate
	 * @param dataset
	 * 	the dataset the classifier was created from
	 * @return the (Evaluation) evaluation of the classifier
	 * 
	 */
	public static Evaluation evaluateClassifier(Classifier classifier, Instances dataset){
		try {
			Evaluation evaluation = new Evaluation(dataset);
			evaluation.evaluateModel(classifier, dataset);
			return evaluation;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * @param filename
	 * 	the path of the CSV to create ARFF from
	 * @param outfile
	 * 	the path of the ARFF to save as
	 * @return the (boolean) result of the save
	 * 
	 */
	public static boolean saveToARFF(String filename, String outfile){
		try {
			DataSource source = new DataSource(filename);
			Instances dataset = source.getDataSet();
			ArffSaver saver = new ArffSaver();
			saver.setInstances(dataset);
			saver.setFile(new File(outfile));
			saver.writeBatch();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void classifyInstances(Classifier classifier, Instances testset) throws Exception{
		System.out.println("\nTesting with Testset:");
		System.out.println("Actual:\t\tClassified As:");
		for(int i=0;i<testset.numInstances();i++){
			Instance row = testset.instance(i);
			double c = classifier.classifyInstance(row);
			System.out.println(testset.classAttribute().value((int) row.classValue())
					+"\t\t"
					+testset.classAttribute().value((int) c));
			
		}
	}
	
	/**
	 * @param value
	 * 	the number to round
	 * @param places
	 * 	the number of decimal places to round to
	 * @return the (double) rounded number
	 * 
	 */
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();
	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
}
