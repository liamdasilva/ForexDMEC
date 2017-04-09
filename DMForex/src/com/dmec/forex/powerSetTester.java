package com.dmec.forex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class powerSetTester {
	
	public static Set<Set<Integer>> powerSet(Set<Integer> originalSet) {
        Set<Set<Integer>> sets = new HashSet<Set<Integer>>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<Integer>());
            return sets;
        }
        List<Integer> list = new ArrayList<Integer>(originalSet);
        Integer head = list.get(0);
        Set<Integer> rest = new HashSet<Integer>(list.subList(1, list.size()));
        for (Set<Integer> set : powerSet(rest)) {
            Set<Integer> newSet = new HashSet<Integer>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        return sets;
    }

	public static void main(String[] args) {
		String userDir = System.getProperty("user.home");
		String inputFileWithPath = userDir+"/desktop/training and testing files/EURUSD60trainingJanFeb.csv";
		String outputFileWithPath = userDir+"/desktop/training and testing files/preprocessed_EURUSD60trainingJanFeb.csv";
		String testingARFF= userDir+"/desktop/training and testing files/test.arff";
		String testInputFileWithPath = userDir+"/desktop/training and testing files/EURUSD60testingMar.csv";
		String testOutputFileWithPath = userDir+"/desktop/training and testing files/preprocessed_EURUSD60testingMar.csv";
		String trainingARFF= userDir+"/desktop/training and testing files/training.arff";
		
		ArrayList<Integer> movingAverages=new ArrayList<Integer>();
		//movingAverages.add(3);
		//movingAverages.add(5);
		movingAverages.add(8);
		movingAverages.add(10);
		movingAverages.add(20);
		movingAverages.add(50);
		movingAverages.add(100);
		movingAverages.add(200);

	
		
		ArrayList<Integer> trendPeriods=new ArrayList<Integer>();
		trendPeriods.add(3);
		trendPeriods.add(4);
		trendPeriods.add(5);
		trendPeriods.add(6);
//		trendPeriods.add(20);

		
		
		ArrayList<Integer> pipsToTry=new ArrayList<Integer>();
		pipsToTry.add(10);
		pipsToTry.add(11);
		pipsToTry.add(12);
		pipsToTry.add(13);
		pipsToTry.add(14);
 		pipsToTry.add(15);
		pipsToTry.add(16);
		pipsToTry.add(17);
//		pipsToTry.add(20);

//		int pips=24;
		int columnNum=5;
		double percent = 0.0;
		double max = 0.0;
		ArrayList<Integer> OPT_movingAverages = new ArrayList<Integer>();
		ArrayList<Integer> OPT_trendPeriods = new ArrayList<Integer>();
		int OPT_pips = 0;
		int correct = 0;
		int total = 0;
		
		List<Integer> list = new ArrayList<Integer>(movingAverages);
        Set<Integer> set = new HashSet<Integer>(list);
		Set<Set<Integer>> movingAveragesPowerSet=powerSet(set);
		
		list = new ArrayList<Integer>(trendPeriods);
        set = new HashSet<Integer>(list);
		Set<Set<Integer>> trendPeriodsPowerSet=powerSet(set); 
		
		for(Set<Integer> movingAverageSet: movingAveragesPowerSet){
			ArrayList<Integer> tempmovingAverages=new ArrayList<Integer>();
			for(int movingAverage:movingAverageSet){
				tempmovingAverages.add(movingAverage);
			}
			for(Set<Integer> trendPeriodSet: trendPeriodsPowerSet){
				ArrayList<Integer> tempTrendPeriods=new ArrayList<Integer>();
				for(int trendPeriod:trendPeriodSet){
					tempTrendPeriods.add(trendPeriod);
				}
				for(Integer p: pipsToTry){
					int pips=p;
					
					ClassifierMaster cmObject = Classification.createClassificationTree(inputFileWithPath,
							outputFileWithPath, trainingARFF, new String[] { "-R", "2-6" }, tempmovingAverages, tempTrendPeriods, pips,
							columnNum, false, "EUR", "EUR");
					ArrayList<ArrayList<String>> results = Classification.classifyData(cmObject.getClassifier(),
							testInputFileWithPath, testOutputFileWithPath, testingARFF, new String[] { "-R", "2-6" }, cmObject.getMovingAverages(),
							cmObject.getTrendPeriods(), cmObject.getPips(), cmObject.getOLHC_ColumnNum(), cmObject.getInstances(), "EUR", "EUR",
							"\n");
					int counter = 0;
					int correctlyClassifiedNonRangingInstances = 0;
					int totalNonRangingClassifications = 0;
//					int incorrectlyClassifiedNonRangingInstances = 0;
//					int nonRangingClassifcationsThatWereRanging = 0;
					for (int i = 0; i < results.size(); i++) {
						/* out.println("<tr>"); */
						if (results.get(i).get(0).equals(results.get(i).get(1))) {
							counter++;
						}

						if (i != 0 && i != (results.size() - 1) && !results.get(i).get(1).equals("RANGING")) {
							if (results.get(i).get(0).equals(results.get(i).get(1))) {
								correctlyClassifiedNonRangingInstances++;
							}
							
							totalNonRangingClassifications++;
						}
					}
//					System.out.println("Yeah");
					percent = (double) correctlyClassifiedNonRangingInstances
							/ (double) (totalNonRangingClassifications);
					if (percent > max) {
						correct = correctlyClassifiedNonRangingInstances;
						total = totalNonRangingClassifications;
						OPT_movingAverages = cmObject.getMovingAverages();
						OPT_trendPeriods = cmObject.getTrendPeriods();
						max = percent;
						OPT_pips = cmObject.getPips();
						System.out.println(correct + "/" + total);
						System.out.println(max);
						System.out.println(OPT_movingAverages);
						System.out.println(OPT_trendPeriods);
						System.out.println(OPT_pips);
						
						
					}
					
				}
			}
		}
		System.out.println(correct + "/" + total);
		System.out.println(max);
		System.out.println(OPT_movingAverages);
		System.out.println(OPT_trendPeriods);
		System.out.println(OPT_pips);
		
//		
//		ClassifierMaster cmObject = Classification.createClassificationTree(inputFileWithPath,
//				outputFileWithPath, new String[] { "-R", "1,3-7" }, movingAverages, trendPeriods, pips,
//				columnNum, false, "EUR", "EUR");
//		
//		ArrayList<ArrayList<String>> results = Classification.classifyData(cmObject.getClassifier(),
//				testInputFileWithPath, testOutputFileWithPath, new String[] { "-R", "1,3-7" }, movingAverages,
//				trendPeriods, pips, cmObject.getOLHC_ColumnNum(), cmObject.getInstances(), "EUR", "EUR",
//				"\n");
//    	List<Integer> list = new ArrayList<Integer>(movingAverages);
//        Set<Integer> set = new HashSet<Integer>(list);
//
//		System.out.println(powerSet(set).size());
//		Set<Set<Integer>> powerSet=powerSet(set); 
////		List<String> newList = new ArrayList<String>(powerSet(set));
//		System.out.println(list);
//		for(Set<Integer> item: powerSet){
//			System.out.println(item);
//		}


	}

}
