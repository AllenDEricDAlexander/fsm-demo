package top.atluofu.fsm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import top.atluofu.fsm.listener.OrderStateListener;
import top.atluofu.fsm.model.Events;
import top.atluofu.fsm.model.States;

import java.util.EnumSet;

/**
 * @ClassName: StateMachineConfig
 * @description: fsm配置类
 * @author: 有罗敷的马同学
 * @datetime: 2023Year-11Month-28Day-8:35
 * @Version: 1.0
 */
@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {
    final OrderStateListener orderStateListener;

    public StateMachineConfig(OrderStateListener orderStateListener) {
        this.orderStateListener = orderStateListener;
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states)
            throws Exception {
        states
                .withStates()
                .initial(States.ORDER_WAIT_PAY)
                .states(EnumSet.allOf(States.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(States.ORDER_WAIT_PAY).target(States.ORDER_WAIT_SEND).event(Events.PAY_ORDER)
                .and()
                .withExternal()
                .source(States.ORDER_WAIT_SEND).target(States.ORDER_WAIT_RECEIVE).event(Events.SEND_ORDER)
                .and()
                .withExternal()
                .source(States.ORDER_WAIT_RECEIVE).target(States.ORDER_FINISH).event(Events.RECEIVE_ORDER);
    }
}
