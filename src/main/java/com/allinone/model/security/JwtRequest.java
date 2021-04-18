package com.allinone.model.security;

public class JwtRequest {

	private String username;
	private String mobNo;
	private String userType;	
	private String password;
			
		public JwtRequest() {
			super();
		}
		public JwtRequest(String mobNo,String type, String password) {
			super();
			this.mobNo = mobNo;
			this.userType = type;
			this.password = password;
		}
		public JwtRequest(String username, String password) {
			super();
			this.username = username;
			this.password = password;
		}		
		
		public String getMobNo() {
			return mobNo;
		}
		public void setMobNo(String mobNo) {
			this.mobNo = mobNo;
		}
		public String getUserType() {
			return userType;
		}
		public void setUserType(String userType) {
			this.userType = userType;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
}
