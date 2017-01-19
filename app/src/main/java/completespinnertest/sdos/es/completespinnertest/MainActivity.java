package completespinnertest.sdos.es.completespinnertest;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import caparso.es.completemultispinner.CompleteMultiSpinner;
import caparso.es.completespinner.Callback;
import caparso.es.completespinner.CompleteSpinner;
import caparso.es.completespinner.adapter.SpinnerAdapter;
import caparso.es.completespinner.constant.SpinnerMode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Complete spinner
        final CompleteSpinner completeSpinner = buildDefaultCompleteSpinner();

        // Complete multiselectable spinner
        final CompleteMultiSpinner completeMultiSpinner = buildDefaultCompleteMultiSpinner();

        // Custom Complete multiselectable spinner
        final CompleteMultiSpinner completeMultiSpinnerCustom = buildCustomMultiSpinner();

        // Example to set selected items in multiselect spinner.
        TextView tvSeleccionaTercero = (TextView) findViewById(R.id.tv_seleciona_tres);
        tvSeleccionaTercero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<SpinnerVO> list = new ArrayList<SpinnerVO>();
                list.add(new SpinnerVO(6, "Ragnar Lothbrok"));
                list.add(new SpinnerVO(2, "Kalesi"));
                completeMultiSpinner.setSelectedItems(list);
            }
        });

        buildCustomDialogCompleteSpinner();

    }

    private CompleteMultiSpinner buildCustomMultiSpinner() {
        LinearLayout llMultiSpinnerContainer = (LinearLayout) findViewById(R.id.ll_multispinner_container);
        TextView tvMultiSpinnerTextView = (TextView) findViewById(R.id.tv_multispinner);
        CompleteMultiSpinner<SpinnerVO> completeMultiSpinner = new CompleteMultiSpinner<>(this);
        completeMultiSpinner.setView(llMultiSpinnerContainer, tvMultiSpinnerTextView);
        completeMultiSpinner.setData(getList());
        completeMultiSpinner.setCallback(new caparso.es.completemultispinner.Callback<SpinnerVO>() {
            @Override
            public void onItemSelected(int position, SpinnerVO item) {
                Toast.makeText(MainActivity.this, "Select " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemAccepted(DialogInterface dialog) {
                Toast.makeText(MainActivity.this, "Accept", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemsRemoved(DialogInterface dialog) {
                Toast.makeText(MainActivity.this, "Remove", Toast.LENGTH_SHORT).show();
            }
        });
        return completeMultiSpinner;
    }

    private CompleteMultiSpinner buildDefaultCompleteMultiSpinner() {
        LinearLayout llMultiSpinnerContainer = (LinearLayout) findViewById(R.id.ll_multi_spìnner_container);
        CompleteMultiSpinner<SpinnerVO> completeMultiSpinner = new CompleteMultiSpinner<>(this);
        llMultiSpinnerContainer.addView(completeMultiSpinner.getView());
        completeMultiSpinner.setData(getList());
        completeMultiSpinner.setCallback(new caparso.es.completemultispinner.Callback<SpinnerVO>() {
            @Override
            public void onItemSelected(int position, SpinnerVO item) {
                Toast.makeText(MainActivity.this, "Select " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemAccepted(DialogInterface dialog) {
                Toast.makeText(MainActivity.this, "Accept", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemsRemoved(DialogInterface dialog) {
                Toast.makeText(MainActivity.this, "Remove", Toast.LENGTH_SHORT).show();
            }
        });
        return completeMultiSpinner;
    }

    private CompleteSpinner buildDefaultCompleteSpinner() {
        // Building complete spinner
        LinearLayout llSpinnerContainerDropDown = (LinearLayout) findViewById(R.id.ll_spinner_container_dropdown);
        final CompleteSpinner<SpinnerVO> completeSpinnerDropDown = new CompleteSpinner<SpinnerVO>(this, SpinnerMode.DROPDOWN);
        llSpinnerContainerDropDown.addView(completeSpinnerDropDown.getView());
        // Setting hint
        completeSpinnerDropDown.setHint(R.string.hint);
        // Setting data
        completeSpinnerDropDown.setData(getList());
        // Adding remove element in the dropdown
        completeSpinnerDropDown.addClearViewInDropdown(new SpinnerVO(0, "Borrar seleccionado"));
        // Setting callbacks
        completeSpinnerDropDown.setCallback(new Callback<SpinnerVO>() {
            @Override
            public void onItemSelected(int position, SpinnerVO item) {
                Toast.makeText(MainActivity.this, "Select " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemAccepted(DialogInterface dialog, int position, SpinnerVO item) {
                Toast.makeText(MainActivity.this, "Accept " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemRemoved(DialogInterface dialog, int position, SpinnerVO item) {
                Toast.makeText(MainActivity.this, "Remove " + position, Toast.LENGTH_SHORT).show();
            }
        });
        return completeSpinnerDropDown;
    }

    private CompleteSpinner buildCustomDialogCompleteSpinner() {
        // Building complete spinner
        LinearLayout llCustomSpinnerContainer = (LinearLayout) findViewById(R.id.ll_custom_dialog_spinner_container);
        final CompleteSpinner<SpinnerVO> completeSpinnerDropDown = new CompleteSpinner<SpinnerVO>(this,
                new SpinnerAdapter(this, R.layout.row_custom, android.R.id.text1, new SpinnerAdapter.OnViewCreated() {
                    @Override
                    public void onViewCreated(final int position, View view) {
                        // TODO: Your own code.
                    }
                }));
        llCustomSpinnerContainer.addView(completeSpinnerDropDown.getView());
        completeSpinnerDropDown.setSpinnerModeDrop();
        completeSpinnerDropDown.setData(getList());
        completeSpinnerDropDown.setAcceptButtonEnabled(false);
        completeSpinnerDropDown.setCleanButtonEnabled(false);
        return completeSpinnerDropDown;
    }

    private List<SpinnerVO> getList() {
        final List<SpinnerVO> spinnerVOList = new ArrayList<SpinnerVO>();
        spinnerVOList.add(new SpinnerVO(0, "King in the north"));
        spinnerVOList.add(new SpinnerVO(1, "Lanister"));
        spinnerVOList.add(new SpinnerVO(2, "Kalesi"));
        spinnerVOList.add(new SpinnerVO(3, "Peter Gregory"));
        spinnerVOList.add(new SpinnerVO(4, "Gavin Belson"));
        spinnerVOList.add(new SpinnerVO(5, "Gilfoyle"));
        spinnerVOList.add(new SpinnerVO(6, "Ragnar Lothbrok"));
        spinnerVOList.add(new SpinnerVO(7, "Jared"));
        spinnerVOList.add(new SpinnerVO(8, "Dinesh"));
        return spinnerVOList;
    }
}