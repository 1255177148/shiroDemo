package com.hezhan.shirodemo.shiroconfig.factory;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

public class MySubjectFactory extends DefaultWebSubjectFactory {

    /**
     * 重写此方法，禁止Subject创建session
     * @param context
     * @return
     */
    @Override
    public Subject createSubject(SubjectContext context) {
        context.setSessionCreationEnabled(false);
        return super.createSubject(context);
    }
}
