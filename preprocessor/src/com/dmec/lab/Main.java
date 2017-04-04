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
		// TODO Auto-generated method stub

	}
	public static void preprocessor(int[] movingAverages, int[]trendPeriods, int pips, String filename){
		/*		fw = new FileWriter("src/output.csv");
		bw = new BufferedWriter(fw);*/

	}

	public static void trendPeriod(int num, int readFromColumnNum,int writeToColumnNum, int maxMovingAverage){

	}

	public String calcTrend (int num, ArrayList<ArrayList<String>>dataset, double range, int columnNum){
		boolean trendUp = true;
		boolean trendDown = true;
		boolean flag = false;
		if (Double.parseDouble(dataset.get(0).get(columnNum)) > Double.parseDouble(dataset.get(1).get(columnNum))){
			trendDown = true;
			trendUp = false;
		}
		else if (Double.parseDouble(dataset.get(0).get(columnNum)) < Double.parseDouble(dataset.get(1).get(columnNum))){
			trendDown = false;
			trendUp = true;
		}
		else{
			return "ranging";
		}
		int counter = 0;
		while(counter <dataset.size()-1){
			if (trendDown && Double.parseDouble(dataset.get(counter).get(columnNum)) < Double.parseDouble(dataset.get(counter+1).get(columnNum))){
				return "ranging";
			}
			if (trendUp && Double.parseDouble(dataset.get(counter).get(columnNum)) > Double.parseDouble(dataset.get(counter+1).get(columnNum))){
				return "ranging";
			}
			counter++;
		}
		if (trendUp) return "trendup";
		if (trendDown) return "trenddown";
		return "ranging";
		
	}
	public static void movingAverage(int num, int readFromColumnNum,int writeToColumnNum, int maxMovingAverage){


	}
	public static double calcNextMovingAverage(double prevMovingAverage, int num,ArrayList<String>row, int columnNum, int prevNum){
		double currentSum = prevMovingAverage*num;
		currentSum = currentSum - prevNum + Double.parseDouble(row.get(columnNum));
		return currentSum / num;
	}

	public static double calcInitMovingAverage (int num, ArrayList<ArrayList<String>> rows, int columnNum){
		double sum = 0;
		for (int i = 0; i< num;i++){
			sum +=Double.parseDouble(rows.get(i).get(columnNum));
		}
		return sum/num;
	}


	public static void Preprocessor (String filepathstr,int[] movingAverages, int[]trendPeriods, int pips){
		String csvFile = "src/EURUSD60.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		FileWriter fw = null;
		BufferedWriter bw=null;

		int maxMovingAverage = 10;
		int num = maxMovingAverage;
		int columnNum =5;
		ArrayList<ArrayList<String>>dataset = new ArrayList();

		try {

			br = new BufferedReader(new FileReader(csvFile));
			fw = new FileWriter("src/output.csv");
			bw = new BufferedWriter(fw);
			int rowCounter =0;
			while ((line = br.readLine()) != null & rowCounter<num) {
				System.out.println(line);
				String[] row = line.split(cvsSplitBy);
				dataset.add(new ArrayList<String>(Arrays.asList(row)));
				rowCounter++;
			}
			double InitMovingAvg = calcInitMovingAverage(num, dataset, columnNum);
			/*boolean calcTrend = 

					bw.write(line+','+InitMovingAvg+'\n');
*/			while ((line = br.readLine()) != null) {
				System.out.println(line);
				String[] row = line.split(cvsSplitBy);
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
