package cn.com.szedu.dao.mapper;

import cn.com.szedu.model.RoleUrlModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IBackUserMapper {
    @Select("<script>SELECT u.role_id as roleId,u.page_id as pageId,sp.page_name as pageName,sp.page_url as pageUrl, sp.parent_node as parentNode,pn.node_name as nodeName\n" +
            "FROM urlrelation u INNER JOIN static_page sp ON u.page_id=sp.id INNER JOIN parent_node pn ON sp.parent_node=pn.id \n" +
            "WHERE u.role_id=#{roleId}</script>")
    List<RoleUrlModel> getPageByRoleId(@Param("roleId") String roleId);

    @Select("<script>SELECT sp.id as pageId,sp.page_name as pageName,sp.page_url as pageUrl, sp.parent_node as parentNode,pn.node_name as nodeName\n" +
            "FROM static_page sp INNER JOIN parent_node pn ON sp.parent_node=pn.id</script>")
    List<RoleUrlModel> getAllPage();
}
