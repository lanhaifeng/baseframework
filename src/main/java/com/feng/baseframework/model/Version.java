package com.feng.baseframework.model;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.io.Serializable;

/**
 * 版本信息
 *
 * @author lanhaifeng
 * @version 1.0
 * @apiNote 时间:2022/4/24 16:39创建:Version
 * @since 1.0
 */
public class Version implements Serializable {
    private static final long serialVersionUID = 2115904208056903869L;

    /**
     * 版本
     */
    private String version;

    /**
     * 项目名
     */
    private String name;

    /**
     * 提交单号
     */
    private String commit;

    /**
     * 构建时间
     */
    private String buildTime;

    /**
     * 分支
     */
    private String branch;

    /**
     * 构建jdk版本
     */
    private String buildVersion;

    @Override
    public String toString() {
        return "Version{" +
                "version='" + version + '\'' +
                ", name='" + name + '\'' +
                ", commit='" + commit + '\'' +
                ", buildTime='" + buildTime + '\'' +
                ", branch='" + branch + '\'' +
                ", buildVersion='" + buildVersion + '\'' +
                '}';
    }

    public String getVersion() {
        return version;
    }

    @JsonSetter("Implementation-Version")
    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    @JsonSetter("Implementation-Title")
    public void setName(String name) {
        this.name = name;
    }

    public String getCommit() {
        return commit;
    }

    @JsonSetter("Implementation-Build")
    public void setCommit(String commit) {
        this.commit = commit;
    }

    public String getBuildTime() {
        return buildTime;
    }

    @JsonSetter("Implementation-BuildTime")
    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    public String getBranch() {
        return branch;
    }

    @JsonSetter("Implementation-Branch")
    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBuildVersion() {
        return buildVersion;
    }

    @JsonSetter("Build-Jdk")
    public void setBuildVersion(String buildVersion) {
        this.buildVersion = buildVersion;
    }
}
