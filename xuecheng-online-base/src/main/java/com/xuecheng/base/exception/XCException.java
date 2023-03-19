package com.xuecheng.base.exception;


/**
 * @description 学成在线项目异常类
 */
public class XCException extends RuntimeException {

    private String errMessage;

    public XCException() {
        super();
    }

    public XCException(String errMessage) {
        super(errMessage);
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public static void cast(CommonError commonError) {
        throw new XCException(commonError.getErrMessage());
    }

    public static void cast(String errMessage) {
        throw new XCException(errMessage);
    }

}
