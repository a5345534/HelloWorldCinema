package com.controller;

import com.entity.Emp;
import com.entity.Func;
import com.entity.Job;
import com.entity.Permission;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.service.FuncService;
import com.service.JobService;
import com.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller("/func")
public class FuncController {

    @Autowired
    FuncService funcService;

    @Autowired
    JobService jobService;

    @Autowired
    PermissionService permissionService;


    @GetMapping("/listAllFunc")
    public String managePermission(@RequestParam("jobId")Integer jobId, Model model) throws JsonProcessingException {
        List<Func> func = funcService.getAll();
        String permissions = permissionService.getPermissionsByJobId(jobId);
        Job job = jobService.getById(jobId);

        model.addAttribute("func", func);
        model.addAttribute("permissions", permissions);
        model.addAttribute("job", job);

        return "back_end/Func/listAllFunc";

    }

//    @PostMapping("")












}
