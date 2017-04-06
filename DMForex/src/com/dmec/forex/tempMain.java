package com.dmec.forex;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class tempMain {

	public static void main(String[] args) throws Exception {
		DataSource source=new DataSource("data/TRAININGSETARFF.arff");
		Instances Trainingdataset = source.getDataSet();
		Trainingdataset.setClassIndex(Trainingdataset.numAttributes()-1);
		
		
		Classifier classifier = Utilities.buildClassifier(Trainingdataset);

		Evaluation eval = Utilities.evaluateClassifier(classifier, Trainingdataset);
		// System.out.println(eval.toSummaryString());
		String correct = Double.toString(Utilities.round(eval.pctCorrect(), 2));
		
		System.out.println("Training: "+correct);
		source=new DataSource("data/TESTSETARFF.arff");
		Instances Testdataset = source.getDataSet();
		Testdataset.setClassIndex(Testdataset.numAttributes()-1);
		eval = Utilities.evaluateClassifier(classifier, Testdataset);
		 correct = Double.toString(Utilities.round(eval.pctCorrect(), 2));
		
		System.out.println("test: "+correct);


		
		String result="";
		try {
			result = Utilities.classifyInstances(classifier, Testdataset);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(result);
		


	}

}
