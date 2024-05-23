package com.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.Mem;

public interface MemRepository extends JpaRepository<Mem, Integer>{
	Mem findByMemId(Integer memId);
	Mem findByMemAcount(String memAcount);
	Mem findByMemEmail(String memEmail);
	List<Mem> findByMemStatus(String memStatus);
}
