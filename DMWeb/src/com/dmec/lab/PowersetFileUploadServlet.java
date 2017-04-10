package com.dmec.lab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import com.dmec.forex.Classification;
import com.dmec.forex.ClassifierMaster;
import com.dmec.forex.mySBSingleton;

import weka.classifiers.Classifier;

@WebServlet("/uploadPowerset")
@MultipartConfig
public class PowersetFileUploadServlet extends HttpServlet {
    @EJB
    private mySBSingleton sbst;
//    private final static String []removeStringArray=new String[]{"1,2-6"};
	private final static Logger LOGGER = Logger.getLogger(PowersetFileUploadServlet.class.getCanonicalName());
	private final static String TrainingFilesFolderName="TrainingFiles";
	private final static String TestingFilesFolderName="TestingFiles";
	private final static String PreprocessedTrainingFilesFolderName="PreprocessedTrainingFiles";
	private final static String PreprocessedTestingFilesFolderName="PreprocessedTestingFiles";
	private final static String ATrFolderName="ArffTrainingFiles";
	private final static String ATeFolderName="ArffTestingFiles";
	


	@SuppressWarnings("deprecation")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		// Create path components to save the file
		// final String path = request.getParameter("destination");
		final String path = this.getServletContext().getRealPath("/WEB-INF/");
		final Part trFilePart = request.getPart("trFile");
		final Part teFilePart = request.getPart("teFile");
		String trFileName = getFileName(trFilePart);
		String teFileName = getFileName(teFilePart);
		String[] filenameList = trFileName.split("\\\\");
		trFileName = filenameList[filenameList.length - 1];
		filenameList = teFileName.split("\\\\");
		teFileName = filenameList[filenameList.length - 1];
		
		OutputStream out = null;
		InputStream filecontent = null;
		final PrintWriter writer = response.getWriter();
		
