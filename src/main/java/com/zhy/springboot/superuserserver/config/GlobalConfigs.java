package com.zhy.springboot.superuserserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.Semaphore;

/**
 * @Author zhy
 * @Date 2023/5/10 13:56
 * @Description 全局配置类
 * @Since version-1.0
 */
@Component
@ConfigurationProperties(prefix="globalconfig")
public class GlobalConfigs {
    private String urlForCrossingModel;
    private String urlForMissingModel;

    private String mainPath;
    private String cropImageBin;
    private String dataPath;
    private String tmpDir;
    private String imageDir;
    private int cropprocess;

    public String getUrlForCrossingModel() {
        return urlForCrossingModel;
    }

    public void setUrlForCrossingModel(String urlForCrossingModel) {
        this.urlForCrossingModel = urlForCrossingModel;
    }

    public String getUrlForMissingModel() {
        return urlForMissingModel;
    }

    public void setUrlForMissingModel(String urlForMissingModel) {
        this.urlForMissingModel = urlForMissingModel;
    }

    public String getMainPath() {
        return mainPath;
    }

    public void setMainPath(String mainpath) {
        mainPath = mainpath;
    }

    public String getCropImageBin() {
        return cropImageBin;
    }

    public void setCropImageBin(String cropimagebin) {
        cropImageBin = cropimagebin;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String datapath) {
        dataPath = datapath;
    }

    public String getTmpdir() {
        return tmpDir;
    }

    public void setTmpdir(String tmpdir) {
        tmpDir = tmpdir;
    }

    public String getImageDir() {
        return imageDir;
    }

    public void setImageDir(String imagedir) {
        imageDir = imagedir;
    }

    public int getCropprocess() {
        return cropprocess;
    }

    public void setCropprocess(int cropprocess) {
        this.cropprocess = cropprocess;
    }

    @Override
    public String toString() {
        return "GlobalConfigs{" +
                "urlForCrossingModel='" + urlForCrossingModel + '\'' +
                ", urlForMissingModel='" + urlForMissingModel + '\'' +
                ", mainPath='" + mainPath + '\'' +
                ", cropImageBin='" + cropImageBin + '\'' +
                ", dataPath='" + dataPath + '\'' +
                ", tmpDir='" + tmpDir + '\'' +
                ", imageDir='" + imageDir + '\'' +
                ", cropprocess=" + cropprocess +
                '}';
    }
}
