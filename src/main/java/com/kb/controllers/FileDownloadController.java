package com.kb.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FileDownloadController implements ServletContextAware {

	private ServletContext servletContext;
	
	private static final int BUFFER_SIZE = 4096;
	
	 private String filePath = "/downloads/example.pdf";
	
	
	@RequestMapping(value = "/displayForm", method = RequestMethod.GET)
	public ModelAndView downloadFileFormDisplay() {

		return new ModelAndView("downloadFile");
		
	}

	
	//Handle the single file upload
	
	@RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
	public void doDownload(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		// get the absolute path of the application
       
        String appPath = servletContext.getRealPath("");
        System.out.println("appPath = " + appPath);
 
        // absolute path of the file
        String fullPath = appPath + filePath;      
        File downloadFile = new File(fullPath);
        FileInputStream inputStream = new FileInputStream(downloadFile);
         
        // MIME type of the file
        String mimeType = servletContext.getMimeType(fullPath);
        if (mimeType == null) {
            // Set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        System.out.println("MIME type: " + mimeType);
 
        // set content attributes for the response object
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());
 
        // set headers for the response object
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                downloadFile.getName());
        response.setHeader(headerKey, headerValue);
 
        // get output stream of the response
        OutputStream outStream = response.getOutputStream();
 
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;
 
        // write each byte of data  read from the input stream into the output stream
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
 
        inputStream.close();
        outStream.close();
 }
	



	public void setServletContext(ServletContext servletContext) {
		this.servletContext=servletContext;
		
	}

}
