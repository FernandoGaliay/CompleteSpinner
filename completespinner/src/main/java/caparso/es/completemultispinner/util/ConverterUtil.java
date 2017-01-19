package caparso.es.completemultispinner.util;

import java.util.List;

public class ConverterUtil {

    public static <T> String[] convertListToArray(List<T> dataList){
        String[] dataArray = null;
        if(dataList != null){
            dataArray = new String[dataList.size()];
            for (int position = 0; position < dataList.size(); position++) {
                dataArray[position] = dataList.get(position).toString();
            }
        }
        return dataArray;
    }
}
