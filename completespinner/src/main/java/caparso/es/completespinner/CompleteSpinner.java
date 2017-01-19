package caparso.es.completespinner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import java.util.List;

import caparso.es.completespinner.adapter.SpinnerAdapter;
import caparso.es.completespinner.constant.SpinnerMode;

public class CompleteSpinner<T> extends View implements View.OnClickListener {

    /**
     * Context of the spinner.
     */
    private Context context;

    /**
     * Layout that contains the whole spinner view.
     */
    private View view;

    /**
     * AutocompleteTextView inside the spinner view. This view shows the selected item.
     */
    private AutoCompleteTextView textView;

    /**
     * Array Adapter to build the dialog / dropdown view.
     */
    private ArrayAdapter<T> adapter;

    /**
     * Alert dialog builder.
     */
    private AlertDialog.Builder alertBuilder;

    /**
     * Selected object.
     */
    private T selectedItem;

    /**
     * Position of the selected item.
     */
    private int selectedItemPosition = -1;

    /**
     * Mode of the spinner. 0: Popup mode, 1: Dropdown mode
     */
    private int spinnerMode;

    /**
     * Callback to retrieve spinner events.
     */
    private Callback<T> callback;

    /**
     * Boolean field to enable or disable click events.
     */
    private Boolean enable = true;

    /**
     * List to build the spinner.
     */
    private List<T> spinnerData;

    /**
     * Object to use in the clear view of the drowdown.
     */
    private T clearItemDropdown;

    /**
     * Boolean field to enable or disable accept button
     */
    private Boolean acceptButtonEnabled = true;

    /**
     * Boolean field to enable or disable clean button
     */
    private Boolean cleanButtonEnabled = true;

    /**
     * Default constructor
     *
     * @param context
     * @param spinnerMode
     */
    public CompleteSpinner(Context context, int spinnerMode) {
        super(context);
        this.context = context;
        this.spinnerMode = spinnerMode;
        buildDefaultSpinnerView();
        removeSelectedItem();
    }

    /**
     * Default constructor for dialog custom view. Dialog mode only.
     *
     * @param context
     */
    public CompleteSpinner(Context context, SpinnerAdapter<T> adapter) {
        super(context);
        this.context = context;
        this.spinnerMode = SpinnerMode.POPUP;
        this.adapter = adapter;
        buildDefaultSpinnerView();
        removeSelectedItem();
    }

    /**
     * Sets a custom view for the spinner.
     *
     * @param view
     * @param textView
     */
    public void setView(final View view, AutoCompleteTextView textView) {
        this.view = view;
        this.textView = textView;
        setupViews(this.view, this.textView);
    }

    /**
     * Builds an adapter with the item list.
     *
     * @param list Objects that are going to be shown in the spinner.
     */
    public void setData(List<T> list) {
        spinnerData = list;
        if (adapter != null) {
            adapter.clear();
            adapter.addAll(list);
        } else if (spinnerMode == SpinnerMode.DROPDOWN) {
            adapter = new ArrayAdapter<T>(context, android.R.layout.simple_list_item_1);
            adapter.addAll(list);
        } else {
            adapter = new ArrayAdapter<T>(context, android.R.layout.simple_list_item_single_choice);
            adapter.addAll(list);
        }
        view.setOnClickListener(this);
    }

    /**
     * Invoked when the spinner view is clicked.
     *
     * @param v Viewgroup of the spinner.
     */
    @Override
    public void onClick(View v) {
        if (spinnerMode == SpinnerMode.DROPDOWN) {
            buildSpinnerDropDown(adapter);
        } else {
            buildSpinnerPopup(adapter);
        }
    }

    /**
     * Enables dropdown mode.
     */
    public void setSpinnerModeDrop() {
        spinnerMode = SpinnerMode.DROPDOWN;
    }

    /**
     * Enables popup mode.
     */
    public void setSpinnerModePopup() {
        spinnerMode = SpinnerMode.POPUP;
    }

    /**
     * Removes the selected item and clear the related data.
     */
    private void removeSelectedItem(@Nullable DialogInterface dialog) {
        if (callback != null) {
            int userPosition = selectedItemPosition;
            if (clearItemDropdown != null)
                userPosition--;
            callback.onItemRemoved(dialog, userPosition, selectedItem);
        }
        textView.setText("");
        selectedItem = null;
        selectedItemPosition = -1;
    }

    /**
     * Removes the selected item and clear the realated data.
     */
    public void removeSelectedItem() {
        removeSelectedItem(null);
    }

    /**
     * Returns the selected item.
     *
     * @return Selected item
     */
    public T getSelectedItem() {
        return selectedItem;
    }

    /**
     * Sets one item as selected.
     *
     * @param selectedItem Selected item.
     */
    private void setSelectedItem(T selectedItem, int position) {
        this.selectedItem = selectedItem;
        this.selectedItemPosition = position;
        int userPosition = position;
        if (SpinnerMode.POPUP == spinnerMode) {
            textView.setText(selectedItem.toString());
        } else {
            if (clearItemDropdown != null) {
                userPosition--;
            }
            textView.setText(selectedItem.toString());
            adapter.getFilter().filter("");
            textView.setAdapter(adapter);
        }
        if (callback != null)
            callback.onItemSelected(userPosition, adapter.getItem(position));
    }

