package com.amazonaws.lambda.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;

public class LambdaLogSample implements RequestHandler<S3Event, Object> {

    static final Logger logger = LogManager.getLogger(LambdaLogSample.class);  
    LocalDate now = LocalDate.now();
    
    @Override
    public Object handleRequest(S3Event input, Context context) {
    	String response = new String("Hello Lambda!!");
        logger.info(logger.getLevel());
        logger.debug(now + response);
        
        return response;

	}
}
