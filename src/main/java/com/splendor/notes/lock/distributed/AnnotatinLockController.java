package com.splendor.notes.lock.distributed;

import com.splendor.notes.lock.annotation.DistributedLock;
import com.splendor.notes.lock.strategy.RedissonLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author splendor.s
 * @create 2022/8/30 下午5:48
 */
@RestController
@Slf4j
public class AnnotatinLockController {

    @Resource
    RedissonLock redissonLock;

    /**
     * 模拟商品库存
     */
    public static volatile Integer TOTAL = 10;

    @GetMapping("annotatin-lock-decrease-stock")
    @DistributedLock(value="goods", leaseTime=5)
    public String lockDecreaseStock() throws InterruptedException {
        if (TOTAL > 0) {
            TOTAL--;
        }
        log.info("===注解模式=== 减完库存后,当前库存===" + TOTAL);
        return "=================================";
    }
}
