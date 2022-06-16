package com.tianblogs.security.controller;

import cn.hutool.json.JSONObject;
import com.tianblogs.security.entity.Result;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 资源控制器 CRUD
 * @author tian
 */
@RestController
@RequestMapping("/resource")
public class ResourceController {

    private static ArrayList<String> list = new ArrayList<>();

    static {
        list.add("原始数据1");
        list.add("原始数据2");
        list.add("原始数据3");
        list.add("原始数据4");
        list.add("原始数据5");
    }

    @PreAuthorize("hasAuthority('resource:set')")
    @PostMapping
    public Result setResource(){
        list.add("新增数据");
        return Result.succ(list);
    }

    @PreAuthorize("hasAuthority('resource:remove')")
    @PostMapping("/remove")
    public Result removeResource(){
        list.remove(list.size()-1);
        return Result.succ(list);
    }

    @PreAuthorize("hasAuthority('resource:get')") //要么是权限 hasAuthority，要去是角色 hasRole ，如果是权限，必须为角色下面的权限。具体自己去实现
    @GetMapping
    public Result getResource(){
        return Result.succ(list);
    }




}
