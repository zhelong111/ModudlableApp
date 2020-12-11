package network.retrofit.api;

import java.lang.reflect.Type;

public interface IResponse<D> {
        void success(D data);

        void failure(Throwable t);

        /**
         * 返回JavaBean的class 对象 return YouBean.class
         * 以此来确定要转换的类型
         * @return
         */
        Type getDataType();
}