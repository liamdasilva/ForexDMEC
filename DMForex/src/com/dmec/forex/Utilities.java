package com.dmec.forex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
		
		try {
			Instances dataset;
			String[] sa = filename.split("\\.");
			if(sa[sa.length-1].equals("csv")){
				CSVLoader loader = new CSVLoader();
				loader.setSource(new File(filename)); //example of filename: "eurusd60edit2.csv"
				dataset = loader.getDataSet();
			}else{
				DataSource source = new DataSource(filename);
				dataset = source.getDataSet();
				
			}
			
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
	
	public static boolean saveToARFF(Instances dataset, String outfile){
		try {
//			DataSource source = new DataSource(filename);
//			Instances dataset = source.getDataSet();
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
	
	public static ArrayList<ArrayList<String>> classifyInstances(Classifier classifier, Instances testset,String newLineStr) throws Exception{
		String result="";
		ArrayList<ArrayList<String>> results=new ArrayList<ArrayList<String>>();
//		System.out.println(testset);
//		System.out.println("\nTesting with Testset:");
		result+="Testing with Testset:"+newLineStr;
//		System.out.println("Actual:\t\tClassified As:");
		result+="Actual:		Classified As:"+newLineStr;
		ArrayList<String> rowInResults=new ArrayList<String>();
		rowInResults.add("Actual");
		rowInResults.add("Classified As");
		results.add(rowInResults);
		int counter=0;
		for(int i=0;i<testset.numInstances();i++){
			Instance row = testset.instance(i);
//			System.out.println(row);
			double c = classifier.classifyInstance(row);
			rowInResults=new ArrayList<String>();
			
//			System.out.println(c);
			if(i==testset.numInstances()-1){
				rowInResults.add("Here is the prediction for the close of the next candle:");
				rowInResults.add(testset.classAttribute().value((int) c));
				result+="Here is the prediction for the close of the next candle:"+newLineStr;
//				System.out.println("Here is the prediction for the close of the next candle:");
				result+=testset.classAttribute().value((int) c)+newLineStr;
			}else{
				rowInResults.add(testset.classAttribute().value((int) row.classValue()));
				rowInResults.add(testset.classAttribute().value((int) c));
				
				result+=testset.classAttribute().value((int) row.classValue())
						+"		"
						+testset.classAttribute().value((int) c)+newLineStr;
//				System.out.println(testset.classAttribute().value((int) row.classValue())
//						+"\t\t"
//						+testset.classAttribute().value((int) c));
			}
			if((testset.classAttribute().value((int) row.classValue())).equals(testset.classAttribute().value((int) c))){
				counter++;
			}
			results.add(rowInResults);
			
		}
//		System.out.println((double)counter/(double)testset.numInstances());
		return results;
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
	
public static void buildARFF(String inputFileName, String outputFileName, ArrayList<Integer> movingAverages,ArrayList<Integer> trendPeriods, int pips){
		
		BufferedReader br = null;
		String line = "";
		FileWriter fw = null;
		BufferedWriter bw = null;
		
		try {
			
			br = new BufferedReader(new FileReader(inputFileName));
			fw = new FileWriter(outputFileName);
			bw = new BufferedWriter(fw);
			String relation="@relation "+"TESTING"+"\n";
			bw.write(relation);
			bw.write("\n");
//			String dateAttribute="@attribute Date NUMERIC"+"\n";
//			bw.write(dateAttribute);
			String timeAttribute="@attribute Time {0:00,1:00,2:00,3:00,4:00,5:00,6:00,7:00,8:00,9:00,10:00,11:00,12:00,13:00,14:00,15:00,16:00,17:00,18:00,19:00,20:00,21:00,22:00,23:00}"+"\n";
			bw.write(timeAttribute);
			String openAttribute="@attribute Open NUMERIC"+"\n";
			bw.write(openAttribute);
			String highAttribute="@attribute High NUMERIC"+"\n";
			bw.write(highAttribute);
			String lowAttribute="@attribute Low NUMERIC"+"\n";
			bw.write(lowAttribute);
			String closeAttribute="@attribute Close NUMERIC"+"\n";
			bw.write(closeAttribute);
			String volumeAttribute="@attribute Volume NUMERIC"+"\n";
			bw.write(volumeAttribute);
			
			
			for(Integer movingAverage:movingAverages){
				String movingAverageAttribute="@attribute Pos_Rel_To_MA_"+movingAverage+" {ABOVE,BELOW}"+"\n";
				bw.write(movingAverageAttribute);
			}
			
			for(Integer trendPeriod:trendPeriods){
				String trendPeriodAttribute="@attribute "+trendPeriod+"_Period_Trend"+" {UPTREND,RANGING,DOWNTREND}"+"\n";
				bw.write(trendPeriodAttribute);
			}
//			bw.write("\n");
			String candleStickAttribute="@attribute CandleStick_Pattern {NONE,BEARISH_ENGULFING,BULLISH_ENGULFING}"+"\n";
			bw.write(candleStickAttribute);
			String classAttribute="@attribute Class_"+pips+"_pips"+" {RANGING,+"+pips+"pips,-"+pips+"pips}"+"\n"; 
			bw.write(classAttribute);
			bw.write("\n");
			String dataAttributeHeader="@data"+"\n";
			bw.write(dataAttributeHeader);
//			bw.write("\n");
				
			while((line = br.readLine()) != null){
				bw.write(line+'\n');
			}
			
			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bw != null)
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			if (fw != null)
				try {
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
}
