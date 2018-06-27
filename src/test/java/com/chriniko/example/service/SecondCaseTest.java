package com.chriniko.example.service;

import com.chriniko.example.domain.Event;
import com.chriniko.example.service.second_case.AService_2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {com.chriniko.example.config.AppConfiguration.class}
)
public class SecondCaseTest {

    @Autowired
    private AService_2 aService2;

    @Test
    public void doTest() {

        //given
        aService2.init();

        //when
        List<Event> result = aService2.first();

        //then
        Assert.assertEquals(1, result.size());

    }

}
