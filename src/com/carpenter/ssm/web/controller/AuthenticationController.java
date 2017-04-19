package com.carpenter.ssm.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Title: AuthenticationController.java
 * @Package com.carpenter.ssm.web.controller
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月23日 上午9:33:11
 * @version V1.0
 */

@Controller
public class AuthenticationController {
	@Autowired
	private RedisOperationsSessionRepository sessionRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter outResponse = response.getWriter();
		HttpSession httpSession = request.getSession();

		httpSession.setAttribute("username", request.getParameter("username"));
		httpSession.setAttribute("password", passwordEncoder.encode(request.getParameter("password")));

		String remember = request.getParameter("remember-me");
		if (remember != null && remember.equals("on")) {
			//System.out.println("success on");
			httpSession.setMaxInactiveInterval(24 * 60 * 60 * 7);
			Cookie cookie = new Cookie("SESSION",httpSession.getId());
			cookie.setMaxAge(24*60*60*7);
			response.addCookie(cookie);
		}
		outResponse.write("{\"state\":1,\"username\":" + request.getParameter("username") + "}");
	}

	@RequestMapping(value = "login_session", method = RequestMethod.POST)
	public void loginSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter outResponse = response.getWriter();
		HttpSession httpSession = request.getSession(false);
		if (httpSession != null && sessionRepository.getSession(httpSession.getId()) != null) {
			if (httpSession.getAttribute("username") != null) {
				outResponse.write("{\"state\":1,\"username\":" + httpSession.getAttribute("username") + "}");
			} else {
				outResponse.write("{\"state\":0,\"username\":}");
			}
		} else {
			outResponse.write("{\"state\":0,\"username\":}");
		}
	}

	@RequestMapping(value = "/login_error", method = RequestMethod.GET)
	public void loginError(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter outResponse = response.getWriter();
		outResponse.write("{\"state\":0,\"username\":}");
	}

//	@RequestMapping(value = "logout", method = RequestMethod.POST)
//	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		//PrintWriter outResponse = response.getWriter();
//		HttpSession httpSession = request.getSession(false);
//
//		if (httpSession != null && sessionRepository.getSession(httpSession.getId()) != null) {
//			sessionRepository.delete(httpSession.getId());
//		}
//		//outResponse.write("{\"state\":1}");
//	}

	
	
	// just for test, delete it when it's going to working
//	@RequestMapping(value = "/admin**", method = RequestMethod.GET)
//	public ModelAndView adminPage(HttpServletRequest request) {
//		HttpSession httpSession = request.getSession(false);
//		if (httpSession == null) {
//			ModelAndView model = new ModelAndView();
//			model.addObject("title", "Spring Security Login Form - Database Authentication");
//			model.addObject("message", "This is default page!");
//			model.setViewName("hello");
//			return model;
//		}
//		System.out.println("1" + httpSession.getAttribute("username"));
//		System.out.println("1" + httpSession.getAttribute("password"));
//		Session session = sessionRepository.getSession(httpSession.getId());
//		if (session != null) {
//			System.out.println("2" + (String) session.getAttribute("username"));
//			System.out.println("2" + (String) session.getAttribute("password"));
//		}
//
//		ModelAndView model = new ModelAndView();
//		model.addObject("title", "Spring Security Login Form - Database Authentication");
//		model.addObject("message", "This page is for ROLE_ADMIN only!");
//		model.setViewName("admin");
//		return model;
//
//	}

	@RequestMapping(value = { "/", "/welcome**" }, method = RequestMethod.GET)
	public ModelAndView defaultPage() {

		ModelAndView model = new ModelAndView();
		model.addObject("title", "Spring Security Login Form - Database Authentication");
		model.addObject("message", "This is default page!");
		model.setViewName("hello");
		return model;

	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login2(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModelAndView model = new ModelAndView();
		model.setViewName("login");
		return model;
	}

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public ModelAndView accesssDenied() {

		ModelAndView model = new ModelAndView();

		// check if user is login
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			model.addObject("username", userDetail.getUsername());
		}

		model.setViewName("403");
		return model;
	}
}
