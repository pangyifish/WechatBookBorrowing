package com.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;



import org.liufeng.course.pojo.Token;
import org.liufeng.course.pojo.WeixinUserInfo;
import org.liufeng.course.util.AdvancedUtil;
import org.liufeng.course.util.CommonUtil;

import com.bean.Tborrow;
import com.dao.DB;

public class BookUtil {

	public static List<Tborrow> getMyBorrow(String openid) {
		ArrayList<Tborrow> borrowlist = new ArrayList<Tborrow>();
		String sql = "  select aa.borrow_id,aa.book_id,aa.return_date,cc.bookName,bb.open_id,bb.uname,bb.xuehao,aa.borrow_date,aa.status, cc.bianhao,cc.author,cc.press "
				+ " from t_borrow aa  "
				+ " join t_user bb on bb.open_id=aa.user_id "
				+ " join t_book cc on cc.id=aa.book_id  where 1=1 ";

		if (openid != null) {
			sql += " and aa.user_id=" + openid;
		}
		Object[] params = {  };
		DB mydb = new DB();
		try {
			mydb.doPstm(sql, params);
			ResultSet rs = mydb.getRs();
			while (rs.next()) {
				Tborrow borrow = new Tborrow();
				borrow.setBorrwID(rs.getString("borrow_id"));
				borrow.setBookID(rs.getString("book_id"));
				borrow.setBookName(rs.getString("bookName"));
				borrow.setUserID(rs.getString("open_id"));
				borrow.setUserName(rs.getString("uname"));
				borrow.setCreateDate(rs.getString("borrow_date"));
				borrow.setReturnDate(rs.getString("return_date"));
				borrow.setStatus(rs.getString("status"));
				borrow.setBianhao(rs.getString("bianhao"));
				borrow.setAuthor(rs.getString("author"));
				borrow.setPress(rs.getString("press"));
				borrow.setXuehao(rs.getString("xuehao"));
				borrowlist.add(borrow);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mydb.closed();
		return borrowlist;
	}

	
	
	public static List<Tborrow> getManageBorrow() {
		ArrayList<Tborrow> borrowlist = new ArrayList<Tborrow>();
		String sql = "  select aa.borrow_id,aa.book_id,aa.return_date,cc.bookName,bb.open_id,bb.uname,bb.xuehao,aa.borrow_date,aa.status  ,cc.bianhao,cc.author,cc.press"
				+ " from t_borrow aa  "
				+ " join t_user bb on bb.open_id=aa.user_id "
				+ " join t_book cc on cc.id=aa.book_id  where 1=1 ";

		
		Object[] params = {  };
		DB mydb = new DB();
		try {
			mydb.doPstm(sql, params);
			ResultSet rs = mydb.getRs();
			while (rs.next()) {
				Tborrow borrow = new Tborrow();
				borrow.setBorrwID(rs.getString("borrow_id"));
				borrow.setBookID(rs.getString("book_id"));
				borrow.setBookName(rs.getString("bookName"));
				borrow.setUserID(rs.getString("open_id"));
				borrow.setUserName(rs.getString("uname"));
				borrow.setCreateDate(rs.getString("borrow_date"));
				borrow.setReturnDate(rs.getString("return_date"));
				borrow.setStatus(rs.getString("status"));
				borrow.setBianhao(rs.getString("bianhao"));
				borrow.setAuthor(rs.getString("author"));
				borrow.setPress(rs.getString("press"));
				borrow.setXuehao(rs.getString("xuehao"));
				borrowlist.add(borrow);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mydb.closed();
		return borrowlist;
	}
	
	
	
	
	public static String getUsernickeName(String openid) {
		try {
			String appId = "wx06b5f6e887e33af3";
			// 第三方用户唯一凭证密钥
			String appSecret = "f2fd7a29e1fe75d1a1cd3f4d8dae302e";
			// 调用接口获取凭证
			Token token = CommonUtil.getToken(appId, appSecret);
			WeixinUserInfo user = AdvancedUtil.getUserInfo(
					token.getAccessToken(), openid);
			return user.getNickname();
		} catch (Exception e) {
			return openid + ":对用的用户(因为微信没有权限)";
		}
	}

	public static boolean userexist(String openid) {
		boolean result = false;
		String sql = " select * from t_user where open_id=?  ";
		Object[] params = { openid };
		DB mydb = new DB();
		try {
			mydb.doPstm(sql, params);
			ResultSet rs = mydb.getRs();
			while (rs.next()) {
				result = true;
				break;
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static String getrealUserID(String openid) {
		String userID = "-1";
		String sql = " select * from t_user where oper_id=?  ";

		Object[] params = { openid };
		DB mydb = new DB();
		try {
			mydb.doPstm(sql, params);
			ResultSet rs = mydb.getRs();
			while (rs.next()) {
				userID = rs.getString("user_id");
				break;
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return userID;
	}
	
	
	
	
	public static List<Tborrow> getMyNote(String openid) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		ArrayList<Tborrow> borrowlist = new ArrayList<Tborrow>();
		String sql = "  select aa.borrow_id,aa.book_id,aa.return_date,cc.bookName,bb.open_id,bb.uname,aa.borrow_date,aa.status"
				+ " from t_borrow aa  "
				+ " join t_user bb on bb.open_id=aa.user_id "
				+ " join t_book cc on cc.id=aa.book_id  where 1=1 and and aa.user_id=?  and aa.status='借阅中' and aa.return_date<"+df.format(new Date());
		Object[] params = { openid };
		DB mydb = new DB();
		try {
			mydb.doPstm(sql, params);
			ResultSet rs = mydb.getRs();
			while (rs.next()) {
				Tborrow borrow = new Tborrow();
				borrow.setBorrwID(rs.getString("borrow_id"));
				borrow.setBookID(rs.getString("book_id"));
				borrow.setBookName(rs.getString("bookName"));
				borrow.setUserID(rs.getString("open_id"));
				borrow.setUserName(rs.getString("uname"));
				borrow.setCreateDate(rs.getString("borrow_date"));
				borrow.setReturnDate(rs.getString("return_date"));
				borrow.setStatus(rs.getString("status"));
				borrowlist.add(borrow);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mydb.closed();
		return borrowlist;
	}
}