    /**
     * Sets one item as selected. EQUALS must be overrided.
     *
     * @param selectedItem Selected item.
     */
    public void setSelectedItem(@NonNull T selectedItem) {
        if (selectedItem != null) {
            if (adapter != null) {
                for (int position = 0; position < adapter.getCount(); position++) {
                    if (selectedItem.equals(adapter.getItem(position))) {
                        setSelectedItem(adapter.getItem(position), position);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Returns if the dropdown is showing
     *
     * @return True if dropdown is showing.
     */
    public Boolean isDropDownShowing() {
        return textView.isPopupShowing();
    }

    /**
     * Shows the dropdown list.
     */
    public void showDropDown() {
        textView.showDropDown();
    }

    /**
     * Closes the dropdown list.
     */
    public void dismissDropDown() {
        textView.dismissDropDown();
    }

    /**
     * Returns the view of the spinner.
     *
     * @return The view of the spinner.
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
     * Builds a default view for the spinner.
     */
    private void buildDefaultSpinnerView() {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View defaultView = inflater.inflate(R.layout.spinner_default, null);
        this.view = (LinearLayout) defaultView.findViewById(R.id.ll_spinner_container);
        this.textView = (AutoCompleteTextView) defaultView.findViewById(R.id.act_spinner);
        setupViews(this.view, this.textView);
    }

    /**
     * Builds the dialog that contains the items list. Just single choice option.
     *
     * @param adapter Dialog adapter.
     */
    private void buildSpinnerPopup(final ArrayAdapter<T> adapter) {
        alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setSingleChoiceItems(adapter, selectedItemPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setSelectedItem(adapter.getItem(which), which);
                if (!acceptButtonEnabled)
                    dialog.dismiss();
            }
        });
        if (acceptButtonEnabled)
            alertBuilder.setPositiveButton(R.string.popup_accept, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (callback != null)
                        callback.onItemAccepted(dialog, selectedItemPosition, selectedItem);
                    dialog.dismiss();
                }
            });
        if (cleanButtonEnabled)
            alertBuilder.setNegativeButton(R.string.popup_delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    removeSelectedItem(dialog);
                    dialog.dismiss();
                }
            });
        if (enable)
            alertBuilder.show();
    }

    /**
     * Builds the dropdown items list. Just single choice option.
     *
     * @param adapter Dropdown adapter.
     */
    private void buildSpinnerDropDown(final ArrayAdapter<T> adapter) {
        textView.setAdapter(adapter);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (clearItemDropdown != null && position == 0) {
                    removeSelectedItem();
                }
                else {
                    setSelectedItem(adapter.getItem(position), position);
                }
            }
        });
        if (enable)
            textView.showDropDown();
    }

    /**
     * Setups the views to build the spinner.
     */
    private void setupViews(final View view, AutoCompleteTextView autoCompleteTextView) {
        if (autoCompleteTextView.getHint() == null || "".equals(autoCompleteTextView.getHint())) {
            autoCompleteTextView.setHint(context.getString(R.string.spinner_hint_default));
        }
        autoCompleteTextView.setFocusable(false);
        autoCompleteTextView.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        autoCompleteTextView.setOnTouchListener(new OnTouchListener() {
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
     * Adds an extra item in the drowdown list, to clear the selected item.
     *
     * @param clearItem Object to print in the clear view
     */
    public void addClearViewInDropdown(@NonNull T clearItem) {
        if (SpinnerMode.DROPDOWN == spinnerMode && spinnerData != null && !spinnerData.isEmpty()) {
            clearItemDropdown = clearItem;
            spinnerData.add(0, clearItemDropdown);
            setData(spinnerData);
        }
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
     * Returns the spinner callback.
     *
     * @return The spinner callback.
     */
    public Callback<T> getCallback() {
        return callback;
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
     */
    @Override
    public void setEnabled(boolean enable) {
        this.view.setEnabled(enable);
        this.enable = enable;
    }

    /**
     * Returns if accept button is enabled.
     *
     * @return True if accept button is enabled.
     */
    public Boolean getAcceptButtonEnabled() {
        return acceptButtonEnabled;
    }

    /**
     * Returns if clean button is enabled.
     *
     * @return True if clean button is enabled
     */
    public Boolean getCleanButtonEnabled() {
        return cleanButtonEnabled;
    }

    /**
     * Sets the accept button to enable or disable.
     *
     * @param acceptButtonEnabled True if the accept button is going to be enabled.
     */
    public void setAcceptButtonEnabled(Boolean acceptButtonEnabled) {
        this.acceptButtonEnabled = acceptButtonEnabled;
    }

    /**
     * Sets the clean button to enable or disable.
     *
     * @param cleanButtonEnabled True if the clean button is going to be enabled.
     */
    public void setCleanButtonEnabled(Boolean cleanButtonEnabled) {
        this.cleanButtonEnabled = cleanButtonEnabled;
    }
}