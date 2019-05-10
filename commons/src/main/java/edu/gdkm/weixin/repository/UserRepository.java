package edu.gdkm.weixin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.gdkm.weixin.domain.User;
//Spring Data JPA 自动生成接口实例
//动态代理技术
//extends 可以得到CRUD方法
@Repository
public interface UserRepository extends JpaRepository<User, String>{


	User findByOpenId(String openId);

	
}
