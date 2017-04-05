package com.dmec.forex;

import java.util.ArrayList;

import javax.ejb.Remote;

@Remote
public interface mySBStatelessRemote {
	public void createClassificationTree(String inputFileWithPath, String outputFileWithPath, String [] removeStringArray, ArrayList<Integer> movingAverages, ArrayList<Integer> trendPeriods, int pips, int columnNum);


}
