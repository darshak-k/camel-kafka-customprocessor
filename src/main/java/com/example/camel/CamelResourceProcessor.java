/*-
* #%L
* Smile CDR - CDR
* %%
* Copyright (C) 2016 - 2024 Smile CDR, Inc.
* %%
* All rights reserved.
* #L%
*/
package com.example.camel;

import java.util.Map;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CamelResourceProcessor implements Processor {
	private static final Logger log = LoggerFactory.getLogger(CamelResourceProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {

		Message message = exchange.getIn();
		String messageString = message.getBody(String.class);

		// Step 1: Parse the full JSON from the message
		JsonObject jsonObject = JsonParser.parseString(messageString).getAsJsonObject();

		// Step 2: Extract the "payload" field from the top-level JSON
		JsonElement payloadElement = jsonObject.get("payload");

		if (payloadElement != null && payloadElement.isJsonObject()) {
			JsonObject payloadObject = payloadElement.getAsJsonObject();

			// Step 3: Extract the inner "payload" field (which is a JSON string)
			String innerPayloadString = payloadObject.get("payload").getAsString();

			// Step 4: Parse the inner JSON string into a JsonObject
			JsonObject innerPayloadObject = JsonParser.parseString(innerPayloadString).getAsJsonObject();

			// Step 5: Sanitize the keys of the inner payload
			JsonObject sanitizedInnerPayload = sanitizeJsonKeys(innerPayloadObject);

			// Replace the inner payload with the sanitized one
			payloadObject.add("payload", sanitizedInnerPayload);

			// Step 6: Convert the modified JsonObject back to a JSON string
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String sanitizedJsonData = gson.toJson(innerPayloadObject);

			// Output the sanitized JSON
			message.setBody(sanitizedJsonData);
		}
	}

	private static JsonObject sanitizeJsonKeys(JsonObject jsonObject) {
		JsonObject sanitizedObject = new JsonObject();

		// Iterate through the JSON object keys
		Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
		for (Map.Entry<String, JsonElement> entry : entries) {
			String sanitizedKey = entry.getKey().replace("\\", ""); // Remove backslashes from the key
			JsonElement value = entry.getValue();

			// Recursively handle nested objects or arrays
			if (value.isJsonObject()) {
				value = sanitizeJsonKeys(value.getAsJsonObject());
			} else if (value.isJsonArray()) {
				value = sanitizeJsonArray(value.getAsJsonArray());
			}

			sanitizedObject.add(sanitizedKey, value); // Add sanitized key-value pair to the new object
		}

		return sanitizedObject;
	}

	private static JsonArray sanitizeJsonArray(JsonArray jsonArray) {
		JsonArray sanitizedArray = new JsonArray();

		// Iterate through the array and sanitize nested objects/arrays
		for (JsonElement element : jsonArray) {
			if (element.isJsonObject()) {
				sanitizedArray.add(sanitizeJsonKeys(element.getAsJsonObject()));
			} else if (element.isJsonArray()) {
				sanitizedArray.add(sanitizeJsonArray(element.getAsJsonArray()));
			} else {
				sanitizedArray.add(element); // Directly add non-object/array elements
			}
		}

		return sanitizedArray;
	}
}