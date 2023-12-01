package top.atluofu.fsm.service.impl;

import cn.hutool.core.exceptions.ValidateException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
import org.springframework.stereotype.Service;
import top.atluofu.fsm.dao.OrderDao;
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
    @Autowired
    private ObjectFactory<PersistStateMachineHandler> stateMachineHandlerObjectFactory;

    public boolean changeStateAction(Message<String> message, OrderPO order) {
        PersistStateMachineHandler persistStateMachineHandler = stateMachineHandlerObjectFactory.getObject();
        boolean b = persistStateMachineHandler.handleEventWithState(message, order.getState());
        if (!b) {
            throw new ValidateException("失败");
        }
        return false;
    }

}

