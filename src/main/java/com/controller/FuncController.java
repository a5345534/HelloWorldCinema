package com.controller;

import com.entity.Func;
import com.entity.Job;
import com.entity.Permission;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.service.FuncService;
import com.service.JobService;
import com.service.PermissionService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/func")
public class FuncController {

    @Autowired
    FuncService funcService;

    @Autowired
    JobService jobService;

    @Autowired
    PermissionService permissionService;


    @GetMapping("/listAllFunc")
    public ModelAndView managePermission() throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("back_end/Func/listAllFunc");
        List<FuncPermsVo> funcPermsVos = new ArrayList<>();
        List<Job> jobs = jobService.getAll();
        for (Job job : jobs) {
            JobVo jobVo = new JobVo(job.getJobId(),job.getTitle());
            List<Integer> funIds = permissionService.getFuncIdByJobId(job.getJobId());
            List<Func> funcs = funcService.getAll();
            List<FuncVo> funcVos = funcs.stream().map(func -> {
                boolean checked = funIds.stream().anyMatch(id -> id.intValue() == func.getFuncId());
                FuncVo funcVo = new FuncVo(func.getFuncId(),func.getFunc(),checked);
                return funcVo;
            }).toList();
            FuncPermsVo funcPermsVo = new FuncPermsVo(jobVo,funcVos);
            funcPermsVos.add(funcPermsVo);
        }
        FuncPermsVoList funcPermsVoList = new FuncPermsVoList();
        funcPermsVoList.list = new ArrayList<>();
        funcPermsVoList.list.addAll(funcPermsVos);
        mav.addObject("funcPermsVoList", funcPermsVoList);
        return mav;

    }

    @PostMapping
    public String submitPermissions(@ModelAttribute FuncPermsVoList funcPermsVoList, Model model) throws JsonProcessingException {

        for (FuncPermsVo funcPermsVo : funcPermsVoList.list) {
            List<Integer> jobFuncIds = permissionService.getFuncIdByJobId(funcPermsVo.jobVo.jobId);
            Integer jobId = funcPermsVo.jobVo.jobId;
            for (FuncVo func : funcPermsVo.funcs) {
                Integer checkingFuncId = func.funcId;
                if (func.checked){
                    //是否存在此權限
                    boolean existed = jobFuncIds.stream().anyMatch(funcId -> funcId.equals(func.funcId));
                    if (!existed){
                        Permission permission = new Permission();
                        Permission.CompositeDetail compositeDetail = new Permission.CompositeDetail();
                        compositeDetail.setFuncId(checkingFuncId);
                        compositeDetail.setJobId(jobId);
                        permission.setCompositeKey(compositeDetail);
                        permissionService.addPermission(permission);
                    }
                }else {
                    boolean existed = jobFuncIds.stream().anyMatch(funcId -> funcId.equals(func.funcId));
                    if (existed){
                        permissionService.deletePermission(jobId,checkingFuncId);
                    }
                }
            }
        }

        return "redirect:/func/listAllFunc";
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FuncPermsVoList {
        public List<FuncPermsVo> list;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FuncPermsVo {
        public JobVo jobVo;
        public List<FuncVo> funcs;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JobVo {
        public Integer jobId;
        public String title;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FuncVo {
        public Integer funcId;
        public String func;
        public boolean checked;
    }

}