		try {
			
			//Write training file
//			------------------------------------------------------------------------------------------------------------
			out = new FileOutputStream(new File(path + File.separator + TrainingFilesFolderName + File.separator + trFileName));
			filecontent = trFilePart.getInputStream();

			int read = 0;
			final byte[] bytes = new byte[1024];

			while ((read = filecontent.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			writer.println("New file " + trFileName + " created at " + path + File.separator + TrainingFilesFolderName);
			LOGGER.log(Level.INFO, "File {0} being uploaded to {1} ",
					new Object[] { trFileName, path + File.separator + TrainingFilesFolderName });
//			------------------------------------------------------------------------------------------------------------
			
			//Write testing file
//			------------------------------------------------------------------------------------------------------------
			out = new FileOutputStream(new File(path + File.separator + TestingFilesFolderName + File.separator + teFileName));
			filecontent = teFilePart.getInputStream();

			read = 0;
			final byte[] bytes2 = new byte[1024];

			while ((read = filecontent.read(bytes2)) != -1) {
				out.write(bytes2, 0, read);
			}
			writer.println("New file " + teFileName + " created at " + path + File.separator + TestingFilesFolderName);
			LOGGER.log(Level.INFO, "File {0} being uploaded to {1} ",
					new Object[] { teFileName, path + File.separator + TestingFilesFolderName });
//			------------------------------------------------------------------------------------------------------------
			
			String inputFileWithPath=path+File.separator+TrainingFilesFolderName+File.separator+trFileName;
			String outputFileWithPath=path+File.separator+PreprocessedTrainingFilesFolderName+File.separator+trFileName;
			String trainingARFF= path+File.separator+ATrFolderName+File.separator+"train.arff";
			String testInputFileWithPath = path+File.separator+TestingFilesFolderName+File.separator+teFileName;
			String testOutputFileWithPath = path+File.separator+PreprocessedTestingFilesFolderName+File.separator+teFileName;
			String testingARFF= path+File.separator+ATeFolderName+File.separator+"test.arff";
			
			//CALL THE CLASSIFICATION
			String[] strMovingAveragesArray=request.getParameter("movingAverages").split(",");
			ArrayList<Integer> movingAverages=new ArrayList<Integer>();
			if(!request.getParameter("movingAverages").equals("")){
				for(String movingAverage: strMovingAveragesArray){
					movingAverages.add(Integer.parseInt(movingAverage));
				}
			}
			
			
			String[] strtrendPeriodsArray=request.getParameter("trendPeriods").split(",");
			ArrayList<Integer> trendPeriods=new ArrayList<Integer>();
			if(!request.getParameter("trendPeriods").equals("")){
				for(String trendPeriod: strtrendPeriodsArray){
					trendPeriods.add(Integer.parseInt(trendPeriod));
				}
			}
			
			String[] pipsArray = request.getParameter("Pips").split(",");
			ArrayList<Integer> pipsToTry=new ArrayList<Integer>();
			if(!request.getParameter("Pips").equals("")){
				for(String pip: pipsArray){
					pipsToTry.add(Integer.parseInt(pip));
				}
			}
			
			String strCalculateOn=request.getParameter("calculateOn");
			int columnNum=0;
			if(strCalculateOn.equals("open")){
				columnNum=2;
			}else if(strCalculateOn.equals("high")){
				columnNum=3;
			}else if(strCalculateOn.equals("low")){
				columnNum=4;
			}else if(strCalculateOn.equals("close")){
				columnNum=5;
			}
			String baseCurr=request.getParameter("baseCurrency");
			String quoteCurr=request.getParameter("quoteCurrency");
			
			double percent = 0.0;
			double max = 0.0;
			ArrayList<Integer> OPT_movingAverages = new ArrayList<Integer>();
			ArrayList<Integer> OPT_trendPeriods = new ArrayList<Integer>();
			int OPT_pips = 0;
			int correct = 0;
			int total = 0;
			ClassifierMaster OPT_ClassifierMaster=null;
			
			List<Integer> list = new ArrayList<Integer>(movingAverages);
	        Set<Integer> set = new HashSet<Integer>(list);
			Set<Set<Integer>> movingAveragesPowerSet=powerSet(set);
			
			list = new ArrayList<Integer>(trendPeriods);
	        set = new HashSet<Integer>(list);
			Set<Set<Integer>> trendPeriodsPowerSet=powerSet(set); 
			int combinationsTried = 0;
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
								columnNum, false, baseCurr, quoteCurr);
						ArrayList<ArrayList<String>> results = Classification.classifyData(cmObject.getClassifier(),
								testInputFileWithPath, testOutputFileWithPath, testingARFF, new String[] { "-R", "2-6" }, cmObject.getMovingAverages(),
								cmObject.getTrendPeriods(), cmObject.getPips(), cmObject.getOLHC_ColumnNum(), cmObject.getInstances(), baseCurr, quoteCurr,
								"\n");
						int counter = 0;
						int correctlyClassifiedNonRangingInstances = 0;
						int totalNonRangingClassifications = 0;
						combinationsTried++;
//						int incorrectlyClassifiedNonRangingInstances = 0;
//						int nonRangingClassifcationsThatWereRanging = 0;
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
//						System.out.println("Yeah");
						percent = (double) correctlyClassifiedNonRangingInstances
								/ (double) (totalNonRangingClassifications);
						if (percent > max) {
							OPT_ClassifierMaster=cmObject;
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
			
			System.out.println("Out of "+combinationsTried+" variable combinations tried, the best is:");
			System.out.println(correct + "/" + total +" correct trading opportunities.");
			System.out.println(max*100 +"% accurate");
			System.out.println("Moving Averages: "+OPT_movingAverages);
			System.out.println("Trend periods: "+OPT_trendPeriods);
			System.out.println("Pips Class: "+OPT_pips);

//			String evaluation=sbst.evaluateClassifier(OPT_ClassifierMaster.getClassifier(), path+File.separator+ATrFolderName+File.separator+"temp.arff", new String[]{"-R","2-6"});
			sbst.temporarilyStoreClassifierMaster(OPT_ClassifierMaster);
			request.setAttribute("combinationsTried",combinationsTried);
			request.setAttribute("numCorrect",correct);
			request.setAttribute("numTotal",total);
			request.setAttribute("cmObject", OPT_ClassifierMaster);
//			request.setAttribute("evaluation", evaluation);
//			request.setAttribute("temp", Arrays.toString(classifierMaster.getColumnIndicesToRemoveArray()));
			RequestDispatcher rd=request.getRequestDispatcher("powersetResults.jsp");
			rd.forward(request, response);
						
		} catch (FileNotFoundException fne) {
			writer.println("You either did not specify a file to upload or are "
					+ "trying to upload a file to a protected or nonexistent " + "location.");
			writer.println("<br/> ERROR: " + fne.getMessage());

			LOGGER.log(Level.FATAL, "Problems during file upload. Error: {0}", new Object[] { fne.getMessage() });
		} finally {
			if (out != null) {
				out.close();
			}
			if (filecontent != null) {
				filecontent.close();
			}
			if (writer != null) {
				writer.close();
			}
		}
		
	}


	private String getFileName(final Part part) {
		final String partHeader = part.getHeader("content-disposition");
		LOGGER.log(Level.INFO, "Part Header = {0}".format(partHeader));
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}
	
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
}