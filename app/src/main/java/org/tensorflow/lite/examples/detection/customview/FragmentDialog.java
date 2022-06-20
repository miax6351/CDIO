//package org.tensorflow.lite.examples.detection.customview;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatDialogFragment;
//
//import org.tensorflow.lite.examples.detection.R;
//
//public class FragmentDialog extends AppCompatDialogFragment {
//
//    private View snackbarText;
//    private FragmentDialogListener listener;
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.fragment_dialog, null);
//
//        builder.setView(view)
//                .setTitle("Recognized cards")
//                .setNegativeButton("Edit", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //String snackbarText = editSnackbar
//                    }
//                })
//                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //snackbarText.getText();
//                        listener.applyText(snackbarText);
//                    }
//                });
//
//        snackbarText = view.findViewById(R.id.snackbarText);
//
//        return builder.create();
//    }
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//
//        try {
//            listener = (FragmentDialogListener) context;
//        } catch (Exception exception) {
//            throw new ClassCastException((context.toString()));
//        }
//    }
//
//    public interface FragmentDialogListener {
//        void applyText(View snackbarText);
//    }
//}
