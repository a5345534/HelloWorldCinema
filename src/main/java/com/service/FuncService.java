package com.service;

import com.dao.FuncRepository;
import com.entity.Emp;
import com.entity.Func;
import com.entity.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FuncService {

    @Autowired
    FuncRepository funcRepository;

    public void addFunc(Func func){
        funcRepository.save(func);
    }

    public void updateFunc(Func func){
        funcRepository.save(func);
    }

    public void deleteFunc(Integer funcId){
        funcRepository.deleteById(funcId);
    }

    public List<Func> getAll(){
       return funcRepository.findAll();
    }

    public Func getById(Integer funcId){
        Optional<Func> optioinal = funcRepository.findById(funcId);
        return optioinal.orElse(null);
    }



}
