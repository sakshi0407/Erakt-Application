package com.example.evelyn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Organization extends AppCompatActivity implements View.OnClickListener{
    public static int badhanRegistered = 0;
    public static int sandhaniRegistered = 0;
    public static int redCrescentRegistered = 0;
    public static int medicineClubRegistered = 0;

    DatabaseReference databaseReference,saveRef;
    private Toolbar mtoolbar;
    private RecyclerView recyclerView;
    private Query redCrescentQuery,badhanQuery,sandhaniQuery,medicineClubQuery;
    FirebaseAuth mAuth;
    private CardView badhanCard,sandhaniCard,redcrescentCard,medicineCard;
    private String currentUserId;
    Button register_btn,find_donor_btn,
            next1_btn,next2_btn,badhanDone,sandhaniDone,redCrescentDone,medicineClubDone,redCrescentFindDone;

    HashMap sandhaniHash,badhanHash;

    int choice;
    AutoCompleteTextView institutionText,deptText,hallNameText,areaText,badhanInstitutionText,sandhaniInstitutionText,bloodGroupText;
    TextView completeRegistration,organizationName,organizationTextView;

    String badhanInstitution,sandhaniInstitution,medicineInstituion,department,hall,area,bloodGroup,name,lastDonate,image;
    SettingsActivity settingsActivity = new SettingsActivity();
    String searchArea,searchGroup;



    String[] University = { "University of Medical Sciences", "Mumbai University for Engineering and Technology (MUET)",
            "Pune University for Engineering and Technology (PUET)",
            "Rajshahi University for Engineering and Technology (RUET)",
            "Satara University for Engineering and Technology (SUET)",
            "University of Mumbai", "LT Agriculture University", "Nashik University",
            "Amravati University", "Jijabai University of Science & Technology(JUST)", "Latur University",
            "Gondwana Agricultural University",
            "Netaji Subhas Science & Technology University",
            "Mahatma Phule Krishi Vidyapeeth",
            "Thane University of Engineering & Technology", "Powai Science & Technology University",
            "College of Engineering Pune(COEP)",
            "Government College of Engineering and Research",
            "Veermata Jijabai Technological Institute", "Aurobindo University",
            "Pune Digital University", "Walchand College of Engineering",
            "Sardar Patel College of Engineering",
            "Shri Guru Gobind Singhji Institute of Engineering and Technology",
            "Chandrapur Science and Technology University", "Laxminarayan Institute of Technology",
            "University Department of Sant Gadge Baba Amravati University", "Ratnam College", "University Institute of Chemical Technology",
            "University Department of Chemical Technology",
            "BVUCOE Pune - Bharati Vidyapeeth Deemed University College of Engineering",
            "JNEC Aurangabad - Mahatma Gandhi Mission’s Jawaharlal Nehru Engineering College", "Govt. BM College",
            "VIT Pune - Vishwakarma Institute of Technology", "Symbiosis Institute of Technology",
            "KJ Somaiya College of Engineering, Mumbai", "VNIT Nagpur - Visvesvaraya National Institute of Technology",
    };



    String[] halls = { "Balaji Mangal Karyalay", "Disciples Community Church",
            "Shradha Exhibition Hall", "Janta Hall,DU", "Shankar Rao Sonawane Sabhagruha", "Sunvill Banquet & Conference Rooms",
            "Hublikar Hall", "Brahman Sahayyak Sangh", "Sana Community Hall", "Kamayani's Hall",
            "Tatya Bapat Smruti Samati", "P V D Paluskar Sanskrutic Bhavan",
            "The C K P Club", "Ram Bhau Mahalvi Probhadni",
            "Balaji Mandir & Maruti Mandir", "Saraswat Mandal",
            "Radhakrupa Ateethi Gruh", "Gomantak Seva Sangh", "Ramchandra Sabhamandap",
            "Ishwani Kendra", "MU Hostel", "Ramkrishna Hostel,DU",
            "Samaj Hall", "Khamla Plot Holder Co-Op Hsg Socy Limited",
            "Jay Bhagvan Sabhagruha Kurla", "Bal Gopal Mitra Mandal Community Hall", "Ashish Hall",
            "Santoshi Mata Hall Booking Office", "Radhakrupa Ateethi Gruh", "Elegant Hall", "Dnyanesh Mangal Karyalaya",
            "Radhakrupa Ateethi Gruh", "Bal Gopal Mitra Mandal Community Hall",
    };


    String[] sandhaniUnit =  {  "Pune Digital University", "Walchand College of Engineering",
            "Sardar Patel College of Engineering",
            "Shri Guru Gobind Singhji Institute of Engineering and Technology",
            "Chandrapur Science and Technology University", "Laxminarayan Institute of Technology",
            "University Department of Sant Gadge Baba Amravati University", "Ratnam College", "University Institute of Chemical Technology",
            "University Department of Chemical Technology",
            "BVUCOE Pune - Bharati Vidyapeeth Deemed University College of Engineering",
            "JNEC Aurangabad - Mahatma Gandhi Mission’s Jawaharlal Nehru Engineering College", "Govt. BM College",
            "VIT Pune - Vishwakarma Institute of Technology", "Symbiosis Institute of Technology",
            "KJ Somaiya College of Engineering, Mumbai", "VNIT Nagpur - Visvesvaraya National Institute of Technology", };

    String[] medicineClub = { "AFMC Pune - Armed Forces Medical College", "Grant Medical College, Mumbai",
            "DYPMC Pune - Dr D Y Patil Medical College Hospital and Research Centre", "GSMC Mumbai - Seth GS Medical College",
            "Mumbai Medical College", "Delta Medical College", "Tilak Ayurved Mahavidyalaya, Pune",
            "BJMC Pune - BJ Government Medical College", "Latur Medical College",
            "AIIPMR Mumbai - All India Institute of Physical Medicine and Rehabilitation", "Kasturba Health Societys Mahatma Gandhi Institute of Medical Sciences",
            "MGM Medical College, Aurangabad", "Rajasthani Medical College", "Amravati Medical College",
            "Thane Medical College", "Lokmanya Tilak Medical College", "LTMMC Mumbai - Lokmanya Tilak Municipal Medical College",
            "TNMC Mumbai - Topiwala National Medical College and BYL Nair Charitable Hospital " };

    ArrayAdapter<String> areaAdapter,institutionAdapter,hallAdapter,sandhaniAdapter,medicineClubAdapter,bloodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);




        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        saveRef = FirebaseDatabase.getInstance().getReference();

        mtoolbar = (Toolbar)findViewById(R.id.organization_toolbar_id);



        organizationTextView = (TextView)findViewById(R.id.organization_textView);
        badhanCard = (CardView)findViewById(R.id.badhanCardview);
        sandhaniCard = (CardView)findViewById(R.id.sandhaniCardview);
        redcrescentCard = (CardView)findViewById(R.id.redcrescnetCardview);
        medicineCard =(CardView)findViewById(R.id.medicineClubCardview);

        recyclerView = (RecyclerView)findViewById(R.id.organization_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);




        register_btn = (Button)findViewById(R.id.register_button);
        find_donor_btn = (Button)findViewById(R.id.Find_donor_organization_button);
        next1_btn = (Button)findViewById(R.id.next1_button);
        next2_btn = (Button)findViewById(R.id.next2_button);

        institutionText = (AutoCompleteTextView)findViewById(R.id.institution_edittext);
        areaText = (AutoCompleteTextView)findViewById(R.id.area_edittext);
        deptText = (AutoCompleteTextView)findViewById(R.id.department_edittext);
        hallNameText = (AutoCompleteTextView)findViewById(R.id.hall_edittext);
        completeRegistration = (TextView)findViewById(R.id.completeRegistrationTextview);
        organizationName = (TextView)findViewById(R.id.organization_name_Textview);
        badhanInstitutionText = (AutoCompleteTextView)findViewById(R.id.badhan_institution_edittext);
        sandhaniInstitutionText = (AutoCompleteTextView)findViewById(R.id.sandhani_institution_edittext);
        bloodGroupText = (AutoCompleteTextView)findViewById(R.id.organization_blood_group_edittext);


        badhanDone = (Button)findViewById(R.id.badhan_done_button);
        sandhaniDone = (Button)findViewById(R.id.sandhani_done_button);
        redCrescentDone = (Button)findViewById(R.id.redCrescent_done_button);
        medicineClubDone = (Button)findViewById(R.id.medicine_club_done_button);
        redCrescentFindDone = (Button)findViewById(R.id.redCrescent_find_done_button);

        badhanCard.setOnClickListener(this);
        sandhaniCard.setOnClickListener(this);
        redcrescentCard.setOnClickListener(this);
        medicineCard.setOnClickListener(this);


        register_btn.setOnClickListener(this);
        find_donor_btn.setOnClickListener(this);
        next2_btn.setOnClickListener(this);
        next1_btn.setOnClickListener(this);

        areaAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,settingsActivity.districts);
        areaText.setThreshold(1);
        areaText.setAdapter(areaAdapter);

        institutionAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,University);
        badhanInstitutionText.setThreshold(1);
        badhanInstitutionText.setAdapter(institutionAdapter);


        hallAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,halls);
        hallNameText.setThreshold(1);
        hallNameText.setAdapter(hallAdapter);

        sandhaniAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,sandhaniUnit);
        sandhaniInstitutionText.setThreshold(1);
        sandhaniInstitutionText.setAdapter(sandhaniAdapter);


        medicineClubAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,medicineClub);
        institutionText.setThreshold(1);
        institutionText.setAdapter(medicineClubAdapter);

        bloodAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,settingsActivity.groups);
        bloodGroupText.setThreshold(1);
        bloodGroupText.setAdapter(bloodAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.badhanCardview){

            goneButton();
            organizationName.setText("NSS");
            if(badhanRegistered == 0){
                register_btn.setVisibility(View.VISIBLE);
            }
            find_donor_btn.setVisibility(View.VISIBLE);
            choice = 3;

        }

        if(v.getId() == R.id.redcrescnetCardview){
            goneButton();
            organizationName.setText("RED CROSS");

            if(redCrescentRegistered == 0){
                register_btn.setVisibility(View.VISIBLE);
            }

            find_donor_btn.setVisibility(View.VISIBLE);
            choice = 1;
        }

        if(v.getId() == R.id.sandhaniCardview){
            goneButton();
            organizationName.setText("ROTARY");
            if(sandhaniRegistered == 0){
                register_btn.setVisibility(View.VISIBLE);
            }

            find_donor_btn.setVisibility(View.VISIBLE);
            choice = 2;
        }
        if(v.getId() == R.id.medicineClubCardview){
            goneButton();
            organizationName.setText("Medicine Club");
            if(medicineClubRegistered == 0){
                register_btn.setVisibility(View.VISIBLE);
            }
            find_donor_btn.setVisibility(View.VISIBLE);
            choice = 4;
        }

        if(v.getId() == R.id.register_button){
            register_btn.setVisibility(View.GONE);
            completeRegistration.setVisibility(View.VISIBLE);
            find_donor_btn.setVisibility(View.GONE);
            databaseReference.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    bloodGroup = dataSnapshot.child("bloodGroup").getValue().toString();
                    name = dataSnapshot.child("name").getValue().toString();
                    lastDonate = dataSnapshot.child("lastDate").getValue().toString();
                    image = dataSnapshot.child("imageUrl").getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            if(choice == 3){
               badhanInstitutionText.setVisibility(View.VISIBLE);
               next1_btn.setVisibility(View.VISIBLE);
               next1_btn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                        badhanInstitution = badhanInstitutionText.getText().toString();
                        if(TextUtils.isEmpty(badhanInstitution)){
                            badhanInstitutionText.requestFocus();
                            badhanInstitutionText.setError("please enter your institution name");
                            return;
                        }

                        else{
                            next1_btn.setVisibility(View.GONE);
                            hallNameText.setVisibility(View.VISIBLE);
                            badhanDone.setVisibility(View.VISIBLE);
                            badhanDone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    hall = hallNameText.getText().toString();
                                    if(TextUtils.isEmpty(hall)){
                                        sandhaniHash = new HashMap();
                                        sandhaniHash.put("institution",badhanInstitution);
                                        saveRef.child("Badhan").child(bloodGroup).child(currentUserId).setValue(sandhaniHash);
                                    }

                                    else{
                                        badhanHash = new HashMap();
                                        badhanHash.put("institution",badhanInstitution);
                                        badhanHash.put("hallName",hall);
                                        saveRef.child("Badhan").child(bloodGroup).child(currentUserId).setValue(badhanHash);
                                    }
                                    Toast.makeText(getApplicationContext(),"Welcome to BADHAN Family",Toast.LENGTH_SHORT).show();
                                    badhanRegistered = 1;
                                    visibleButtons();
                                    badhanDone.setVisibility(View.GONE);
                                }
                            });

                        }

                   }
               });
            }

            if(choice == 1){

                areaText.setVisibility(View.VISIBLE);
                redCrescentDone.setVisibility(View.VISIBLE);
                redCrescentDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        area = areaText.getText().toString();
                        if(TextUtils.isEmpty(area)){
                            areaText.requestFocus();
                            areaText.setError("please enter your Area Name");
                            return;
                        }
                        else{

                            sandhaniHash = new HashMap();
                            sandhaniHash.put("institution",area);
                            redCrescentRegistered = 1;
                            visibleButtons();
                            saveRef.child("RedCrescent").child(bloodGroup).child(currentUserId).setValue(sandhaniHash);
                            redCrescentDone.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Welcome to Red Crescent Family",Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }

            if(choice == 2){
                sandhaniInstitutionText.setVisibility(View.VISIBLE);
                sandhaniDone.setVisibility(View.VISIBLE);
                sandhaniDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        sandhaniInstitution = sandhaniInstitutionText.getText().toString();
                        if(TextUtils.isEmpty(sandhaniInstitution)){
                            sandhaniInstitutionText.requestFocus();
                            sandhaniInstitutionText.setError("please enter your institution name");
                            return;
                        }
                        else{
                            sandhaniHash = new HashMap();
                            sandhaniHash.put("institution",sandhaniInstitution);

                            visibleButtons();
                            saveRef.child("Sandhani").child(bloodGroup).child(currentUserId).setValue(sandhaniHash);
                            sandhaniRegistered = 1;
                            visibleButtons();
                            sandhaniDone.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Welcome to SANDHANI Family",Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }

            if(choice == 4){
                institutionText.setVisibility(View.VISIBLE);
                medicineClubDone.setVisibility(View.VISIBLE);
                medicineClubDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        medicineInstituion = institutionText.getText().toString();
                        if(TextUtils.isEmpty(medicineInstituion)){
                            institutionText.requestFocus();
                            institutionText.setError("please enter your institution name");
                            return;
                        }
                        else{
                            sandhaniHash = new HashMap();
                            sandhaniHash.put("institution",medicineInstituion);
                            saveRef.child("MedicineClub").child(bloodGroup).child(currentUserId).setValue(sandhaniHash);
                            medicineClubRegistered = 1;
                            visibleButtons();
                            medicineClubDone.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Welcome to MEDICINE CLUB Family",Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }

        }


        if(v.getId() == R.id.Find_donor_organization_button){
            register_btn.setVisibility(View.GONE);
            find_donor_btn.setVisibility(View.GONE);
            completeRegistration.setVisibility(View.VISIBLE);
            completeRegistration.setText("Find Donor in this organization...");


            if(choice == 1){
               bloodGroupText.setVisibility(View.VISIBLE);
               areaText.setVisibility(View.VISIBLE);
               redCrescentFindDone.setVisibility(View.VISIBLE);
               redCrescentFindDone.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       showItem();
                   }
               });

            }

             if(choice == 2){
                 bloodGroupText.setVisibility(View.VISIBLE);
                  sandhaniInstitutionText.setVisibility(View.VISIBLE);
                  sandhaniDone.setVisibility(View.VISIBLE);
                  sandhaniDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showItem();
                        }
                    });
                }
                if(choice == 3){
                    bloodGroupText.setVisibility(View.VISIBLE);
                    badhanInstitutionText.setVisibility(View.VISIBLE);
                    badhanDone.setVisibility(View.VISIBLE);
                    badhanDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showItem();
                        }
                    });
                }
                if(choice == 4){
                    bloodGroupText.setVisibility(View.VISIBLE);
                    institutionText.setVisibility(View.VISIBLE);
                    medicineClubDone.setVisibility(View.VISIBLE);
                    medicineClubDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showItem();
                        }
                    });
                }



        }
    }

    private void showItem() {
        completeRegistration.setVisibility(View.GONE);
        organizationName.setVisibility(View.VISIBLE);
        areaText.setVisibility(View.GONE);
        institutionText.setVisibility(View.GONE);
        badhanInstitutionText.setVisibility(View.GONE);
        sandhaniInstitutionText.setVisibility(View.GONE);
        sandhaniDone.setVisibility(View.GONE);
        badhanDone.setVisibility(View.GONE);
        medicineClubDone.setVisibility(View.GONE);
        bloodGroupText.setVisibility(View.GONE);
        redCrescentFindDone.setVisibility(View.GONE);

        recyclerView.setVisibility(View.VISIBLE);


        if(choice == 1){
            searchArea = areaText.getText().toString();
            searchGroup = bloodGroupText.getText().toString();
            redCrescentQuery = FirebaseDatabase.getInstance().getReference("RedCrescent").child(searchGroup).orderByChild("institution").equalTo(searchArea);

        }
        if(choice == 2){
            searchArea = sandhaniInstitutionText.getText().toString();
            searchGroup = bloodGroupText.getText().toString();
            redCrescentQuery = FirebaseDatabase.getInstance().getReference("Sandhani").child(searchGroup).orderByChild("institution").equalTo(searchArea);

        }
        if(choice == 3){
            searchArea = badhanInstitutionText.getText().toString();
            searchGroup = bloodGroupText.getText().toString();
            redCrescentQuery = FirebaseDatabase.getInstance().getReference("Badhan").child(searchGroup).orderByChild("institution").equalTo(searchArea);

        }
        if(choice == 4){
            searchArea = institutionText.getText().toString();
            searchGroup = bloodGroupText.getText().toString();
            redCrescentQuery = FirebaseDatabase.getInstance().getReference("MedicineClub").child(searchGroup).orderByChild("institution").equalTo(searchArea);

        }

        FirebaseRecyclerOptions<Upload> redCrescentOptions =
                new FirebaseRecyclerOptions.Builder<Upload>()
                .setQuery(redCrescentQuery,Upload.class)
                .build();

       FirebaseRecyclerAdapter<Upload, Organization.organizationResultViewHolder> redCrescentAdapter
               = new FirebaseRecyclerAdapter<Upload, organizationResultViewHolder>(redCrescentOptions) {
           @Override
           protected void onBindViewHolder(@NonNull final organizationResultViewHolder organizationResultViewHolder, final int it, @NonNull Upload upload) {

               final String user = getRef(it).getKey();
               databaseReference.child("Users").child(user).addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       final String organ_name = dataSnapshot.child("name").getValue().toString();
                       final String organ_image = dataSnapshot.child("imageUrl").getValue().toString();
                       final String organ_lastDate = dataSnapshot.child("lastDate").getValue().toString();

                       organizationResultViewHolder.nameTextView.setText(organ_name);
                       organizationResultViewHolder.lastDonateTextView.setText("Last Donate : "+organ_lastDate);
                       organizationResultViewHolder.groupTextView.setText("Blood Group : "+ searchGroup);
                       organizationResultViewHolder.institutionTextView.setText(searchArea);
                       Picasso.get().load(organ_image).into(organizationResultViewHolder.circleImageView);

                       organizationResultViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               String visit_user_Id = getRef(it).getKey();
                               Intent AccountIntent = new Intent(Organization.this,AccountActivity.class);
                               AccountIntent.putExtra("visit_user_Id",visit_user_Id);
                               startActivity(AccountIntent);
                           }
                       });

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });
                organizationResultViewHolder.nameTextView.setText(upload.getName());
                organizationResultViewHolder.lastDonateTextView.setText("Last Donate : "+upload.getLastDate());
                organizationResultViewHolder.groupTextView.setText("Blood Group : "+ searchGroup);
                organizationResultViewHolder.institutionTextView.setText(searchArea);
                Picasso.get().load(upload.getImageUrl()).into(organizationResultViewHolder.circleImageView);

                organizationResultViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String visit_user_Id = getRef(it).getKey();
                        Intent AccountIntent = new Intent(Organization.this,AccountActivity.class);
                        AccountIntent.putExtra("visit_user_Id",visit_user_Id);
                        //AccountIntent.putExtra("Institution",searchArea);
                        startActivity(AccountIntent);
                    }
                });

           }



           @NonNull
           @Override
           public organizationResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

               View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.organization_recycler,parent,false);
               Organization.organizationResultViewHolder holder = new Organization.organizationResultViewHolder(view);
               return holder;
           }
       };

       recyclerView.setAdapter(redCrescentAdapter);
       redCrescentAdapter.startListening();

    }

    public static class organizationResultViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView,groupTextView,lastDonateTextView,institutionTextView;
        CircleImageView circleImageView;
        public organizationResultViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.recycler_name_id);
            groupTextView = itemView.findViewById(R.id.recycler_blood_Group_id);
            lastDonateTextView = itemView.findViewById(R.id.recycler_donation_id);
            institutionTextView = itemView.findViewById(R.id.recycler_institution_id);
            circleImageView = itemView.findViewById(R.id.recycler_profile_image);

        }
    }

    private void goneButton() {

        organizationName.setVisibility(View.VISIBLE);
        organizationTextView.setVisibility(View.GONE);
        badhanCard.setVisibility(View.GONE);
        sandhaniCard.setVisibility(View.GONE);
        redcrescentCard.setVisibility(View.GONE);
        medicineCard.setVisibility(View.GONE);
        mtoolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Organizations");
    }

    private void visibleButtons() {
        areaText.setVisibility(View.GONE);
        organizationTextView.setVisibility(View.VISIBLE);
        mtoolbar.setVisibility(View.GONE);
        badhanCard.setVisibility(View.VISIBLE);
        sandhaniCard.setVisibility(View.VISIBLE);
        redcrescentCard.setVisibility(View.VISIBLE);
        medicineCard.setVisibility(View.VISIBLE);
        institutionText.setVisibility(View.GONE);
        deptText.setVisibility(View.GONE);
        hallNameText.setVisibility(View.GONE);
        redCrescentDone.setVisibility(View.GONE);
        completeRegistration.setVisibility(View.GONE);
        organizationName.setVisibility(View.GONE);
        badhanInstitutionText.setVisibility(View.GONE);
        sandhaniInstitutionText.setVisibility(View.GONE);
    }
}
