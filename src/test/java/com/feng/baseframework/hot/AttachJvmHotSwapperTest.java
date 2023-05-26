package com.feng.baseframework.hot;

import com.feng.baseframework.constant.ResultEnum;
import com.feng.baseframework.exception.Assert;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.util.List;
import java.util.Objects;

/**
 * 远程部署测试
 *
 * @author lanhaifeng
 * @version v2.0
 * @apiNote 时间:2023/5/15 16:09创建:RemoteHotTest
 * @since v2.0
 */
public class AttachJvmHotSwapperTest {
    public static void main(String[] args) throws Exception {
        List<VirtualMachineDescriptor> vms = VirtualMachine.list();
        String targetName = "baseframework-2.0.0.jar";
        String agentJar = "E:\\core\\baseframework\\myagent\\target\\myagent-2.0.0.jar";
        String targetClass = "com.feng.baseframework.controller.BaseController";
        VirtualMachine targetMachine = null;
        for (VirtualMachineDescriptor vm : vms) {
            if(vm.displayName().equals(targetName)) {
                targetMachine = vm.provider().attachVirtualMachine(vm);
                break;
            }
        }
        Assert.state(Objects.nonNull(targetMachine), com.feng.baseframework.exception.BusinessException::new, ResultEnum.ATTACH_JVM_ERROR);
        targetMachine.loadAgent(agentJar, targetClass);
        targetMachine.detach();
    }
}
