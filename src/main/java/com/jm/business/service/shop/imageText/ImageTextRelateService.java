package com.jm.business.service.shop.imageText;

import com.jm.mvc.vo.shop.imageText.ImageTextRelateCo;
import com.jm.mvc.vo.shop.imageText.ImageTextRelateUo;
import com.jm.repository.jpa.shop.imageText.ImageTextRelateRepository;
import com.jm.repository.po.shop.imageText.ImageTextRelate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>官方图文H5图片库</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/3 17:50
 */
@Service
public class ImageTextRelateService {
    @Autowired
    ImageTextRelateRepository imageTextRelateRepository;


    public List<ImageTextRelate> findImageTextRelateByImageTextId(Integer imageTextId) {
       return  imageTextRelateRepository.findImageTextRelateByImageTextId(imageTextId);
    }
    @Transactional
    public void save(Integer id, List<ImageTextRelateCo> list) {
        for (ImageTextRelateCo imageTextRelateCo:list) {
            ImageTextRelate imageTextRelate = new ImageTextRelate();
            BeanUtils.copyProperties(imageTextRelateCo,imageTextRelate);
            imageTextRelate.setImageTextId(id);
            imageTextRelateRepository.save(imageTextRelate);
        }
    }
    @Transactional
    public void update(List<ImageTextRelateUo> list, Integer id) {
        for (ImageTextRelateUo imageTextRelateUo:list) {
            ImageTextRelate imageTextRelate = new ImageTextRelate();
            BeanUtils.copyProperties(imageTextRelateUo,imageTextRelate);
            imageTextRelate.setImageTextId(id);
            imageTextRelateRepository.save(imageTextRelate);
        }
    }
    @Transactional
    public void delete(Integer id) {
        imageTextRelateRepository.delete(id);
    }

    public ImageTextRelate findImageTextRelateById(Integer id) {
        return imageTextRelateRepository.findOne(id);
    }

    @Transactional
    public void deleteByImageTextId(Integer id) {
        imageTextRelateRepository.deleteImageTextRelByImageTextId(id);
    }
}
