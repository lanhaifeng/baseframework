package com.feng.baseframework.controller;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.common.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @since 1.0
 **/
@RestController
public class SolrController {

	private Logger logger = LoggerFactory.getLogger(getClass());

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
		} catch (SolrServerException e1) {
			logger.error(ExceptionUtils.getFullStackTrace(e1));
		} catch (IOException e2) {
			logger.error(ExceptionUtils.getFullStackTrace(e2));
		}

		return solrDocument == null ? "" : solrDocument.toString();
	}
}
