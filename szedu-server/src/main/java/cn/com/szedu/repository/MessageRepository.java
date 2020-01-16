package cn.com.szedu.repository;

import cn.com.szedu.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Integer>,PagingAndSortingRepository<Message,Integer>,JpaSpecificationExecutor<Message> {

    Integer countByAlreadyReadIsFalse();

    Integer countByRecipientIdAndAlreadyRead(String teacherId,String status);//未读消息(根据老师)(根据接收者事用户)
    //Message findByRecipientId(String teacherId);

    Message findByRecipientIdAndSenderid(String teacherId,String sendId);

    List<Message> findByRecipientIdAndSendAndSenderid(String teacherId, String send, String sendId);
    Integer countByRecipientIdAndSendAndSenderid(String teacherId,String send,String sendId);
    //系统
    Integer countBySenderidIsNullAndAlreadyReadIsTrue();
    //Integer countBySenderidIsNull
    //互动
    Integer countBySenderidIsNotNullAndAlreadyRead(String status);
   /* //留言
    Message findByRecipientIdAndSenderid(String teacherId,String sendId);*/

   Message findByIdIs(Integer id);

    Message findByRecipientIdAndSenderidAndId(String teacherId,String sendId,Integer id);

    List<Message> findBySenderidAndRecipientId(String sendId,String teacherId);

    List<Message> findByRecipientId(String teacherId);
}
