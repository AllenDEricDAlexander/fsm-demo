package top.atluofu.fsm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import top.atluofu.fsm.handler.PersistStateMachineHandler;
import top.atluofu.fsm.listener.OrderPersistStateChangeListener;
import top.atluofu.fsm.model.Events;
import top.atluofu.fsm.model.States;
import top.atluofu.fsm.service.impl.OrderServiceImpl;

/**
 * @ClassName: OrderPersistHandlerConfig
 * @description: TODO
 * @author: 有罗敷的马同学
 * @datetime: 2023Year-11Month-28Day-12:46
 * @Version: 1.0
 */
@Configuration
public class OrderPersistHandlerConfig {

    @Autowired
    private StateMachine<States, Events> stateMachine;

    @Bean
    public OrderServiceImpl persist() {
        PersistStateMachineHandler handler = persistStateMachineHandler();
        handler.addPersistStateChangeListener(persistStateChangeListener());
        return new OrderServiceImpl(handler);
    }

    @Bean
    public PersistStateMachineHandler persistStateMachineHandler() {
        return new PersistStateMachineHandler(stateMachine);
    }

    @Bean
    public OrderPersistStateChangeListener persistStateChangeListener() {
        return new OrderPersistStateChangeListener();
    }

}