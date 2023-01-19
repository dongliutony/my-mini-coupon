package com.dongliu.coupon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupon_path")
public class Path {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "path_pattern", nullable = false)
    private String pathPattern;

    @Basic
    @Column(name = "http_method", nullable = false)
    private String httpMethod;

    @Basic
    @Column(name = "path_name", nullable = false)
    private String pathName;

    @Basic
    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Basic
    @Column(name = "op_mode", nullable = false)
    private String opMode;


    public Path(String pathPattern, String httpMethod, String pathName, String serviceName, String opMode) {

        this.pathPattern = pathPattern;
        this.httpMethod = httpMethod;
        this.pathName = pathName;
        this.serviceName = serviceName;
        this.opMode = opMode;
    }
}
