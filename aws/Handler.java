package com.amazonaws.lambda.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Handler implements RequestHandler<Object, String>{
	
Gson gson = new GsonBuilder().setPrettyPrinting().create();
static final Logger logger2 = LogManager.getLogger(LambdaLogSample.class);

@Override
	public String handleRequest(Object event, Context context){
	
	 LocalDate now = LocalDate.now();

	 LambdaLogger logger = context.getLogger();
	 String response = new String("Hello Lamda!!");
	 logger.log(now + " " + response); 
	 logger2.debug(now + response);
	 
	 return response;

	}

}