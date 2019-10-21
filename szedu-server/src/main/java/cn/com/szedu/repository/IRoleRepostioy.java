package cn.com.szedu.repository;

import cn.com.szedu.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepostioy extends JpaRepository<Role,String> {
}
