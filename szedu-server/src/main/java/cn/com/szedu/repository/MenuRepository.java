package cn.com.szedu.repository;

import cn.com.szedu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu,Integer> {
    List<Menu> findAll();
    List<Menu> findByPid(Integer pid);
    @Transactional
    @Modifying
    @Query(value = "insert into menu(title,pid) values(?1,?2)",nativeQuery = true)
    void insertTreeNode(String name,Integer pid);
    Menu findFirstById(Integer id);
}
