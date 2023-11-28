package top.atluofu.fsm.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.atluofu.fsm.model.Events;
import top.atluofu.fsm.model.States;
import top.atluofu.fsm.po.OrderPO;
import top.atluofu.fsm.service.OrderService;
import top.atluofu.fsm.utils.ResultUtils;

/**
 * (Order)表控制层
 *
 * @author atluofu
 * @since 2023-11-28 09:43:54
 */
@RestController
@Slf4j
@Validated
@RequestMapping("order")
public class OrderController {
    /**
     * 服务对象
     */
    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("test/{name}")
    public ResultUtils demo(@PathVariable String name) {
        OrderPO build = OrderPO.builder()
                .orderName(name)
                .state(States.ORDER_WAIT_PAY.toString())
                .build();
        orderService.save(build);
        MessageBuilder.withPayload(Events.PAY_ORDER).setHeader("orderId", build.getId()).build();
        OrderPO byId = orderService.getById(build.getId());
        return ResultUtils.success(byId);
    }

    @Autowired
    private StateMachine<States, Events> orderStateMachine;

    private boolean changeStateAction(Message<Events> message, OrderPO order) {
        try {
            //启动状态机
            orderStateMachine.start();
            //从Redis缓存中读取状态机，缓存的Key为orderId+"STATE"，这是自定义的，读者可以根据自己喜好定义
            OrderPO byId = orderService.getById(message.getHeaders().get("orderId").toString());
            //将Message发送给OrderStateListener
            boolean res = orderStateMachine.sendEvent(message);
            //将更改完订单状态的 状态机 存储到 Redis缓存
            byId.setState(message.getPayload().toString());
            orderService.save(byId);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            orderStateMachine.stop();
        }
        return false;
    }

}

