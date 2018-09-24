package com.action;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.liufeng.course.util.JsonUtil;

import com.bean.Tbook;
import com.bean.Tuser;
import com.dao.DB;
public class user_servlet extends HttpServlet {
	public void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		String type = req.getParameter("type");
		if (type.endsWith("userlogin")) {
			userlogin(req, res);
		}
		
		
		if (type.endsWith("userLogout")) {
			userLogout(req, res);
		}
		
		
		if (type.endsWith("userBind")) {
			userBind(req, res);
		}
		
		
		
		if (type.endsWith("userJson")) {
			userJson(req, res);
		}
	}
	
	
	
	public void userJson(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException
	{
		res.setContentType("text/plain"); 
		req.setCharacterEncoding("utf-8");
		res.setCharacterEncoding("utf-8");
		List userList=new ArrayList();
		String sql=" select  * from t_user  ";
		Object[] params={};
		DB mydb=new DB();
		try
		{
			mydb.doPstm(sql, params);
			ResultSet rs=mydb.getRs();
			while(rs.next())
			{
				Tuser user=new Tuser();
				user.setUname(rs.getString("uname"));
				user.setUpass(rs.getString("xuehao"));
			
				user.setId(rs.getString("open_id"));
			
				user.setRole(rs.getString("role"));
				userList.add(user);
		    }
			rs.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		mydb.closed();
		res.getWriter().println(JsonUtil.list2json(userList));
	}
	
	
	
	public void userBind(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException { 
		 req.setCharacterEncoding("utf-8");
		 res.setCharacterEncoding("utf-8");
		 String uname = req.getParameter("uname");
		 String upass = req.getParameter("upass");
		
		 String openID=req.getParameter("openID");
         String sql2="delete from t_user where open_id=? ";
		 String sql = " insert into t_user(uname,xuehao,role,open_id) values(?,?,?,? ) ";
		 Object[] params = {uname,upass,"学生",openID};
		 Object[] params2 = {openID};
		 DB mydb = new DB();
		 mydb.doPstm(sql2, params2);
		 mydb.doPstm(sql, params);
		 mydb.closed();
		 Tuser user =new Tuser();
		 user.setId(openID);
		 user.setUname(uname);
		 req.getSession().setAttribute("user", user);
		 
		 
		  Cookie cookie = new Cookie("cookie_user",uname+"-"+openID);                  
          cookie.setMaxAge(60*60*24*30); //cookie 保存30天  
          res.addCookie(cookie);     

		 req.setAttribute("message", "绑定成功");
		 req.setAttribute("path","/weixinbook/page/main.jsp");
		 String targetURL = "/common/success.jsp";
		 dispatch(targetURL, req, res);
	}
	
	

	public void userLogout(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		 req.getSession().removeAttribute("user");
		 dispatch("/page/login.jsp",req,res);
	}
	
	
	public void userlogin(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		boolean mark=false;
		String result="no";
		String loginName = req.getParameter("loginName");
		String loginPw = req.getParameter("loginPw");
		String sql="select * from t_user where  uname=? and xuehao=? ";
		Object[] params={loginName,loginPw};
		DB mydb=new DB();
		try
		{
			mydb.doPstm(sql, params);
			ResultSet rs=mydb.getRs();
			mark=(rs==null||!rs.next()?false:true);
			if(mark==false)
			{
				result="no";
			}
			if(mark==true)
			{
				Tuser user=new Tuser();
				user.setUname(rs.getString("uname"));
				user.setUpass(rs.getString("xuehao"));
				user.setRole(rs.getString("role"));
				user.setId(rs.getString("open_id"));
				System.out.println(" username is:"+rs.getString("uname"));
				result="yes";
	            req.getSession().setAttribute("user", user);
			}
			rs.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		mydb.closed();
		
		if(mark==true){
			 req.setAttribute("message", "恭喜您登陆成功");
			 req.setAttribute("path","/weixinbook/page/main.jsp");
			 String targetURL = "/common/success.jsp";
			 dispatch(targetURL, req, res);
		}else{
			 req.setAttribute("message", "登录失败");
			 req.setAttribute("path","/weixinbook/page/login.jsp");
			 String targetURL = "/common/success.jsp";
			 dispatch(targetURL, req, res);
		}
	   
	}
	
	
	public void dispatch(String targetURI,HttpServletRequest request,HttpServletResponse response) 
	{
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher(targetURI);
		try 
		{
		    dispatch.forward(request, response);
		    return;
		} 
		catch (ServletException e) 
		{
                    e.printStackTrace();
		} 
		catch (IOException e) 
		{
			
		    e.printStackTrace();
		}
	}
	public void init(ServletConfig config) throws ServletException 
	{
		super.init(config);
	}
	
	public void destroy() 
	{
	}
}
