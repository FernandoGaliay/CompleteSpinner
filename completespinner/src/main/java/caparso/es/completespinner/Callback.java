package caparso.es.completespinner;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.Nullable;

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
     * @param position
     * @param item
     */
    void onItemAccepted(DialogInterface dialog, int position, T item);

    /**
     * Invoked when selected item is removed.
     *
     * @param dialog
     * @param position
     * @param item
     */
    void onItemRemoved(DialogInterface dialog, int position, T item);
}
