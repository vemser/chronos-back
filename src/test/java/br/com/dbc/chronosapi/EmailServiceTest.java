package br.com.dbc.chronosapi;

import br.com.dbc.chronosapi.service.EmailService;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Value("${spring.mail.username}")
    private String from;

    @Mock
    private freemarker.template.Configuration fmConfiguration;
}

