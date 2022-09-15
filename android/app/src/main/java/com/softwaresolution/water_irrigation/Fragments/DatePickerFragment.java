//package com.softwaresolution.water_irrigation.Fragments;
//
//import androidx.fragment.app.DialogFragment;
//
//public static class DatePickerFragment extends DialogFragment
//        implements DatePickerDialog.OnDateSetListener {
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        // Use the current date as the default date in the picker
//        final Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//
//        // Create a new instance of DatePickerDialog and return it
//        return new DatePickerDialog(requireContext(), this, year, month, day);
//    }
//
//    public void onDateSet(DatePicker view, int year, int month, int day) {
//        // Do something with the date chosen by the user
//    }
//}