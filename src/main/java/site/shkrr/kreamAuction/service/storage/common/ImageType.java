package site.shkrr.kreamAuction.service.storage.common;

import lombok.Getter;

@Getter
public enum ImageType {
    BRAND("brandImgDir"),PRODUCT("productImgDir");
    private String dirPath;
     ImageType(String dirPath){
        this.dirPath=dirPath;
    }
}
