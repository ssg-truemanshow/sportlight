package com.tms.sportlight.domain;

/**
 * 알림 대상 등급
 */
public enum NotiGrade {
  NORMAL,         //일반회원
  INSTRUCTOR,     //강사
  ADMINISTRATOR, //관리자
  MEMBER,        //일반회원, 강사
  ALL            //전부
}
