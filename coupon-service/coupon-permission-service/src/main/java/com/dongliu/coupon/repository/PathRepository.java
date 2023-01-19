package com.dongliu.coupon.repository;

import com.dongliu.coupon.entity.Path;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import java.util.List;

@Repository
public interface PathRepository extends JpaRepository<Path, Integer> {

    List<Path> findAllByServiceName(String serviceName);

    Path findByPathPatternAndHttpMethod(String pathPattern, String httpMethod);


}
