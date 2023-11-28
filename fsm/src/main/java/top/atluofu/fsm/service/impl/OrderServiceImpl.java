package top.atluofu.fsm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import top.atluofu.fsm.dao.OrderDao;
import top.atluofu.fsm.handler.PersistStateMachineHandler;
import top.atluofu.fsm.po.OrderPO;
import top.atluofu.fsm.service.OrderService;

/**
 * (Order)表服务实现类
 *
 * @author atluofu
 * @since 2023-11-28 09:43:54
 */
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderPO> implements OrderService {
    private PersistStateMachineHandler handler;

    @Lazy
    @Autowired
    public OrderServiceImpl(PersistStateMachineHandler handler) {
        this.handler = handler;
    }


}

