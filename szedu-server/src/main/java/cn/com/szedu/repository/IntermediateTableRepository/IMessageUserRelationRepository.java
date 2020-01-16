package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.MessageUserRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IMessageUserRelationRepository extends JpaRepository<MessageUserRelation,Integer> {
    @Transactional
    void deleteByMIdIn(List<Integer> mid);//根据消息id删除消息
    @Transactional
    void deleteByMIdAndUId(Integer id,String uid);
}
