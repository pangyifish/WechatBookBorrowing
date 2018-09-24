package com.action;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.liufeng.course.util.JsonUtil;

import com.bean.Tbook;
import com.bean.Tborrow;
import com.bean.Tuser;
import com.dao.DB;
import com.util.BookUtil;

public class book_servlet extends HttpServlet
{
	public void service(HttpServletRequest req,HttpServletResponse res)throws ServletException, IOException 
	{
        String type=req.getParameter("type");
		if(type.endsWith("bookList"))
		{
			bookList(req, res);
		}
		
		
		if(type.endsWith("bookquery"))
		{
			bookquery(req, res);
		}
		
		
		if(type.endsWith("bookDel"))
		{
			bookDel(req, res);
		}
		
		if(type.endsWith("bookAdd"))
		{
			bookAdd(req, res);
		}
		
		if(type.endsWith("bookEdit"))
		{
			bookEdit(req, res);
		}
		
		if(type.endsWith("bookDetail"))
		{
			bookDetail(req, res);
		}
		
		
		if(type.endsWith("borrowBook"))
		{
			borrowBook(req, res);
		}
		
		if(type.endsWith("returnBook"))
		{
			returnBook(req, res);
		}
		
		
		if(type.endsWith("myborrow"))
		{
			myborrow(req, res);
		}
		
		
		if(type.endsWith("borrowmanage"))
		{
			borrowmanage(req, res);
		}
		
		
		if(type.endsWith("bookJson"))
		{
			bookJson(req, res);
		}
	}
	
	
	
	
	public void bookDel(HttpServletRequest req,HttpServletResponse res)
	{
		String id=req.getParameter("id");
		String sql="delete from t_book where id=?";
		Object[] params={id};
		DB mydb=new DB();
		mydb.doPstm(sql, params);
		mydb.closed();
		
		req.setAttribute("message", "sucess");
		req.setAttribute("path", "book?type=bookList");
        String targetURL = "/common/success.jsp";
		dispatch(targetURL, req, res);
	}

