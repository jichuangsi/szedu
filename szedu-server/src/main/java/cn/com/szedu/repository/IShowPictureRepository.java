package cn.com.szedu.repository;

import cn.com.szedu.entity.ShowPicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IShowPictureRepository extends JpaRepository<ShowPicture,Integer> {
    ShowPicture findByid(Integer id);
    List<ShowPicture> findByWayAndStatus(String way,String status);
    int countByStatusAndWay(String status,String way);
}
