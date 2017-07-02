package com.jm.staticcode.util;


import java.io.*;

public class SerializeUtil {
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            //序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {

        }
        return null;
    }

    public static <T>T unserialize(byte[] bytes,Class<T> tClass) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = null;
        //反序列化
        bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        T t = (T) ois.readObject();
        return t;
    }
}
