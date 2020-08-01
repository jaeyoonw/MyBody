package com.example.tempapp.Register;

public class Idcheck_sql_control {

    static public void idChk(String id){    //아이디 체크
        IdcheckMysql idchk=new IdcheckMysql(id,2);
        IdcheckMysql.active=true;
        idchk.start();      //idchk는 Thread임, 2

    }
}
