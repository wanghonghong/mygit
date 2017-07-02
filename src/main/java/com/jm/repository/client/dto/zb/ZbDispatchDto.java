package com.jm.repository.client.dto.zb;


import com.jm.repository.po.zb.system.ZbDispatchHistory;
import lombok.Data;

import java.util.List;

/**
 * Created by ME on 2016/9/21.
 */
@Data
public class ZbDispatchDto {

    List roles;

    List<ZbDispatchHistory> zbDispatchHistoryList;

}
