package com.example.imageupload.data;

public class ImageData {

    private String assetNo;
    private String fileDateTime;
    private String filePath;
    private String fileName;
    private String originalFileName;
    private String fileType;
    private String useYN;
    private String uploadYN;


    public String getAssetNo(){
        return assetNo;
    }
    public void setAssetNo(String assetNo){
        this.assetNo = assetNo;
    }

    public String getFileDateTime(){
        return fileDateTime;
    }
    public void setFileDateTime(String fileDateTime){
        this.fileDateTime = fileDateTime;
    }

    public String getFilePath(){
        return filePath;
    }
    public void setFilePath(String filePath){
        this.filePath = filePath;
    }

    public String getFileName(){
        return fileName;
    }
    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public String getOriginalFileName(){
        return originalFileName;
    }
    public void setOriginalFileName(String originalFileName){
        this.originalFileName = originalFileName;
    }

    public String getFileType(){
        return fileType;
    }
    public void setFileType(String fileType){
        this.fileType = fileType;
    }

    public String getUseYN(){
        return useYN;
    }
    public void setUseYN(String useYN){
        this.useYN = useYN;
    }

    public String getUploadYN(){
        return uploadYN;
    }
    public void setUploadYN(String uploadYN){
        this.uploadYN = uploadYN;
    }

}
