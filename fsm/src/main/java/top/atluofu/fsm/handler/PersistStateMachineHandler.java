package top.atluofu.fsm.handler;


import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.access.StateMachineAccess;
import org.springframework.statemachine.listener.AbstractCompositeListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.LifecycleObjectSupport;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.util.Assert;
import top.atluofu.fsm.model.Events;
import top.atluofu.fsm.model.States;

import java.util.Iterator;
import java.util.List;

/**
 * @author ：luoqi/02216
 * @date ：Created in 2020/6/4 4:03 下午
 * @description：监听器的Handler以及接口定义
 */
public class PersistStateMachineHandler extends LifecycleObjectSupport {

    private final StateMachine<States, Events> stateMachine;
    private final PersistingStateChangeInterceptor interceptor = new PersistingStateChangeInterceptor();
    private final CompositePersistStateChangeListener listeners = new CompositePersistStateChangeListener();

    /**
     * 实例化一个新的持久化状态机Handler
     *
     * @param stateMachine
     */
    public PersistStateMachineHandler(StateMachine<States, Events> stateMachine) {
        Assert.notNull(stateMachine, "State machine must be set");
        this.stateMachine = stateMachine;
    }

    @Override
    protected void onInit() throws Exception {
        stateMachine.getStateMachineAccessor().doWithAllRegions(function -> function.addStateMachineInterceptor(interceptor));
    }

    /**
     * 处理entity的事件
     *
     * @param event
     * @param state
     * @return
     */
    public boolean handleEventWithState(Message<Events> event, States state) {
        stateMachine.stop();
        List<StateMachineAccess<States, Events>> withAllRegions = stateMachine.getStateMachineAccessor()
                .withAllRegions();
        for (StateMachineAccess<States, Events> access : withAllRegions) {
            access.resetStateMachine(new DefaultStateMachineContext<>(state, null, null, null));
        }
        stateMachine.start();
        return stateMachine.sendEvent(event);
    }

    /**
     * 添加listener
     *
     * @param listener
     */
    public void addPersistStateChangeListener(PersistStateChangeListener listener) {
        listeners.register(listener);
    }

    /**
     * 可以通过addPersistStateChangeListener，增加当前Handler的PersistStateChangeListener。
     * 在状态变化的持久化触发时，会调用相应的实现了PersistStateChangeListener的Listener实例。
     */
    public interface PersistStateChangeListener {
        void onPersist(State<States, Events> state, Message<Events> message,
                       Transition<States, Events> transition, StateMachine<States, Events> stateMachine);
    }

    private class PersistingStateChangeInterceptor extends StateMachineInterceptorAdapter<States, Events> {

        // 状态预处理的拦截器方法
//        @Override
//        public void preStateChange(State<States, Events> state, Message<Events> message,
//                                   Transition<States, Events> transition, StateMachine<States, Events> stateMachine) {
//            listeners.onPersist(state, message, transition, stateMachine);
//        }
    }

    private class CompositePersistStateChangeListener extends AbstractCompositeListener<PersistStateChangeListener> implements PersistStateChangeListener {

        @Override
        public void onPersist(State<States, Events> state, Message<Events> message,
                              Transition<States, Events> transition, StateMachine<States, Events> stateMachine) {
            for (Iterator<PersistStateChangeListener> iterator = getListeners().reverse(); iterator.hasNext(); ) {
                PersistStateChangeListener listener = iterator.next();
                listener.onPersist(state, message, transition, stateMachine);
            }
        }
    }

}
