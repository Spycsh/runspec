package com.runspec.producer.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// no logger support currently
//import org.apache.log4j.logger;

public class PropertyFileReader {
//    private static final Logger logger = Logger.getLogger(PropertyFileReader.class);

    private static Properties prop = new Properties();
    public static Properties readPropertyFile() throws Exception {
        if(prop.isEmpty()){
            InputStream input = PropertyFileReader.class.getClassLoader().getResourceAsStream("producer.properties");
            try {
                prop.load(input);
            } catch (IOException ex){
                // logger.error(ex);
                System.out.println(ex);
                throw ex;
            } finally{
                if(input!=null){
                    input.close();
                }
            }
        }
        return prop;
    }
}
