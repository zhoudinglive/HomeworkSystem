package com.carpenter.ssm.web.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.carpenter.ssm.assist.FileMaker;
import com.carpenter.ssm.assist.StringToDate;
import com.carpenter.ssm.pojo.BaseInfo;
import com.carpenter.ssm.pojo.HomeworkInfo;
import com.carpenter.ssm.pojo.HomeworkSubmit;
import com.carpenter.ssm.pojo.StayInfo;
import com.carpenter.ssm.pojo.StaySubmit;
import com.carpenter.ssm.service.impl.BaseInfoServiceImpl;
import com.carpenter.ssm.service.impl.HomeworkInfoServiceImpl;
import com.carpenter.ssm.service.impl.HomeworkSubmitServiceImpl;
import com.carpenter.ssm.service.impl.StayInfoServiceImpl;
import com.carpenter.ssm.service.impl.StaySubmitServiceImpl;
import com.carpenter.ssm.service.impl.UserServiceImpl;

/**
 * @Title: UserController.java
 * @Package com.carpenter.ssm.web.controller
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月23日 上午9:33:27
 * @version V1.0
 */

@Controller
@RequestMapping(value = "/user")
public class UserController {
	@Autowired
	private RedisOperationsSessionRepository sessionRepository;

	@Autowired
	private BaseInfoServiceImpl baseInfoService;

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private HomeworkInfoServiceImpl homeworkInfoService;

	@Autowired
	private HomeworkSubmitServiceImpl homeworkSubmitService;

	@Autowired
	private StayInfoServiceImpl stayInfoService;

	@Autowired
	private StaySubmitServiceImpl staySubmitService;

