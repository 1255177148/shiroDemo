package com.hezhan.shirodemo.shiroconfig.subject;

import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.subject.Subject;

public class MySubjectDAO extends DefaultSubjectDAO {

    @Override
    protected boolean isSessionStorageEnabled(Subject subject) {
        return false;
    }
}
