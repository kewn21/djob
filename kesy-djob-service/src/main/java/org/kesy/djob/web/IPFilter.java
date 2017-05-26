/**
 * 2014年9月9日
 */
package org.kesy.djob.web;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kewn
 *
 */
public class IPFilter implements Filter {
	
	private static final Logger logger = LoggerFactory.getLogger(IPFilter.class);
	
	private static final String[] IP_WHITE_LIST = new String[] 
			{
				"113.108.232.31",
				"113.108.232.32",
				"113.108.232.33",
				"113.108.232.34",
				"113.108.232.35",
				"113.108.232.36",
				"113.108.232.37",
				"113.108.232.38",
				"113.108.232.39",
				"183.60.177.227",
				"183.60.177.229",
				"113.106.251.85",
				"113.106.251.82",
				"183.60.177.247",
				"127.0.0.1"
			};

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		
		String clientIp = getRealIp((HttpServletRequest)req);
		logger.info("Client ip address : {}", clientIp);
		
		boolean valid = false;
		for (String whiteIp : IP_WHITE_LIST) {
			if (whiteIp.equals(clientIp)) {
				valid = true;
				break;
			}
		}
		
		if (valid) {
			chain.doFilter(req, resp);    
		}
		else {
			logger.info("Invalid ip : [{}]", clientIp);
			resp.getWriter().write("none auth!");
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}
	
	private static final Pattern IS_LICIT_IP_PATTERN = Pattern.compile("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$");
	
	public static String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isEmpty(ip))
            return request.getRemoteAddr();
        int index = ip.lastIndexOf(',');
        String lastip = ip.substring(index + 1).trim();
        if("127.0.0.1".equals(lastip) || !isLicitIp(lastip))
            return request.getRemoteAddr();
        else
            return lastip;
    }
	
	public static boolean isLicitIp(String ip) {
        if(StringUtils.isEmpty(ip))
            return false;
        Matcher m = IS_LICIT_IP_PATTERN.matcher(ip);
        return m.find();
    }

}
