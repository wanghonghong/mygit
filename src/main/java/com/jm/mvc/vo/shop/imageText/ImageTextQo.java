package com.jm.mvc.vo.shop.imageText;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p></p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/10 15:53
 */
@Data
public class ImageTextQo {

        @ApiModelProperty("图文标题")
        private String imageTextTile;

        @ApiModelProperty("图文分类id")
        private Integer imageTextTypeId;

        @ApiModelProperty(value = "上下架")
        private String flag;//"Y"

        @ApiModelProperty(value = "当前页")
        private Integer curPage;

        @ApiModelProperty(value = "每页显示条数")
        private Integer pageSize;

        public ImageTextQo(){
                this.curPage = 0;
                this.pageSize = 20;
        }



}
