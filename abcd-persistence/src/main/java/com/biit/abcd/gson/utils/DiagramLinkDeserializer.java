package com.biit.abcd.gson.utils;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.Node;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class DiagramLinkDeserializer implements JsonDeserializer<DiagramLink>{

	@Override
	public DiagramLink deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		final JsonObject jsonObject = json.getAsJsonObject();

		DiagramLink diagramLink = new DiagramLink();

		diagramLink.setJointjsId(JsonUtils.getStringValue(jsonObject, "id"));
		diagramLink.setType(JsonUtils.getStringValue(jsonObject, "type"));
		diagramLink.setEmbeds(JsonUtils.getStringValue(jsonObject, "embeds"));
		diagramLink.setZ(JsonUtils.getIntValue(jsonObject, "z"));
		
		JsonElement sourceJson =  jsonObject.get("source");
		if(sourceJson!=null){
			Node source = context.deserialize(sourceJson, Node.class);
			diagramLink.setSource(source);
		}
		
		JsonElement targetJson =  jsonObject.get("target");
		if(targetJson!=null){
			Node target = context.deserialize(targetJson, Node.class);
			diagramLink.setTarget(target);
		}
		
		JsonArray labels = jsonObject.getAsJsonArray("labels");
		if(labels!=null && labels.size()>0){
			JsonObject element = labels.get(0).getAsJsonObject();
			if(element!=null){
				JsonObject attrs = element.getAsJsonObject("attrs");
				if(attrs!=null){
					JsonObject textContainer = attrs.getAsJsonObject("text");
					if(textContainer!=null){
						//Text is text property inside a text object.
						diagramLink.setText(JsonUtils.getStringValue(textContainer, "text"));
					}
				}
			}
		}
		
		diagramLink.setSmooth(JsonUtils.getBooleanValue(jsonObject,"smooth"));
		diagramLink.setManhattan(JsonUtils.getBooleanValue(jsonObject,"manhattan"));
		//Store JsonObjects as strings.
		diagramLink.setVertices(JsonUtils.getJsonStringArray(jsonObject, "vertices"));
		diagramLink.setAttrs(JsonUtils.getJsonString(jsonObject, "attrs"));
		
		return diagramLink;
	}

}
