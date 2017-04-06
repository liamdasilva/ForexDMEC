package com.dmec.forex;

import java.io.Serializable;
import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.core.Instances;

public class ClassifierMaster implements Serializable{
	
	private Classifier classifier;
	private String[] columnIndicesToRemoveArray;
	private ArrayList<Integer> movingAverages;
	private ArrayList<Integer> trendPeriods;
	private int pips;
	private int OLHC_ColumnNum;
	private Instances instances;
	private String baseCurr;
	private String quoteCurr;
	
	public ClassifierMaster(Classifier classifier, String [] columnIndicesToRemoveArray, ArrayList<Integer> movingAverages, ArrayList<Integer> trendPeriods, int pips, int OLHC_ColumnNum,Instances instances, String baseCurr, String quoteCurr){
		this.setClassifier(classifier);
		this.setColumnIndicesToRemoveArray(columnIndicesToRemoveArray);
		this.setMovingAverages(movingAverages);
		this.setTrendPeriods(trendPeriods);
		this.setPips(pips);
		this.setOLHC_ColumnNum(OLHC_ColumnNum);
		this.setInstances(instances);
		this.setBaseCurr(baseCurr);
		this.setQuoteCurr(quoteCurr);
	}

	public Classifier getClassifier() {
		return classifier;
	}

	public void setClassifier(Classifier classifier) {
		this.classifier = classifier;
	}

	public String[] getColumnIndicesToRemoveArray() {
		return columnIndicesToRemoveArray;
	}

	public void setColumnIndicesToRemoveArray(String[] columnIndicesToRemoveArray) {
		this.columnIndicesToRemoveArray = columnIndicesToRemoveArray;
	}

	public ArrayList<Integer> getMovingAverages() {
		return movingAverages;
	}

	public void setMovingAverages(ArrayList<Integer> movingAverages) {
		this.movingAverages = movingAverages;
	}

	public ArrayList<Integer> getTrendPeriods() {
		return trendPeriods;
	}

	public void setTrendPeriods(ArrayList<Integer> trendPeriods) {
		this.trendPeriods = trendPeriods;
	}

	public int getPips() {
		return pips;
	}

	public void setPips(int pips) {
		this.pips = pips;
	}

	public int getOLHC_ColumnNum() {
		return OLHC_ColumnNum;
	}

	public void setOLHC_ColumnNum(int OLHC_ColumnNum) {
		this.OLHC_ColumnNum = OLHC_ColumnNum;
	}

	public Instances getInstances() {
		return instances;
	}

	public void setInstances(Instances instances) {
		this.instances = instances;
	}

	public String getBaseCurr() {
		return baseCurr;
	}

	public void setBaseCurr(String baseCurr) {
		this.baseCurr = baseCurr;
	}

	public String getQuoteCurr() {
		return quoteCurr;
	}

	public void setQuoteCurr(String quoteCurr) {
		this.quoteCurr = quoteCurr;
	}
	
	

}
