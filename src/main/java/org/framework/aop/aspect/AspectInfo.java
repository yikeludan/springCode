package org.framework.aop.aspect;

import org.omg.CORBA.PUBLIC_MEMBER;

public class AspectInfo {
    private int orderIndex;
    private DefaultAspect defaultAspect;

    public AspectInfo(){
        
    }
    public AspectInfo(int orderIndex,DefaultAspect defaultAspect){
        this.orderIndex = orderIndex;
        this.defaultAspect = defaultAspect;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public DefaultAspect getDefaultAspect() {
        return defaultAspect;
    }

    public void setDefaultAspect(DefaultAspect defaultAspect) {
        this.defaultAspect = defaultAspect;
    }
}
