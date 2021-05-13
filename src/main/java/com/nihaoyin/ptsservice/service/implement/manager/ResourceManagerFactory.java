package com.nihaoyin.ptsservice.service.implement.manager;

import com.nihaoyin.ptsservice.service.interfaces.manager.ResourceManager;

final public class ResourceManagerFactory {
    private static ResourceManager resourceManager;
    static{
        try {
            resourceManager = new ResourceManagerImpl();
            resourceManager.init();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public static ResourceManager getResourceManager(){
        return resourceManager;
    }
}
