package top.atluofu.fsm.listener;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import top.atluofu.fsm.handler.PersistStateMachineHandler;
import top.atluofu.fsm.model.Events;
import top.atluofu.fsm.model.States;

/**
 * @ClassName: OrderPersistStateChangeListener
 * @description: TODO
 * @author: 有罗敷的马同学
 * @datetime: 2023Year-11Month-28Day-12:54
 * @Version: 1.0
 */
public class OrderPersistStateChangeListener implements PersistStateMachineHandler.PersistStateChangeListener {

    @Override
    public void onPersist(State<States, Events> state, Message<Events> message,
                          Transition<States, Events> transition, StateMachine<States, Events> stateMachine) {
        if (message != null && message.getHeaders().containsKey("order")) {
            Integer orderNum = message.getHeaders().get("order", Integer.class);
            // save持久化
            States status = state.getId();
            System.out.println("save order: " + orderNum + ", order state: " + status);
        }
    }

}
