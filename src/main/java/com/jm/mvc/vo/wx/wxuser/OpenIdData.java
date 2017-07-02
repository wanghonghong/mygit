package com.jm.mvc.vo.wx.wxuser;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class OpenIdData {
	private List<String> openid = new ArrayList<>();

}
