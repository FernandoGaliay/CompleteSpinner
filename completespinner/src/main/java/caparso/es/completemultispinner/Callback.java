package caparso.es.completemultispinner;

import android.content.DialogInterface;

/**
 * Created by fernando.galiay on 14/07/2015.
 */
public interface Callback<T> {

    /**
     * Invoked when item is selected.
     *
     * @param position
     * @param item
     */
    void onItemSelected(int position, T item);

    /**
     * Invoked when accept button is pressed.
     *
     * @param dialog
     */
    void onItemAccepted(DialogInterface dialog);

    /**
     * Invoked when selected item is removed.
     *
     * @param dialog
     */
    void onItemsRemoved(DialogInterface dialog);


}
