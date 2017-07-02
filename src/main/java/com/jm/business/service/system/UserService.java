package com.jm.business.service.system;

import com.jm.mvc.vo.order.UserQueryVo;
import com.jm.mvc.vo.system.JmUserUo;
import com.jm.mvc.vo.system.LoginVo;
import com.jm.mvc.vo.system.UserForQueryVo;
import com.jm.repository.jpa.system.UserRepository;
import com.jm.repository.po.system.user.User;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Toolkit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p></p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/15
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    
    public void deleteUser(int userId){
        userRepository.delete(userId);
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public User updateUser(JmUserUo userVo){
        User user = userRepository.findOne(userVo.getUserId());
        Toolkit.copyPropertiesIgnoreNull(userVo,user);
        user.setHeadImg(ImgUtil.substringUrl(userVo.getHeadImg()));
        user.setCardNumImg(ImgUtil.substringUrl(userVo.getCardNumImg()));
        user.setUpdateDate(new Date());
        return userRepository.save(user);
    }

    public User queryUser(User user){
        return userRepository.save(user);
    }

    public User getUser(int userId){
        return userRepository.findOne(userId);
    }

    public UserForQueryVo getUserQueryVo(int id){
        User user =  userRepository.findOne(id);
        UserForQueryVo userQueryVo = new UserForQueryVo();
        BeanUtils.copyProperties(user,userQueryVo);
        userQueryVo.setHeadImg(ImgUtil.appendUrl(userQueryVo.getHeadImg(),200));
        userQueryVo.setCardNumImg(ImgUtil.appendUrl(userQueryVo.getCardNumImg(),200));
        return userQueryVo;
    }

    public Page<User> queryUser(Integer page, UserQueryVo userQueryVo) {
        PageRequest pageRequest = new PageRequest(page,20);
        return userRepository.findAll(getSpec(userQueryVo),pageRequest);

    }

    public Specification<User> getSpec(final UserQueryVo userQueryVo){
        Specification<User> spec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
            	List<Predicate> predicates = new ArrayList<>();
                Predicate predicate = cb.conjunction();
                 if ( null != userQueryVo ) {
                	 
                	 if(userQueryVo.getPhoneNumber()!=""){
                		 predicates.add(cb.like(root.get("phoneNumber").as(String.class), "%" + userQueryVo.getPhoneNumber() + "%"));
                	 }
                }
                predicate.getExpressions().addAll(predicates);
                return predicate;
            }
        };
        return spec;
    }

    /**
     * 验证用户名密码是否正确
     * @param loginVo
     * @return
     */
    public User findUser(LoginVo loginVo){
        return userRepository.findUserByPhoneNumberAndPassword(loginVo.getPhoneNumber(),loginVo.getPassword());
    }

    public User findUserByPhoneNumber(String phoneNumber){
        return userRepository.findUserByPhoneNumber(phoneNumber);
    }

   /**
    * 根据店铺Id获取店铺员工列表
    * @param shopId
    * @return
    */
    public List queryUserList(Integer shopId,Integer page,Integer pageSize){
        return userRepository.queryUserList(shopId,page,pageSize);
    }

    
    /**
     * 根据手机号码获取店铺员工列表
     * @param shopId 
     * @param phonenumber
     * @return
     */
     public List queryUserListByPhoneNumber(String phonenumber, Integer shopId){
         return userRepository.queryUserListByPhoneNumber(phonenumber,shopId);
     }



}
