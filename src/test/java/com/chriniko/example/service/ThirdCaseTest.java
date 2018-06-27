package com.chriniko.example.service;

import com.chriniko.example.domain.Event;
import com.chriniko.example.service.fourth_case.AService_3;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {com.chriniko.example.config.AppConfiguration.class}
)
public class ThirdCaseTest {

    @Autowired
    private AService_3 s;

    private static ExecutorService executorService;

    private static int workers;

    @BeforeClass
    public static void setUp() {
        workers = 25;
        System.out.println("Workers = " + workers);
        executorService = Executors.newFixedThreadPool(workers);
    }

    @AfterClass
    public static void tearDown() {
        executorService.shutdownNow();
        System.exit(0);
    }

    @Test
    public void testIsolationLevel_ReadUncommitted() throws InterruptedException {

        try {

            testIsolationLevelInternal(
                    _s -> _s.insertEventIfNotExists_ReadUncommitted("same-msg"),
                    s,
                    records -> records == 1);

        } catch (Exception error) {

            assertTrue(error instanceof CompletionException);

            ObjectOptimisticLockingFailureException cause = (ObjectOptimisticLockingFailureException) error.getCause();
            assertEquals(Event.class.getName(), cause.getPersistentClassName());
            assertNull(cause.getPersistentClass());

            UUID uuid = UUID.fromString((String) cause.getIdentifier());
            assertNotNull(uuid);
        }

    }

    @Test
    public void testIsolationLevel_ReadCommitted() throws InterruptedException {

        try {

            testIsolationLevelInternal(
                    _s -> _s.insertEventIfNotExists_ReadCommitted("same-msg"),
                    s,
                    records -> records == 1);

        } catch (Exception error) {

            assertTrue(error instanceof CompletionException);

            ObjectOptimisticLockingFailureException cause = (ObjectOptimisticLockingFailureException) error.getCause();
            assertEquals(Event.class.getName(), cause.getPersistentClassName());
            assertNull(cause.getPersistentClass());

            UUID uuid = UUID.fromString((String) cause.getIdentifier());
            assertNotNull(uuid);
        }

    }

    @Test
    public void testIsolationLevel_RepeatableRead() throws InterruptedException {

        try {

            testIsolationLevelInternal(
                    _s -> _s.insertEventIfNotExists_RepeatableRead("same-msg"),
                    s,
                    records -> records == 1);
        } catch (Exception error) {

            assertTrue(error instanceof CompletionException);

            ObjectOptimisticLockingFailureException cause = (ObjectOptimisticLockingFailureException) error.getCause();
            assertEquals(Event.class.getName(), cause.getPersistentClassName());
            assertNull(cause.getPersistentClass());

            UUID uuid = UUID.fromString((String) cause.getIdentifier());
            assertNotNull(uuid);
        }

    }

    @Test
    public void testIsolationLevel_Serializable() throws InterruptedException {

        try {

            testIsolationLevelInternal(
                    _s -> _s.insertEventIfNotExists_Serializable("same-msg"),
                    s,
                    records -> records == 1);

        } catch (Exception error) {

            assertTrue(error instanceof CompletionException);

            ObjectOptimisticLockingFailureException cause = (ObjectOptimisticLockingFailureException) error.getCause();
            assertEquals(Event.class.getName(), cause.getPersistentClassName());
            assertNull(cause.getPersistentClass());

            UUID uuid = UUID.fromString((String) cause.getIdentifier());
            assertNotNull(uuid);
        }
    }

    private void testIsolationLevelInternal(Consumer<AService_3> serviceConsumer,
                                            AService_3 service,
                                            Predicate<Integer> shouldSatisfy) {

        int noOfTries = 4;
        int currentTry = 0;

        while (currentTry++ < noOfTries) {

            //given
            int writeOperationsPerWorker = 10;
            s.deleteAll();

            //when
            List<CompletableFuture<Boolean>> resultsFromWorkers = IntStream
                    .rangeClosed(1, workers)
                    .boxed()
                    .map(idx -> {

                        CompletableFuture<Boolean> cf = new CompletableFuture<>();

                        executorService.submit(() -> {
                            try {

                                int counter = 0;
                                while (counter++ < writeOperationsPerWorker) {
                                    serviceConsumer.accept(service);
                                }

                                System.out.println(Thread.currentThread().getName() + " just finished work!");
                                cf.complete(true);

                            } catch (Exception error) {
                                cf.completeExceptionally(error);
                            }
                        });

                        return cf;
                    })
                    .collect(Collectors.toList());

            resultsFromWorkers.forEach(CompletableFuture::join);


            //then
            int noOfRecords = s.findAll().size();
            System.out.printf("noOfRecords = %s\n", noOfRecords);
            if (!shouldSatisfy.test(noOfRecords)) {
                throw new IllegalStateException();
            }

        }

    }

}
