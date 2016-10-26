package hl.util.mybatis;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ub-06 on 2016-10-25.
 */
public class ConvertIbatis2MybatisXml {
    public static Map<String, String> i2mMap = new HashMap<String, String>();


    public static String replaceIbatis2MybatisXml(File file) throws IOException {
        // #,$ 파라미터 바꿈
        i2mMap.put("#([a-zA-Z0-9_]{1,})#", "#{$1}");
        i2mMap.put("\\$([a-zA-Z0-9_]{1,})\\$", "\\${$1}");
        // resultClass, parameterClass 바꿈
        i2mMap.put("resultClass", "resultType");
        i2mMap.put("parameterClass\\=\"[a-zA-Z0-9.]{1,}\"", "");
        // prepend
        i2mMap.put("(<((isEqual)|(isNotEqual)|(isGreaterThan)|(isNotEmpty))[\\s\\\"a-zA-Z0-9=]{0,}prepend=\"AND\"[\\s\\\"a-zA-Z0-9=]{0,}>\\r\\n\\s{1,})", "$1AND");
        // isEqual, isGreaterThan, isNotEmpty
        i2mMap.put("<isEqual[\\s\\\"a-zA-Z=]{0,}property=\"([\\w]{0,})\"[\\s\\\"a-zA-Z=]{0,}compareValue=\"([\\w]{0,})\"[\\s\\\"a-zA-Z=]{0,}>", "<if test = \"$1 == '$2'\">");
        i2mMap.put("<isGreaterThan[\\s\\\"a-zA-Z=]{0,}property=\"([a-zA-Z0-9]{0,})\"[\\s\\\"a-zA-Z=]{0,}compareValue=\"([a-zA-Z0-9]{0,})\"[\\s\\\"a-zA-Z=]{0,}>", "<if test = \"$1 > $2\">");
        i2mMap.put("<isNotEmpty[\\s\\\"a-zA-Z=]{0,}property=\"([a-zA-Z0-9_]{0,})\"[\\s\\\"a-zA-Z=]{0,}>", "<if test = \"$1 != null and $1 !=''\">");
        i2mMap.put("(</isEqual>)|(</isGreaterThan>)|(</isNotEmpty>)", "</if>");


        String fileContent = FileUtils.readFileToString(file, "UTF-8");

        for(String source : i2mMap.keySet()){
            String destination = i2mMap.get(source);

            fileContent= fileContent.replaceAll(source, destination);
        }

        System.out.println(fileContent);

        return null;
    }

    public static void main(String[] args) throws IOException {
        replaceIbatis2MybatisXml(new File("D:\\Project\\File\\AdminEquipment_SQL.xml"));
    }
}
