package com.dmec.forex;

import java.util.ArrayList;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Session Bean implementation class mySBStateless
 */
@Stateless
@LocalBean
public class mySBStateless implements mySBStatelessRemote {

    /**
     * Default constructor. 
     */
    public mySBStateless() {
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
