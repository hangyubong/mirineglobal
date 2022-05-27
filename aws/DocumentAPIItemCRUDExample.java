/**
 * Copyright 2010-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * This file is licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License. A copy of
 * the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
*/


package com.amazonaws.lambda.demo.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.amazonaws.lambda.demo.guide.DynamoDBMapperQueryScanExample;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DocumentAPIItemCRUDExample {
	static final Logger logger = LogManager.getLogger(DynamoDBMapperQueryScanExample.class);
	
    public static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    public static DynamoDB dynamoDB = new DynamoDB(client);

    static String tableName = "MG_MEMBER";

//    public static void main(String[] args) throws IOException {
//
//        createItems();
//
//        retrieveItem();
//
//        // Perform various updates.
//        updateMultipleAttributes();
//        updateAddNewAttribute();
//        updateExistingAttributeConditionally();
//
//        // Delete the item.
//        deleteItem();
//
//    }

    public void createItems() {
        Table table = dynamoDB.getTable(tableName);
        
//        LocalDateTime ldt = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("UTC")); //LocalDateTime보다는 ZoneID를 쓰기때문에 ZonedDateTime쓰도록함.
        DateTimeFormatter fomatter = DateTimeFormatter.ofPattern("yyyy/mm/dd HH:mm:ss");
//        String nowDate = fomatter.format(ldt);
        String nowDate2 = fomatter.format(zdt);
        
        try {

            Item item = new Item().withPrimaryKey("id", "mg13").withString("mg_name", "Jeny")
                .withString("birth_date", "1992/09/06")
                .withString("email_address", "Jeny@gmail.com")
                .withString("created_at", nowDate2)
                .withString("updated_at", nowDate2)
                .withString("insert_user", "insert-admin")
                .withString("updated_user", "insert-admin")
                .withNumber("version", 13);
            table.putItem(item);

            item = new Item().withPrimaryKey("id", "mg14").withString("mg_name", "clock")
                    .withString("birth_date", "1987/12/26")
                    .withString("email_address", "clock@gmail.com")
                    .withString("created_at", nowDate2)
                    .withString("updated_at", nowDate2)
                    .withString("insert_user", "insert-admin")
                    .withString("updated_user", "insert-admin")
                    .withNumber("version", 14);
                table.putItem(item);
                

        }
        catch (Exception e) {
            System.err.println("Create items failed.");
            System.err.println(e.getMessage());
            
        }
    }

//    private static void retrieveItem() {
//        Table table = dynamoDB.getTable(tableName);
//
//        try {
//
//            Item item = table.getItem("Id", 120, "Id, ISBN, Title, Authors", null);
//
//            System.out.println("Printing item after retrieving it....");
//            System.out.println(item.toJSONPretty());
//
//        }
//        catch (Exception e) {
//            System.err.println("GetItem failed.");
//            System.err.println(e.getMessage());
//        }
//
//    }
//
//    private static void updateAddNewAttribute() {
//        Table table = dynamoDB.getTable(tableName);
//
//        try {
//
//            UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("Id", 121)
//                .withUpdateExpression("set #na = :val1").withNameMap(new NameMap().with("#na", "NewAttribute"))
//                .withValueMap(new ValueMap().withString(":val1", "Some value")).withReturnValues(ReturnValue.ALL_NEW);
//
//            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
//
//            // Check the response.
//            System.out.println("Printing item after adding new attribute...");
//            System.out.println(outcome.getItem().toJSONPretty());
//
//        }
//        catch (Exception e) {
//            System.err.println("Failed to add new attribute in " + tableName);
//            System.err.println(e.getMessage());
//        }
//    }
//
//    private static void updateMultipleAttributes() {
//
//        Table table = dynamoDB.getTable(tableName);
//
//        try {
//
//            UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("Id", 120)
//                .withUpdateExpression("add #a :val1 set #na=:val2")
//                .withNameMap(new NameMap().with("#a", "Authors").with("#na", "NewAttribute"))
//                .withValueMap(
//                    new ValueMap().withStringSet(":val1", "Author YY", "Author ZZ").withString(":val2", "someValue"))
//                .withReturnValues(ReturnValue.ALL_NEW);
//
//            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
//
//            // Check the response.
//            System.out.println("Printing item after multiple attribute update...");
//            System.out.println(outcome.getItem().toJSONPretty());
//
//        }
//        catch (Exception e) {
//            System.err.println("Failed to update multiple attributes in " + tableName);
//            System.err.println(e.getMessage());
//
//        }
//    }
//
//    private static void updateExistingAttributeConditionally() {
//
//        Table table = dynamoDB.getTable(tableName);
//
//        try {
//
//            // Specify the desired price (25.00) and also the condition (price =
//            // 20.00)
//
//            UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("Id", 120)
//                .withReturnValues(ReturnValue.ALL_NEW).withUpdateExpression("set #p = :val1")
//                .withConditionExpression("#p = :val2").withNameMap(new NameMap().with("#p", "Price"))
//                .withValueMap(new ValueMap().withNumber(":val1", 25).withNumber(":val2", 20));
//
//            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
//
//            // Check the response.
//            System.out.println("Printing item after conditional update to new attribute...");
//            System.out.println(outcome.getItem().toJSONPretty());
//
//        }
//        catch (Exception e) {
//            System.err.println("Error updating item in " + tableName);
//            System.err.println(e.getMessage());
//        }
//    }
//
    
//    public void deleteItem() {//수정필요.
//
//        Table table = dynamoDB.getTable("MG_MEMBER");
//        try {
//
//            DeleteItemSpec deleteItemSpec = new DeleteItemSpec().withPrimaryKey("id", "mg16")
//                .withConditionExpression("mg_name = :val").withNameMap(new NameMap().with("mg_name", "山田　太郎"))
//                .withValueMap(new ValueMap().withBoolean("山田　太郎", false)).withReturnValues(ReturnValue.ALL_OLD);
//
//            DeleteItemOutcome outcome = table.deleteItem(deleteItemSpec);
//
//            // Check the response.
//            logger.info(outcome.getItem().toJSONPretty());
//        }
//        
//        catch (Exception e) {
//            System.err.println("Error deleting item in " + tableName);
//            System.err.println(e.getMessage());
//        }
//    }
    
}//END class

