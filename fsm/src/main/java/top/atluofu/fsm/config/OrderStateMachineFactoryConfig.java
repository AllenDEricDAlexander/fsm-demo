package top.atluofu.fsm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import top.atluofu.fsm.model.Events;
import top.atluofu.fsm.model.States;

/**
 * @ClassName: OrderStateMachineFactoryConfig
 * @description: OrderStateMachineFactoryConfig
 * @author: 有罗敷的马同学
 * @datetime: 2023Year-12Month-01Day-8:36
 * @Version: 1.0
 */
@Configuration
@EnableStateMachineFactory(name = "orderStateMachineFactory")
public class OrderStateMachineFactoryConfig extends StateMachineConfigurerAdapter<String, String> {
    @Override
    public void configure(StateMachineStateConfigurer<String, String> states)
            throws Exception {
        states.withStates()
                .initial(States.ORDER_WAIT_PAY.toString())
                .state(States.ORDER_WAIT_SEND.toString())
                .state(States.ORDER_WAIT_RECEIVE.toString())
                .end(States.ORDER_FINISH.toString());
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions)
            throws Exception {
        transitions.withExternal()
                .source(States.ORDER_WAIT_PAY.toString())
                .target(States.ORDER_WAIT_SEND.toString())
                .event(Events.PAY_ORDER.toString())
                .and()
                .withExternal()
                .source(States.ORDER_WAIT_SEND.toString())
                .target(States.ORDER_WAIT_RECEIVE.toString())
                .event(Events.SEND_ORDER.toString())
                .and()
                .withExternal()
                .source(States.ORDER_WAIT_RECEIVE.toString())
                .target(States.ORDER_FINISH.toString())
                .event(Events.RECEIVE_ORDER.toString());
    }
}
