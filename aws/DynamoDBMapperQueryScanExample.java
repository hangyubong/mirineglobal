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
    		
    		//scan으로 취득
//    		Map<String, AttributeValue> values = new HashMap<String, AttributeValue>();
//    		values.put(":v1", new AttributeValue().withS("19"));
//    		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression("begins_with(birth_date, :v1)")
//    				.withExpressionAttributeValues(values);
//    		List<MgMemberTable> result = dynamoDBMapper.scan(MgMemberTable.class, scanExpression);
//    		
//    		logger.info("birth? " + result.get(0).getBirth_date());
//    		logger.info("result size ? " + result.size());
    		
    		//query로 취득--query취득은 partition key먼저 입력후 찾아야함
//    		Map<String, AttributeValue> values = new HashMap<String, AttributeValue>();
//    		values.put(":v1", new AttributeValue().withS("mg17")); //파티션 키의 키값
//    		values.put(":v2", new AttributeValue().withS("mi"));  //위의 파티션키값에 속한 속성내에서 취득
//    		DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
//    				.withKeyConditionExpression("id = :v1") //파티션 키 = 별도로 담기위해 :v1 으로 뺌
//					.withFilterExpression("begins_with(mg_name, :v2)")//어디서부터 찾을지. 
//    				.withExpressionAttributeValues(values);//별도로 담은 변수 값을 넣어 찾음
//    		List<MgMemberTable> result = dynamoDBMapper.query(MgMemberTable.class, queryExpression);
//    		
//    		logger.info("mg_name? " + result.get(0).getMgName());//get(0)은 목록의 첫번째 데이터를 가리킴 
//    		logger.info("result size ? " + result.size());//결과가 몇개인지.

    		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    		DynamoDB dynamoDB = new DynamoDB(client);

    		Table table = dynamoDB.getTable("MG_MEMBER");
    		Index index = table.getIndex("MG_MEMBER_GSI_1");

    		QuerySpec spec = new QuerySpec()
    		    .withKeyConditionExpression("id = :v")
    		    .withNameMap(new NameMap()
    		        .with("id", "mg_name"))
    		    .withValueMap(new ValueMap()
    		        .withString(":v","mi"));
    		MgMemberTable member = new MgMemberTable();
    		ItemCollection<QueryOutcome> items = index.query(spec);
    		Iterator<Item> iter = items.iterator(); 
    		while (iter.hasNext()) {
    			logger.info("mg_name? " + member.getMgName());
    		}  		

        }
        catch (Throwable t) {
            System.err.println("Error running the DynamoDBMapperQueryScanExample: " + t);
            t.printStackTrace();
        }
    }


    
    
}//END class

