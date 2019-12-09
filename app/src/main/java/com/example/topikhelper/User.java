package com.example.topikhelper;
/**
 * 사용자의 정보를 담는 클래스
 * 이메일, 닉네임, 성별
 */
public class User {

    public String email = "";
    public String nickname = "";
    public String sex = "";
    public String history = "";

    //사용자 생성자
    public User(String email, String nickname, String sex, String history) {
        this.email = email;
        this.nickname = nickname;
        this.sex = sex;
        this.history = history;
    }
}