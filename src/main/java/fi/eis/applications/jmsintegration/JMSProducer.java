package fi.eis.applications.jmsintegration;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class JMSProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessages() throws JMSException{
        jmsTemplate.send(new MessageCreator(){

            @Override
            public Message createMessage(Session session) throws JMSException {
            TextMessage message  = session.createTextMessage("test message from spring");
            message.setStringProperty("text", "Hello World");
                return message;
            }
        });
    }

    public void receiveMessages() throws JMSException{
        System.out.println("Getting message from queue "+ jmsTemplate.receive().getStringProperty("text"));
    }

}