package caparso.es.completemultispinner.util;

import java.util.List;

/**
 * Created by fernando.galiay on 04/12/2015.
 */
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
