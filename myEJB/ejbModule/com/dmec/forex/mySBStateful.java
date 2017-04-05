package com.dmec.forex;

import java.util.ArrayList;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;

/**
 * Session Bean implementation class mySBStateful
 */
@Stateful
@LocalBean
public class mySBStateful implements mySBStatefulRemote {

    /**
     * Default constructor. 
     */
    public mySBStateful() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public void createClassificationTree(String inputFileWithPath, String outputFileWithPath,
			String[] removeStringArray, ArrayList<Integer> movingAverages, ArrayList<Integer> trendPeriods, int pips,
			int columnNum) {
		Classification.createClassificationTree(inputFileWithPath, outputFileWithPath, removeStringArray, movingAverages, trendPeriods, pips, columnNum);

		// TODO Auto-generated method stub
		
	}
    
	

}
