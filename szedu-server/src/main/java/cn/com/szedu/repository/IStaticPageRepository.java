package cn.com.szedu.repository;

import cn.com.szedu.entity.StaticPage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStaticPageRepository extends JpaRepository<StaticPage,Integer> {
}
