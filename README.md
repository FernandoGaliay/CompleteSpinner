Development of Android Spinner that implements **hint text**, **dropdown/popup mode** and easy **layout customization**. Easy to use.

![enter image description here](http://oi61.tinypic.com/11j40wp.jpg)

Features
--------
* Customization of the spinner view.
* No need to implement adapter for custom objects. 
* Allows hint text.
* Handles dropdown or dialog mode.
* Customization of the spinner dialog/dropdown.

Changelog
--------
// TODO 

Considerations and code
--------

**Override toString method**

CompleteSpinner builds the string value of the object shown in the row view, using **toString()** method from *java.lang.Object* class. So, it is necesary to override this method in the object class, to get the right String format.
```java
    @Override
    public String toString() {
        // Overrides toString method
    }
```

**Override equals method**

To set (explicity) any object as selected in the spinner, you must to override *equals(Object o)* method from *java.lang.Object* class. 
```java
    @Override
    public boolean equals(Object o) {
        // Overrides equals method
    }
```

---------------

**COMPLETE SPINNER**

Building a spinner with default implementation.

```java
  CompleteSpinner<SpinnerVO> completeSpinnerDropDown = new CompleteSpinner<SpinnerVO>(this, SpinnerMode.MODE_DROPDOWN);
  completeSpinnerDropDown.setHint(R.string.hint);
  completeSpinnerDropDown.setAdapter(getList());
  llSpinnerContainerDropDown.addView(completeSpinnerDropDown.getView());
```
 
**Spinner with custom view**

Spinner view is composed of: **parent Layout**, that represents the container of the Spinner, and an **AutoCompleteTextView Layout**, where is going to be shown the selected object. Then, it's possible to add some extra views to customize the Spinner. AutocompleteTextView layout can act like parent view too.

*XML*
```xml
      <LinearLayout
        android:id="@+id/ll_spinner_container"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@drawable/shape_box_container"
        android:gravity="center"
        android:orientation="horizontal">

        <AutoCompleteTextView
            android:id="@+id/act_spinner"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:background="@android:color/transparent"
            android:padding="10dp"/>

        <ImageView
            android:layout_width="40dp"
            android:layout_height="40dp"
            android:scaleType="centerInside"
            android:src="@mipmap/dropdown_arrow"/>

    </LinearLayout>
```


*JAVA*
```java
AutoCompleteTextView actSpinner = (AutoCompleteTextView) findViewById(R.id.act_spinner);
LinearLayout llSpinnerContainer = (LinearLayout) findViewById(R.id.ll_spinner_container);
        
final CompleteSpinner<SpinnerVO> completeSpinner = new CompleteSpinner<SpinnerVO>(this, SpinnerMode.MODE_POPUP);
completeSpinner.setView(llSpinnerContainer, actSpinner);
completeSpinner.setAdapter(getList());
```

**Callbacks**

Adding spinner callback to handle the following events: item selection, delete item and accept selected item.
```java
 completeSpinnerDropDown.setCallback(new Callback<SpinnerVO>() {
            @Override
            public void onItemSelected(int position, SpinnerVO item) {
                Toast.makeText(MainActivity.this, "Select", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemAccepted(DialogInterface dialog, int position, SpinnerVO item) {
                Toast.makeText(MainActivity.this, "Accept", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemRemoved(DialogInterface dialog, int position, SpinnerVO item) {
                Toast.makeText(MainActivity.this, "Remove", Toast.LENGTH_SHORT).show();
            }
        });
```

**Public methods**

* setData(List<Object> data):
Sets the list of the data to build the spinner.

* setCallback(Callback<Object> callback):
Sets callback to handle differents events of the spinner.

* setView(View view, AutocompleteTextView atcTextView) / getView():
Setter and getter of the view of the spinner.

* setHint(String hint):
Sets hint to the spinner.

------------------

**COMPLETEMULTISPINNER**

Building a multi-spinner with default implementation.

```java
LinearLayout llMultiSpinnerContainer = (LinearLayout) findViewById(R.id.ll_multi_spìnner_container);
CompleteMultiSpinner<SpinnerVO> completeMultiSpinner = new CompleteMultiSpinner<>(this);
llMultiSpinnerContainer.addView(completeMultiSpinner.getView());
completeMultiSpinner.setData(getList());
```
 
**Spinner with custom view**

Spinner view must be composed of: **parent Layout**, that represents the container of the Spinner, and an **TextView Layout**, where is going to be shown the selected object. Then, it's possible to add some extra views to customize the Spinner. TextView layout can act like parent view too.

*XML*
```xml
    <LinearLayout
        android:id="@+id/ll_multispinner_container"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="15dp"
        android:background="#CCCCCC"
        android:gravity="center"
        android:orientation="horizontal">

        <TextView
            android:id="@+id/tv_multispinner"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:background="@android:color/transparent"
            android:padding="10dp"/>

        <ImageView
            android:layout_width="40dp"
            android:layout_height="40dp"
            android:scaleType="centerInside"
            android:src="@drawable/abc_ic_menu_moreoverflow_mtrl_alpha"/>

    </LinearLayout>
```


*JAVA*
```java
LinearLayout llMultiSpinnerContainer = (LinearLayout) findViewById(R.id.ll_multispinner_container);
TextView tvMultiSpinnerTextView = (TextView) findViewById(R.id.tv_multispinner);
CompleteMultiSpinner<SpinnerVO> completeMultiSpinner = new CompleteMultiSpinner<>(this);
completeMultiSpinner.setView(llMultiSpinnerContainer, tvMultiSpinnerTextView);
```

**Callbacks**

Adding spinner callback to handle the following events: item selection, delete item and accept selected item.
```java
 completeSpinnerDropDown.setCallback(new Callback<SpinnerVO>() {
            @Override
            public void onItemSelected(int position, SpinnerVO item) {
                Toast.makeText(MainActivity.this, "Select", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemAccepted(DialogInterface dialog) {
                Toast.makeText(MainActivity.this, "Accept", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemRemoved(DialogInterface dialog) {
                Toast.makeText(MainActivity.this, "Remove", Toast.LENGTH_SHORT).show();
            }
        });
```


**Public methods**

* setData(List<Object> data):
Sets the list of the data to build the spinner.

* setCallback(Callback<Object> callback):
Sets callback to handle differents events of the spinner.

* setView(View view, TextView atcTextView) / getView():
Setter and getter of the view of the spinner.

* setHint(String hint):
Sets hint to the spinner.

* removeSelectedItemS():
Removes selected item of the data list of the spinner.

* getSelectedItemS() / setSelectedItem(List<Object> data):
Setter and getter of the selected items of the data list of the spinner.

* setEnabled(Boolean enable) / isEnabled():
Setter and getter to enable the spinner.

* removeSelectedItem():
Removes selected item of the data list of the spinner.

* getSelectedItem() / setSelectedItem(Object data):
Setter and getter of the selected item of the data list of the spinner.

* setEnabled(Boolean enable) / isEnabled():
Setter and getter to enable the spinner.

* addRemoveSelectedItemInDropdown(Object data):
If spinner is in dropdown mode, adds fake object at the begining of the dropdown to clear the selected item.
