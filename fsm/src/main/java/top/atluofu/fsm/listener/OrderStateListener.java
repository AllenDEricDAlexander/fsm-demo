package top.atluofu.fsm.listener;

import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;
import top.atluofu.fsm.model.States;
import top.atluofu.fsm.po.OrderPO;
import top.atluofu.fsm.service.impl.OrderServiceImpl;

import java.util.Objects;

/**
 * @ClassName: OrderStateListener
 * @description: TODO
 * @author: 有罗敷的马同学
 * @datetime: 2023Year-11Month-28Day-8:57
 * @Version: 1.0
 */
@Component
@WithStateMachine(name = "ORDER_STATE_LISTENER")
public class OrderStateListener {
    final OrderServiceImpl orderService;

    public OrderStateListener(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @OnTransition(source = "ORDER_WAIT_PAY", target = "ORDER_WAIT_SEND")
    public void payToSend(Message<States> message) {
        OrderPO byId = orderService.getById(Objects.requireNonNull(message.getHeaders().get("orderId")).toString());
        byId.setState(States.ORDER_WAIT_SEND.toString());
        orderService.save(byId);
    }
}
