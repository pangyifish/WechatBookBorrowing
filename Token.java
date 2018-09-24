package org.liufeng.course.pojo;

/**
 * ƾ֤
 * 
 * 
 * @date 2018-01-02
 */
public class Token {
	// �ӿڷ���֤
	private String accessToken;
	
	private int expiresIn;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
}