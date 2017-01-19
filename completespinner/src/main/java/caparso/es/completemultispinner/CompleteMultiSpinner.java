package caparso.es.completemultispinner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import caparso.es.completemultispinner.util.ConverterUtil;
import caparso.es.completespinner.R;

public class CompleteMultiSpinner<T> extends View implements View.OnClickListener {

    /**
     * Context of the spinner.
     */
    private Context context;

    /**
     * Layout that contains the whole spinner view.
     */
    private View view;

    /**
     * AutocompleteTextView inside the spinner view.
     */
    private TextView textView;

    /**
     * Array Adapter to build the dialog / dropdown view.
     */
    private ArrayAdapter<T> adapter;

    /**
     * Alert dialog builder.
     */
    private AlertDialog.Builder alertBuilder;

    /**
     * Callback to retrieve the spinner events.
     */
    private Callback<T> callback;

    /**
     * List to build the spinner.
     */
    private List<T> spinnerData;

    /**
     * List to persist the checked value of each item.
     */
    private boolean[] selectedItemList;

    /**
     * Field value to enable or disable click events.
     */
    private Boolean enable = true;

    /**
     * Default constructor
     *
     * @param context
     */
    public CompleteMultiSpinner(Context context) {
        super(context);
        this.context = context;
        buildDefaultSpinnerView();
        removeSelectedItems();
    }

    /**
     * Set sa custom view for the spinner.
     *
     * @param view
     * @param textView
     */
    public void setView(final View view, TextView textView) {
        this.view = view;
        this.textView = textView;
        setupViews(this.view, this.textView);
    }

    /**
     * Builds a default view for the spinner.
     */
    private void buildDefaultSpinnerView() {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View defaultView = inflater.inflate(R.layout.multi_spinner_default, null);
        this.view = defaultView.findViewById(R.id.ll_spinner_container);
        this.textView = (TextView) defaultView.findViewById(R.id.act_spinner);
        setupViews(this.view, this.textView);
    }

    /**
     * Sets one item as selected.
     *
     * @param selectedItem
     */
    private void setSelectedItem(T selectedItem, int position, boolean isChecked) {
        selectedItemList[position] = isChecked;
        textView.setText(context.getString(R.string.multi_popup_selected_items, getSelectedItemsCount()));
        if (callback != null)
            callback.onItemSelected(position, adapter.getItem(position));
    }

    /**
     * Returns the amount of the selected items.
     *
     * @return
     */
    private int getSelectedItemsCount() {
        int count = 0;
        for (int index = 0; index < spinnerData.size(); index++) {
            if (selectedItemList[index]) {
                count++;
            }
        }
        return count;
    }

    /**
     * Builds a string with the selected items.
     *
     * @return
     */
    private String getSelectedItemsText() {
        String selectedItemsString = "";
        for (int index = 0; index < spinnerData.size(); index++) {
            if (selectedItemList[index]) {
                selectedItemsString += spinnerData.get(index).toString() + ", ";
            }
        }
        if (selectedItemsString.length() > 0) {
            selectedItemsString = selectedItemsString.substring(0, selectedItemsString.length() - 3);
        }
        return selectedItemsString;
    }

    /**
     *  Setups the views to build the spinner.
     */
    private void setupViews(final View view, TextView textView) {
        if (textView.getHint() == null || "".equals(textView.getHint())) {
            textView.setHint(context.getString(R.string.spinner_hint_default));
        }
        textView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    view.performClick();
                }
                return true;
            }
        });
    }

    /**
     * Removes the selected item and clear the related data.
     */
    private void removeSelectedItems(@Nullable DialogInterface dialog) {
        if (callback != null) {
            callback.onItemsRemoved(dialog);
        }
        if (selectedItemList != null) {
            for (int position = 0; position < selectedItemList.length; position++) {
                selectedItemList[position] = false;
            }
        }
        textView.setText("");
    }

    /**
     * Removes the selected item and clear the related data.
     */
    public void removeSelectedItems() {
        removeSelectedItems(null);
    }

    /**
     * Builds an adapter with the item list.
     *
     * @param list Objects that are going to be shown in the spinner.
     */
    public void setData(List<T> list) {
        spinnerData = list;
        selectedItemList = new boolean[list.size()];
        for (int position = 0; position < selectedItemList.length; position++) {
            selectedItemList[position] = false;
        }
        adapter = new ArrayAdapter<T>(context, android.R.layout.simple_list_item_single_choice);
        adapter.addAll(list);
        view.setOnClickListener(this);
    }

    /**
     * Invoked when the spinner view is clicked.
     *
     * @param v Viewgroup of the spinner.
     */
    @Override
    public void onClick(View v) {
        buildSpinnerPopup(adapter);
    }

    /**
     * Builds the dialog that contains the items list. Just single choice option.
     *
     * @param adapter Dialog adapter.
     */
    private void buildSpinnerPopup(final ArrayAdapter<T> adapter) {
        alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle(R.string.popup_choose_one);

        String[] dataArray = ConverterUtil.convertListToArray(spinnerData);
        alertBuilder.setMultiChoiceItems(dataArray, selectedItemList, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                setSelectedItem(adapter.getItem(which), which, isChecked);
            }
        });
        alertBuilder.setPositiveButton(R.string.popup_accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (callback != null)
                    callback.onItemAccepted(dialog);
                dialog.dismiss();
            }
        });
        alertBuilder.setNegativeButton(R.string.popup_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeSelectedItems(dialog);
                dialog.dismiss();
            }
        });
        if (enable)
            alertBuilder.show();
    }

    public void setSelectedItems(List<T> itemList) {
        List<Integer> indexPositionList = new ArrayList<>();
        for (int index = 0; index < spinnerData.size(); index++) {
            T spinnerItem = spinnerData.get(index);
            for (T item : itemList) {
                if (spinnerItem.equals(item)) {
                    indexPositionList.add(index);
                    break;
                }
            }
            selectedItemList[index] = false;
        }
        for (Integer index : indexPositionList) {
            setSelectedItem(spinnerData.get(index), index, true);
        }
    }

    /**
     * Returns the list of the selected items.
     * @return List of the selected items.
     */
    public List<T> getSelectedItems() {
        List<T> itemList = new ArrayList<>();
        for (int index = 0; index < spinnerData.size(); index++) {
            if (selectedItemList[index]) {
                T spinnerItem = spinnerData.get(index);
                itemList.add(spinnerItem);
            }
        }
        return itemList;
    }

    /**
     * Returns the view of the spinner.
     *
     * @return
     */
    public View getView() {
        return view;
    }

    /**
     * Sets the hint text for the Spinner
     *
     * @param resId Id of the resource string.
     */
    public void setHint(@StringRes int resId) {
        textView.setHint(context.getString(resId));
    }

    /**
     * Sets the hint text for the Spinner
     *
     * @param hint String of the hint.
     */
    public void setHint(String hint) {
        textView.setHint(hint);
    }

    /**
     * Returns if the spinner is enabled or disabled.
     *
     * @return True if the spinner is enabled.
     */
    @Override
    public boolean isEnabled() {
        return enable;
    }

    /**
     * Sets the spinner to enabled or disabled.
     *
     * @return
     */
    @Override
    public void setEnabled(boolean enable) {
        this.view.setEnabled(enable);
        this.enable = enable;
    }

    /**
     * Sets the spinner callback.
     *
     * @param callback
     */
    public void setCallback(Callback<T> callback) {
        this.callback = callback;
    }

    /**
     * Gets the spinner callback.
     *
     * @return
     */
    public Callback<T> getCallback() {
        return callback;
    }

}
