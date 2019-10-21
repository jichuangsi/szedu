package cn.com.szedu.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper

public interface IUserPositionMapper {
    @Update("<script>UPDATE userposition set status='D' WHERE userid=#{uid}</script>")
    void updatePositionByuserid(@Param("uid") String uid);

    @Update("<script>UPDATE userposition set status=#{status} WHERE userid=#{uid}</script>")
    void updatePositionByGradeId(@Param("classId") String classId, @Param("status") String status);
}
