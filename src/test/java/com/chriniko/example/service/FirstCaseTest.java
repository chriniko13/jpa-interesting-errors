package com.chriniko.example.service;

import com.chriniko.example.service.first_case.AService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {com.chriniko.example.config.AppConfiguration.class}
)
public class FirstCaseTest {

    @Autowired
    private AService aService;

    @Test
    public void doTest() {

        try {
            //when
            aService.first();

            //then
        } catch (Exception error) {
            /*
                Because they share the same transaction it will fail, more detail:

                AService#first ----spawned a tx1 --->
                               BService#second ---will use tx1 (join tx) --->
                               and throws a BException (BService#second), the whole tx will rollback (because they share it),
                               meaning db interactions in AService#first will not be successful.
             */
            Assert.assertNotNull(error);
        }

    }

}
