package com.dmec.forex;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class myEJBClient {

    public static void main(String[] args) throws NamingException {
//    	Path currentRelativePath = Paths.get("");
//    	String s = currentRelativePath.toAbsolutePath().toString();
//    	System.out.println("Current relative path is: " + s);
    	mySBSingletonRemote myBean = (mySBSingletonRemote) InitialContext
    			.doLookup("myEAR/myEJB/mySBSingleton!com.dmec.forex.mySBSingletonRemote");

    	System.out.println("Here are the available classifiers:");
    	String userDir = System.getProperty("user.home");
    	String objectsPath = userDir+"/Documents/enterprise/wildfly-10.1.0.Final/standalone/deployments/myEAR.ear/DMWeb.war/WEB-INF/Objects/";
    	String pFilesPath = userDir+"/Documents/enterprise/wildfly-10.1.0.Final/standalone/deployments/myEAR.ear/DMWeb.war/WEB-INF/PreprocessedTestingFiles/";
    	String aFilesPath = userDir+"/Documents/enterprise/wildfly-10.1.0.Final/standalone/deployments/myEAR.ear/DMWeb.war/WEB-INF/ArffTestingFiles/";
    	File[] files = new File(objectsPath).listFiles();
    	for(File file:files){
    		if (!file.isDirectory()){
    			System.out.println(file.getName());
    		}
    	}
    	System.out.println("Enter the name of the classifier you would like:");
    	Scanner scanner = new Scanner(System.in);
    	String input = scanner.nextLine();
    	ClassifierMaster cm = myBean.getClassifierMaster(input, objectsPath);
    	System.out.println("Enter the path of the testing file you would like to use:");
    	input = scanner.nextLine();
    	ArrayList<ArrayList<String>> results = myBean.classifyData(cm.getClassifier(), input, pFilesPath+"pre.csv",aFilesPath+"pre.arff", cm.getColumnIndicesToRemoveArray(), cm.getMovingAverages(), cm.getTrendPeriods(), cm.getPips(), cm.getOLHC_ColumnNum(), cm.getInstances(), cm.getBaseCurr(), cm.getQuoteCurr(), "");
    	
    	System.out.println(results.toString());
        System.out.println("RMI done!");

    }

}

