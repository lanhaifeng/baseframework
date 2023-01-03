package com.feng.baseframework.snmp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.PDU;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * baseframework
 * 2021/1/8 16:48
 * snmp结果校验
 *
 * @author lanhaifeng
 * @since
 **/
public class SnmpResponseValidate {

	private static Logger logger = LoggerFactory.getLogger(SnmpResponseValidate.class);

	private static OID usmStatsErrorOidPre = new OID(new int[]{1, 3, 6, 1, 6, 3, 15, 1, 1});

	/**
	 * 2020/12/17 17:23
	 * 校验snmp响应结果
	 *
	 * @param expectOid
	 * @param response
	 * @author lanhaifeng
	 * @return boolean
	 */
	public static boolean validateResponse(String expectOid, PDU response){
		if(Objects.isNull(response)) return false;
		if(response.getErrorStatus() != PDU.noError){
			logger.error("request oid {},response error:{}", expectOid , response.getErrorStatusText());
			return false;
		}

		List<? extends VariableBinding> usmStatsErrorOids = response.getBindingList(usmStatsErrorOidPre);
		if(Objects.nonNull(usmStatsErrorOids) && !usmStatsErrorOids.isEmpty()){
			logger.error("request oid {},usmStatsError error,response message:{} {}"
					, usmStatsErrorOids.stream().map(VariableBinding::getOid).collect(Collectors.toList()),
					usmStatsErrorOids.stream().map(variableBinding->variableBinding.getVariable().getSyntaxString()).collect(Collectors.toList()));
			return false;
		}

		List<? extends VariableBinding> allVariableBindings = response.getVariableBindings();
		boolean result = true;
		if(Objects.nonNull(allVariableBindings) && !allVariableBindings.isEmpty()){
			for (VariableBinding variableBinding : allVariableBindings) {
				if(result){
					result = validateVariable(expectOid, variableBinding);
				}
			}
		}


		return result;
	}

	/**
	 * 2020/12/17 17:24
	 * 校验walk响应
	 *
	 * @param targetOID
	 * @param pdu
	 * @param vb
	 * @author lanhaifeng
	 * @return boolean
	 */
	public static boolean checkWalkFinished(OID targetOID, PDU pdu, VariableBinding vb) {
		boolean finished = false;
		if (pdu.getErrorStatus() != 0) {
			logger.error("[true] responsePDU.getErrorStatus() != 0 ");
			logger.error(pdu.getErrorStatusText());
			finished = true;
		} else if (vb.getOid() == null) {
			logger.error("[true] vb.getOid() == null");
			finished = true;
		} else if (vb.getOid().size() < targetOID.size()) {
			logger.error("[true] vb.getOid().size() < targetOID.size()");
			finished = true;
		} else if (targetOID.leftMostCompare(targetOID.size(), vb.getOid()) != 0) {
			//log.debug("[true] targetOID.leftMostCompare() != 0");
			finished = true;
		} else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
			logger.error("[true] Null.isExceptionSyntax(vb.getVariable().getSyntax())");
			finished = true;
		} else if (vb.getOid().compareTo(targetOID) <= 0) {
			logger.info(vb.toString() + " <= " + targetOID);
			finished = true;
		} else if(vb.getVariable().toString().equals("noSuchObject")){
			logger.error(targetOID.toString() + " noSuchObject ");
			finished = true;
		}
		return finished;
	}

	/**
	 * 2020/12/17 17:22
	 * 验证VariableBinding
	 *
	 * @param expectOid
	 * @param variableBinding
	 * @author lanhaifeng
	 * @return boolean
	 */
	private static boolean validateVariable(String expectOid, VariableBinding variableBinding){
		if(Objects.isNull(variableBinding)) return false;
		if (Null.isExceptionSyntax(variableBinding.getVariable().getSyntax())) {
			logger.error("request oid {},response is NULL,response message:{} {}"
					, variableBinding.getOid(), variableBinding.getOid(), variableBinding.getVariable().getSyntaxString());
			return false;
		}
		if(variableBinding.getVariable().toString().equals("noSuchObject")){
			logger.error("request oid {},Error is noSuchObject,response message OID:{} {}"
					, expectOid , variableBinding.getOid(), variableBinding.getVariable().getSyntaxString());
			return false;
		}

		return true;
	}
}
