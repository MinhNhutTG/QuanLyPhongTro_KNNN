package com.quanlyphongtro.models;

import jakarta.persistence.*;

@Entity
@Table(name = "taikhoan")
public class TaiKhoan {

    @Id
    @Column(name = "UserName", length = 30)
    private String userName;

    @Column(name = "PassWord", length = 255, nullable = false)
    private String passWord;

    @Column(name = "EmailUser", length = 100)
    private String emailUser;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getEmailUser() {
		return emailUser;
	}

	public void setEmailUser(String emailUser) {
		this.emailUser = emailUser;
	}
    
    
}
