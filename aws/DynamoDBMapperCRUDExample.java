package com.amazonaws.lambda.demo.service;

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

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.lambda.demo.handler.MgMemberHandler;
import com.amazonaws.lambda.demo.table.MgMemberTable;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

public class DynamoDBMapperCRUDExample {
	static final Logger logger = LogManager.getLogger(MgMemberHandler.class);
    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();

    
    public void testCRUDOperations() {//정상적으로 1건 삭제 됨.
    	
    	MgMemberTable item = new MgMemberTable();
        item.setId("mg15");

        // Save the item (MgMember).
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        mapper.save(item);

        // Retrieve the updated item.
        DynamoDBMapperConfig config = DynamoDBMapperConfig.builder()
            .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
        .build();
        MgMemberTable updatedItem = mapper.load(MgMemberTable.class, "mg15", config);
        
        // Delete the item.
        mapper.delete(updatedItem);

        // Try to retrieve deleted item.
        MgMemberTable deletedItem = mapper.load(MgMemberTable.class, updatedItem.getId(), config);
        if (deletedItem == null) {
            logger.info("success! delete!!");
        }
    }
}