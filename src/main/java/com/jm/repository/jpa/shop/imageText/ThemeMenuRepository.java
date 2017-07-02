package com.jm.repository.jpa.shop.imageText;

import com.jm.repository.po.shop.imageText.ImageText;
import com.jm.repository.po.shop.imageText.ThemeMenu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>主题模板菜单</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/4/6 16:38
 */
public interface ThemeMenuRepository extends JpaRepository<ThemeMenu,Integer> {

    List<ThemeMenu> findThemeMenuByShopId(Integer shopId);
}
