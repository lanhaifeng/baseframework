package com.feng.baseframework.util;

import com.feng.baseframework.common.JunitBaseTest;
import com.feng.baseframework.model.SolrUpdateLog;
import io.jsonwebtoken.lang.Assert;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CursorMarkParams;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.*;

@ActiveProfiles("dev")
public class SolrUtilTest extends JunitBaseTest {

	private SolrUpdateLog solrUpdateLog;
	private List<SolrUpdateLog> solrUpdateLogs;
	private String collection;
	private String auditId;
	private String auditIds;
	private SolrQuery solrQuery;
	private String DATEFORMAT_SOLR;

	@Before
	public void setUp() {
		collection = "13";
		auditId = "5f845581-50c3-49c6-837a-947aa3b774ef";
		solrUpdateLogs = new ArrayList<>();
		DATEFORMAT_SOLR = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	}

	@Test
	public void addDoc() {
	}

	@Test
	public void addDocs() {
	}

	@Test
	public void updateIndex() {
		solrUpdateLog = new SolrUpdateLog();
		solrUpdateLog.setAuditId(auditId);
		solrUpdateLog.addFiledAndValues(new SolrUpdateLog.UpdateField("review", "y"));
		solrUpdateLog.addFiledAndValues(new SolrUpdateLog.UpdateField("review_time", DateUtil.dateToString(new Date(), DATEFORMAT_SOLR, null)));
		solrUpdateLog.setCollection(collection);

		Assert.state(SolrUtil.updateIndex(solrUpdateLog), "更新索引失败");
	}

	@Test
	public void updateIndexs() {
		auditIds = "74615947-883a-4f21-abaf-8e1d90037236,f5d94eb0-b635-4ff2-a3d8-f45ae0bd600e,372ae9d1-7d90-4a66-9411-d463732933d0";
		List<String> ids = Arrays.asList(auditIds.split(","));
		for (String id : ids) {
			solrUpdateLog = new SolrUpdateLog();
			solrUpdateLog.setAuditId(id);
			solrUpdateLog.addFiledAndValues(new SolrUpdateLog.UpdateField("review", "y"));
			solrUpdateLog.addFiledAndValues(new SolrUpdateLog.UpdateField("issue_deal_type", "0"));
			solrUpdateLog.addFiledAndValues(new SolrUpdateLog.UpdateField("review_id", "100"));
			solrUpdateLog.addFiledAndValues(new SolrUpdateLog.UpdateField("review_time", DateUtil.dateToString(new Date(), DATEFORMAT_SOLR, null)));
			solrUpdateLog.setCollection(collection);

			solrUpdateLogs.add(solrUpdateLog);
		}


		Assert.state(SolrUtil.updateIndexs(collection, solrUpdateLogs), "更新索引失败");
	}

	@Test
	public void getDoc() {
		SolrDocument doc = SolrUtil.getDoc(collection, auditId);
		System.out.println(doc.toString());
	}

	@Test
	public void ping() throws IOException {
		Assert.state(SolrUtil.ping(collection), "ping失败");
		SolrUtil.close();

		Assert.state(!SolrUtil.ping(collection), "ping失败");
	}

	@Test
	public void getDocs() {
		collection = "1_1547605293500";
		solrQuery = new SolrQuery();
		solrQuery.set("q", "optime:* AND hour:*");//logon_time:[2019-01-17T13:45:00.00Z TO *]
		solrQuery.set("fl", "id,hour,optime");//logon_time,optime
		solrQuery.setStart(0);
		solrQuery.setRows(100);
		solrQuery.addSort("id", SolrQuery.ORDER.desc);

		SolrUtil.loadCore(collection);

		QueryResponse queryResponse = SolrUtil.getDocs(collection, solrQuery, true);
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		String cursorMark = queryResponse.getNextCursorMark();
		List<SolrDocument> docs = solrDocumentList.subList(0, solrDocumentList.size());
		long count = solrDocumentList.getNumFound();
		System.out.println("sum:" + count);
		printTimeResult(docs);
		int loopTime = (int) (count % 10000 == 0 ? count / 10000 : count / 10000 + 1);
		for (int i = 1; i < loopTime; i++) {
			solrQuery.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
			queryResponse = SolrUtil.getDocs(collection, solrQuery, true);
			cursorMark = queryResponse.getNextCursorMark();
			solrDocumentList = queryResponse.getResults();
			printTimeResult(solrDocumentList.subList(0, solrDocumentList.size()));
		}
	}

	private void printTimeResult(List<SolrDocument> docs) {
		String hour = "";
		String id = "";
		Object logonTimeFiled;
		String logonTime = "";
		if (docs != null && !docs.isEmpty()) {
			for (SolrDocument doc : docs) {
				try {
					id = doc.getFieldValue("id").toString();
					hour = doc.getFieldValue("hour").toString();
					logonTimeFiled = doc.getFieldValue("optime");
					logonTime = DateUtil.dateToString((Date) logonTimeFiled, "HHmm", TimeZone.getTimeZone("GMT"));

					if (!hour.equals(logonTime)) {
						System.out.println(id);
						System.out.println(hour);
						System.out.println(logonTime);
					}
				} catch (Exception e) {
					System.out.println(id);
				}
			}
		}
	}
}