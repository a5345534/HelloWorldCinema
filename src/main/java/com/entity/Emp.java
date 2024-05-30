package com.entity;

import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.sql.Date;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;

@Entity
@Table(name = "emp")
public class Emp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private Integer empId;

    @Column(name = "emp_name", length = 10)
    @NotEmpty(message="員工姓名: 請勿空白")
    @Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$", message = "員工姓名: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間")
    private String empName;

    @Column(name = "emp_password", length = 12,nullable = false)
    @NotEmpty(message="員工密碼: 請勿空白")
    @Pattern(regexp = "^[(a-zA-Z0-9_)]{2,10}$", message = "員工密碼: 只能是英文字母、數字和_ , 且長度必需在2到10之間")
    private String empPassword;

    @Column(name = "emp_email", length = 40)
    @NotEmpty(message="員工信箱: 請勿空白")
    @Pattern(regexp = "^[(a-zA-Z0-9_)]{2,40}$", message = "員工密碼: 只能是英文字母、數字和_ , 且長度必需在2到40之間")
    private String empEmail;

    @Column(name = "hiredate")
    @NotNull(message="雇用日期: 請勿空白")
    @Future(message="日期必須是在今日(不含)之後")
    @Past(message="日期必須是在今日(含)之前")
    private Date hireDate;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(name = "emp_status", length = 3,nullable = false)
    @NotEmpty(message="員工狀態: 請勿空白")
    private String empStatus;




    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpPassword() {
        return empPassword;
    }

    public void setEmpPassword(String empPassword) {
        this.empPassword = empPassword;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getEmpStatus() {
        return empStatus;
    }

    public void setEmpStatus(String empStatus) {
        this.empStatus = empStatus;
    }

    @Override
    public String toString() {
        return "Emp [empId=" + empId + ", empName=" + empName + ", empPassword=" + empPassword + ", empEmail="
                + empEmail + ", hireDate=" + hireDate + ", job=" + job + ", empStatus=" + empStatus + "]";
    }






}
