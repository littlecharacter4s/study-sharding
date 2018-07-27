package com.lc.sharding.util;


import com.lc.sharding.init.Inits;
import com.lc.sharding.mybatismapper.TOrderMapper;
import com.lc.sharding.pojo.TOrder;
import com.lc.sharding.pojo.TOrderExample;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


/**
 * Created by wanghh on 2018-7-25.
 */
@RunWith(SpringJUnit4ClassRunner.class) // 使用junit4进行测试
@ContextConfiguration({"classpath:applicationContext.xml"}) // 加载配置文件*/
public class UniqueIDUtilsTest {
    @Autowired
    private TOrderMapper tOrderMapper;

    @Before
    public void init() throws Exception {
        new Inits().init();
    }

    @Test
    public void bulidOrderIdTest() {

        UniqueIDUtils uniqueIDUtils = new UniqueIDUtils();
        TOrder tOrder = new TOrder();
        tOrder.setBusinessId(112L);
        long userId = 10000001;
        for (int i = 0; i < 64; i++) {
            tOrder.setUserId(userId + i);
            long orderId = uniqueIDUtils.bulidOrderId(tOrder.getUserId());
            System.out.println("第" + (i + 1) + "次userId=" + Long.toBinaryString(tOrder.getUserId()));
            System.out.println("第" + (i + 1) + "次orderId=" + Long.toBinaryString(orderId));
            tOrder.setOrderId(orderId);
            tOrder.setId(orderId);
            int result = tOrderMapper.insert(tOrder);
        }

    }

    @Test
    public void selectTest() {
        TOrderExample example = new TOrderExample();
        example.createCriteria().andOrderIdEqualTo(149917559837785907L);//存在从库中
        List<TOrder> list = tOrderMapper.selectByExample(example);
        System.out.println("订单id:" + (list.size() > 0 ? true : false));

        TOrderExample tOrderExampleUserId = new TOrderExample();
        tOrderExampleUserId.createCriteria().andUserIdEqualTo(9000019L);//存在主库中
        list = tOrderMapper.selectByExample(tOrderExampleUserId);
        System.out.println("user_id:" + (list.size() > 0? true : false));
    }
}
