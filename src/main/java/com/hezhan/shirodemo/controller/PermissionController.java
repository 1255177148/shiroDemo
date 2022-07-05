package com.hezhan.shirodemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/permission")
public class PermissionController {

    /**
     * 这个接口模拟有权限后成功调用
     * @return
     */
    @GetMapping("/show")
    @RequiresPermissions("user:add")
    public String showPermission(){
        return "有权限看到此信息...";
    }

    /**
     * 这个接口模拟没有权限
     * @return
     */
    @GetMapping("/showUnable")
    @RequiresPermissions("user:update")
    public String showPermissionUnable(){
        return "有权限看到此信息...";
    }
}
