package ly.common;

public enum  RoleEnum {
    ORDIN_USER(0,"普通用户"),
    ADMIN(3 , "管理员"),
    SUPER_ADMIN(5,"超级管理员");
    private final Integer role ;
    private final String desc ;
    RoleEnum(Integer role , String desc)
    {
        this.role = role ;
        this.desc = desc ;
    }
}
