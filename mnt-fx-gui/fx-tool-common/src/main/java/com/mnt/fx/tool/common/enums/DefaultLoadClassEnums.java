package com.mnt.fx.tool.common.enums;

/**
 * 引入指定的类需要默认导入的class
 * @author cc
 * @date 2018/9/7 10:03
 */
public enum DefaultLoadClassEnums {

    DATE("Date", "java.util.Date"),
    BIG_DECIMAL("BigDecimal", "java.math.BigDecimal"),
    LIST("List", "java.util.List"),

    ;
    private String name;
    private String importClass;

    DefaultLoadClassEnums(String name, String importClass) {
        this.name = name;
        this.importClass = importClass;
    }


    /**
     * 通过名称获取导入的类
     * @param name
     * @return
     */
    public static DefaultLoadClassEnums getByName(String name) {
        if(null == name || "".equals(name)) {
            return null;
        }

        for (DefaultLoadClassEnums currEnum : DefaultLoadClassEnums.values()) {
            if(currEnum.getName().equals(name)) {
                return currEnum;
            }
        }

        return null;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImportClass() {
        return importClass;
    }

    public void setImportClass(String importClass) {
        this.importClass = importClass;
    }
}
