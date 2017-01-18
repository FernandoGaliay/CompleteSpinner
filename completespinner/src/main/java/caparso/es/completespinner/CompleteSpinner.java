package caparso.es.completespinner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
     * AutocompleteTextView inside spinner view. This view shows selected item.
     */
    private AutoCompleteTextView textView;

    /**
     * Array Adapter to build the data list of the spinner.
     */
    private ArrayAdapter<T> adapter;

    /**
     * Alert dialog builder for dialog mode.
     */
    private AlertDialog.Builder alertBuilder;

    /**
     * Selected object of the data list.
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
     * Enable or disable click events on spinner.
     */
    private Boolean enable = true;

    /**
     * Original data list to build the spinner.
     */
    private List<T> spinnerData;

    /**
     * Object to clear selected item in dropdown mode.
     */
    private T clearItemDropdown;

    private Boolean acceptButtonEnabled = true;

    private Boolean cancelButtonEnabled = true;

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
     * Set a custom view for the spinner.
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
     * Set the data list for the adapter of the spinner.
     *
     * @param list list with the objects that are going to be shown in the spinner.
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
     * Invoked when spinner viewgroup is clicked.
     *
     * @param v viewgroup of the spinner.
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
     * Enable dropdown mode.
     */
    public void setSpinnerModeDrop() {
        spinnerMode = SpinnerMode.DROPDOWN;
    }

    /**
     * Enable popup mode.
     */
    public void setSpinnerModePopup() {
        spinnerMode = SpinnerMode.POPUP;
    }

    /**
     * Remove selected item and clear data of the spinner.
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
     * Remove selected item and clear data of the spinner.
     */
    public void removeSelectedItem() {
        removeSelectedItem(null);
    }

    /**
     * Return selected item of the spinner.
     *
     * @return selected item
     */
    public T getSelectedItem() {
        return selectedItem;
    }

    /**
     * Set item as selected.
     *
     * @param selectedItem
     */
    private void setSelectedItem(T selectedItem, int position) {
        this.selectedItem = selectedItem;
        this.selectedItemPosition = position;
        int userPosition = position;
        if (SpinnerMode.POPUP == spinnerMode) {
            textView.setText(selectedItem.toString());
        } else {
            // Si existe elemento para borrar seleccion, hay que restar uno a la posicion del callback.
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
     * Set one of the adapter items as selected. If item are null or not contained inside the adapter list, has not effect. EQUALS must be overrided.
     *
     * @param selectedItem
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
     * Return if dropdown of the spinner is showing
     *
     * @return true if dropdown is showing, false in other case.
     */
    public Boolean isDropDownShowing() {
        return textView.isPopupShowing();
    }

    /**
     * Show the dropdown list of the Spinner.
     */
    public void showDropDown() {
        textView.showDropDown();
    }

    /**
     * Close the dropdown list of the Spinner.
     */
    public void dismissDropDown() {
        textView.dismissDropDown();
    }

    /**
     * Return the container view of the spinner.
     *
     * @return
     */
    public View getView() {
        return view;
    }

    /**
     * Set hint text to the Spinner
     *
     * @param resId id of the resource string.
     */
    public void setHint(int resId) {
        textView.setHint(context.getString(resId));
    }

    /**
     * Set hint text to the Spinner
     *
     * @param hint String of the hint.
     */
    public void setHint(String hint) {
        textView.setHint(hint);
    }

    /**
     * Build a default view for the spinner.
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
     * Build the popup with the items list to show on click event. Just single choice option.
     *
     * @param adapter Popup adapter with the list of options.
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
        if (cancelButtonEnabled)
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
     * Build dropdown items list to show on click event. Just single choice option.
     *
     * @param adapter Dropdown adapter with the list of options.
     */
    private void buildSpinnerDropDown(final ArrayAdapter<T> adapter) {
        textView.setAdapter(adapter);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Si existe el elemento para borrar y se presiona su posicion. Se elimina la seleccion.
                if (clearItemDropdown != null && position == 0) {
                    removeSelectedItem();
                }
                // En cualquier otro caso, se selecciona el elemento.
                else {
                    setSelectedItem(adapter.getItem(position), position);
                }
            }
        });
        if (enable)
            textView.showDropDown();
    }

    /**
     * Prepare view elements to build the spinner. AutoCompleteTextView must be not focusable, no suggestion type and perform the viewgroup click.
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
     * Add extra item in the drowdown list to clear selected item.
     *
     * @param clearItem
     */
    public void addRemoveSelectedItemInDropdown(@NonNull T clearItem) {
        if (SpinnerMode.DROPDOWN == spinnerMode && spinnerData != null && !spinnerData.isEmpty()) {
            clearItemDropdown = clearItem;
            spinnerData.add(0, clearItemDropdown);
            setData(spinnerData);
        }
    }

    /**
     * Set spinner callback.
     *
     * @param callback
     */
    public void setCallback(Callback<T> callback) {
        this.callback = callback;
    }

    /**
     * Get spinner callback.
     *
     * @return
     */
    public Callback<T> getCallback() {
        return callback;
    }

    /**
     * Get is spinner is enabled or disabled.
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return enable;
    }

    /**
     * Set is spinner is enabled or disabled.
     *
     * @return
     */
    @Override
    public void setEnabled(boolean enable) {
        this.view.setEnabled(enable);
        this.enable = enable;
    }

    public Boolean getAcceptButtonEnabled() {
        return acceptButtonEnabled;
    }

    public Boolean getCancelButtonEnabled() {
        return cancelButtonEnabled;
    }

    public void setAcceptButtonEnabled(Boolean acceptButtonEnabled) {
        this.acceptButtonEnabled = acceptButtonEnabled;
    }

    public void setCancelButtonEnabled(Boolean cancelButtonEnabled) {
        this.cancelButtonEnabled = cancelButtonEnabled;
    }
}