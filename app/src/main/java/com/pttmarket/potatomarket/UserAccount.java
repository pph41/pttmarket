package com.pttmarket.potatomarket;

public class UserAccount {//계정 회원가입, 로그인에 사용되는 리얼타임 db

   public UserAccount(){
   }
    // id토큰 = id number 역할
    public String getIdToken() { return idToken; }
    public  void setIdToken(String idToken){
       this.idToken = idToken;
    }
    private String idToken;

    public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) { this.emailId = emailId; }
    private String emailId;

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    private String password;

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    private String nickname;

}