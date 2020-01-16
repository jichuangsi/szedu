package cn.com.szedu.repository;

import cn.com.szedu.entity.MessageFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMessageFeedbackRepository extends JpaRepository<MessageFeedback,Integer> {

}
