package com.dmec.forex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Preprocessor {
	private static final String STR_RANGING="RANGING";
	private static final String STR_UP_TREND="UPTREND";
	private static final String STR_DOWN_TREND="DOWNTREND";
	private static final String STR_ABOVE="ABOVE";
	private static final String STR_BELOW="BELOW";
	private static final String STR_EQUAL="EQUAL";
	private static final String STR_JPY="JPY";
	private static final int LOW_COLUMN_NUM=4;
	private static final int HIGH_COLUMN_NUM=3;
	private static final int OPEN_COLUMN_NUM=2;
	private static final int CLOSE_COLUMN_NUM=5;
	private static final int CANDLESTICK_TREND_MA=50;

//	private static final String OUTPUT_FOLDER_PATH="data/output/";
//	private static final String DATA_FOLDER_PATH="data/";
	
	
	public static String calcTrend(int num, ArrayList<ArrayList<String>> dataset, int columnNum) {
		boolean trendUp = false;
		boolean trendDown = false;
		

		if (Double.parseDouble(dataset.get(dataset.size() - 1).get(columnNum)) > Double
				.parseDouble(dataset.get(dataset.size() - 2).get(columnNum))) {
			trendUp = true;
		} else if (Double.parseDouble(dataset.get(dataset.size() - 1).get(columnNum)) < Double
				.parseDouble(dataset.get(dataset.size() - 2).get(columnNum))) {
			trendDown = true;
		} else {
			return STR_RANGING;
		}
		int counter = 1;
		while (counter < num - 1) {
			if (trendDown && Double.parseDouble(dataset.get(dataset.size() - 1 - counter).get(columnNum)) >= Double
					.parseDouble(dataset.get(dataset.size() - 2 - counter).get(columnNum))) {
				return STR_RANGING;
			}
			if (trendUp && Double.parseDouble(dataset.get(dataset.size() - 1 - counter).get(columnNum)) <= Double
					.parseDouble(dataset.get(dataset.size() - 2 - counter).get(columnNum))) {
				return STR_RANGING;
			}
			counter++;
		}
		if (trendUp)
			return STR_UP_TREND;
		if (trendDown)
			return STR_DOWN_TREND;
		return STR_RANGING;

	}

	public static double calcNextMovingAverage(double prevMovingAverage, int num, ArrayList<String> row, int columnNum,
			double prevNum) {
		double currentSum = prevMovingAverage * num;
		currentSum = currentSum - prevNum + Double.parseDouble(row.get(columnNum));
		return currentSum / num;
	}

	public static double calcInitMovingAverage(int num, ArrayList<ArrayList<String>> rows, int columnNum) {
		double sum = 0;
		for (int i = 0; i < num; i++) {
			sum += Double.parseDouble(rows.get(rows.size() - 1 - i).get(columnNum));
		}
		return sum / num;
	}
	
//	public static double calcNextMovingAverage
	
	public static void preprocessTestData(){
		
	}

	public static void startPreprocessing(String inputFileName, String outputFileName, ArrayList<Integer> movingAverages,
			ArrayList<Integer> trendPeriods, int pips, int columnNum, boolean testData, String baseCurr, String quoteCurr) {
//		String csvFile = "src/EURUSD60.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		FileWriter fw = null;
		BufferedWriter bw = null;

		ArrayList<ArrayList<String>> dataset = new ArrayList();

		int NUM_ROWS_TO_STORE = CANDLESTICK_TREND_MA;
//		int columnNum = 5;
		
		
		

		// determine how many rows should be store for trend and moving average
		// calculations
		for (Integer movingAverageVal : movingAverages) {
			if (movingAverageVal > NUM_ROWS_TO_STORE) {
				NUM_ROWS_TO_STORE = movingAverageVal;
			}
		}

		for (Integer trendPeriodVal : trendPeriods) {
			if (trendPeriodVal > NUM_ROWS_TO_STORE) {
				NUM_ROWS_TO_STORE = trendPeriodVal;
			}
		}

		try {

			br = new BufferedReader(new FileReader(inputFileName));
//			fw = new FileWriter("data/output.csv");
			fw = new FileWriter(outputFileName);

			bw = new BufferedWriter(fw);
			
			//create column headers
//			String columnHeaders="";
//			columnHeaders+="Date,Time,Open,High,Low,Close,Volume";
//			for (Integer movingAverageVal : movingAverages) {
//				columnHeaders+=",Pos_Rel_To_MA_"+movingAverageVal;
//			}
//
//			for (Integer trendPeriodVal : trendPeriods) {
//				columnHeaders+=","+trendPeriodVal+"_Period_Trend";
//			}
//			columnHeaders+=",CandleStick_Pattern";
//			columnHeaders+=",Class_"+pips+"_pips";
//			bw.write(columnHeaders+'\n');
			
			int rowCounter = 0;
			ArrayList<String> firstRow=new ArrayList<String>();
//			Double prevRowClose=0.0;
			Candlestick prevCandle=null;
			Candlestick currentCandle=null;
//			Double currentRowClose;
//			Double currentRowLow;
//			Double currentRowHigh;
			
			if(NUM_ROWS_TO_STORE==0 && (line = br.readLine()) != null){
				String[] row = line.split(cvsSplitBy);
				firstRow=new ArrayList<String>(Arrays.asList(row));
//				prevRowClose=Double.parseDouble(firstRow.get(columnNum));
				prevCandle=new Candlestick(Double.parseDouble(firstRow.get(OPEN_COLUMN_NUM)),
						Double.parseDouble(firstRow.get(HIGH_COLUMN_NUM)),
						Double.parseDouble(firstRow.get(LOW_COLUMN_NUM)),
						Double.parseDouble(firstRow.get(CLOSE_COLUMN_NUM)));
				
			}
				
			while (rowCounter < NUM_ROWS_TO_STORE && (line = br.readLine()) != null) {
//				System.out.println(line);
				String[] row = line.split(cvsSplitBy);
				String time=row[1];
				int index=row[1].indexOf(":");
				time=time.substring(0, index+1)+"00";	
				row[1]=time;
				line = Arrays.toString(row);
				line=line.substring(1,line.length()-1);
				line=line.replaceAll("\\s+", "");
//				System.out.println(line);
				dataset.add(new ArrayList<String>(Arrays.asList(row)));
				firstRow=new ArrayList<String>(Arrays.asList(row));
//				prevRowClose=Double.parseDouble(firstRow.get(columnNum));
				prevCandle=new Candlestick(Double.parseDouble(firstRow.get(OPEN_COLUMN_NUM)),
						Double.parseDouble(firstRow.get(HIGH_COLUMN_NUM)),
						Double.parseDouble(firstRow.get(LOW_COLUMN_NUM)),
						Double.parseDouble(firstRow.get(CLOSE_COLUMN_NUM)));
				rowCounter++;
			}

			ArrayList<Double> prevMovingAverageValues = new ArrayList<Double>();
			// calculate initial moving averages
			
			for (Integer movingAverageVal : movingAverages) {
				double initMovingAvg = calcInitMovingAverage(movingAverageVal, dataset, columnNum);
//				
				String movingAvgVal="";
				if(Double.parseDouble(firstRow.get(columnNum))>initMovingAvg){
					movingAvgVal=STR_ABOVE;
				}else if(Double.parseDouble(firstRow.get(columnNum))<initMovingAvg){
					movingAvgVal=STR_BELOW;
				}else{
					movingAvgVal=STR_ABOVE;
				}
//				line += "," + initMovingAvg;
				line += "," + movingAvgVal;
				prevMovingAverageValues.add(initMovingAvg);
			}

			double prevCandlestickTrend_MA=calcInitMovingAverage(CANDLESTICK_TREND_MA, dataset, columnNum);
			double currentCandlestickTrend_MA;
			// calculate initial trends
			for (Integer trendPeriodVal : trendPeriods) {
				String trend = calcTrend(trendPeriodVal, dataset, columnNum);
				line += "," + trend;
			}
			line+=","+"NONE";
			String prevRowToWrite=line;
//			bw.write(line);

			while ((line = br.readLine()) != null) {
//				System.out.println(line);
				String[] row = line.split(cvsSplitBy);
//				System.out.println(line);
				String time=row[1];
				int index=row[1].indexOf(":");
				time=time.substring(0, index+1)+"00";	
				row[1]=time;
				line = Arrays.toString(row);
				line=line.substring(1,line.length()-1);
				line=line.replaceAll("\\s+", "");
//				System.out.println(line);
				ArrayList<String> currentRow=new ArrayList<String>(Arrays.asList(row));
				
//				dataset.add(new ArrayList<String>(Arrays.asList(row)));
				for (int i = 0; i < movingAverages.size(); i++) {
					Double valueToRemove = Double
							.parseDouble(dataset.get((dataset.size()-1) - (movingAverages.get(i)-1)).get(columnNum));
					double movingAvg = calcNextMovingAverage(prevMovingAverageValues.get(i), movingAverages.get(i),
							new ArrayList<String>(Arrays.asList(row)), columnNum, valueToRemove);
					String movingAvgVal="";
					if(Double.parseDouble(currentRow.get(columnNum))>movingAvg){
						movingAvgVal=STR_ABOVE;
					}else if(Double.parseDouble(currentRow.get(columnNum))<movingAvg){
						movingAvgVal=STR_BELOW;
					}else{
						movingAvgVal=STR_ABOVE;
					}
//					line += "," + movingAvg;
					line+=","+movingAvgVal;
					prevMovingAverageValues.set(i, movingAvg);
				}
				dataset.add(new ArrayList<String>(Arrays.asList(row)));
				// calculate remaining trends
				for (Integer trendPeriodVal : trendPeriods) {
					String trend = calcTrend(trendPeriodVal, dataset, columnNum);
					line += "," + trend;
				}

				// remove the first element in the dataset
				dataset.remove(0);
//				Double prevRowClose = Double.parseDouble((dataset.get(dataset.size() - 2).get(columnNum)));
//				Double currentRowClose = Double.parseDouble((dataset.get(dataset.size() - 1).get(columnNum)));
				currentCandle=new Candlestick(Double.parseDouble(currentRow.get(OPEN_COLUMN_NUM)),
						Double.parseDouble(currentRow.get(HIGH_COLUMN_NUM)),
						Double.parseDouble(currentRow.get(LOW_COLUMN_NUM)),
						Double.parseDouble(currentRow.get(CLOSE_COLUMN_NUM)));
//				System.out.println(dataset.size());
				Double valueToRemove = Double
						.parseDouble(dataset.get((dataset.size()-1) - (CANDLESTICK_TREND_MA-1)).get(columnNum));
				currentCandlestickTrend_MA=calcNextMovingAverage(prevCandlestickTrend_MA, CANDLESTICK_TREND_MA,
						new ArrayList<String>(Arrays.asList(row)), columnNum, valueToRemove);
				String candlestickPattern=Candlestick.getPattern(currentCandle, prevCandle, prevCandlestickTrend_MA,currentCandlestickTrend_MA);
				line+=","+candlestickPattern;
//				currentRowLow=Double.parseDouble(currentRow.get(LOW_COLUMN_NUM));
//				currentRowHigh=Double.parseDouble(currentRow.get(HIGH_COLUMN_NUM));
//				currentRowClose=Double.parseDouble(currentRow.get(columnNum));
				// classification
				String prevRowClassification;
				Double pipDivisor=0.0;
				if(quoteCurr.equals("JPY")){
					pipDivisor=100.0;
				}else{
					pipDivisor=10000.0;
				}
				if ((currentCandle.high - prevCandle.close) > (pips / pipDivisor)) {
					prevRowClassification = "," + "+" + pips + "pips";
				} else if ((currentCandle.low - prevCandle.close) < (-1 * pips / pipDivisor)) {
					prevRowClassification = "," + "-" + pips + "pips";

				} else {
					prevRowClassification = ","+STR_RANGING;
				}
//				bw.write(prevRowClassification + '\n');
//				bw.write(line);
				prevRowToWrite=prevRowToWrite.substring(11,prevRowToWrite.length());
				prevRowToWrite+=prevRowClassification+'\n';
				bw.write(prevRowToWrite);
				prevRowToWrite=line;
//				prevRowClose=currentRowClose;
				prevCandle=currentCandle;
				prevCandlestickTrend_MA=currentCandlestickTrend_MA;
			}
			if(testData){
				prevRowToWrite=prevRowToWrite.substring(11,prevRowToWrite.length());
				bw.write(prevRowToWrite+",?");
				
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
