package top.atluofu.fsm.controller;


import cn.hutool.core.exceptions.ValidateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
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

    @GetMapping("test1/{name}")
    public ResultUtils demo1(@PathVariable String name) {
        OrderPO build = OrderPO.builder()
                .orderName(name)
                .state(States.ORDER_WAIT_PAY.toString())
                .build();
        boolean save = orderService.save(build);
        System.out.println(save);
        System.out.println(build);
        System.out.println("---------------------");
        Message<String> orderId1 = MessageBuilder.withPayload(Events.PAY_ORDER.toString()).setHeader("orderId", build.getId()).build();
        changeStateAction(orderId1, build);
        OrderPO byId = orderService.getById(build.getId());
        System.out.println(byId);
        return ResultUtils.success(byId);
    }

    @GetMapping("test2/{name}/{id}")
    public ResultUtils demo2(@PathVariable String name, @PathVariable Integer id) {
        OrderPO build = orderService.getById(id);
        System.out.println("---------------------");
        Message<String> orderId2 = MessageBuilder.withPayload(Events.SEND_ORDER.toString()).setHeader("orderId", build.getId()).build();
        changeStateAction(orderId2, build);
        OrderPO byId = orderService.getById(build.getId());
        System.out.println(byId);
        System.out.println("---------------------");
        return ResultUtils.success(byId);
    }

    @GetMapping("test3/{name}/{id}")
    public ResultUtils demo3(@PathVariable String name, @PathVariable Integer id) {
        OrderPO build = orderService.getById(id);
        System.out.println("---------------------");
        Message<String> orderId3 = MessageBuilder.withPayload(Events.RECEIVE_ORDER.toString()).setHeader("orderId", build.getId()).build();
        changeStateAction(orderId3, build);
        OrderPO byId = orderService.getById(build.getId());
        System.out.println(byId);
        System.out.println("---------------------");
        return ResultUtils.success(byId);
    }


    @Autowired
    private ObjectFactory<PersistStateMachineHandler> stateMachineHandlerObjectFactory;

    private boolean changeStateAction(Message<String> message, OrderPO order) {
        PersistStateMachineHandler persistStateMachineHandler = stateMachineHandlerObjectFactory.getObject();
        boolean b = persistStateMachineHandler.handleEventWithState(message, message.getPayload());
        if(!b){
            throw new  ValidateException("失败");
        }
        return false;
    }

}

