package com.jm.repository.jpa.order.recycle;

import com.jm.repository.po.order.recycle.RecycleNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>订单</p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6
 */
public interface RecycleNodeRepository extends JpaRepository<RecycleNode, Integer> {


}