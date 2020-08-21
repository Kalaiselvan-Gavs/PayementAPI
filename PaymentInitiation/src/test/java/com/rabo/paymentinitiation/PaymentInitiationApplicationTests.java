package com.rabo.paymentinitiation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.rabo.paymentinitiation.exception.CustomExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.rabo.paymentinitiation.controller.PaymentInitiationController;

//https://thepracticaldeveloper.com/2020/06/04/guide-spring-boot-controller-tests/

@RunWith(SpringRunner.class)
@SpringBootTest
class PaymentInitiationApplicationTests {

	private MockMvc mvc;
	
	@InjectMocks
    private PaymentInitiationController paymentInitiationController;
	
	@BeforeEach
    public void setup() {
        // We would need this line if we would not use the MockitoExtension
        // MockitoAnnotations.initMocks(this);
        // Here we can't use @AutoConfigureJsonTesters because there isn't a Spring context
        //JacksonTester.initFields(this, new ObjectMapper());
        // MockMvc standalone approach
        mvc = MockMvcBuilders.standaloneSetup(paymentInitiationController)
                .setControllerAdvice(new CustomExceptionHandler())
                //.addFilters(new SuperHeroFilter())
                .build();
    }
	
	@Test
    void canRetrieveByIdWhenExists() throws Exception {
        // when
        MockHttpServletResponse response = mvc.perform(get("/initiate-payment").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        /*assertThat(response.getContentAsString()).isEqualTo(
                jsonSuperHero.write(new SuperHero("Rob", "Mannon", "RobotMan")).getJson()
        );*/
    }

}
