/**
 * @author Administrator
 *
 */
package com.cloudside.redis.common.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbit")
public class RabbitCtl {

	@Autowired
	private Sender helloSender1;
	@Autowired
	private Sender helloSender2;

	@PostMapping("/hello")
	public void hello() {
		helloSender1.send("helloSender1");
	}
	
	/**
     * 单生产者-多消费者
     */
    @PostMapping("/oneToMany")
    public void oneToMany() {
        for(int i=0;i<10;i++){
        	helloSender1.send("hellomsg:"+i);
        }
    }
    
    /**
     * 多生产者-多消费者
     */
    @PostMapping("/manyToMany")
    public void manyToMany() {
        for(int i=0;i<10;i++){
            helloSender1.send("hellomsg:"+i);
            helloSender2.send("hellomsg:"+i);
        }
    }
}
