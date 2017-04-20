package com.carpenter.ssm.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.carpenter.ssm.assist.FileMaker;
import com.carpenter.ssm.assist.StringToDate;
import com.carpenter.ssm.dao.HomeworkInfoDaoMapper;
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

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.tools.ant.taskdefs.Zip;

/**
 * @Title: AdminController.java
 * @Package com.carpenter.ssm.web.controller
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月23日 上午9:33:41
 * @version V1.0
 */

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
	@Autowired
	private HomeworkInfoDaoMapper homeworkInfoDao;

	@Autowired
	private HomeworkInfoServiceImpl homeworkInfoService;

	@Autowired
	private HomeworkSubmitServiceImpl homeworkSubmitService;

	@Autowired
	private StayInfoServiceImpl stayInfoService;

	@Autowired
	private StaySubmitServiceImpl staySubmitService;

	@Autowired
	private RedisOperationsSessionRepository sessionRepository;

	@Autowired
	private BaseInfoServiceImpl baseInfoService;

	@RequestMapping(value = "/createHomework", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String createHomework(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setCharacterEncoding("UTF-8");
		String homework_name = request.getParameter("homework_name");// .getBytes("iso-8859-1"),"utf-8");
		String homework_details = request.getParameter("homework_details");// .getBytes("iso-8859-1"),
																			// "utf-8");
		Date homework_deadline = StringToDate.stringToDate(request.getParameter("homework_deadline"));
		HttpSession httpSession = request.getSession(false);
		// System.out.println(homework_name);
		if (homework_name == null || homework_name.trim().equals("") || homework_deadline == null) {
			return "{\"state\":0}";
		} else if (!sessionChecker(httpSession)) {
			return "{\"state\":0}";
		}

		HomeworkInfo homeworkInfo = new HomeworkInfo();
		homeworkInfo.setHomework_name(homework_name);
		if (homework_details != null) {
			homeworkInfo.setHomework_details(homework_details);
		}
		homeworkInfo.setHomework_deadline(homework_deadline);
		try {
			homeworkInfo.setHomework_initiator((String) httpSession.getAttribute("username"));
		} catch (Exception e) {
			return "{\"state\":0}";
		}

		try {
			if (this.homeworkInfoService.createNewHomework(homeworkInfo)) {
				return "{\"state\":1}";
			}
			// return this.homeworkInfoService.createNewHomework(homeworkInfo);
			return "{\"state\":0}";
		} catch (Exception e) {
			return "{\"state\":0}";
		}
	}

	@RequestMapping(value = "/getMyCreateHomework", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String getMyCreateHomework(HttpServletRequest request) throws Exception {
		HttpSession httpSession = request.getSession(false);
		if (!sessionChecker(httpSession)) {
			return "{\"state\":0}";
		}
		String username = (String) httpSession.getAttribute("username");
		try {
			List<HomeworkInfo> homeworkInfoAll = this.homeworkInfoService.selectAll();
			if (homeworkInfoAll == null) {
				return "{\"state\":0}";
			}
			boolean flagger = false;
			String header = "{\"homeworks\":[";
			for (int i = 0; i < homeworkInfoAll.size(); ++i) {
				HomeworkInfo homeworkInfo = homeworkInfoAll.get(i);
				if (username.equals(homeworkInfo.getHomework_initiator()) || username.equals("2014211598")) {
					flagger = true;
					if (i != homeworkInfoAll.size() - 1) {
						int flag = StringToDate.compareToNow(homeworkInfo.getHomework_deadline()) ? 1 : 0;
						String ctime = new SimpleDateFormat("yyyy-MM-dd H:m:s")
								.format(homeworkInfo.getHomework_createtime());
						String dtime = new SimpleDateFormat("yyyy-MM-dd H:m:s")
								.format(homeworkInfo.getHomework_deadline());
						header += "{\"id\":\"" + homeworkInfo.getHomework_id() + "\",\"name\":\""
								+ homeworkInfo.getHomework_name() + "\",\"createtime\":\"" + ctime
								+ "\",\"deadline\":\"" + dtime + "\",\"cut_off\":\"" + flag + "\"},";
					} else {
						int flag = StringToDate.compareToNow(homeworkInfo.getHomework_deadline()) ? 1 : 0;
						String ctime = new SimpleDateFormat("yyyy-MM-dd H:m:s")
								.format(homeworkInfo.getHomework_createtime());
						String dtime = new SimpleDateFormat("yyyy-MM-dd H:m:s")
								.format(homeworkInfo.getHomework_deadline());
						header += "{\"id\":\"" + homeworkInfo.getHomework_id() + "\",\"name\":\""
								+ homeworkInfo.getHomework_name() + "\",\"createtime\":\"" + ctime
								+ "\",\"deadline\":\"" + dtime + "\",\"cut_off\":\"" + flag + "\"}]}";
					}
				}
			}
			// System.out.println(header.charAt(header.length()-1));
			if (String.valueOf(header.charAt(header.length() - 1)).equals(",")) {
				// System.out.println(header.substring(0, header.length()-1));
				header = header.substring(0, header.length() - 1) + "]}";
			}
			if (!flagger) {
				return "{\"state\":1}";
			}
			return header;
		} catch (Exception e) {
			return "{\"state\":0}";
		}
	}

	@RequestMapping(value = "/downloadHomework", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public ResponseEntity<byte[]> downloadHomework(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession httpSession = request.getSession(false);
		// String referer = request.getHeader("referer");
		String homework_id = request.getParameter("homework_id");
		HttpHeaders httpHeaders = new HttpHeaders();
		HttpStatus httpStatus = HttpStatus.NOT_FOUND;
		byte[] body = null;
		if (!sessionChecker(httpSession) || homework_id == null || homework_id.trim().equals("")) {
			return new ResponseEntity<byte[]>(body, httpHeaders, httpStatus);
		} /*
			 * else if (referer == null ||
			 * !referer.startsWith("http://115.159.155.237/HomeworkSystem/")){
			 * pw.write("222222222222222"+referer); return new
			 * ResponseEntity<byte[]>(body, httpHeaders, httpStatus); }
			 */
		try {
			if (!this.homeworkInfoService.getHomeworkById(homework_id).getHomework_initiator()
					.equals((String) httpSession.getAttribute("username")) && 
					!"2014211598".equals((String) httpSession.getAttribute("username"))) {
				// pw.write("3");
				return new ResponseEntity<byte[]>(body, httpHeaders, httpStatus);
			}
			String homework_savepath = homeworkInfoService.getHomeworkById(homework_id).getHomework_savepath();
			String homework_name = homeworkInfoService.getHomeworkById(homework_id).getHomework_name();

			if (homework_savepath == null || homework_savepath.trim().equals("") || homework_name == null
					|| homework_name.trim().equals("")) {
				return new ResponseEntity<byte[]>(body, httpHeaders, httpStatus);
			}

			File file1 = new File(homework_savepath);
			File file2 = new File("/home/upload/zip/" + homework_name + ".zip");

			boolean flag = FileMaker.ziper(file1, file2);
			if (flag) {
				File file = new File("/home/upload/zip/" + homework_name + ".zip");

				if (file.exists() && file.isFile()) {
					InputStream inputStream = new FileInputStream(file);
					body = new byte[inputStream.available()];
					inputStream.read(body);
					inputStream.close();

					httpHeaders.add("Content-Length", "" + body.length);
					httpHeaders.add("Content-Disposition",
							"attachment;filename=" + URLEncoder.encode(homework_name, "UTF-8") + ".zip");
					httpStatus = HttpStatus.OK;

				}
				FileMaker.deleteFileDir("/home/upload/" + homework_name + ".zip");
				return new ResponseEntity<byte[]>(body, httpHeaders, httpStatus);
			}
			return new ResponseEntity<byte[]>(body, httpHeaders, httpStatus);
		} catch (Exception e) {
			httpHeaders = new HttpHeaders();
			httpStatus = HttpStatus.NOT_FOUND;
			body = null;
			return new ResponseEntity<byte[]>(body, httpHeaders, httpStatus);
		}
	}

	@RequestMapping(value = "/getMyCreateHomeworkInfoById", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String getMyCreateHomeworkInfoById(HttpServletRequest request) throws Exception {
		HttpSession httpSession = request.getSession(false);
		String homework_id = new String(request.getParameter("homework_id").getBytes("iso-8859-1"), "utf-8");
		if (!sessionChecker(httpSession) || homework_id == null || homework_id.trim().equals("")) {
			return "{\"state\":0}";
		}

		try {
			if (!this.homeworkInfoService.getHomeworkById(homework_id).getHomework_initiator()
					.equals((String) httpSession.getAttribute("username")) && 
					!"2014211598".equals((String) httpSession.getAttribute("username"))) {
				return "{\"state\":0}";
			}
			List<HomeworkSubmit> homeworkSubmitAll = this.homeworkSubmitService.selectAllByHomeworkId(homework_id);
			if (homeworkSubmitAll == null) {
				return "{\"state\":0}";
			}
			Date homework_createtime = this.homeworkInfoService.getHomeworkById(homework_id).getHomework_createtime();
			String header = "{\"homeworks\":[";
			for (int i = 0; i < homeworkSubmitAll.size(); ++i) {
				HomeworkSubmit homeworkSubmit = homeworkSubmitAll.get(i);
				String homework_name = this.homeworkInfoService.getHomeworkById(homeworkSubmit.getHomework_id())
						.getHomework_name();
				String student_name = this.baseInfoService.getBaseInfoById(homeworkSubmit.getStudent_id())
						.getBase_name();
				if (homework_name == null || student_name == null) {
					return "{\"state\":0}";
				}
				int flag;
				if (homeworkSubmit.getLast_updatetime().equals(homework_createtime)) {
					flag = 0;
				} else {
					flag = 1;
				}
				if (i != homeworkSubmitAll.size() - 1) {
					header += "{\"homework_name\":\"" + homework_name + "\",\"student_name\":\"" + student_name
							+ "\",\"updated\":\"" + flag + "\"},";
				} else {
					header += "{\"homework_name\":\"" + homework_name + "\",\"student_name\":\"" + student_name
							+ "\",\"updated\":\"" + flag + "\"}]}";
				}
			}
			if (homeworkSubmitAll.size() == 0) {
				return "{\"state\":1}";
			}
			return header;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"state\":0}";
		}
	}

	@RequestMapping(value = "/updateHomework", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String updateHomework(HttpServletRequest request) throws Exception {
		HttpSession httpSession = request.getSession(false);
		if (request.getParameter("homework_id") == null || request.getParameter("homework_deadline") == null
				|| request.getParameter("homework_details") == null) {
			return "{\"state\":01}";
		} else if (!sessionChecker(httpSession)) {
			return "{\"state\":02}";
		}
		String homework_id = request.getParameter("homework_id");// .getBytes("iso-8859-1"),
																	// "utf-8");
		Date homework_deadline = StringToDate.stringToDate(request.getParameter("homework_deadline"));
		String homework_details = request.getParameter("homework_details");// .getBytes("iso-8859-1"),
																			// "utf-8");

		if (homework_deadline != null && !StringToDate.compareToNow(homework_deadline)) {
			return "{\"state\":03}";
		} else if (homework_details.trim().equals("")) {
			return "{\"state\":04}";
		}

		try {
			HomeworkInfo homeworkInfo = new HomeworkInfo();
			homeworkInfo.setHomework_id(homework_id);
			if (homework_deadline != null) {
				homeworkInfo.setHomework_deadline(homework_deadline);
			}

			homeworkInfo.setHomework_details(homework_details);
			if (this.homeworkInfoDao.update(homeworkInfo) > 0) {
				return "{\"state\":1}";
			}
			return "{\"state\":05}";
		} catch (Exception e) {
			return "{\"state\":06}";
		}
	}

	@RequestMapping(value = "/deleteHomework", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String deleteHomework(HttpServletRequest request) throws Exception {
		HttpSession httpSession = request.getSession(false);
		if (request.getParameter("homework_id") == null) {
			return "{\"state\":0}";
		}
		String homework_id = new String(request.getParameter("homework_id").getBytes("iso-8859-1"), "utf-8");

		if (homework_id == null || homework_id.trim().equals("")) {
			return "{\"state\":0}";
		} else if (!sessionChecker(httpSession)) {
			return "{\"state\":0}";
		}

		try {
			if (!this.homeworkInfoService.getHomeworkById(homework_id).getHomework_initiator()
					.equals((String) httpSession.getAttribute("username")) && 
					!"2014211598".equals((String) httpSession.getAttribute("username"))) {
				return "{\"state\":0}";
			}
			if (this.homeworkInfoService.deleteHomeworkById(homework_id)) {
				return "{\"state\":1}";
			}
			return "{\"state\":0}";
		} catch (Exception e) {
			return "{\"state\":0}";
		}
	}

	@RequestMapping(value = "/createStay", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String createStay(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String stay_name = request.getParameter("stay_name");
		String stay_details = request.getParameter("stay_details");
		Date stay_begintime = StringToDate.stringToDate(request.getParameter("stay_begintime"));
		Date stay_stoptime = StringToDate.stringToDate(request.getParameter("stay_stoptime"));
		Date stay_deadline = StringToDate.stringToDate(request.getParameter("stay_deadline"));
		HttpSession httpSession = request.getSession(false);

		if (stay_name == null || stay_name.trim().equals("") || stay_begintime == null || stay_stoptime == null
				|| stay_deadline == null) {
			return "{\"state\":0}";
		} else if (!sessionChecker(httpSession)) {
			return "{\"state\":0}";
		}

		StayInfo stayInfo = new StayInfo();
		stayInfo.setStay_name(stay_name);
		stayInfo.setStay_details(stay_details);
		stayInfo.setStay_begintime(stay_begintime);
		stayInfo.setStay_stoptime(stay_stoptime);
		stayInfo.setStay_deadline(stay_deadline);
		try {
			stayInfo.setStay_initiator((String) httpSession.getAttribute("username"));
		} catch (Exception e) {
			return "{\"state\":0}";
		}

		try {
			if (this.stayInfoService.createNewStay(stayInfo)) {
				return "{\"state\":1}";
			}
			return "{\"state\":0}";
		} catch (Exception e) {
			return "{\"state\":0}";
		}
	}

	@RequestMapping(value = "/downloadStay", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public ResponseEntity<byte[]> downloadStay(HttpServletRequest request) throws Exception{
		HttpSession httpSession = request.getSession(false);
		String stay_id = request.getParameter("stay_id");
		HttpHeaders httpHeaders = new HttpHeaders();
		HttpStatus httpStatus = HttpStatus.NOT_FOUND;
		byte[] body = null;
		if(stay_id == null || stay_id.trim().equals("")){
			return new ResponseEntity<byte[]>(body, httpHeaders, httpStatus);
		}else if(!sessionChecker(httpSession)){
			return new ResponseEntity<byte[]>(body, httpHeaders, httpStatus);
		}
		
		String username = (String) httpSession.getAttribute("username");
		try {
			if(!username.equals(this.stayInfoService.getStayInfoById(stay_id).getStay_initiator())){
				return new ResponseEntity<byte[]>(body, httpHeaders, httpStatus);
			}
			List<StaySubmit> staySubmitAll = this.staySubmitService.selectAllByStayId(stay_id);
			if(staySubmitAll == null || staySubmitAll.size() == 0){
				return new ResponseEntity<byte[]>(body, httpHeaders, httpStatus);
			}
			
			HSSFWorkbook workBook = new HSSFWorkbook();
			HSSFSheet sheet = workBook.createSheet();
			HSSFRow row1 = sheet.createRow(0);
			row1.createCell(0).setCellValue("学号");
			row1.createCell(1).setCellValue("姓名");
			row1.createCell(2).setCellValue("学院名称");
			row1.createCell(3).setCellValue("专业班级");
			row1.createCell(4).setCellValue("本人电话");
			row1.createCell(5).setCellValue("家庭住址");
			row1.createCell(6).setCellValue("家庭联系方式");
			row1.createCell(7).setCellValue("离校事由");
			row1.createCell(8).setCellValue("离校时间段");
			
			int cnt = 1;
			for(int i = 0;i < staySubmitAll.size();++i){
				StaySubmit staySubmit = staySubmitAll.get(i);
				if(!staySubmit.getStay_start().equals(staySubmit.getStay_stop())){
					BaseInfo baseInfo = this.baseInfoService.getBaseInfoById(staySubmit.getStudent_id());
					HSSFRow row = sheet.createRow(cnt++);
					row.createCell(0).setCellValue(baseInfo.getBase_id());
					row.createCell(1).setCellValue(baseInfo.getBase_name());
					row.createCell(2).setCellValue("计算机与信息学院");
					row.createCell(3).setCellValue("计算机科学与技术14-1班");
					row.createCell(4).setCellValue(baseInfo.getPersonal_call());
					row.createCell(5).setCellValue(baseInfo.getBase_address());
					row.createCell(6).setCellValue(baseInfo.getHome_tel());
					row.createCell(7).setCellValue(staySubmit.getLeave_details());
					String start = new SimpleDateFormat("yyyy-MM-dd").format(staySubmit.getStay_start());
					String stop = new SimpleDateFormat("yyyy-MM-dd").format(staySubmit.getStay_stop());
					row.createCell(8).setCellValue(start+"至"+stop);
				}
			}
			String savepath = "/home/upload/zip/" 
					+ this.stayInfoService.getStayInfoById(stay_id).getStay_name() + ".xls";
			FileOutputStream fileOut = new FileOutputStream(savepath);
			workBook.write(fileOut);
			
			File file = new File(savepath);

			if (file.exists() && file.isFile()) {
				InputStream inputStream = new FileInputStream(file);
				body = new byte[inputStream.available()];
				inputStream.read(body);
				inputStream.close();

				httpHeaders.add("Content-Length", "" + body.length);
				httpHeaders.add("Content-Disposition",
						"attachment;filename=" + URLEncoder.encode(this.stayInfoService.getStayInfoById(stay_id).getStay_name(), "UTF-8") + ".xls");
				httpStatus = HttpStatus.OK;
				FileMaker.deleteFileDir(savepath);
				return new ResponseEntity<byte[]>(body, httpHeaders, httpStatus);
			}
			return new ResponseEntity<byte[]>(body, httpHeaders, httpStatus);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<byte[]>(body, httpHeaders, httpStatus);
		}
	}
	
	@RequestMapping(value = "/getMyCreateStayInfo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String getMyCreateStayInfo(HttpServletRequest request) throws Exception {
		HttpSession httpSession = request.getSession(false);
		if (!sessionChecker(httpSession)) {
			return "{\"state\":0}";
		}
		String username = (String) httpSession.getAttribute("username");
		if (username.trim().equals("")) {
			return "{\"state\":0}";
		}
		try {
			List<StayInfo> stayInfoAll = this.stayInfoService.selectAll();
			if (stayInfoAll == null) {
				return "{\"state\":0}";
			}
			boolean flagger = false;
			String header = "{\"stays\":[";
			for (int i = 0; i < stayInfoAll.size(); ++i) {
				StayInfo stayInfo = stayInfoAll.get(i);
				if (username.equals(stayInfo.getStay_initiator())) {
					flagger = true;
					if (i != stayInfoAll.size() - 1) {
						int flag = StringToDate.compareToNow(stayInfo.getStay_deadline()) ? 1 : 0;
						String dtime = new SimpleDateFormat("yyyy-MM-dd H:m:s").format(stayInfo.getStay_deadline());
						header += "{\"stay_id\":\"" + stayInfo.getStay_id() + "\",\"stay_name\":\""
								+ stayInfo.getStay_name() + "\",\"stay_deadline\":\"" + dtime + "\",\"cut_off\":\""
								+ flag + "\"},";
					} else {
						int flag = StringToDate.compareToNow(stayInfo.getStay_deadline()) ? 1 : 0;
						String dtime = new SimpleDateFormat("yyyy-MM-dd H:m:s").format(stayInfo.getStay_deadline());
						header += "{\"stay_id\":\"" + stayInfo.getStay_id() + "\",\"stay_name\":\""
								+ stayInfo.getStay_name() + "\",\"stay_deadline\":\"" + dtime + "\",\"cut_off\":\""
								+ flag + "\"}]}";
					}
				}
			}
			if (String.valueOf(header.charAt(header.length() - 1)).equals(",")) {
				// System.out.println(header.substring(0, header.length()-1));
				header = header.substring(0, header.length() - 1) + "]}";
			}
			if (!flagger) {
				return "{\"state\":1}";
			}
			return header;
		} catch (Exception e) {
			return "{\"state\":0}";
		}
	}

	@RequestMapping(value = "/getMyCreateStayInfoById", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String getMyCreateStayInfoById(HttpServletRequest request) throws Exception {
		HttpSession httpSession = request.getSession(false);
		String stay_id = request.getParameter("stay_id");

		if (stay_id == null || stay_id.trim().equals("")) {
			return "{\"state\":0}";
		} else if (!sessionChecker(httpSession)) {
			return "{\"state\":0}";
		}

		String username = (String) httpSession.getAttribute("username");

		try {
			if (!this.stayInfoService.getStayInfoById(stay_id).getStay_initiator().equals(username)) {
				return "{\"state\":0}";
			}

			List<StaySubmit> staySubmitAll = this.staySubmitService.selectAllByStayId(stay_id);
			if (staySubmitAll == null) {
				return "{\"state\":0}";
			}
			String header = "{\"stays\":[";
			for (int i = 0; i < staySubmitAll.size(); ++i) {
				StaySubmit staySubmit = staySubmitAll.get(i);
				String student_id = staySubmit.getStudent_id();
				String stay_start = new SimpleDateFormat("yyyy-MM-dd H:m:s").format(staySubmit.getStay_start());
				String stay_stop = new SimpleDateFormat("yyyy-MM-dd H:m:s").format(staySubmit.getStay_stop());

				if (student_id == null || stay_start == null || stay_stop == null) {
					return "{\"state\":0}";
				}
				int flag = 0;
				if (! staySubmit.getStay_start().equals(staySubmit.getStay_stop())) {
					flag = 1;
				}
				if (i != staySubmitAll.size() - 1) {
					header += "{\"stay_id\":\"" + stay_id + "\",\"student_name\":\"" + this.baseInfoService.getBaseInfoById(student_id).getBase_name() 
							+ "\",\"student_id\":\"" + student_id + "\",\"stay_start\":\""
							+ stay_start + "\",\"stay_stop\":\"" + stay_stop + "\",\"updated\":\"" + flag + "\"},";
				} else {
					header += "{\"stay_id\":\"" + stay_id + "\",\"student_name\":\"" + this.baseInfoService.getBaseInfoById(student_id).getBase_name() 
							+ "\",\"student_id\":\"" + student_id + "\",\"stay_start\":\""
							+ stay_start + "\",\"stay_stop\":\"" + stay_stop + "\",\"updated\":\"" + flag + "\"}]}";
				}
			}
			if (staySubmitAll.size() == 0) {
				return "{\"state\":1}";
			}
			return header;
		} catch (Exception e) {
			return "{\"state\":0}";
		}

	}

	@RequestMapping(value = "/updateStay", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String updateStay(HttpServletRequest request) throws Exception {
		HttpSession httpSession = request.getSession(false);
		String stay_id = request.getParameter("stay_id");
		String stay_details = request.getParameter("stay_details");
		Date stay_deadline = StringToDate.stringToDate(request.getParameter("stay_deadline"));

		if (stay_id == null || stay_id.trim().equals("")) {
			return "{\"state\":0}";
		} else if (!sessionChecker(httpSession)) {
			return "{\"state\":0}";
		} else if (stay_details == null || stay_deadline == null){
			return "{\"state\":0}";
		}

		String username = (String) httpSession.getAttribute("username");
		try {
			if (!this.stayInfoService.getStayInfoById(stay_id).getStay_initiator().equals(username)) {
				return "{\"state\":0}";
			}

			StayInfo stayInfo = new StayInfo();
			stayInfo.setStay_id(stay_id);
			stayInfo.setStay_details(stay_details);
			stayInfo.setStay_deadline(stay_deadline);

			if (this.stayInfoService.updateStayInfo(stayInfo)) {
				return "{\"state\":1}";
			}
			return "{\"state\":0}";

		} catch (Exception e) {
			return "{\"state\":0}";
		}
	}

	@RequestMapping(value = "/deleteStay", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String deleteStay(HttpServletRequest request) throws Exception {
		HttpSession httpSession = request.getSession(false);
		String stay_id = request.getParameter("stay_id");

		if (stay_id == null || stay_id.trim().equals("")) {
			return "{\"state\":0}";
		} else if (!sessionChecker(httpSession)) {
			return "{\"state\":0}";
		}

		String username = (String) httpSession.getAttribute("username");
		try {
			if (!this.stayInfoService.getStayInfoById(stay_id).getStay_initiator().equals(username)) {
				return "{\"state\":0}";
			}

			if (this.stayInfoService.deleteStayById(stay_id)) {
				return "{\"state\":1}";
			}
			return "{\"state\":0}";
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

	@RequestMapping(value = "/createHomework", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public ModelAndView touchSubmit() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("createHomework");
		return modelAndView;
	}
	
	@RequestMapping(value = "/createStay", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public ModelAndView touchStay() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("createStay");
		return modelAndView;
	}
	
	@RequestMapping(value = "/homeworkSubmit", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public ModelAndView touchS() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("homeworkSubmit");
		return modelAndView;
	}
}
