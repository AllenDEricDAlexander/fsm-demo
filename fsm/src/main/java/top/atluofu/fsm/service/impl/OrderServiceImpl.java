package top.atluofu.fsm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.atluofu.fsm.dao.OrderDao;
import top.atluofu.fsm.po.OrderPO;
import top.atluofu.fsm.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * (Order)表服务实现类
 *
 * @author atluofu
 * @since 2023-11-28 09:43:54
 */
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderPO> implements OrderService {

}

