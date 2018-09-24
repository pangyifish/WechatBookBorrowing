package org.liufeng.course.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.liufeng.course.pojo.SNSUserInfo;
import org.liufeng.course.pojo.WeixinOauth2Token;
import org.liufeng.course.util.AdvancedUtil;

/**
 * 授权后的回调请求处理
 * 
 * 
 * @date 2013-11-12
 */
public class OAuthServlet extends HttpServlet {
	private static final long serialVersionUID = -1847238807216447030L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		// 用户同意授权后，能获取到code
		String code = request.getParameter("utf-8");
		String state = request.getParameter("state");
		
		System.out.println("state is"+state);
		// 用户同意授权
		if (!"authdeny".equals(code)) {
			// 获取网页授权access_token
			WeixinOauth2Token weixinOauth2Token = AdvancedUtil.getOauth2AccessToken("wx06b5f6e887e33af3", "f2fd7a29e1fe75d1a1cd3f4d8dae302e", code);
			// 网页授权接口访问凭证
			String accessToken = weixinOauth2Token.getAccessToken();
			// 用户标识
			String openId = weixinOauth2Token.getOpenId();
			// 获取用户信息
			SNSUserInfo snsUserInfo = AdvancedUtil.getSNSUserInfo(accessToken, openId);

			// 设置要传递的参数
			request.setAttribute("snsUserInfo", snsUserInfo);
		}
		// 跳转到index.jsp
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
}
