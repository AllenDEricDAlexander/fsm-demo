package top.atluofu.fsm.listener;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import top.atluofu.fsm.model.States;
import top.atluofu.fsm.po.OrderPO;
import top.atluofu.fsm.service.impl.OrderServiceImpl;

import java.util.Objects;

/**
 * @ClassName: OrderStatePersistChangeListener
 * @description: OrderStatePersistChangeListener
 * @author: 有罗敷的马同学
 * @datetime: 2023Year-12Month-01Day-8:41
 * @Version: 1.0
 */
public class OrderStatePersistChangeListener implements PersistStateMachineHandler.PersistStateChangeListener {
    @Autowired
    OrderServiceImpl orderService;

    @Override
    public void onPersist(State<String, String> state, Message<String> message, Transition<String, String> transition, StateMachine<String, String> stateMachine) {
        if (ObjectUtil.isNotNull(message) && message.getHeaders().containsKey("orderId")) {
            OrderPO byId = orderService.getById(Integer.valueOf(Objects.requireNonNull(message.getHeaders().get("orderId")).toString()));
            String id = state.getId();
            States states = States.valueOf(id);
            byId.setState(states.toString());
            orderService.saveOrUpdate(byId);
        }
    }
}