	@RequestMapping(value = "getBaseInfoById", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String getBaseInfo(HttpServletRequest request) throws Exception {
		HttpSession httpSession = request.getSession(false);
		if (!sessionChecker(httpSession)) {
			return "{\"state\":0}";
		}

		try {
			BaseInfo baseInfo = baseInfoService.getBaseInfoById((String) httpSession.getAttribute("username"));
			return "{\"state\":1,\"name\":\"" + baseInfo.getBase_name() + "\",\"student_id\":\"" + baseInfo.getBase_id()
					+ "\",\"sex\":\"" + baseInfo.getBase_sex() + "\",\"portraitpath\":\"" + baseInfo.getBase_portrait()
					+ "\"}";
		} catch (Exception e) {
			return "{\"state\":0}";
		}
	}

	@RequestMapping(value = "updatePassword", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String updatePassword(HttpServletRequest request) throws Exception {
		HttpSession httpSession = request.getSession(false);
		if (!sessionChecker(httpSession)) {
			return "{\"state\":0,\"username\":}";
		}

		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		if (oldPassword == null || oldPassword.trim().equals("") || newPassword == null
				|| newPassword.trim().equals("")) {
			return "{\"state\":0,\"username\":}";
		} else {
			try {
				if (this.userService.updatePassword((String) httpSession.getAttribute("username"), oldPassword,
						newPassword)) {
					return "{\"state\":1,\"username\":\"" + (String) httpSession.getAttribute("username") + "\"}";
				}
				return "{\"state\":0,\"username\":}";
			} catch (Exception e) {
				return "{\"state\":0,\"username\":}";
			}
		}
	}

	@RequestMapping(value = "getAllHomeworkInfo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String getAllHomeworkInfo(HttpServletRequest request) throws Exception {
		HttpSession httpSession = request.getSession(false);
		if (!sessionChecker(httpSession)) {
			return "{\"state\":0}";
		}
		try {
			List<HomeworkInfo> homeworkInfoAll = this.homeworkInfoService.selectAll();
			if (homeworkInfoAll == null) {
				return "{\"state\":0}";
			}
			String header = "{\"homeworks\":[";
			for (int i = 0; i < homeworkInfoAll.size(); ++i) {
				HomeworkInfo homeworkInfo = homeworkInfoAll.get(i);
				int flag = StringToDate.compareToNow(homeworkInfo.getHomework_deadline()) ? 1 : 0;
				if (i != homeworkInfoAll.size() - 1) {
					String ctime = new SimpleDateFormat("yyyy-MM-dd H:m:s")
							.format(homeworkInfo.getHomework_createtime());
					String dtime = new SimpleDateFormat("yyyy-MM-dd H:m:s").format(homeworkInfo.getHomework_deadline());
					header += "{\"id\":\"" + homeworkInfo.getHomework_id() + "\",\"name\":\""
							+ homeworkInfo.getHomework_name() + "\",\"createtime\":\"" + ctime + "\",\"deadline\":\""
							+ dtime + "\",\"cut_off\":\"" + flag + "\"},";
				} else {
					String ctime = new SimpleDateFormat("yyyy-MM-dd H:m:s")
							.format(homeworkInfo.getHomework_createtime());
					String dtime = new SimpleDateFormat("yyyy-MM-dd H:m:s").format(homeworkInfo.getHomework_deadline());
					header += "{\"id\":\"" + homeworkInfo.getHomework_id() + "\",\"name\":\""
							+ homeworkInfo.getHomework_name() + "\",\"createtime\":\"" + ctime + "\",\"deadline\":\""
							+ dtime + "\",\"cut_off\":\"" + flag + "\"}]}";
				}
			}
			if (homeworkInfoAll.size() == 0) {
				return "{\"state\":1}";
			}
			return header;
		} catch (Exception e) {
			return "{\"state\":0}";
		}
	}

	@RequestMapping(value = "getHomeworkInfoById", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String getHomeworkInfoById(HttpServletRequest request) throws Exception {
		HttpSession httpSession = request.getSession(false);
		if (!sessionChecker(httpSession) || request.getParameter("homework_id") == null) {
			return "{\"state\":0}";
		}

		try {
			HomeworkInfo homeworkInfo = this.homeworkInfoService.getHomeworkById(request.getParameter("homework_id"));
			if (homeworkInfo == null) {
				return "{\"state\":0}";
			}
			int flag = StringToDate.compareToNow(homeworkInfo.getHomework_deadline()) ? 1 : 0;
			return "{\"id\":\"" + homeworkInfo.getHomework_id() + "\",\"name\":\"" + homeworkInfo.getHomework_name()
					+ "\",\"details\":\"" + homeworkInfo.getHomework_details() + "\",\"photo\":\""
					+ homeworkInfo.getHomework_photo() + "\",\"createtime\":\""
					+ new SimpleDateFormat("yyyy-MM-dd H:m:s").format(homeworkInfo.getHomework_createtime())
					+ "\",\"deadline\":\""
					+ new SimpleDateFormat("yyyy-MM-dd H:m:s").format(homeworkInfo.getHomework_deadline())
					+ "\",\"initiator\":\""
					+ this.baseInfoService.getBaseInfoById(homeworkInfo.getHomework_initiator()).getBase_name()
					+ "\",\"cut_off\":\"" + flag + "\"}";
		} catch (Exception e) {
			return "{\"state\":0}";
		}

	}

	@RequestMapping(value = "/homeworkSubmit", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String homeworkSubmit(HttpServletRequest request) throws Exception {
		HttpSession httpSession = request.getSession();

		if (!sessionChecker(httpSession)) {
			return "{\"state\":0}";
		} else if (request.getParameter("homework_id") == null) {
			return "{\"state\":0,\"username\":" + (String) httpSession.getAttribute("username") + "}";
		} else if (!ServletFileUpload.isMultipartContent(request)) {
			return "{\"state\":0,\"username\":" + (String) httpSession.getAttribute("username") + "}";
		} else if (!StringToDate.compareToNow(
				this.homeworkInfoService.getHomeworkById(request.getParameter("homework_id")).getHomework_deadline())) {
			return "{\"state\":0,\"username\":" + (String) httpSession.getAttribute("username") + "}";
		}
		String savepath = null;
		try {
			savepath = this.homeworkSubmitService.getExactHomeworkSubmitById(request.getParameter("homework_id"),
					(String) httpSession.getAttribute("username")).getSave_path();
			// String savepath = "/home/upload/";
			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
			diskFileItemFactory.setSizeThreshold(4096);
			diskFileItemFactory.setRepository(new File("/tmp"));

			ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
			servletFileUpload.setFileSizeMax(-1);
			servletFileUpload.setSizeMax(100 * 1024 * 1024);
			servletFileUpload.setHeaderEncoding("utf-8");

			List<FileItem> fileItems = servletFileUpload.parseRequest(request);
			if (fileItems == null) {
				return "{\"state:0\",\"username\":" + (String) httpSession.getAttribute("username") + "}";
			}
			boolean flag = false;
			FileMaker.deleteFileDirButItself(savepath);
			this.homeworkSubmitService.updateTime(request.getParameter("homework_id"),
					(String) httpSession.getAttribute("username"), this.homeworkInfoService
							.getHomeworkById(request.getParameter("homework_id")).getHomework_createtime());
			for (FileItem fileItem : fileItems) {
				if (!fileItem.isFormField() && fileItem != null && !fileItem.getName().trim().equals("")) {
					flag = true;
					if (fileItem.getSize() > 100 * 1024 * 1024) {
						FileMaker.deleteFileDirButItself(savepath);
						this.homeworkSubmitService.updateTime(request.getParameter("homework_id"),
								(String) httpSession.getAttribute("username"), this.homeworkInfoService
										.getHomeworkById(request.getParameter("homework_id")).getHomework_createtime());
						return "{\"state\":0,\"username\":" + (String) httpSession.getAttribute("username") + "}";
					}

					String path = savepath + File.separator + fileItem.getName();
					File file = new File(path);
					try {
						fileItem.write(file);
					} catch (Exception e) {
						// e.printStackTrace();
						FileMaker.deleteFileDirButItself(savepath);
						this.homeworkSubmitService.updateTime(request.getParameter("homework_id"),
								(String) httpSession.getAttribute("username"), this.homeworkInfoService
										.getHomeworkById(request.getParameter("homework_id")).getHomework_createtime());
						return "{\"state\":0,\"username\":" + (String) httpSession.getAttribute("username") + "}";
					}
				}
			}
			if (!flag) {
				FileMaker.deleteFileDirButItself(savepath);
				this.homeworkSubmitService.updateTime(request.getParameter("homework_id"),
						(String) httpSession.getAttribute("username"), this.homeworkInfoService
								.getHomeworkById(request.getParameter("homework_id")).getHomework_createtime());

				return "{\"state\":0,\"username\":" + (String) httpSession.getAttribute("username") + "}";
			}
			this.homeworkSubmitService.updateTime(request.getParameter("homework_id"),
					(String) httpSession.getAttribute("username"), new Date());
			return "{\"state\":1,\"username\":" + (String) httpSession.getAttribute("username") + "}";

		} catch (Exception e) {
			FileMaker.deleteFileDirButItself(savepath);
			this.homeworkSubmitService.updateTime(request.getParameter("homework_id"),
					(String) httpSession.getAttribute("username"), this.homeworkInfoService
							.getHomeworkById(request.getParameter("homework_id")).getHomework_createtime());
			return "{\"state\":0,\"username\":" + (String) httpSession.getAttribute("username") + "}";
		}
	}

	@RequestMapping(value = "/getAllStayInfo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String getAllStayInfo(HttpServletRequest request) throws Exception {
		HttpSession httpSession = request.getSession(false);

		if (!sessionChecker(httpSession)) {
			return "{\"state\":0}";
		}

		try {
			List<StayInfo> stayInfoAll = this.stayInfoService.selectAll();
			if (stayInfoAll == null) {
				return "{\"state\":0}";
			}
			String header = "{\"stays\":[";
			for (int i = 0; i < stayInfoAll.size(); ++i) {
				StayInfo stayInfo = stayInfoAll.get(i);
				String stay_id = stayInfo.getStay_id();
				String stay_name = stayInfo.getStay_name();
				String stay_deadline = new SimpleDateFormat("yyyy-MM-dd H:m:s").format(stayInfo.getStay_deadline());
				int flag = StringToDate.compareToNow(stayInfo.getStay_deadline()) ? 1 : 0;

				if (i != stayInfoAll.size() - 1) {
					header += "{\"stay_id\":\"" + stay_id + "\",\"stay_name\":\"" + stay_name
							+ "\",\"stay_deadline\":\"" + stay_deadline + "\",\"cut_off\":\"" + flag + "\"},";
				} else {
					header += "{\"stay_id\":\"" + stay_id + "\",\"stay_name\":\"" + stay_name
							+ "\",\"stay_deadline\":\"" + stay_deadline + "\",\"cut_off\":\"" + flag + "\"}]}";
				}
			}
			if (stayInfoAll.size() == 0) {
				return "{\"state\":1}";
			}
			return header;
		} catch (Exception e) {
			return "{\"state\":0}";
		}
	}

	@RequestMapping(value = "/getStayInfoById", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String getStayInfoById(HttpServletRequest request) throws Exception {
		HttpSession httpSession = request.getSession(false);
		String stay_id = request.getParameter("stay_id");

		if (stay_id == null || stay_id.trim().equals("")) {
			return "{\"state\":0}";
		} else if (!sessionChecker(httpSession)) {
			return "{\"state\":0}";
		}

		try {
			StayInfo stayInfo = this.stayInfoService.getStayInfoById(stay_id);
			if (stayInfo == null) {
				return "{\"state\":0}";
			}
			int flag = StringToDate.compareToNow(stayInfo.getStay_deadline()) ? 1 : 0;
			return "{\"stay_id\":\"" + stayInfo.getStay_id() + "\",\"stay_name\":\"" + stayInfo.getStay_name()
					+ "\",\"stay_details\":\"" + stayInfo.getStay_details() + "\",\"stay_begintime\":\""
					+ new SimpleDateFormat("yyyy-MM-dd H:m:s").format(stayInfo.getStay_begintime())
					+ "\",\"stay_stoptime\":\""
					+ new SimpleDateFormat("yyyy-MM-dd H:m:s").format(stayInfo.getStay_stoptime())
					+ "\",\"stay_createtime\":\""
					+ new SimpleDateFormat("yyyy-MM-dd H:m:s").format(stayInfo.getStay_createtime())
					+ "\",\"stay_deadline\":\""
					+ new SimpleDateFormat("yyyy-MM-dd H:m:s").format(stayInfo.getStay_deadline()) + "\",\"cut_off\":\""
					+ flag + "\"}";
		} catch (Exception e) {
			return "{\"state\":0}";
		}
	}

	@RequestMapping(value = "/updateTime", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String updateTime(HttpServletRequest request) throws Exception {
		HttpSession httpSession = request.getSession(false);
		String stay_id = request.getParameter("stay_id");
		Date stay_start = StringToDate.stringToDate(request.getParameter("stay_start"));
		Date stay_stop = StringToDate.stringToDate(request.getParameter("stay_stop"));
		String leave_details = request.getParameter("leave_details");

		if (stay_id == null || stay_id.trim().equals("")) {
			return "{\"state\":0}";
		} else if (!sessionChecker(httpSession)) {
			return "{\"state\":0}";
		}

		String username = (String) httpSession.getAttribute("username");
		try {
			StaySubmit testStay = this.staySubmitService.getExactStaySubmitById(stay_id, username);
			if(testStay.getStay_start().equals(testStay.getStay_stop()) && (leave_details == null || leave_details.trim().equals(""))){
				return "{\"state\":0}";
			}
			StaySubmit staySubmit = new StaySubmit();
			staySubmit.setStay_id(stay_id);
			staySubmit.setStudent_id(username);
			//System.out.println(stay_stop);
			if(stay_start!=null){
				staySubmit.setStay_start(stay_start);
			}
			if(stay_stop!=null){
				staySubmit.setStay_stop(stay_stop);
			}
			if(leave_details!=null && !leave_details.trim().equals("")){
				staySubmit.setLeave_details(leave_details);
			}
			if(staySubmit.getStay_start()==null&&staySubmit.getStay_stop()==null&&staySubmit.getLeave_details()==null){
				return "{\"state\":0}";
			}
			
			if (this.staySubmitService.updateInterval(staySubmit)) {
				return "{\"state\":1}";
			}
			return "{\"state\":0}";
		} catch (Exception e) {
			return "{\"state\":0}";
		}
	}
	
	@RequestMapping(value = "/getMyUpdated", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	private @ResponseBody String getMyUpdated(HttpServletRequest request) throws Exception{
		HttpSession httpSession = request.getSession(false);
		String stay_id = request.getParameter("stay_id");
		
		if(stay_id == null || stay_id.trim().equals("")){
			return "{\"state\":0}";
		}else if(!sessionChecker(httpSession)){
			return "{\"state\":0}";
		}
		
		String username = (String) httpSession.getAttribute("username");
		try {
			StaySubmit staySubmit = this.staySubmitService.getExactStaySubmitById(stay_id, username);
			if(staySubmit == null){
				return "{\"state\":0}";
			}
			if(staySubmit.getStay_start().equals(staySubmit.getStay_stop())){
				return "{\"state\":0}";
			}else{
				String start = new SimpleDateFormat("yyyy-MM-dd H:m:s").format(staySubmit.getStay_start());
				String stop = new SimpleDateFormat("yyyy-MM-dd H:m:s").format(staySubmit.getStay_stop());
				return "{\"stay_start\":\"" + start 
						+ "\",\"stay_stop\":\"" + stop
						+ "\",\"leave_details\":\"" + staySubmit.getLeave_details() + "\"}";
			}
			
		} catch (Exception e) {
			return "{\"state\":0}";
		}
	}
	
	private boolean sessionChecker(HttpSession httpSession) {
		if (httpSession == null) {
			return false;
		} else {
			if (this.sessionRepository.getSession(httpSession.getId()) == null) {
				return false;
			}
			if (httpSession.getAttribute("username") == null) {
				return false;
			}
		}
		return true;
	}

	// for jsp
	@RequestMapping(value = "/homeworkSubmit", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public ModelAndView touchHomeworkSubmit() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("homeworkSubmit");
		return modelAndView;
	}

	@RequestMapping(value = "/task", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public ModelAndView touchTaskSubmit() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("task");
		return modelAndView;
	}
	
	@RequestMapping(value = "/stayUpdate", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public ModelAndView touchStayUpdate() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("stayUpdate");
		return modelAndView;
	}
}
