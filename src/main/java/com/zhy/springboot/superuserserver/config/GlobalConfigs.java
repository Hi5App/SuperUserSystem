package com.zhy.springboot.superuserserver.config;

import lombok.Getter;
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
@Getter
@Component
@ConfigurationProperties(prefix="globalconfig")
public class GlobalConfigs {
    private String urlForGetBBImage;
    private String urlForGetBBSwc;
    private String urlForGetImageList;
    private String urlForCrossingModel;
    private String urlForMissingModel;
    private String urlForBranchingModel;

    private String mainPath;
    private String cropImageBin;
    private String dataPath;
    private String tmpDir;
    private String imageDir;
    private int cropprocess;
    private String savePathForPredict;
    private int[] tipPatchSize;
    private int[] branchingPatchSize;
    private int[] crossingPatchSize;

    private String username;
    private String password;

    public void setTmpDir(String tmpDir) {
        this.tmpDir = tmpDir;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrlForGetBBImage(String urlForGetBBimage) {
        this.urlForGetBBImage = urlForGetBBimage;
    }

    public void setUrlForGetBBSwc(String urlForGetBBSwc) {
        this.urlForGetBBSwc = urlForGetBBSwc;
    }

    public void setUrlForGetImageList(String urlForGetImageList) {
        this.urlForGetImageList = urlForGetImageList;
    }

    public void setTipPatchSize(int[] patchSize) {
        this.tipPatchSize = new int[patchSize.length];
        System.arraycopy(patchSize, 0, this.tipPatchSize, 0, patchSize.length);
    }

    public void setBranchingPatchSize(int[] branchingPatchSize) {
        this.branchingPatchSize = new int[branchingPatchSize.length];
        System.arraycopy(branchingPatchSize, 0, this.branchingPatchSize, 0, branchingPatchSize.length);
    }

    public void setCrossingPatchSize(int[] patchSize) {
        this.crossingPatchSize = new int[patchSize.length];
        System.arraycopy(patchSize, 0, this.crossingPatchSize, 0, patchSize.length);
    }

    public void setSavePathForPredict(String savePathForPredict) {
        this.savePathForPredict = savePathForPredict;
    }

    public void setUrlForCrossingModel(String urlForCrossingModel) {
        this.urlForCrossingModel = urlForCrossingModel;
    }

    public void setUrlForMissingModel(String urlForMissingModel) {
        this.urlForMissingModel = urlForMissingModel;
    }

    public void setUrlForBranchingModel(String urlForBranchingModel) {
        this.urlForBranchingModel = urlForBranchingModel;
    }

    public void setMainPath(String mainpath) {
        mainPath = mainpath;
    }

    public void setCropImageBin(String cropimagebin) {
        cropImageBin = cropimagebin;
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

    public void setImageDir(String imagedir) {
        imageDir = imagedir;
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
                ", urlForBranchingModel='" + urlForBranchingModel + '\'' +
                ", mainPath='" + mainPath + '\'' +
                ", cropImageBin='" + cropImageBin + '\'' +
                ", dataPath='" + dataPath + '\'' +
                ", tmpDir='" + tmpDir + '\'' +
                ", imageDir='" + imageDir + '\'' +
                ", cropprocess=" + cropprocess +
                ", savePathForPredict='" + savePathForPredict + '\'' +
                ", tipPatchSize=" + Arrays.toString(tipPatchSize) +
                ", branchingPatchSize=" + Arrays.toString(branchingPatchSize) +
                ", crossingPatchSize=" + Arrays.toString(crossingPatchSize) +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
