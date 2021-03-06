package com.amazonaws.lambda.demo.guide;
 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

 
import com.amazonaws.lambda.demo.handler.MgMemberHandler;
import com.amazonaws.lambda.demo.table.MgMemberTable;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class DynamoDBMapperQueryScanExample {

	static final Logger logger = LogManager.getLogger(DynamoDBMapperQueryScanExample.class);

	private DynamoDBMapper dynamoDBMapper;
 
	public DynamoDBMapperQueryScanExample(AmazonDynamoDB client) {
 
		dynamoDBMapper = new DynamoDBMapper(client);
 
	}


    public void getMember() {
    	try {
		
//    		DynamoDBMapper mapper = new DynamoDBMapper(client);

    		
    		//scan?????? ??????
//    		Map<String, AttributeValue> values = new HashMap<String, AttributeValue>();
//    		values.put(":v1", new AttributeValue().withS("19"));
//    		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression("begins_with(birth_date, :v1)")
//    				.withExpressionAttributeValues(values);
//    		List<MgMemberTable> result = dynamoDBMapper.scan(MgMemberTable.class, scanExpression);
//    		
//    		logger.info("birth? " + result.get(0).getBirth_date());
//    		logger.info("result size ? " + result.size());
    		
    		//query??? ??????--query????????? partition key?????? ????????? ????????????
//    		Map<String, AttributeValue> values = new HashMap<String, AttributeValue>();
//    		values.put(":v1", new AttributeValue().withS("mg17")); //????????? ?????? ??????
//    		values.put(":v2", new AttributeValue().withS("mi"));  //?????? ?????????????????? ?????? ??????????????? ??????
//    		DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
//    				.withKeyConditionExpression("id = :v1") //????????? ??? = ????????? ???????????? :v1 ?????? ???
//					.withFilterExpression("begins_with(mg_name, :v2)")//??????????????? ?????????. 
//    				.withExpressionAttributeValues(values);//????????? ?????? ?????? ?????? ?????? ??????
//    		List<MgMemberTable> result = dynamoDBMapper.query(MgMemberTable.class, queryExpression);
//    		
//    		logger.info("mg_name? " + result.get(0).getMgName());//get(0)??? ????????? ????????? ???????????? ????????? 
//    		logger.info("result size ? " + result.size());//????????? ????????????.


    		//Global Secondary Index -scan?????? ??????
//    		Map<String, AttributeValue> values = new HashMap<String, AttributeValue>();
//    		values.put(":v1", new AttributeValue().withS("1990"));
//    		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
//    				.withIndexName("MG_MEMBER_GSI_1")
//    				.withFilterExpression("birth_date >= :v1")
//    				.withExpressionAttributeValues(values);
//    		List<MgMemberTable> result = dynamoDBMapper.scan(MgMemberTable.class, scanExpression);
//    		
////    		logger.info("birth_date? " + result.get(0).getBirth_date());
//    		logger.info("birth_date? " + result.get(12).getBirth_date());
//    		logger.info("result size ? " + result.size());   		

    		//Global Secondary Index - query??? ??????--query????????? partition key??? ???????????????????????? ????????????
    		HashMap<String, AttributeValue> values = new HashMap<String, AttributeValue>();
    		values.put(":v1",  new AttributeValue().withS("miura"));
    		values.put(":v2",  new AttributeValue().withS("19"));

    		DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
    		    .withIndexName("MG_MEMBER_GSI_1")
    		    .withConsistentRead(false)
    		    .withKeyConditionExpression("mg_name = :v1 and begins_with(birth_date, :v2)")
    		    .withExpressionAttributeValues(values);

    		List<MgMemberTable> result =  dynamoDBMapper.query(MgMemberTable.class, queryExpression);
    		logger.info("mg_name? " + result.get(0).getMgName());//get(0)??? ????????? ????????? ???????????? ????????? 
    		logger.info("birth_date? " + result.get(0).getBirth_date());//get(0)??? ????????? ????????? ???????????? ????????? 
    		logger.info("result size ? " + result.size());//????????? ????????????.


        }
        catch (Throwable t) {
            System.err.println("Error running the DynamoDBMapperQueryScanExample: " + t);
            t.printStackTrace();
        }
    }

    

}//END class