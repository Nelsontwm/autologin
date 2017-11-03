package com.dsjg.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dsjg.domain.User;
import com.dsjg.service.UserService;
import com.itheima.util.MD5Utils;


public class LoginServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		//MD5加密
		password = MD5Utils.md5(password);
		UserService us = new UserService();
		User user = us.findUser(username,password);
		
		if(user!=null){
			String autologin = request.getParameter("autologin");
			Cookie cookie = new Cookie("user", user.getUsername()+"&"+user.getPassword());
			cookie.setPath("/");
			if(autologin!=null){//要把用户信息保存到cookie中
				cookie.setMaxAge(60*60*24*7);
			}else{//要清除cookie对象的数据
				cookie.setMaxAge(0);
			}
			response.addCookie(cookie);//把cookie对象保存到客户端
			
			request.getSession().setAttribute("user", user);
			request.getRequestDispatcher("/home.jsp").forward(request, response);
		}else{
			request.setAttribute("msg", "用户名或密码错误,请重新登录！");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
