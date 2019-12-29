package cn.com.szedu.repository;

import cn.com.szedu.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MessageRepository extends JpaRepository<Message,Integer>,PagingAndSortingRepository<Message,Integer>,JpaSpecificationExecutor<Message> {

    Integer countByAlreadyReadIsFalse();

    Integer countByRecipientIdAndAlreadyReadIsFalse(String teacherId);//未读消息(根据老师)(根据接收者事用户)
    Message findByRecipientId(String teacherId);

    Message findByRecipientIdAndSenderid(String teacherId,String sendId);
    Message findByRecipientIdAndSendAndSenderid(String teacherId,String send,String sendId);
    Integer countByRecipientIdAndSendAndSenderid(String teacherId,String send,String sendId);
}
