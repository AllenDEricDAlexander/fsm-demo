package top.atluofu.fsm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.atluofu.fsm.model.Events;

@SpringBootTest
class FsmApplicationTests {

	@Test
	void contextLoads() {
		System.out.println(Events.RECEIVE_ORDER.toString());
	}

}
