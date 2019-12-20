package cn.com.szedu.repository;

import cn.com.szedu.entity.SchoolInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISchoolInfoRepository extends JpaRepository<SchoolInfo,String> {
}
