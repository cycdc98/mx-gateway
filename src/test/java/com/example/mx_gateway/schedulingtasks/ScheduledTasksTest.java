package com.example.mx_gateway.schedulingtasks;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ScheduledTasks 单元测试。
 * 覆盖: 直接调用、日志输出内容验证、时间格式校验、重复调用。
 */
class ScheduledTasksTest {

    private ScheduledTasks scheduledTasks;
    private Logger log4jLogger;
    private TestListAppender testAppender;

    @BeforeEach
    void setUp() {
        scheduledTasks = new ScheduledTasks();
        log4jLogger = (Logger) LogManager.getLogger(ScheduledTasks.class);
        testAppender = new TestListAppender();
        testAppender.start();
        log4jLogger.addAppender(testAppender);
        log4jLogger.setLevel(Level.INFO);
    }

    @AfterEach
    void tearDown() {
        log4jLogger.removeAppender(testAppender);
        testAppender.stop();
    }

    /**
     * 自定义 Appender，将日志事件收集到 List 中用于断言。
     */
    private static class TestListAppender extends AbstractAppender {
        private final List<LogEvent> events = new ArrayList<>();

        TestListAppender() {
            super("TestListAppender", null, null, true, null);
        }

        @Override
        public void append(LogEvent event) {
            events.add(event.toImmutable());
        }

        List<LogEvent> getEvents() {
            return events;
        }
    }

    // ==================== 正常路径测试 ====================

    @Test
    void reportCurrentTime_shouldNotThrowException() {
        assertDoesNotThrow(() -> scheduledTasks.reportCurrentTime(),
                "reportCurrentTime should not throw any exception");
    }

    @Test
    void reportCurrentTime_shouldLogInfoMessage() {
        scheduledTasks.reportCurrentTime();

        List<LogEvent> events = testAppender.getEvents();
        assertFalse(events.isEmpty(), "Should capture at least one log event");
        assertEquals(Level.INFO, events.get(0).getLevel(), "Log level should be INFO");
    }

    @Test
    void reportCurrentTime_messageShouldStartWithExpectedPrefix() {
        scheduledTasks.reportCurrentTime();

        String message = testAppender.getEvents().get(0).getMessage().getFormattedMessage();
        assertTrue(message.startsWith("The time is now "),
                "Log message should start with 'The time is now ', but got: " + message);
    }

    @Test
    void reportCurrentTime_shouldContainTimeInHHmmssFormat() {
        scheduledTasks.reportCurrentTime();

        String message = testAppender.getEvents().get(0).getMessage().getFormattedMessage();
        Pattern timePattern = Pattern.compile("The time is now \\d{2}:\\d{2}:\\d{2}");
        assertTrue(timePattern.matcher(message).matches(),
                "Log message should contain time in HH:mm:ss format, but got: " + message);
    }

    // ==================== 边界 / 重复调用测试 ====================

    @Test
    void reportCurrentTime_repeatedCalls_shouldNotThrowException() {
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                scheduledTasks.reportCurrentTime();
            }
        }, "100 repeated calls should not throw exception");
    }

    @Test
    void reportCurrentTime_repeatedCalls_shouldProduceMultipleLogs() {
        for (int i = 0; i < 10; i++) {
            scheduledTasks.reportCurrentTime();
        }

        assertEquals(10, testAppender.getEvents().size(),
                "Should produce exactly 10 log events");
    }

    @Test
    void reportCurrentTime_calledOnce_shouldProduceSingleLog() {
        scheduledTasks.reportCurrentTime();
        assertEquals(1, testAppender.getEvents().size(),
                "Single call should produce exactly 1 log event");
    }
}
