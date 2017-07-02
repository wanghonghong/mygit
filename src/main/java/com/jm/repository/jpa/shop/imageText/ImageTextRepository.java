package com.jm.repository.jpa.shop.imageText;

import com.jm.repository.po.shop.imageText.ImageText;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>官方图文</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/3 17:42
 */
public interface ImageTextRepository extends JpaRepository<ImageText,Integer> {

 @Query(value="select i,u.userName,t.typeName from ImageText i ,User u , ImageTextType t " +
         " where i.imageTextType=t.id and i.createStaffId=u.userId " +
         " and i.isValid='Y' and i.shopId=? ")
 List findImageTextByShopId(Integer shopId);

 @Query(value="select i from ImageText i  where i.isValid='Y' and i.shopId=?1 and i.typeId=?2 order by i.sort desc,i.id desc")
 List<ImageText> findImageTextByShopIdAndTypeId(Integer shopId ,Integer typeId);

 @Query(value = "select count(1) from ImageText where imageTextType=? and isValid='Y'")
 Integer getCountByTypeId(Integer imageTextType);

 @Modifying
 @Query(value = "update ImageText set imageTextType = null where imageTextType=?1")
 void updateImageTextByTypeId(Integer id);

 @Query(value="select i,u.userName,t.typeName from ImageText i ,User u , ImageTextType t " +
         " where i.imageTextType=t.id and i.createStaffId=u.userId " +
         " and i.isValid='Y' and i.shopId=?1 and i.imageTextTile like ?2 and i.typeId in?3")
 Page findAll(Integer shopId, String imageTextTile, List typeIds, Pageable pageable);

 @Query(value="select i,u.userName,t.typeName from ImageText i ,User u , ImageTextType t " +
         " where i.imageTextType=t.id and i.createStaffId=u.userId " +
         " and i.isValid='Y' and i.shopId=?1 and i.imageTextTile like ?2 and i.typeId in ?3 and ifnull(i.status,0) = 0")
 Page findAllWap(Integer shopId, String imageTextTile, List typeIds, Pageable pageable);

 @Query(value="select i,u.userName,t.typeName from ImageText i ,User u , ImageTextType t " +
         " where i.imageTextType=t.id and i.createStaffId=u.userId " +
         " and i.imageTextType=?1 and ifnull(i.status,0) = 0 and i.isValid='Y'")
 Page findAllByImageTypeId(Integer imageTypeId, Pageable pageable);
}
