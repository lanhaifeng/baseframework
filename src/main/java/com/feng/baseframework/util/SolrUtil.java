package com.feng.baseframework.util;

import com.feng.baseframework.model.SolrUpdateLog;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.CoreAdminParams;
import org.apache.solr.common.params.CursorMarkParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * baseframework
 * 2018/12/24 10:42
 * solr工具操作类
 *
 * @author lanhaifeng
 * @since
 **/
public class SolrUtil {

	private static final Logger logger = LoggerFactory.getLogger(SolrUtil.class);
	public static SolrClient solrClient = SpringUtil.getBeanByType(SolrClient.class);

	public static boolean addDoc(String collection, SolrInputDocument doc){
		List<SolrInputDocument> docs = new ArrayList<>();
		docs.add(doc);
		return addDocs(collection, docs);
	}

	public static boolean addDocs(String collection, Collection<SolrInputDocument> docs){
		Boolean result = true;
		try {
			solrClient.add(collection, docs);
			solrClient.commit(collection);
		} catch (Exception e) {
			logger.error("新增索引失败," + ExceptionUtils.getFullStackTrace(e));
			result = false;
		}

		return result;
	}

	public static boolean updateIndex(SolrUpdateLog solrUpdateLog){
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", solrUpdateLog.getAuditId());
		List<SolrUpdateLog.UpdateField> fields = solrUpdateLog.getFields();
		for (SolrUpdateLog.UpdateField field : fields) {
			doc.addField(field.getField(),
					field.getUpdateAtomicVal());
		}

		return addDoc(solrUpdateLog.getCollection(), doc);
	}

	public static boolean updateIndexs(String solrName, List<SolrUpdateLog> solrUpdateLogs){
		SolrInputDocument doc;
		List<SolrInputDocument> docs = new ArrayList<>();
		for (SolrUpdateLog solrUpdateLog : solrUpdateLogs) {
			doc = new SolrInputDocument();
			doc.addField("id", solrUpdateLog.getAuditId());
			List<SolrUpdateLog.UpdateField> fields = solrUpdateLog.getFields();
			for (SolrUpdateLog.UpdateField field : fields) {
				doc.addField(field.getField(),
						field.getUpdateAtomicVal());
			}
			docs.add(doc);
		}
		return addDocs(solrName, docs);
	}

	public static SolrDocument getDoc(String collection, String id){
		try {
			return solrClient.getById(collection, id);
		} catch (Exception e) {
			logger.error("获取索引文档失败,{}", ExceptionUtils.getFullStackTrace(e));
			return null;
		}
	}

	public static QueryResponse getDocs(String collection, SolrQuery params){
		try{
			return solrClient.query(collection, params);
		}catch(Exception e){
		    logger.error("根据条件获取索引文档失败" + ExceptionUtils.getFullStackTrace(e));
		    return null;
		}
	}

	public static QueryResponse getDocs(String collection, SolrQuery params, Boolean isCursorMark) {
		if (!Optional.ofNullable(isCursorMark).orElse(false)) {
			return getDocs(collection, params);
		}
		String cursorMark = params.get(CursorMarkParams.CURSOR_MARK_PARAM);
		params.remove("start");
		if(StringUtils.isBlank(cursorMark)){
			cursorMark = CursorMarkParams.CURSOR_MARK_START;
		}
		QueryResponse rsp = null;
		try {
			params.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);//变化游标条件
			rsp = solrClient.query(collection, params);//执行多次查询读取
			return rsp;
		} catch (Exception e) {
			logger.error("使用游标根据条件获取索引文档失败" + ExceptionUtils.getFullStackTrace(e));
			return null;
		}
	}

	/**
	 * 查看core是否挂载
	 * @param coreName
	 * @return
	 */
	public static boolean existCore(String coreName) throws IOException, SolrServerException {
		NamedList coreStatus = getCoreStatusByName(coreName);
		if(coreStatus == null || coreStatus.size() == 0){
			return false;
		}
		return true;
	}

	public static NamedList getCoreStatusByName(String coreName) throws IOException, SolrServerException {
		return getCoreStatusByName(coreName, false);
	}

	/**
	 * solr/admin 原生API
	 * 查看core的状态
	 * @param coreName
	 * @param indexInfoNeeded  true 会查询index的详细信息，强压力情况下indexing core会被锁牢，调用该接口会卡死;
	 *                          false 则没该问题;
	 * @return
	 * @throws IOException
	 * @throws SolrServerException
	 */
	public static NamedList getCoreStatusByName(String coreName, boolean indexInfoNeeded) throws IOException, SolrServerException {
		NamedList<Object> result = getAllCoreStatus(indexInfoNeeded);
		SimpleOrderedMap map = (SimpleOrderedMap) result.get(coreName);
		return map;
	}

	/**
	 * 获得所有挂载core的信息
	 * @param indexInfoNeeded true 会查询index的详细信息，强压力情况下indexing core会被锁牢，调用该接口会卡死；
	 *                         false 则没该问题
	 * @return
	 * @throws IOException
	 * @throws SolrServerException
	 */
	public static NamedList getAllCoreStatus(boolean indexInfoNeeded) throws IOException, SolrServerException {
		CoreAdminRequest request = new CoreAdminRequest();
		request.setAction(CoreAdminParams.CoreAdminAction.STATUS);
		request.setIndexInfoNeeded(indexInfoNeeded);
		CoreAdminResponse response = request.process(solrClient);
		logger.info("-- getAllCoreStatus - cost:" + response.getElapsedTime() + "ms");
		NamedList namedList = response.getCoreStatus();

		return namedList;
	}

	public static Boolean loadCore(String collection) {
		Boolean result = true;
		try {
			if (!existCore(collection)) {
				CoreAdminRequest.createCore(collection, collection, solrClient);
			}
		} catch (Exception e) {
			logger.error("加载" + collection + "失败：" + ExceptionUtils.getFullStackTrace(e));
			result = false;
		}

		return result;
	}

	public static Boolean unloadCore(String collection){
		return unloadCore(collection , false);
	}

	public static Boolean unloadCore(String collection, boolean deleteIndex) {
		Boolean result = true;
		if(StringUtils.isBlank(collection)){
			return false;
		}
		try {
			CoreAdminResponse coreAdminResponse = CoreAdminRequest.getStatus(collection, solrClient);
			if (coreAdminResponse == null || coreAdminResponse.getCoreStatus(collection) == null || coreAdminResponse.getCoreStatus(collection).size() < 1) {
				CoreAdminRequest.unloadCore(collection, deleteIndex, solrClient);
			}
		} catch (Exception e) {
			logger.error("加载" + collection + "失败：" + ExceptionUtils.getFullStackTrace(e));
			result = false;
		}

		return result;
	}

	/**
	 * 2021/4/9 11:15
	 * 关闭连接
	 *
	 * @param
	 * @author lanhaifeng
	 * @return void
	 */
	public static void close() throws IOException {
		solrClient.close();
	}

	/**
	 * 2021/4/12 10:43
	 * ping数据
	 *
	 * @param collection
	 * @author lanhaifeng
	 * @return boolean
	 */
	public static boolean ping(String collection) {
		try {
			CoreAdminResponse coreAdminResponse = CoreAdminRequest.getStatus(collection, solrClient);
			if (coreAdminResponse != null && coreAdminResponse.getCoreStatus(collection) != null
					&& coreAdminResponse.getCoreStatus(collection).size() > 1) {
				return true;
			}
		} catch (Exception e) {
			logger.error("ping错误：" + ExceptionUtils.getFullStackTrace(e));
		}
		return false;
	}
}
