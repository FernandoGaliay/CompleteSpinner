package caparso.es.completespinner.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by fernando.galiay on 21/06/2016.
 */
public class SpinnerAdapter<Object> extends ArrayAdapter<Object> {

    private OnViewCreated onViewCreated;

    public SpinnerAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public SpinnerAdapter(Context context, int resource, int textViewResourceId, Object[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public SpinnerAdapter(Context context, int resource, int textViewResourceId, List objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public SpinnerAdapter(Context context, int resource, int textViewResourceId, OnViewCreated onViewCreated) {
        super(context, resource, textViewResourceId);
        this.onViewCreated = onViewCreated;
    }

    public SpinnerAdapter(Context context, int resource, int textViewResourceId, Object[] objects, OnViewCreated onViewCreated) {
        super(context, resource, textViewResourceId, objects);
        this.onViewCreated = onViewCreated;
    }

    public SpinnerAdapter(Context context, int resource, int textViewResourceId, List objects, OnViewCreated onViewCreated) {
        super(context, resource, textViewResourceId, objects);
        this.onViewCreated = onViewCreated;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = super.getView(position, convertView, parent);
        if (onViewCreated != null)
            onViewCreated.onViewCreated(position, row);
        return row;
    }

    public interface OnViewCreated {
        void onViewCreated(int position, View view);
    }
}
