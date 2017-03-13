package com.dreamaker.util;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class BodyWriteHttpServletResponseWrapper extends HttpServletResponseWrapper{

	ByteArrayOutputStream output;
	FilterServletOutputStream filterOutput;

	public BodyWriteHttpServletResponseWrapper(HttpServletResponse response) {
	    super(response);
	    output = new ByteArrayOutputStream();
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
	    if (filterOutput == null) {
	        filterOutput = new FilterServletOutputStream(output);
	    }
	    return filterOutput;
	    
	}
	
	

	@Override
	public PrintWriter getWriter() throws IOException {
		// TODO Auto-generated method stub
		
		return super.getWriter();
	}

	public byte[] getDataStream() {

	    return output.toByteArray();
	}
}
