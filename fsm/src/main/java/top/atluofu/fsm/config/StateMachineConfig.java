package top.atluofu.fsm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import top.atluofu.fsm.listener.OrderStateListener;
import top.atluofu.fsm.model.Events;
import top.atluofu.fsm.model.States;
import top.atluofu.fsm.po.OrderPO;

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

    @Bean
    public StateMachinePersister<States, Events, OrderPO> persister() {
        return new DefaultStateMachinePersister<>(new StateMachinePersist<States, Events, OrderPO>() {

            @Override
            public void write(StateMachineContext<States, Events> stateMachineContext, OrderPO orderPO) throws Exception {

            }

            @Override
            public StateMachineContext<States, Events> read(OrderPO orderPO) throws Exception {
                return null;
            }
        });
    }
}
