package org.liufeng.course.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.liufeng.course.service.CoreService2;
import org.liufeng.course.util.SignUtil;
/**
 * 请求处理的核心类
 * 
 * 
 * @date 2013-09-29
 */
public class CoreServlet extends HttpServlet {
	private static final long serialVersionUID = 4440739483644821986L;

	/**
	 * 请求校验（确认请求来自微信服务器）
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 微信加密签名
		String signature = request.getParameter("signature");
		
		System.out.println("signature:------------"+signature);
		
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		
		System.out.println("timestamp:------------"+timestamp);
		// 随机数
		String nonce = request.getParameter("nonce");
		
		System.out.println("nonce:------------"+nonce);
		// 随机字符串
		String echostr = request.getParameter("echostr");
		
		System.out.println("echostr:------------"+echostr);

		PrintWriter out = response.getWriter();
		
		System.out.println("-------------------------------------");
		// 请求校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			out.print(echostr);
			System.out.println("success successsuccesssuccesssuccesssuccesssuccess ");
		}else{
			System.out.println("failure failure failure failure failure failure ");
		}
		out.close();
		out = null;
	}

	/**
	 * 处理微信服务器发来的消息
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("dopost:------------");
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		// 调用核心业务类接收消息、处理消息
		String respXml = CoreService2.processRequest(request);
        System.out.println("respXml is:"+respXml);
		// 响应消息
		PrintWriter out = response.getWriter();
		out.print(respXml);
		out.close();
	}
}
