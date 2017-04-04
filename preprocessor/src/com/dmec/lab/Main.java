package com.dmec.lab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		String filepathstr="/src/EURUSD60.csv";
		ArrayList<Integer> movingAverages=new ArrayList<Integer>();
		movingAverages.add(10);
		movingAverages.add(20);
		ArrayList<Integer> trendPeriods=new ArrayList<Integer>();
		trendPeriods.add(3);
		trendPeriods.add(20);
		int pips=10;
		
		Preprocessor (filepathstr,movingAverages, trendPeriods, pips);
		// TODO Auto-generated method stub

	}

	public static String calcTrend (int num, ArrayList<ArrayList<String>>dataset, int columnNum){
		boolean trendUp = false;
		boolean trendDown = false;
		String RANGING="RANGING";
		String TRENDUP="TRENDUP";
		String TRENDDOWN="TRENDDOWN";

		
		if (Double.parseDouble(dataset.get(dataset.size()-1).get(columnNum)) > Double.parseDouble(dataset.get(dataset.size()-2).get(columnNum))){
			trendUp = true;
		}
		else if (Double.parseDouble(dataset.get(dataset.size()-1).get(columnNum)) < Double.parseDouble(dataset.get(dataset.size()-2).get(columnNum))){
			trendDown = true;
		}
		else{
			return RANGING;
		}
		int counter = 1;
		while(counter<num-1){
			if (trendDown && Double.parseDouble(dataset.get(dataset.size()-1-counter).get(columnNum)) > Double.parseDouble(dataset.get(dataset.size()-2-counter).get(columnNum))){
				return RANGING;
			}
			if (trendUp && Double.parseDouble(dataset.get(dataset.size()-1-counter).get(columnNum)) < Double.parseDouble(dataset.get(dataset.size()-2-counter).get(columnNum))){
				return RANGING;
			}
			counter++;
		}
		if (trendUp) return TRENDUP;
		if (trendDown) return TRENDDOWN;
		return RANGING;
		
	}
	
	
	public static double calcNextMovingAverage(double prevMovingAverage, int num,ArrayList<String>row, int columnNum, double prevNum){
		double currentSum = prevMovingAverage*num;
		currentSum = currentSum - prevNum + Double.parseDouble(row.get(columnNum));
		return currentSum / num;
	}

	public static double calcInitMovingAverage (int num, ArrayList<ArrayList<String>> rows, int columnNum){
		double sum = 0;
		for (int i = 0; i< num;i++){
			sum +=Double.parseDouble(rows.get(rows.size()-1-i).get(columnNum));
		}
		return sum/num;
	}


	public static void Preprocessor (String filepathstr,ArrayList<Integer> movingAverages, ArrayList<Integer>trendPeriods, int pips){
		String csvFile = "src/EURUSD60.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		FileWriter fw = null;
		BufferedWriter bw=null;

		ArrayList<ArrayList<String>>dataset = new ArrayList();

		int NUM_ROWS_TO_STORE=0;
		int columnNum =5;
		
		//determine how many rows should be store for trend and moving average calculations
		for(Integer movingAverageVal: movingAverages){
			if(movingAverageVal>NUM_ROWS_TO_STORE){
				NUM_ROWS_TO_STORE=movingAverageVal;
			}
		}
		
		for(Integer trendPeriodVal: trendPeriods){
			if(trendPeriodVal>NUM_ROWS_TO_STORE){
				NUM_ROWS_TO_STORE=trendPeriodVal;
			}
		}
		

		try {

			br = new BufferedReader(new FileReader(csvFile));
			fw = new FileWriter("src/output.csv");
			bw = new BufferedWriter(fw);
			int rowCounter =0;
			while (rowCounter<NUM_ROWS_TO_STORE && (line = br.readLine()) != null) {
//				System.out.println(line);
				String[] row = line.split(cvsSplitBy);
				dataset.add(new ArrayList<String>(Arrays.asList(row)));
				rowCounter++;
			}
//			System.out.println(dataset);
//			System.out.println(dataset.size());
			
			ArrayList<Double> prevMovingAverageValues=new ArrayList<Double>();
			//calculate initial moving averages
			for(Integer movingAverageVal: movingAverages){
				double initMovingAvg = calcInitMovingAverage(movingAverageVal, dataset, columnNum);
				line+=","+initMovingAvg;
				prevMovingAverageValues.add(initMovingAvg);
			}
			
			//calculate initial trends
			for(Integer trendPeriodVal: trendPeriods){
				String trend=calcTrend(trendPeriodVal,dataset,columnNum);
				line+=","+trend;
//				System.out.println(trend);
			}
			
			//calculate next row trend
			
//			System.out.println(line);
			bw.write(line);
					
			while ((line = br.readLine()) != null) {
								
				String[] row = line.split(cvsSplitBy);
				dataset.add(new ArrayList<String>(Arrays.asList(row)));
				for(int i=0;i<movingAverages.size();i++){
					Double valueToRemove=Double.parseDouble(dataset.get(dataset.size()-1-movingAverages.get(i)).get(columnNum));
					double movingAvg = calcNextMovingAverage(prevMovingAverageValues.get(i), movingAverages.get(i), new ArrayList<String>(Arrays.asList(row)), columnNum,valueToRemove);
					line+=","+movingAvg;
					prevMovingAverageValues.set(i, movingAvg);
				}
				
				//calculate remaining trends
				for(Integer trendPeriodVal: trendPeriods){
					String trend=calcTrend(trendPeriodVal,dataset,columnNum);
					line+=","+trend;
				}
				
				//remove the first element in the dataset
				dataset.remove(0);
				Double prevRowClose=Double.parseDouble((dataset.get(dataset.size()-2).get(columnNum)));
				Double currentRowClose=Double.parseDouble((dataset.get(dataset.size()-1).get(columnNum)));
//				System.out.println(currentRowClose);
//				System.out.println(prevRowClose);
//				System.out.println(currentRowClose-prevRowClose>0.001);
//				System.out.println(pips/10000.0);

				String prevRowClassification;
				if((currentRowClose-prevRowClose)>(pips/10000.0)){
					prevRowClassification=","+"+"+pips+"pips";
				}else if((currentRowClose-prevRowClose)<(-1*pips/10000.0)){
					prevRowClassification=","+"-"+pips+"pips";

				}else{
					prevRowClassification=",RANGING";
				}
				bw.write(prevRowClassification+'\n');
				bw.write(line);
				
//				rowCounter++;
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
		}	}
}
