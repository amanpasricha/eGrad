package com.egrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BatchDetailsActivity extends AppCompatActivity {
    private static final String[] DEPARTMENTS = new String[] {
            "CS", "IT", "CCE", "ECE", "EE", "ME", "MECH", "AE", "CE", "CHEM"
    };

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_details);

        String batchName = getIntent().getStringExtra(BatchModel.BATCH_NAME);

        final MaterialToolbar toolbar = findViewById(R.id.toolbar);
        final TabLayout tabLayout = findViewById(R.id.tabLayout);
        final ViewPager2 viewPager = findViewById(R.id.viewPager);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(batchName);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        DocumentReference batchRef = firestore.collection("Students").document(batchName);

        Task<QuerySnapshot> CS_Query = batchRef.collection("Computer Science").get();
        Task<QuerySnapshot> IT_Query = batchRef.collection("Information Technology").get();
        Task<QuerySnapshot> CCE_Query = batchRef.collection("Computer & Communication").get();
        Task<QuerySnapshot> ECE_Query = batchRef.collection("Electronics & Communication").get();
        Task<QuerySnapshot> EE_Query = batchRef.collection("Electrical").get();
        Task<QuerySnapshot> ME_Query = batchRef.collection("Mechanical").get();
        Task<QuerySnapshot> MECH_Query = batchRef.collection("Mechatronics").get();
        Task<QuerySnapshot> AE_Query = batchRef.collection("Automobile").get();
        Task<QuerySnapshot> CE_Query = batchRef.collection("Civil").get();
        Task<QuerySnapshot> CHEM_Query = batchRef.collection("Chemical").get();

        ArrayList<StudentDataModel> CS = new ArrayList<>();
        ArrayList<StudentDataModel> IT = new ArrayList<>();
        ArrayList<StudentDataModel> CCE = new ArrayList<>();
        ArrayList<StudentDataModel> ECE = new ArrayList<>();
        ArrayList<StudentDataModel> EE = new ArrayList<>();
        ArrayList<StudentDataModel> ME = new ArrayList<>();
        ArrayList<StudentDataModel> MECH = new ArrayList<>();
        ArrayList<StudentDataModel> AE = new ArrayList<>();
        ArrayList<StudentDataModel> CE = new ArrayList<>();
        ArrayList<StudentDataModel> CHEM = new ArrayList<>();

        fetchStudentsData(CS_Query, CS);
        fetchStudentsData(IT_Query, IT);
        fetchStudentsData(CCE_Query, CCE);
        fetchStudentsData(ECE_Query, ECE);
        fetchStudentsData(EE_Query, EE);
        fetchStudentsData(ME_Query, ME);
        fetchStudentsData(MECH_Query, MECH);
        fetchStudentsData(AE_Query, AE);
        fetchStudentsData(CE_Query, CE);
        fetchStudentsData(CHEM_Query, CHEM);

        ArrayList<ArrayList<StudentDataModel>> currentBatch = new ArrayList<>();
        currentBatch.add(CS);
        currentBatch.add(IT);
        currentBatch.add(CCE);
        currentBatch.add(ECE);
        currentBatch.add(EE);
        currentBatch.add(ME);
        currentBatch.add(MECH);
        currentBatch.add(AE);
        currentBatch.add(CE);
        currentBatch.add(CHEM);

        Tasks.whenAllSuccess(CS_Query, IT_Query, CCE_Query, ECE_Query, EE_Query, ME_Query, MECH_Query,
                AE_Query, CE_Query, CHEM_Query)
                .addOnSuccessListener(this, objects -> {

                    viewPager.setAdapter(new BatchDetailsAdapter(getSupportFragmentManager(), getLifecycle(), currentBatch));
                    new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(DEPARTMENTS[position])).attach();
                });

    }

    private void fetchStudentsData(Task<QuerySnapshot> task, ArrayList<StudentDataModel> list) {
        task.addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                list.add(queryDocumentSnapshot.toObject(StudentDataModel.class));
            }
            list.trimToSize();
        });
    }
}


class BatchDetailsAdapter extends FragmentStateAdapter {
    private final ArrayList<ArrayList<StudentDataModel>> currentBatch;

    public BatchDetailsAdapter(@NonNull FragmentManager fragmentManager,
                               @NonNull Lifecycle lifecycle,
                               ArrayList<ArrayList<StudentDataModel>> currentBatch) {

        super(fragmentManager, lifecycle);
        this.currentBatch = currentBatch;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return DepartmentDetailsFragment.newInstance(currentBatch.get(position));
    }

    @Override
    public int getItemCount() {
        return currentBatch.size();
    }
}