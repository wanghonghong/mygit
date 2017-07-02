package com.jm.repository.jpa.shop.imageText;

import com.jm.repository.po.shop.imageText.ImageTextRelate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>官方图文H5图片库</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/3 17:57
 */
public interface ImageTextRelateRepository extends JpaRepository<ImageTextRelate,Integer>{

    @Query("select a from ImageTextRelate a where a.imageTextId=?1 ")
    List<ImageTextRelate> findImageTextRelateByImageTextId(Integer imageTextId);

    @Modifying
    @Query("delete from ImageTextRelate a where a.imageTextId=?1")
    void deleteImageTextRelByImageTextId(Integer imageTextId);
}
