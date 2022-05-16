package com.amazonaws.lambda.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Handler implements RequestHandler<Object, String>{
	
static final Logger logger2 = LogManager.getLogger(Handler.class);

@Override
	public String handleRequest(Object event, Context context){
	
	 LocalDate now = LocalDate.now();

	 LambdaLogger logger = context.getLogger();
	 String response = new String("Hello Lamda!!");
	 logger.log(now + " " + response); 
	 logger2.debug(now + response);
	 logger2.info("Hello Lamda!!");
	 
	 return response;

	}

}