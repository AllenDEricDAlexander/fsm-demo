package top.atluofu.fsm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
import top.atluofu.fsm.listener.OrderStatePersistChangeListener;

/**
 * @ClassName: OrderStatePersistConfig
 * @description: OrderStatePersistConfig
 * @author: 有罗敷的马同学
 * @datetime: 2023Year-12Month-01Day-8:53
 * @Version: 1.0
 */
@Configuration
public class OrderStatePersistConfig {
    @Autowired
    private StateMachineFactory<String, String> orderStateMachineFactory;
    @Autowired
    private OrderStatePersistChangeListener persistStateChangeListener;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public PersistStateMachineHandler getPersistStateMachineHandler() {
        StateMachine<String, String> orderStateMachine = orderStateMachineFactory.getStateMachine();
        PersistStateMachineHandler handler = new PersistStateMachineHandler(orderStateMachine);
        handler.addPersistStateChangeListener(persistStateChangeListener);
        return handler;
    }

}
