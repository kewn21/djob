/**
 * This file created at 2014年5月16日.
 */
package org.kesy.djob.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.kesy.djob.core.exception.ExceptionPrint;
import org.kesy.djob.core.json.Json;
import org.kesy.djob.core.spring.SpringFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <code>{@link DataCommandServlet}</code>
 *
 * TODO : document me
 *
 * @author dengqb
 */
public class DataCommandServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;
	private static final String	CONTENT_TYPE_FORMAT	= "text/json;charset=%s";
	private static Logger		logger				= LoggerFactory.getLogger(DataCommandServlet.class);
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String encoding = req.getCharacterEncoding();
		
		DataResponse response = null;
		try {
			String reqJson = readCommandString(req, encoding);
			
			if (logger.isDebugEnabled()) {
				logger.debug("DataRequest json : " + reqJson);
			}
			
			DataRequest request = Json.toObject(reqJson, DataRequest.class);
			response = invokeCommand(request);
		} catch (Exception e) {
			logger.error(ExceptionPrint.getStackTrace(e));
			response = DataResponse.newBuilder().setException(ExceptionPrint.getStackTrace(e)).build();
		}
		
		try {
			String respJson = Json.toJson(response);
			
			if (logger.isDebugEnabled()) {
				logger.debug("DataResponse json : " + respJson);
			}
			
			writeCommandResponse(respJson, resp, encoding);
		} catch (Exception e) {
			logger.error(ExceptionPrint.getStackTrace(e));
			throw new ServletException(e);
		}
	}
	
	private DataResponse invokeCommand(DataRequest request) throws Exception {
		if (request == null) {
			throw new IllegalArgumentException(
					"DataRequest converted from HttpServletRequest must be not null");
		}
		
        String beanName = null;
        String methodName = null;
        if (request.getCommandName().indexOf(".") > 0) {
        	String[] cmdArr = request.getCommandName().split("\\.");
        	if (cmdArr.length == 2) {
        		beanName = cmdArr[0];
                methodName = cmdArr[1];
			}
        }
        
        if (StringUtils.isNotEmpty(beanName) && StringUtils.isNotEmpty(methodName)) {
        	Object command = SpringFactory.getBean(beanName);
        	if (command != null) {
        		Method method = MethodUtils.getAccessibleMethod(command.getClass(), 
        				methodName, 
        				DataRequest.class);
        		
        		if (method == null) {
        			throw new UnsupportedOperationException(String.format(
        					"the method [%s] of beanName [%s] for command [%s] doesn't exists", 
        					methodName,
        					beanName, 
            				request));
				}
        		return (DataResponse)method.invoke(command, request);
			}
        	else {
        		throw new UnsupportedOperationException(String.format(
        				"the bean [%s] for command [%s] doesn't exist", 
        				beanName, 
        				request.getCommandName()));
			}
		}
        else {
        	throw new UnsupportedOperationException(
        			"the command message sent by DataRequest which doesn't has correct content");
		}
	}
	
	private String readCommandString(HttpServletRequest req, String encoding) throws UnsupportedEncodingException{
		StringBuilder cmdSb = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(
					new InputStreamReader(req.getInputStream(), encoding));
			String line;
			while ((line = reader.readLine()) != null) {
				cmdSb.append(line + "\n");
			}
		} catch (UnsupportedEncodingException e) {
		} catch (IOException e) {
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
			}
		}
		
		return URLDecoder.decode(cmdSb.toString(), encoding);
	}

	private void writeCommandResponse(String respJson, HttpServletResponse resp, String encoding){
		resp.setContentType(String.format(CONTENT_TYPE_FORMAT, encoding));
		
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(
					new OutputStreamWriter(resp.getOutputStream(), encoding));
			writer.write(respJson);
			writer.flush();
		} catch (IOException e) {
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
			}
		}
	}
}
