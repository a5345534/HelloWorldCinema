package com.entity;

import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "job")
public class Job {

    @Id
    @Column(name = "job_id",nullable = false)
    @NotEmpty(message="請勿空白")
    @Pattern(regexp = "^[(0-9)]$", message = "員工職位: 只能是數字")
    private Integer jobId;

    @Column(name = "title",length = 4)
    @NotEmpty(message="請勿空白")
    @Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{1,4}$", message = "員工姓名: 只能是中、英文字母、數字和_ , 且長度必需在1到4之間")
    private String title;
    @OneToMany(cascade=CascadeType.REMOVE, fetch=FetchType.EAGER, mappedBy="job")
    private Set<Emp> emps = new HashSet<Emp>();



    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Set<Emp> getEmps() {
        return emps;
    }

    public void setEmps(Set<Emp> emps) {
        this.emps = emps;
    }

    @Override
    public String toString() {
        return "Job [jobId=" + jobId + ", title=" + title + "]";
    }




}
