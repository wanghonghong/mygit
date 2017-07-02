package com.jm.repository.jpa.shop.imageText;

import com.jm.repository.po.shop.imageText.ImageTextTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ImageTextTemplateRepository extends JpaRepository<ImageTextTemplate,Integer> {

    @Modifying
    @Query(value = "update ImageTextTemplate set menuId = null where menuId = ?1")
    void updateImageTextTemplateByMenuId(Integer id);

    ImageTextTemplate findImageTextTemplateByMenuId(Integer menuId);
}
