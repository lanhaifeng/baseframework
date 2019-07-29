package com.feng.baseframework.controller;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * baseframework
 * 2019/7/26 16:57
 * solr控制器
 *
 * @author lanhaifeng
 * @since
 **/
@RestController
public class SolrController {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private SolrClient solrClient;

	@RequestMapping(value = "/baseManage/solrSearch",method= RequestMethod.GET)
	public String solrSearch() {
		SolrDocument solrDocument = null;
		try {
			CoreAdminResponse coreAdminResponse = CoreAdminRequest.getStatus("2",solrClient);
			logger.info(coreAdminResponse.toString());
			if(coreAdminResponse == null || coreAdminResponse.getCoreStatus("2") == null || coreAdminResponse.getCoreStatus("2").size() < 1){
				CoreAdminRequest.createCore("2","2",solrClient);
			}

			logger.info(coreAdminResponse.toString());

			solrDocument = solrClient.getById("2","1");
		} catch (SolrServerException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}

		return solrDocument == null ? "" : solrDocument.toString();
	}
}
