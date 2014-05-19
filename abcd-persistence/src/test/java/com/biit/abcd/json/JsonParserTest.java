package com.biit.abcd.json;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class JsonParserTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DIAGRAM_IN_JSON = "'{\"cells\":[{\"type\":\"biit.SourceNode\",\"tooltip\":\"Source Tooltip\",\"size\":{\"width\":30,\"height\":30},\"position\":{\"x\":328,\"y\":470},\"angle\":0,\"id\":\"a052d3a6-007c-4057-a789-c7aa19008b0f\",\"embeds\":\"\",\"z\":1,\"attrs\":{}},{\"type\":\"biit.RuleNode\",\"tooltip\":\"Rule Tooltip\",\"size\":{\"width\":30,\"height\":30},\"position\":{\"x\":490,\"y\":418},\"angle\":0,\"id\":\"31db56a7-53de-4e38-acf8-98ee300a20a1\",\"embeds\":\"\",\"z\":2,\"attrs\":{}},{\"type\":\"biit.SinkNode\",\"tooltip\":\"Sink Tooltip\",\"size\":{\"width\":30,\"height\":30},\"position\":{\"x\":644,\"y\":472},\"angle\":0,\"id\":\"62958d22-23ff-467f-9ad5-f034ac24ad1d\",\"embeds\":\"\",\"z\":3,\"attrs\":{}},{\"type\":\"link\",\"id\":\"aa228ac0-a038-4072-9ea2-45c8a39af388\",\"embeds\":\"\",\"source\":{\"id\":\"a052d3a6-007c-4057-a789-c7aa19008b0f\",\"selector\":\"g:nth-child(1) circle:nth-child(2)   \",\"port\":\"out\"},\"target\":{\"id\":\"31db56a7-53de-4e38-acf8-98ee300a20a1\",\"selector\":\"g:nth-child(1) circle:nth-child(2)   \",\"port\":\"in\"},\"z\":4,\"attrs\":{\".marker-source\":{\"d\":\"M 10 0 L 0 5 L 10 10 z\",\"transform\":\"scale(0.001)\"},\".marker-target\":{\"d\":\"M 10 0 L 0 5 L 10 10 z\"},\".connection\":{\"stroke\":\"black\"}}},{\"type\":\"link\",\"id\":\"5a5dfab6-3682-4d1e-b85c-c6d24de1a555\",\"embeds\":\"\",\"source\":{\"id\":\"31db56a7-53de-4e38-acf8-98ee300a20a1\",\"selector\":\"g:nth-child(1) circle:nth-child(3)   \",\"port\":\"out\"},\"target\":{\"id\":\"62958d22-23ff-467f-9ad5-f034ac24ad1d\",\"selector\":\"g:nth-child(1) circle:nth-child(2)   \",\"port\":\"in\"},\"z\":5,\"attrs\":{\".marker-source\":{\"d\":\"M 10 0 L 0 5 L 10 10 z\",\"transform\":\"scale(0.001)\"},\".marker-target\":{\"d\":\"M 10 0 L 0 5 L 10 10 z\"},\".connection\":{\"stroke\":\"black\"}}}]}'";

	@Test(groups = { "jsonParser" })
	public void convertJsonToDiagram() {

	}
}
