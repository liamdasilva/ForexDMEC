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

import com.dmec.forex.mySBSingleton;
import com.dmec.forex.mySBStateful;
import com.dmec.forex.mySBStateless;

import weka.classifiers.Classifier;

@WebServlet("/confirm")
@MultipartConfig
public class ConfirmClassifierServlet extends HttpServlet {
    @EJB
    private mySBStateful sbsf;
    @EJB
    private mySBStateless sbsl;
    @EJB
    private mySBSingleton sbst;
//    private final static String []removeStringArray=new String[]{"1,3-7"};
	private final static Logger LOGGER = Logger.getLogger(ConfirmClassifierServlet.class.getCanonicalName());
	private final static String ObjectsFolderName="Objects";


	@SuppressWarnings("deprecation")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

//		final PrintWriter writer = response.getWriter();
//		writer.println("SAving fil"+request.getParameter("confirmSave"));
			
		String selection=request.getParameter("confirmSave");
		String fileName=request.getParameter("fileName");
		if(selection.equals("Yes")){
			final String path = this.getServletContext().getRealPath("/WEB-INF/");
			boolean saveSuccessful=sbst.saveClassifierMaster(path+File.separator+ObjectsFolderName+File.separator+fileName);
			if(saveSuccessful){
				request.setAttribute("saveConfirmationMessage", "Classifier was successfully saved");

			}else{
				request.setAttribute("saveConfirmationMessage", "Classifier was not successfully saved");
			}
			RequestDispatcher rd=request.getRequestDispatcher("ConfirmationPage.jsp");
			rd.forward(request, response);
		}else if(selection.equals("No")){
			response.sendRedirect("/DMWeb");
		}
		
//		writer.close();
		
		
	}


	
}