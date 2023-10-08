package com.zhy.springboot.superuserserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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
    private String urlForGetBBImage;
    private String urlForGetBBSwc;
    private String urlForGetImageList;
    private String urlForCrossingModel;
    private String urlForMissingModel;

    private String mainPath;
    private String cropImageBin;
    private String dataPath;
    private String tmpDir;
    private String imageDir;
    private int cropprocess;
    private String savePathForPredict;
    private int[] tipPatchSize;
    private int[] crossingPatchSize;

    private String username;
    private String password;

    public String getTmpDir() {
        return tmpDir;
    }

    public void setTmpDir(String tmpDir) {
        this.tmpDir = tmpDir;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrlForGetBBImage() {
        return urlForGetBBImage;
    }

    public void setUrlForGetBBImage(String urlForGetBBimage) {
        this.urlForGetBBImage = urlForGetBBimage;
    }

    public String getUrlForGetBBSwc() {
        return urlForGetBBSwc;
    }

    public void setUrlForGetBBSwc(String urlForGetBBSwc) {
        this.urlForGetBBSwc = urlForGetBBSwc;
    }

    public String getUrlForGetImageList() {
        return urlForGetImageList;
    }

    public void setUrlForGetImageList(String urlForGetImageList) {
        this.urlForGetImageList = urlForGetImageList;
    }

    public int[] getTipPatchSize() {
        return tipPatchSize;
    }

    public void setTipPatchSize(int[] patchSize) {
        this.tipPatchSize = new int[patchSize.length];
        for(int i=0;i<patchSize.length;i++){
            this.tipPatchSize[i]=patchSize[i];
        }
    }

    public int[] getCrossingPatchSize() {
        return crossingPatchSize;
    }

    public void setCrossingPatchSize(int[] patchSize) {
        this.crossingPatchSize = new int[patchSize.length];
        for(int i=0;i<patchSize.length;i++){
            this.crossingPatchSize[i]=patchSize[i];
        }
    }

    public String getSavePathForPredict() {
        return savePathForPredict;
    }

    public void setSavePathForPredict(String savePathForPredict) {
        this.savePathForPredict = savePathForPredict;
    }

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
                "urlForGetBBImage='" + urlForGetBBImage + '\'' +
                ", urlForGetBBSwc='" + urlForGetBBSwc + '\'' +
                ", urlForGetImageList='" + urlForGetImageList + '\'' +
                ", urlForCrossingModel='" + urlForCrossingModel + '\'' +
                ", urlForMissingModel='" + urlForMissingModel + '\'' +
                ", mainPath='" + mainPath + '\'' +
                ", cropImageBin='" + cropImageBin + '\'' +
                ", dataPath='" + dataPath + '\'' +
                ", tmpDir='" + tmpDir + '\'' +
                ", imageDir='" + imageDir + '\'' +
                ", cropprocess=" + cropprocess +
                ", savePathForPredict='" + savePathForPredict + '\'' +
                ", tipPatchSize=" + Arrays.toString(tipPatchSize) +
                ", crossingPatchSize=" + Arrays.toString(crossingPatchSize) +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
