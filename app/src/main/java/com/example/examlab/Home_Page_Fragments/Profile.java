package com.example.examlab.Home_Page_Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examlab.R;
import com.example.examlab.StarterPages.WalkThroughPages;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends Fragment {
EditText fName,mName,phoneNumber;
TextView fullName,email;
LinearLayout container;
Button editProfile,save;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        editProfile=view.findViewById(R.id.edit_profile_button);
        save=view.findViewById(R.id.save);
        fName=view.findViewById(R.id.editFirstName);
        mName=view.findViewById(R.id.editMiddleName);
        phoneNumber=view.findViewById(R.id.phoneNumber);
        fullName=view.findViewById(R.id.full_name);
        Button delete=view.findViewById(R.id.delete_profile_button);
        email=view.findViewById(R.id.email);
        container=view.findViewById(R.id.nameContainer);
        FirebaseFirestore database=FirebaseFirestore.getInstance();
        Button signOut=view.findViewById(R.id.signOut);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("USER_IDENTITY", MODE_PRIVATE);
        fullName.setText(sharedPreferences.getString("usrFName",null)+"\t"+sharedPreferences.getString("usrMName",null));
        email.setText(sharedPreferences.getString("email",null));
        phoneNumber.setText(sharedPreferences.getString("usrPn",null));
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
mAuth.signOut();
                Intent intent=new Intent(getActivity(), WalkThroughPages.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        ViewGroup finalContainer = container;
        editProfile.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
finalContainer.setVisibility(View.VISIBLE);
save.setVisibility(View.VISIBLE);
fName.setEnabled(true);
        mName.setEnabled(true);
        phoneNumber.setEnabled(true);
        fName.setText(sharedPreferences.getString("usrFName",null));
        mName.setText(sharedPreferences.getString("usrMName",null));
        phoneNumber.setText(sharedPreferences.getString("usrPn",null));
    }
});
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.confirmation_dialog);
                TextView messageTextView = dialog.findViewById(R.id.messageTextView);
                Button yesButton = dialog.findViewById(R.id.yesButton);
                Button noButton = dialog.findViewById(R.id.noButton);
                messageTextView.setText("Are you sure you want to remove Your Account?");
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser != null) {
                            currentUser.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                database.collection("User").document(sharedPreferences.getString("user_id",null)).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(getActivity(), "Account Removed ", Toast.LENGTH_SHORT).show();
                                                        Intent intent=new Intent(getActivity(),WalkThroughPages.class);
                                                        startActivity(intent);
                                                    }
                                                });
                                                // User deletion successful

                                            } else {
                                                // User deletion failed
                                                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            dialog.dismiss();
                        }
                    }
                });
                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
            });

save.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        SharedPreferences.Editor editor=sharedPreferences.edit();
        database.collection("User").document(sharedPreferences.getString("user_id",null)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentReference reference=task.getResult().getReference();
                    //save to database
                    reference.update("U_fname",fName.getText().toString());
                    reference.update("U_mname",mName.getText().toString());
                    reference.update("U_phone_number",phoneNumber.getText().toString());
                    //save to sharedPreference
                    editor.putString("usrFName", fName.getText().toString());
                    editor.putString("usrMName", mName.getText().toString());
                    editor.putString("usrPn", phoneNumber.getText().toString());
                    editor.commit();
                    finalContainer.setVisibility(View.GONE);
                    save.setVisibility(View.GONE);
                    fName.setEnabled(false);
                    mName.setEnabled(false);
                    phoneNumber.setEnabled(false);
                    Toast.makeText(getActivity(), "Your Profile Has Been Updated", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "something went wrong:\t"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    finalContainer.setVisibility(View.GONE);
                    save.setVisibility(View.GONE);
                    fName.setEnabled(false);
                    mName.setEnabled(false);
                    phoneNumber.setEnabled(false);
                }

            }
        });



    }
});
        return view;
    }
}