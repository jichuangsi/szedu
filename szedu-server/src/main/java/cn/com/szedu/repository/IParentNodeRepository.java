package cn.com.szedu.repository;


import cn.com.szedu.entity.ParentNode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IParentNodeRepository extends JpaRepository<ParentNode,Integer> {
}
