package com.example.tempapp.Login;

public class FindingPw_sql_control {

    public static void sendingPW(String pw,String mail){ //changing pw
        FindingPwMysql sendingPW = new FindingPwMysql(pw,mail);
        FindingPwMysql.active=true;
        sendingPW.start();
    }

    public static void findingPW(String id,String name,String mail){ //changing pw
        FindingPwMysql findingPW = new FindingPwMysql(id,name,mail);
        FindingPwMysql.active=true;
        findingPW.start();
    }

}