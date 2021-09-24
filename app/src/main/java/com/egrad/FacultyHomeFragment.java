package com.egrad;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FacultyHomeFragment extends Fragment {
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final ArrayList<BatchModel> batchList;
    private RecyclerView recyclerView;
    private BatchAdapter adapter;
    private int batchNum = 2021;

    public FacultyHomeFragment() {
        // Required empty public constructor
        batchList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_faculty_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ExtendedFloatingActionButton addNewBatch = view.findViewById(R.id.addNewBatch);
        addNewBatch.setOnClickListener(this::addNewBatch);

        adapter = new BatchAdapter(getActivity(), batchList);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        adapter.setOnItemClickListener(currentBatch -> {
            Intent batchDetails = new Intent(getActivity(), BatchDetailsActivity.class);
            batchDetails.putExtra(BatchModel.BATCH_NAME, currentBatch.getBatchName());
            startActivity(batchDetails);
        });

        firestore.collection("Students").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                    for(int x = documentSnapshots.size()-1; x>=0; x--) {
                        DocumentSnapshot documentSnapshot = documentSnapshots.get(x);
                        String batchName = documentSnapshot.getId();
                        Long numOfStudents = documentSnapshot.get("numOfStudents", Long.class);
                        assert numOfStudents != null;
                        batchList.add(new BatchModel(batchName, numOfStudents.intValue()));
                    }
                    adapter.notifyItemRangeInserted(0, queryDocumentSnapshots.size());
                    batchNum = Integer.parseInt( documentSnapshots.get(documentSnapshots.size()-1).getId().substring(0, 4));
                });
    }

    private void addNewBatch(View view) {

        ActivityCompat.requestPermissions(requireActivity(),
                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

        final Intent filePicker = new Intent(Intent.ACTION_GET_CONTENT);
        filePicker.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        filePickerResultLauncher.launch(filePicker);
    }

    private final ActivityResultLauncher<Intent> filePickerResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK) {
                    String filePath = "";
                    if(result.getData() != null) {
                        filePath = result.getData().getData().getPath();
                        System.out.println("Data -> Path: " + filePath);
                    }

                    File file;

                    if(filePath.startsWith("/document")) {
                        String newPath = "/storage/emulated/";
                        String sub = filePath.substring(10);

                        if(sub.startsWith("primary:"))
                            newPath += "0/" + filePath.substring(18);
                        else if(sub.startsWith("secondary:"))
                            newPath += "1/" + filePath.substring(20);

                        file = new File(newPath);

                    } else {
                        file = new File(filePath);
                    }

                    try {
                        if(file.exists()) {
                            ArrayList<StudentDataModel> CSE = new ArrayList<>();
                            ArrayList<StudentDataModel> IT = new ArrayList<>();
                            ArrayList<StudentDataModel> CCE = new ArrayList<>();
                            ArrayList<StudentDataModel> ECE = new ArrayList<>();
                            ArrayList<StudentDataModel> EE = new ArrayList<>();
                            ArrayList<StudentDataModel> ME = new ArrayList<>();
                            ArrayList<StudentDataModel> MECH = new ArrayList<>();
                            ArrayList<StudentDataModel> AE = new ArrayList<>();
                            ArrayList<StudentDataModel> CE = new ArrayList<>();
                            ArrayList<StudentDataModel> CHEM = new ArrayList<>();

                            FileInputStream inputStream = new FileInputStream(file);
                            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

                            XSSFSheet sheet = workbook.getSheetAt(0);
                            Iterator<Row> rowIterator = sheet.rowIterator();

//                            int rowNum=0;
                            while (rowIterator.hasNext()) {
//                                System.out.println(rowNum++);
                                XSSFRow row = (XSSFRow) rowIterator.next();
                                long registrationNum = (long) row.getCell(0).getNumericCellValue();
                                String studentName = row.getCell(1).getStringCellValue();
                                String emailAddress = row.getCell(2).getStringCellValue();
                                String password = row.getCell(3).getStringCellValue();
                                String department = row.getCell(4).getStringCellValue();
                                Date dateOfBirth = row.getCell(5).getDateCellValue();
                                String fathersName = row.getCell(6).getStringCellValue();
                                String mothersName = row.getCell(7).getStringCellValue();
                                String bloodGroup = row.getCell(8).getStringCellValue();
                                String state = row.getCell(9).getStringCellValue();
                                String city = row.getCell(10).getStringCellValue();
                                long mobileNumber = (long) row.getCell(11).getNumericCellValue();


                                StudentDataModel currentStudent = new StudentDataModel(
                                        registrationNum, studentName, emailAddress, password,
                                        new Timestamp(dateOfBirth), fathersName, mothersName,
                                        bloodGroup, state, city, mobileNumber, batchNum);

                                switch(department) {
                                    case "CSE":
                                        CSE.add(currentStudent);
                                        break;

                                    case "IT":
                                        IT.add(currentStudent);
                                        break;

                                    case "CCE":
                                        CCE.add(currentStudent);
                                        break;

                                    case "ECE":
                                        ECE.add(currentStudent);
                                        break;

                                    case "EE":
                                        EE.add(currentStudent);
                                        break;

                                    case "ME":
                                        ME.add(currentStudent);
                                        break;

                                    case "MECH":
                                        MECH.add(currentStudent);
                                        break;

                                    case "AE":
                                        AE.add(currentStudent);
                                        break;

                                    case "CE":
                                        CE.add(currentStudent);
                                        break;

                                    case "CHEM":
                                        CHEM.add(currentStudent);
                                        break;

                                    default:
                                        Toast.makeText(getActivity(), department + " do not exist", Toast.LENGTH_SHORT).show();
                                }

                            }

                            ArrayList<ArrayList<StudentDataModel>> newBatch = new ArrayList<>();
                            newBatch.add(CSE);                      //  Computer Science
                            newBatch.add(IT);                       //  Information Technology
                            newBatch.add(CCE);                      //  Computer & Communication
                            newBatch.add(ECE);                      //  Electronics & Communication
                            newBatch.add(EE);                       //  Electrical
                            newBatch.add(ME);                       //  Mechanical
                            newBatch.add(MECH);                     //  Mechatronics
                            newBatch.add(AE);                       //  Automobile
                            newBatch.add(CE);                       //  Civil
                            newBatch.add(CHEM);                     //  Chemical

                            String batchName = ++batchNum + " Passouts";
                            String[] department = new String[] {"Computer Science", "Information Technology",
                                    "Computer & Communication", "Electronics & Communication", "Electrical",
                                    "Mechanical", "Mechatronics", "Automobile", "Civil", "Chemical"};

                            int numOfStudents = 0;
                            for(int index = 0; index< newBatch.size(); index++) {
                                for(StudentDataModel student : newBatch.get(index)) {
                                    firestore.collection("Students").document(batchName)
                                        .collection(department[index]).document(student.getEmailAddress())
                                        .set(student)
                                        .addOnFailureListener(e ->
                                            Toast.makeText(getActivity(), student.toString() +
                                            "could not be added", Toast.LENGTH_SHORT).show());
                                }
                                numOfStudents += newBatch.get(index).size();
                            }
                            Map<String, Object> batchDetails = new HashMap<>();
                            batchDetails.put("numOfStudents", numOfStudents);
                            firestore.collection("Students").document(batchName).set(batchDetails);


                            batchList.add(0, new BatchModel(batchName, numOfStudents));
                            adapter.notifyItemInserted(0);
                            adapter.notifyItemRangeChanged(0, batchList.size()-1);
                            recyclerView.scrollToPosition(0);

                            Toast.makeText(getActivity(), sheet.getSheetName() + " Retrieved", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getActivity(),
                                "File can not be accessed. Please select the file through File Manager",
                                Toast.LENGTH_LONG).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    );
}

class BatchAdapter extends RecyclerView.Adapter<BatchAdapter.BatchViewHolder> {

    private final Context context;
    private final List<BatchModel> batchList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(BatchModel currentBatch);
    }

    public BatchAdapter(Context context, List<BatchModel> batchList) {
        this.context = context;
        this.batchList = batchList;
    }

    @NonNull
    @Override
    public BatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BatchViewHolder( LayoutInflater.from(context)
                                    .inflate(R.layout.layout_batch, parent, false) );
    }

    @Override
    public void onBindViewHolder(@NonNull  BatchAdapter.BatchViewHolder holder, int position) {
        BatchModel currentBatch = batchList.get(position);
        String students = currentBatch.getNumOfStudents() + " Students";

        holder.editTextBatchName.setText(currentBatch.getBatchName());
        holder.editTextNumOfStudents.setText(students);
        holder.itemView.setOnClickListener(view -> listener.onItemClick(currentBatch));

        switch(position % 6) {
            case 0:
            case 1:
                holder.batchHolder.setCardBackgroundColor(ContextCompat.getColor(context, R.color.brown_50));
                break;

            case 2:
            case 3:
                holder.batchHolder.setCardBackgroundColor(ContextCompat.getColor(context, R.color.blue_grey_50));
                break;

            case 4:
            case 5:
                holder.batchHolder.setCardBackgroundColor(ContextCompat.getColor(context, R.color.grey_100));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return batchList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    static class BatchViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView batchHolder;
        private final MaterialTextView editTextBatchName, editTextNumOfStudents;
        public BatchViewHolder(@NonNull View itemView) {
            super(itemView);
            batchHolder = itemView.findViewById(R.id.batchHolder);
            editTextBatchName = itemView.findViewById(R.id.batchName);
            editTextNumOfStudents = itemView.findViewById(R.id.numOfStudents);
        }
    }
}