package com.dmec.forex;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedMap;

import java.io.IOException;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.compress.utils.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import weka.classifiers.Classifier;
import weka.core.Instances;

@Path("/")
@RequestScoped
public class myDMRS {

	@EJB
	private mySBSingleton sbst;
	private final static String TestingFilesFolderName = "TestingFiles";
	private final static String PreprocessedTestingFilesFolderName = "PreprocessedTestingFiles";
	private final static String ObjectsFolderName = "Objects";
	private final static String TrainingFilesFolderName = "TrainingFiles";
	private final static String PreprocessedTrainingFilesFolderName = "PreprocessedTrainingFiles";
	private final static String ATrFolderName = "ArffTrainingFiles";
	private final static String ATeFolderName="ArffTestingFiles";

	@POST
	@Path("/uploadTrain")
	@Consumes("multipart/form-data")
	@Produces("text/html")
	public String uploadTrain(MultipartFormDataInput input) throws IOException {
		// check if all form parameters are provided
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");
		System.out.println("===============");
		System.out.println(uploadForm.keySet());
		System.out.println("===============");
		String path = "/Documents/enterprise/wildfly-10.1.0.Final/standalone/deployments/myEAR.ear/DMWeb.war/WEB-INF/";
		String TrainingFilesFolderName = "input";
		String PreprocessedTrainingFilesFolderName = "output";
		
		String fileName = "";
		final String ObjectsFolderName = "Objects";

		// Response response = new Response();
		for (InputPart inputPart : inputParts) {
			try {

				MultivaluedMap<String, String> header = inputPart.getHeaders();
				fileName = getFileName(header);

				// convert the uploaded file to inputstream
				InputStream inputStream = inputPart.getBody(InputStream.class, null);

				byte[] bytes = IOUtils.toByteArray(inputStream);
				// constructs upload file path

				fileName = fileName;
				writeFile(bytes, path + File.separator + TrainingFilesFolderName + File.separator + fileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// inputParts = uploadForm.get("movingAverages");
		inputParts = uploadForm.get("movingAverages");
		String[] strMovingAveragesArray = null;
		for (InputPart inputPart : inputParts) {
			strMovingAveragesArray = inputPart.getBodyAsString().split(",");
		}
		// String[] strMovingAveragesArray = ((String)
		// input.getFormDataPart("movingAverages", null)).split(",");
		ArrayList<Integer> movingAverages = new ArrayList<Integer>();
		if (strMovingAveragesArray.length > 0) {
			for (String movingAverage : strMovingAveragesArray) {
				movingAverages.add(Integer.parseInt(movingAverage));
			}
		}
		inputParts = uploadForm.get("trendPeriods");
		String[] strtrendPeriodsArray = null;
		for (InputPart inputPart : inputParts) {
			strtrendPeriodsArray = inputPart.getBodyAsString().split(",");
		}
		// String[] strtrendPeriodsArray = ((String)
		// input.getFormDataPart("trendPeriods", null)).split(",");

		ArrayList<Integer> trendPeriods = new ArrayList<Integer>();
		if (strtrendPeriodsArray.length > 0) {
			for (String trendPeriod : strtrendPeriodsArray) {
				trendPeriods.add(Integer.parseInt(trendPeriod));
			}
		}

		int pips = 12;
		inputParts = uploadForm.get("Pips");
		for (InputPart inputPart : inputParts) {
			pips = Integer.parseInt(inputPart.getBodyAsString());
		}

		String strCalculateOn = "close";
		inputParts = uploadForm.get("calculateOn");
		for (InputPart inputPart : inputParts) {
			strCalculateOn = inputPart.getBodyAsString();
		}

		int columnNum = 0;
		if (strCalculateOn.equals("open")) {
			columnNum = 2;
		} else if (strCalculateOn.equals("high")) {
			columnNum = 3;
		} else if (strCalculateOn.equals("low")) {
			columnNum = 4;
		} else if (strCalculateOn.equals("close")) {
			columnNum = 5;
		}
		String baseCurr = "USD";
		inputParts = uploadForm.get("baseCurrency");
		for (InputPart inputPart : inputParts) {
			baseCurr = inputPart.getBodyAsString();
		}
		String quoteCurr = "EUR";
		inputParts = uploadForm.get("quoteCurrency");
		for (InputPart inputPart : inputParts) {
			quoteCurr = inputPart.getBodyAsString();
		}

		String inputFileWithPath = path + File.separator + TrainingFilesFolderName + File.separator + fileName;
		String outputFileWithPath = path + File.separator + PreprocessedTrainingFilesFolderName + File.separator
				+ fileName;
		String arffOutputFileWithPath = path + File.separator + PreprocessedTrainingFilesFolderName + File.separator
				+ fileName;
		System.out.println(outputFileWithPath);
		String[] removeStringArray = new String[] { "-R", "1,3-7" };

		System.out.println("=======================================================================================");
		System.out.println(inputFileWithPath);
		System.out.println(outputFileWithPath);
		System.out.println(movingAverages);
		System.out.println(trendPeriods);
		System.out.println(pips);
		System.out.println(columnNum);
		System.out.println(baseCurr);
		System.out.println(quoteCurr);
		System.out.println("=======================================================================================");

		ClassifierMaster classifierMaster = sbst.createClassificationTree(inputFileWithPath, outputFileWithPath,
				path + File.separator + ATrFolderName + File.separator + "temp.arff", new String[] { "-R", "2-6" },
				movingAverages, trendPeriods, pips, columnNum, baseCurr, quoteCurr);
		String evaluation = sbst.evaluateClassifier(classifierMaster.getClassifier(),
				path + File.separator + ATrFolderName + File.separator + "temp.arff", new String[] { "-R", "2-6" });
		sbst.temporarilyStoreClassifierMaster(classifierMaster);

		URI uri = UriBuilder.fromPath("../confirmClassifier.jsp").queryParam("evaluation", evaluation).build();
		fileName = baseCurr + "_" + quoteCurr + "_" + pips + "pips" + "_"
				+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + ".bin";

		boolean saveSuccessful = sbst
				.saveClassifierMaster(path + File.separator + ObjectsFolderName + File.separator + fileName);
		if (saveSuccessful) {
			return "<html><head><title>Results</title>" + "<link rel='stylesheet' type='text/css' href='style.css'>"
					+ "<link rel=\"icon\" type=\"image/ico\" href=\"favicon.png\">" + "</head>" + "<body>"
					+ "Evaluation: " + evaluation + " was saved as " + fileName + "<br>"
					+ "<a href=\"http://localhost:8080/myRS/\">Return to Homepage</a>";
		} else {
			return "<html><head><title>Results</title>" + "<link rel='stylesheet' type='text/css' href='style.css'>"
					+ "<link rel=\"icon\" type=\"image/ico\" href=\"favicon.png\">" + "</head>" + "<html><body>"
					+ "File was not saved" + "<br>" + "<a href=\"http://localhost:8080/myRS/\">Return to Homepage</a>";
		}
	}

	private String getFileName(MultivaluedMap<String, String> header) {

		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {

				String[] name = filename.split("=");

				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

	@POST
	@Path("/uploadTest")
	@Consumes("multipart/form-data")
	@Produces("text/html")
	public String uploadTest(MultipartFormDataInput input) throws IOException {
		// check if all form parameters are provided
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");
		System.out.println("===============");
		System.out.println(uploadForm.keySet());
		System.out.println("===============");
		String path = "/Documents/enterprise/wildfly-10.1.0.Final/standalone/deployments/myEAR.ear/DMWeb.war/WEB-INF/";
		String TrainingFilesFolderName = "input";
		String PreprocessedTrainingFilesFolderName = "output";
		String fileName = "";
		final String ObjectsFolderName = "Objects";
		final String TestingFilesFolderName = "output";

		// Response response = new Response();
		for (InputPart inputPart : inputParts) {
			try {

				MultivaluedMap<String, String> header = inputPart.getHeaders();
				fileName = getFileName(header);

				// convert the uploaded file to inputstream
				InputStream inputStream = inputPart.getBody(InputStream.class, null);

				byte[] bytes = IOUtils.toByteArray(inputStream);
				// constructs upload file path

				fileName = fileName;
				writeFile(bytes, path + File.separator + TestingFilesFolderName + File.separator + fileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String inputFileWithPath = path + File.separator + TestingFilesFolderName + File.separator + fileName;
		String outputFileWithPath = path + File.separator + PreprocessedTestingFilesFolderName + File.separator
				+ fileName;
		System.out.println(outputFileWithPath);
		String[] removeStringArray = new String[] { "-R", "1,3-7" };

		String objectName = "test.bin";
		inputParts = uploadForm.get("fileName");
		for (InputPart inputPart : inputParts) {
			objectName = inputPart.getBodyAsString();
		}

		System.out.println("=======================================================================================");
		System.out.println(inputFileWithPath);
		System.out.println(outputFileWithPath);

		System.out.println("=======================================================================================");

		ClassifierMaster classifierMaster = sbst.getClassifierMaster(objectName,
				path + File.separator + ObjectsFolderName + File.separator);
		Classifier classifier = classifierMaster.getClassifier();
		String testInputFileWithPath = path + File.separator + TestingFilesFolderName + File.separator + fileName;
		String testOutputFileWithPath = path + File.separator + PreprocessedTestingFilesFolderName + File.separator
				+ fileName;
		String[] columnIndicesToRemoveArray = classifierMaster.getColumnIndicesToRemoveArray();
		ArrayList<Integer> movingAverages = classifierMaster.getMovingAverages();
		ArrayList<Integer> trendPeriods = classifierMaster.getTrendPeriods();
		int pips = classifierMaster.getPips();
		int OLHC_ColumnNum = classifierMaster.getOLHC_ColumnNum();
		Instances dataset = classifierMaster.getInstances();
		String baseCurr = classifierMaster.getBaseCurr();
		String quoteCurr = classifierMaster.getQuoteCurr();
		// System.out.println(Arrays.toString(columnIndicesToRemoveArray));
		String newLineStr = "<br>";
		ArrayList<ArrayList<String>> results = sbst.classifyData(classifier, testInputFileWithPath,
				testOutputFileWithPath, path + File.separator + ATeFolderName + File.separator + "temp.arff",
				columnIndicesToRemoveArray, movingAverages, trendPeriods, pips, OLHC_ColumnNum, dataset, baseCurr,
				quoteCurr, newLineStr);
		// writer.println(Arrays.toString(results.toArray())); }
		String returnHTML = "<html><head><title>Results</title>"
				+ "<link rel='stylesheet' type='text/css' href='style.css'>"
				+ "<link rel=\"icon\" type=\"image/ico\" href=\"favicon.png\">" + "</head>" + "<html><body>";

		returnHTML += "Here is the classification of the last/only row from the test data that was provided(prediction): "
				+ results.get(results.size() - 1).get(1) + "<br>";
		int counter = 0;
		int correctlyClassifiedNonRangingInstances = 0;
		int totalNonRangingClassifications = 0;
		int incorrectlyClassifiedNonRangingInstances = 0;
		int nonRangingClassifcationsThatWereRanging = 0;
		String table = "";
		table += "<table>";
		/* out.println("<table>"); */
		for (int i = 0; i < results.size(); i++) {
			/* out.println("<tr>"); */
			table += "<tr>";
			if (results.get(i).get(0).equals(results.get(i).get(1))) {
				counter++;
			}

			if (i != 0 && i != (results.size() - 1) && !results.get(i).get(1).equals("RANGING")) {
				if (results.get(i).get(0).equals(results.get(i).get(1))) {
					correctlyClassifiedNonRangingInstances++;
				}
				if (results.get(i).get(0).contains("+") && results.get(i).get(1).contains("-")) {
					incorrectlyClassifiedNonRangingInstances++;
				} else if (results.get(i).get(0).contains("-") && results.get(i).get(1).contains("+")) {
					incorrectlyClassifiedNonRangingInstances++;
				} else if (!results.get(i).get(1).equals("RANGING") && results.get(i).get(0).equals("RANGING")) {
					nonRangingClassifcationsThatWereRanging++;
				}
				totalNonRangingClassifications++;
			}
			for (int j = 0; j < results.get(i).size(); j++) {
				if (i == 0) {
					/* out.print("<th>"+results.get(i).get(j)+"</th>"); */
					table += "<th>" + results.get(i).get(j) + "</th>";

				} else if (i != (results.size() - 1)) {
					/* out.print("<td>"+results.get(i).get(j)+"</td>"); */
					table += "<td>" + results.get(i).get(j) + "</td>";

				}
			}

			/* out.println("</tr>"); */
			table += "</tr>";
		}

		/* out.println("</table>"); */
		table += "</table>";
		returnHTML += correctlyClassifiedNonRangingInstances + "/" + totalNonRangingClassifications
				+ " successful trading opportunities were captured in the test data set<br>";
		returnHTML += incorrectlyClassifiedNonRangingInstances + "/" + totalNonRangingClassifications
				+ " unsuccessful trading opportunities were captured in the test data set<br>";
		returnHTML += nonRangingClassifcationsThatWereRanging + "/" + totalNonRangingClassifications
				+ " classifications that ended up 'RANGING'<br>";

		Double accuracy = (double) counter / (double) (results.size());
		returnHTML += "Total correctly classified instances (from test data): " + accuracy * 100 + "%" + "<br>";
		returnHTML += "Here are the classification results of the test data:<br>";
		returnHTML += table;
		returnHTML += "</body></html>";

		return returnHTML;

	}

	// Utility method
	private void writeFile(byte[] content, String filename) throws IOException {
		File file = new File(filename);
		if (!file.exists()) {
			System.out.println("not exist> " + file.getAbsolutePath());
			file.createNewFile();
		}
		FileOutputStream fop = new FileOutputStream(file);
		fop.write(content);
		fop.flush();
		fop.close();
	}
}