	public void bookAdd(HttpServletRequest req,HttpServletResponse res)
	{
	    String bookName=req.getParameter("bookName");
	    String description=req.getParameter("description");
	    String bianhao=req.getParameter("bianhao");
	    String author=req.getParameter("author");
	    String press=req.getParameter("press");
	    String num=req.getParameter("num");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
	    String shijian = df.format(new Date());
	    String sql = "insert into t_book(bookName,description,createDate,status,bianhao,author, press,num) values(?,?,?,?, ?,?,?,?)";
	    Object[] params = { bookName, description, shijian, "可借",bianhao,author, press,num};
	    DB mydb = new DB();
	    mydb.doPstm(sql, params);
	    mydb.closed();
	    req.setAttribute("message", "sucess");
	    req.setAttribute("path", "book?type=bookList");
		
        String targetURL = "/common/success.jsp";
		dispatch(targetURL, req, res);
	   
	}
	
	
	public void bookEdit(HttpServletRequest req,HttpServletResponse res)
	{
		String id=req.getParameter("id");
		String bookName=req.getParameter("bookName");
	    String description=req.getParameter("description");
	 
	    String bianhao=req.getParameter("bianhao");
	    String author=req.getParameter("author");
	    String press=req.getParameter("press");
	    String num=req.getParameter("num");
		
	    String sql = " update t_book set bookName=?,description=? ,bianhao=?,author=?, press=?,num=?  where id=?";
	    Object[] params = { bookName, description,bianhao,author, press,num, id};
	    DB mydb = new DB();
	    mydb.doPstm(sql, params);
	    mydb.closed();
	    req.setAttribute("message", "sucess");
	    req.setAttribute("path", "book?type=bookList");
		
        String targetURL = "/common/success.jsp";
		dispatch(targetURL, req, res);
	   
	}
	public void bookList(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException
	{
		List bookList=new ArrayList();
		String sql="  select  a.*,(select count(*) from t_borrow b where b.book_id=a.id and b.status='借阅中' ) borrownum from t_book  a where 1=1 ";
		Object[] params={};
		DB mydb=new DB();
		try
		{
			mydb.doPstm(sql, params);
			ResultSet rs=mydb.getRs();
			while(rs.next())
			{
				Tbook book=new Tbook();
				book.setBookID(rs.getString("id"));
				book.setBookName(rs.getString("bookName"));
				book.setCreateDate(rs.getString("createDate"));
				book.setDescription(rs.getString("description"));
				book.setStatus(rs.getString("status"));
				book.setAuthor(rs.getString("author"));
				book.setBianhao(rs.getString("bianhao"));
				book.setPress(rs.getString("press"));
				book.setNum(rs.getString("num"));
				book.setBorrownum(rs.getString("borrownum"));
				bookList.add(book);
		    }
			rs.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		mydb.closed();
		
		req.setAttribute("bookList", bookList);
		req.getRequestDispatcher("page/bookList.jsp").forward(req, res);
	}
	
	
	public void bookquery(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException
	{
		
		String keyword=req.getParameter("keyword");
		List bookList=new ArrayList();
		String sql=" select  a.*,(select count(*) from t_borrow b where b.book_id=a.id and b.status='借阅中' ) borrownum from t_book  a where 1=1 ";
		if(keyword!=null && !keyword.equalsIgnoreCase("")){
			sql=sql+" and ( bookName like '%"+keyword.trim()+"%' or author like '%"+keyword.trim()+"%' or press like '%"+keyword.trim()+"%' ) ";
		}
		Object[] params={};
		DB mydb=new DB();
		try
		{
			mydb.doPstm(sql, params);
			ResultSet rs=mydb.getRs();
			while(rs.next())
			{
				Tbook book=new Tbook();
				book.setBookID(rs.getString("id"));
				book.setBookName(rs.getString("bookName"));
				book.setCreateDate(rs.getString("createDate"));
				book.setDescription(rs.getString("description"));
				book.setStatus(rs.getString("status"));
				book.setAuthor(rs.getString("author"));
				book.setBianhao(rs.getString("bianhao"));
				book.setPress(rs.getString("press"));
				book.setNum(rs.getString("num"));
				book.setBorrownum(rs.getString("borrownum"));
				bookList.add(book);
		    }
			rs.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		mydb.closed();
		req.setAttribute("keyword", keyword);
		req.setAttribute("bookList", bookList);
		req.getRequestDispatcher("page/bookquery.jsp").forward(req, res);
	}
	
	public void bookJson(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException
	{
		res.setContentType("text/plain"); 
		req.setCharacterEncoding("utf-8");
		res.setCharacterEncoding("utf-8");
		List bookList=new ArrayList();
		String sql=" select  * from t_book where num>0 ";
		Object[] params={};
		DB mydb=new DB();
		try
		{
			mydb.doPstm(sql, params);
			ResultSet rs=mydb.getRs();
			while(rs.next())
			{
				Tbook book=new Tbook();
				book.setBookID(rs.getString("id"));
				book.setBookName(rs.getString("bookName"));
				book.setCreateDate(rs.getString("createDate"));
				book.setDescription(rs.getString("description"));
				book.setStatus(rs.getString("status"));
				book.setAuthor(rs.getString("author"));
				book.setBianhao(rs.getString("bianhao"));
				book.setPress(rs.getString("press"));
				book.setNum(rs.getString("num"));
				bookList.add(book);
		    }
			rs.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		mydb.closed();
		res.getWriter().println(JsonUtil.list2json(bookList));
	}
	
	
	
	public void bookDetail(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException
	{
		String id=req.getParameter("id");
		Tbook book=new Tbook();
		String sql=" select  * from t_book  where id=? ";
			
		Object[] params={id};
		DB mydb=new DB();
		try
		{
			mydb.doPstm(sql, params);
			ResultSet rs=mydb.getRs();
			rs.next();
			book.setBookID(rs.getString("id"));
			book.setBookName(rs.getString("bookName"));
			book.setCreateDate(rs.getString("createDate"));
			book.setDescription(rs.getString("description"));
			book.setStatus(rs.getString("status"));
			
			book.setAuthor(rs.getString("author"));
			book.setBianhao(rs.getString("bianhao"));
			book.setPress(rs.getString("press"));
			book.setNum(rs.getString("num"));
			
			rs.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		mydb.closed();
		req.setAttribute("book", book);
		req.getRequestDispatcher("page/detail.jsp").forward(req, res);
	}
	
	
	
	public void borrowBook(HttpServletRequest req,HttpServletResponse res)
	{
	    String userID=req.getParameter("userID");
	    String bookID=req.getParameter("bookID");
	    String begindate=req.getParameter("begindate");
	    String returndate=req.getParameter("returndate");
	    String sql = " insert into t_borrow (user_id,book_id,borrow_date,return_date,status)  values(?,?,?,?,?) ";
	    String sql2 = "update  t_book set num=num-1 where id=? ";
	    Object[] params = {userID,bookID,begindate,returndate,"借阅中" };
	    Object[] params2 = {bookID};
	    DB mydb = new DB();
	    mydb.doPstm(sql, params);
	    mydb.doPstm(sql2, params2);
	    mydb.closed();
	    req.setAttribute("message", "借书成功");
		req.setAttribute("path", "book?type=borrowmanage");
		
        String targetURL = "/common/success.jsp";
		dispatch(targetURL, req, res);  
	}
	
	
	
	public void returnBook(HttpServletRequest req,HttpServletResponse res)
	{
	   
		String id=req.getParameter("id");
	    String bookID=req.getParameter("bookID");
	    String returndate=req.getParameter("returndate");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
	    String shijian = df.format(new Date());
	    if(returndate==null){
	    	returndate=shijian;
	    }
	    String sql = "  update t_borrow set status='已完成',return_date=? where borrow_id=? ";
	    String sql2 = "update  t_book set num=num+1 where id=? ";
	    Object[] params = {returndate,id};
	    Object[] params2 = {bookID};
	    DB mydb = new DB();
	    mydb.doPstm(sql, params);
	    mydb.doPstm(sql2, params2);
	    mydb.closed();
	    req.setAttribute("message", "还书成功");
		req.setAttribute("path", "book?type=borrowmanage");
		
        String targetURL = "/common/success.jsp";
		dispatch(targetURL, req, res);  
	}
	
	public void myborrow(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException
	{
		Tuser user=(Tuser)req.getSession().getAttribute("user");
		if(user!=null){
			List<Tborrow> borrowlist=BookUtil.getMyBorrow(user.getId());
			req.setAttribute("borrowlist", borrowlist);
			req.getRequestDispatcher("page/borrowList.jsp").forward(req, res);
		}else{
			req.getRequestDispatcher("page/login.jsp").forward(req, res);
		}
	}
	
	
	
	public void borrowmanage(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException
	{
		Tuser user=(Tuser)req.getSession().getAttribute("user");
		if(user!=null){
			List<Tborrow> borrowlist=BookUtil.getManageBorrow();
			req.setAttribute("borrowlist", borrowlist);
			req.getRequestDispatcher("page/manageborrowList.jsp").forward(req, res);
		}else{
			req.getRequestDispatcher("page/login.jsp").forward(req, res);
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
