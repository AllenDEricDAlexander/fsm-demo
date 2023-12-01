package top.atluofu.fsm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.messaging.Message;
import top.atluofu.fsm.po.OrderPO;

/**
 * (Order)表服务接口
 *
 * @author atluofu
 * @since 2023-11-28 09:43:54
 */
public interface OrderService extends IService<OrderPO> {
    boolean changeStateAction(Message<String> message, OrderPO order);
}

