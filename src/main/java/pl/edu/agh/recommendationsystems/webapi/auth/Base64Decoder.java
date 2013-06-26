package pl.edu.agh.recommendationsystems.webapi.auth;

import javax.xml.bind.DatatypeConverter;

/**
 * Created with IntelliJ IDEA.
 * User: Łukasz Macionczyk
 * Date: 25.06.13
 * Time: 01:24
 * To change this template use File | Settings | File Templates.
 */
public class Base64Decoder {

    public static String[] decode(String auth) {
        auth = auth.replaceFirst("[B|b]asic ", "");
        byte[] decodedBytes = DatatypeConverter.parseBase64Binary(auth);
        if(decodedBytes == null || decodedBytes.length == 0){
            return null;
        }
        return new String(decodedBytes).split(":", 2);
    }

    private Base64Decoder() {}
}
