package com.feng.baseframework.groovy

import com.sun.tools.attach.VirtualMachine

import java.lang.management.ManagementFactory
import java.lang.management.RuntimeMXBean

class MonitorBean {

    static void printBeans() {
        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean()
        String name = bean.getName()
        int index = name.indexOf('@')
        String pid = name.substring(0, index)
        //这里要区分系统：windows下用的是WindowsAttachProvider，linux下用的是LinuxAttachProvider
        //VirtualMachine.attach方法会查找对于的AttachProvider
        VirtualMachine machine = VirtualMachine.attach(pid)
        InputStream is = machine.heapHisto("-all")
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        int readed
        byte[] buff = new byte[1024]
        while ((readed = is.read(buff)) > 0)
            baos.write(buff, 0, readed)
        is.close()
        machine.detach()
        System.out.println(baos)
    }

}
