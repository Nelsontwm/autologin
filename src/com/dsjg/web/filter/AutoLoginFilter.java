package com.dsjg.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dsjg.domain.User;
import com.dsjg.service.UserService;

public class AutoLoginFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// 1、转换两个对象HttpServletRequest，HttpServletResponse
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String uri = req.getRequestURI();// /autologin/login.jsp
		String path = req.getContextPath();// /autologin
		path = uri.substring(path.length()); // /login.jsp

		//如果请求的资源不是login.jsp，也不是/servlet/loginServlet，才往下执行
		if (!("/login.jsp".equals(path) || "/servlet/loginServlet".equals(path))) {
			User user = (User) req.getSession().getAttribute("user");
			//如果session得到了user对象，说明已经登录过或自动登录过。
			//那么请求下一个资源时就不用执行自动登录了。
			//用户没有登录过，我们才执行自动登录 
			if (user == null) {
				System.out.println("aaaaaaaaaaaaaaa");
				// 2、处理业务
				// 得到cookies数组
				Cookie[] cookies = req.getCookies();
				String username = "";
				String password = "";
				// 从数组中找到想要的user对象的信息
				for (int i = 0; cookies != null && i < cookies.length; i++) {
					if ("user".equals(cookies[i].getName())) {
						String value = cookies[i].getValue();// tom&123
						String[] values = value.split("&");
						username = values[0];
						password = values[1];
					}
				}

				// 登录操作
				UserService us = new UserService();
				User u = us.findUser(username, password);
				// 如果登录成功，把用户信息存到session中
				if (u != null) {
					req.getSession().setAttribute("user", u);
				}
			}
		}
		// 3、放行
		chain.doFilter(request, response);
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

}
