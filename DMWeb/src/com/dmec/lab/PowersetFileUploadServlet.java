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
	private final static String PreprocessedTrainingFilesFolderName="PreprocessedTrainingFiles";
	private final static String ATrFolderName="ArffTrainingFiles";


	@SuppressWarnings("deprecation")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		// Create path components to save the file
		// final String path = request.getParameter("destination");
		final String path = this.getServletContext().getRealPath("/WEB-INF/");
		final Part trFilePart = request.getPart("trFile");
		final Part teFilePart = request.getPart("teFile");
		String fileName = getFileName(filePart);
		String[] filenameList = fileName.split("\\\\");
		fileName = filenameList[filenameList.length - 1];
		OutputStream out = null;
		InputStream filecontent = null;
		final PrintWriter writer = response.getWriter();
		
		try {
			out = new FileOutputStream(new File(path + File.separator + TrainingFilesFolderName + File.separator + fileName));
			filecontent = filePart.getInputStream();

			int read = 0;
			final byte[] bytes = new byte[1024];

			while ((read = filecontent.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			writer.println("New file " + fileName + " created at " + path + File.separator + TrainingFilesFolderName);
			LOGGER.log(Level.INFO, "File {0} being uploaded to {1} ",
					new Object[] { fileName, path + File.separator + TrainingFilesFolderName });
			
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
			
			
			int pips=Integer.parseInt(request.getParameter("Pips"));
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
			String baseCurr=request.getParameter("f");
			String quoteCurr=request.getParameter("quoteCurrency");
			
			System.out.println(request.getParameter("movingAverages")+
			request.getParameter("trendPeriods")+
			request.getParameter("Pips")+
			request.getParameter("baseCurrency")+
			request.getParameter("quoteCurrency")+
			request.getParameter("calculateOn"));
			String inputFileWithPath=path+File.separator+TrainingFilesFolderName+File.separator+fileName;
			String outputFileWithPath=path+File.separator+PreprocessedTrainingFilesFolderName+File.separator+fileName;
			System.out.println(outputFileWithPath);
//			String []removeStringArray = new String[]{"-R","2-6"};
			
			
//			sbsf.createClassificationTree(inputFileWithPath, outputFileWithPath, removeStringArray, movingAverages, trendPeriods, pips, columnNum);
			ClassifierMaster classifierMaster=sbst.createClassificationTree(inputFileWithPath, outputFileWithPath,path+File.separator+ATrFolderName+File.separator+"temp.arff", new String[]{"-R","2-6"}, movingAverages, trendPeriods, pips, columnNum,baseCurr, quoteCurr);
			String evaluation=sbst.evaluateClassifier(classifierMaster.getClassifier(), path+File.separator+ATrFolderName+File.separator+"temp.arff", new String[]{"-R","2-6"});
			sbst.temporarilyStoreClassifierMaster(classifierMaster);
			request.setAttribute("evaluation", evaluation);
//			request.setAttribute("temp", Arrays.toString(classifierMaster.getColumnIndicesToRemoveArray()));
			RequestDispatcher rd=request.getRequestDispatcher("confirmClassifier.jsp");
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
}