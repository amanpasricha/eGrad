package com.egrad;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class DepartmentDetailsFragment extends Fragment {
    private static final String DEPARTMENT_DETAILS = "DEPARTMENT DETAILS";
    private ArrayList<StudentDataModel> studentList;

    public DepartmentDetailsFragment() {
        // Required empty public constructor
    }


    public static DepartmentDetailsFragment newInstance(ArrayList<StudentDataModel> studentList) {
        DepartmentDetailsFragment fragment = new DepartmentDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(DEPARTMENT_DETAILS, studentList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            studentList = getArguments().getParcelableArrayList(DEPARTMENT_DETAILS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_department_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new DepartmentDetailsAdapter(studentList));
        recyclerView.setHasFixedSize(true);
    }
}

class DepartmentDetailsAdapter extends RecyclerView.Adapter<DepartmentDetailsAdapter.DepartmentDetailsViewHolder> {
    private final ArrayList<StudentDataModel> studentList;

    public DepartmentDetailsAdapter(ArrayList<StudentDataModel> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public DepartmentDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DepartmentDetailsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_student, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentDetailsAdapter.DepartmentDetailsViewHolder holder, int position) {
        StudentDataModel currentStudent = studentList.get(position);
        String registrationNumber = "" + currentStudent.getRegistrationNum();
        String mobileNumber = "" + currentStudent.getMobileNumber();

        holder.name.setText(currentStudent.getStudentName());
        holder.reg.setText(registrationNumber);
        holder.mobile.setText(mobileNumber);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    static class DepartmentDetailsViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView name, reg, mobile;

        public DepartmentDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            reg = itemView.findViewById(R.id.reg);
            mobile = itemView.findViewById(R.id.mobile);
        }
    }
}