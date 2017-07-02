package com.jm.repository.jpa.shop.imageText;

import com.jm.repository.po.shop.imageText.ImageTextType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>官方图文类型</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/3 17:57
 */
public interface ImageTextTypeRepository extends JpaRepository<ImageTextType,Integer>{

    @Query("select a from ImageTextType a where a.shopId=?1 and a.typeId in?2 order by a.sort ")
    List<ImageTextType> findImageTextTypeByShopId(Integer shopId, List typeIds);

    @Query("select a from ImageTextType a where a.shopId=?1 and a.typeId in?2 and a.typeName like ?3")
    Page findAll(Integer shopId, List typeIds,String typeName, Pageable pageable);

    @Query("select MAX(sort) from ImageTextType where shopId=?1")
    Integer findMaxSortByShopId(Integer shopId);
}
