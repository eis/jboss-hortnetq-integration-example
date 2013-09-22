package fi.eis.applications.jmsintegration;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    JMSProducer jmsProducer;
    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String home(Locale locale, Model model) {
        logger.info("Welcome home! The client locale is {}.", locale);
        try {
            jmsProducer.sendMessages();
            return jmsProducer.receiveMessages();
        } catch (Exception e){
            logger.error("error sending", e);
            return e.getMessage();
        }
    }
}