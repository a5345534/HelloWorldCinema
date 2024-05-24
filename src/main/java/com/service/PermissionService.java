package com.service;

import com.dao.PermissionRepository;
import com.entity.Permission;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;


@Service("permissionService")
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public void addPermission(Permission permission) {
        permissionRepository.save(permission);

    }

    public Permission getdById(Permission.CompositeDetail compositeDetail) {
        return permissionRepository.findById(compositeDetail).orElse(null);
    }

    public void deleteById(Permission.CompositeDetail compositeDetail) {
        permissionRepository.deleteById(compositeDetail);
    }


    public List<Integer> getFuncIdByJobId(Integer jobId) throws JsonProcessingException {
        List<Permission> jobs = permissionRepository.findByJobId(jobId);
        List<Integer> funIds = new ArrayList<>();
        for (Permission p : jobs) {
            Integer funId = p.getCompositeKey().getFuncId();
            funIds.add(funId);
        }
        return funIds;
    }

    public void deletePermission(Integer jobId, Integer funcId) {
        Permission byFuncIdAndJobId = permissionRepository.findByFuncIdAndJobId(funcId, jobId);
        Permission.CompositeDetail compositeKey = byFuncIdAndJobId.getCompositeKey();
        permissionRepository.deleteById(compositeKey);
    }

    public List<Permission> getPermissionByJobId(Integer jobId) {
        return permissionRepository.findByJobId(jobId);
    }
}





