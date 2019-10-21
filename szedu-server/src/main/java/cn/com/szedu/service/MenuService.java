package cn.com.szedu.service;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.Menu;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MenuService {
    @Resource
    private MenuRepository menuRepository;

    @Transactional
    public void addNode(Menu menu) throws UserServiceException {
        if (StringUtils.isEmpty(menu.getTitle()) || StringUtils.isEmpty(menu.getPid())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        menuRepository.insertTreeNode(menu.getTitle(),menu.getPid());
    }

    public List<Menu> getAllMenu(){
        return menuRepository.findAll();
    }

    public List<Menu> getMenuByPid(Integer pid) throws UserServiceException {
        if (StringUtils.isEmpty(pid)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        return menuRepository.findByPid(pid);
    }

    @Transactional
    public void updateNode(Integer id,String title) throws UserServiceException {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(title)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        Menu menu=menuRepository.findFirstById(id);
        menu.setTitle(title);
        menuRepository.save(menu);
    }

    @Transactional
    public void deleteNode(Integer id) throws UserServiceException {
        if (StringUtils.isEmpty(id)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        menuRepository.deleteById(id);
    }
}
