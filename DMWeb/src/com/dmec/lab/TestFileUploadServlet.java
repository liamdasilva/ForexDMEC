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
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
import com.dmec.forex.mySBStateful;
import com.dmec.forex.mySBStatefulRemote;
import com.dmec.forex.mySBStateless;

import weka.classifiers.Classifier;
import weka.core.Instances;

@WebServlet("/uploadTest")
@MultipartConfig
public class TestFileUploadServlet extends HttpServlet {
    @EJB
    private mySBStateful sbsf;
    @EJB
    private mySBStateless sbsl;
    @EJB
    private mySBSingleton sbst;
//    private final static String []removeStringArray=new String[]{"1,3-7"};
	private final static Logger LOGGER = Logger.getLogger(TestFileUploadServlet.class.getCanonicalName());
	private final static String TestingFilesFolderName="TestingFiles";
	private final static String PreprocessedTestingFilesFolderName="PreprocessedTestingFiles";
	private final static String ObjectsFolderName="Objects";


	@SuppressWarnings("deprecation")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		

		 
		response.setContentType("text/html;charset=UTF-8");

		// Create path components to save the file
		// final String path = request.getParameter("destination");
		final String path = this.getServletContext().getRealPath("/WEB-INF/");
		final Part filePart = request.getPart("file");
		String fileName = getFileName(filePart);
		String[] filenameList = fileName.split("\\\\");
		fileName = filenameList[filenameList.length - 1];
		OutputStream out = null;
		InputStream filecontent = null;
		final PrintWriter writer = response.getWriter();

		try {
			out = new FileOutputStream(new File(path + File.separator + TestingFilesFolderName + File.separator + fileName));
			filecontent = filePart.getInputStream();

			int read = 0;
			final byte[] bytes = new byte[1024];

			while ((read = filecontent.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			writer.println("New file " + fileName + " created at " + path + File.separator + TestingFilesFolderName);
			LOGGER.log(Level.INFO, "File {0} being uploaded to {1} ",
					new Object[] { fileName, path + File.separator + TestingFilesFolderName });
			String objectName=request.getParameter("objectName");
			System.out.println(objectName);

			ClassifierMaster classifierMaster=sbst.getClassifierMaster(objectName, path+File.separator+ObjectsFolderName+File.separator);
			Classifier classifier=classifierMaster.getClassifier();
			String testInputFileWithPath=path+File.separator+TestingFilesFolderName+File.separator+fileName;
			String testOutputFileWithPath=path+File.separator+PreprocessedTestingFilesFolderName+File.separator+fileName;
			String []columnIndicesToRemoveArray=classifierMaster.getColumnIndicesToRemoveArray();
			ArrayList<Integer> movingAverages=classifierMaster.getMovingAverages();
			ArrayList<Integer> trendPeriods=classifierMaster.getTrendPeriods();
			int pips=classifierMaster.getPips();
			int OLHC_ColumnNum=classifierMaster.getOLHC_ColumnNum();
			Instances dataset=classifierMaster.getInstances();
			String baseCurr=classifierMaster.getBaseCurr();
			String quoteCurr=classifierMaster.getQuoteCurr();
//			System.out.println(Arrays.toString(columnIndicesToRemoveArray));	
			String newLineStr="<br>";
			ArrayList<ArrayList<String>>results=sbst.classifyData(classifier, testInputFileWithPath, testOutputFileWithPath, columnIndicesToRemoveArray, movingAverages, trendPeriods, pips, OLHC_ColumnNum, dataset, baseCurr, quoteCurr,newLineStr);
//			writer.println(Arrays.toString(results.toArray()));
			
//			String []removeStringArray = new String[]{"-R","1,3-7"};
			
			
			request.setAttribute("results", results);
			
			RequestDispatcher rd=request.getRequestDispatcher("testDataResults.jsp");
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