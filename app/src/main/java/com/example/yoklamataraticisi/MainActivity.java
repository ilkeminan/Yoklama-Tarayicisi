package com.example.yoklamataraticisi;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.yalantis.ucrop.UCrop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinnerNumberOfWeeks;
    TextView textViewNumberOfWeeks, textViewFirstWeek;
    EditText editTextFirstWeek;
    ImageButton buttonChooseImage, buttonUseCamera, imageButtonConfirm, homeButton, logoutButton;
    Bitmap bitmap = null, bmBlackAndWhite = null, bmMarked = null;
    List<Integer> lineListX;
    List<Integer> lineListY;
    ArrayList<Attendance> attendances;
    ArrayList<Student> students;
    Course course;
    int numberOfWeeks, firstWeek;
    ProgressBar progressBar;
    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        textViewNumberOfWeeks = (TextView) this.findViewById(R.id.textViewNumberOfWeeks);
        textViewFirstWeek = (TextView) this.findViewById(R.id.textViewFirstWeek);
        editTextFirstWeek = (EditText) this.findViewById(R.id.editTextFirstWeek);
        spinnerNumberOfWeeks = (Spinner) this.findViewById(R.id.spinnerNumberOfWeeks);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNumberOfWeeks.setAdapter(adapter);
        spinnerNumberOfWeeks.setOnItemSelectedListener(this);
        spinnerNumberOfWeeks.setSelection(3);
        Intent intentMainActivity = getIntent();
        course = (Course) intentMainActivity.getSerializableExtra("course");
        students = new ArrayList<Student>();
        attendances = new ArrayList<Attendance>();
        buttonChooseImage = (ImageButton) this.findViewById(R.id.buttonChooseImage);
        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editTextFirstWeek.getText().toString().equals("") && !editTextFirstWeek.getText().toString().contains(".")) {
                    firstWeek = Integer.parseInt(editTextFirstWeek.getText().toString());
                    if (firstWeek > 0 && firstWeek <= 14) {
                        int sheetNumber = ((firstWeek - 1) / 4) + 1;
                        if (firstWeek + numberOfWeeks - 1 <= sheetNumber * 4 && firstWeek + numberOfWeeks - 1 <= 14) {
                            Toast.makeText(getApplicationContext(),"Yoklama taraması yaklaşık 90 saniye sürebilir.",Toast.LENGTH_LONG).show();
                            Intent intentChooseImage = new Intent();
                            intentChooseImage.setType("image/*");
                            intentChooseImage.setAction(Intent.ACTION_PICK);
                            startActivityForResult(intentChooseImage, 1);
                        } else {
                            Toast.makeText(getApplicationContext(), "Yoklama kağıdında olmayan haftalar belirlenemez.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Geçerli bir hafta giriniz.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Geçerli bir hafta giriniz.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        homeButton = this.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ActivityChooseCourse.class);
                startActivity(intent);
            }
        });
        logoutButton = this.findViewById(R.id.imageButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ActivityLogin.class);
                startActivity(intent);
            }
        });
        buttonUseCamera = (ImageButton) this.findViewById(R.id.buttonCamera);
        buttonUseCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextFirstWeek.getText().toString().equals("") && !editTextFirstWeek.getText().toString().contains(".")) {
                    firstWeek = Integer.parseInt(editTextFirstWeek.getText().toString());
                    if (firstWeek > 0 && firstWeek <= 14) {
                        int sheetNumber = ((firstWeek - 1) / 4) + 1;
                        if (firstWeek + numberOfWeeks - 1 <= sheetNumber * 4 && firstWeek + numberOfWeeks - 1 <= 14) {
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                                    //permissions not enabled. request them.
                                    String [] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                    requestPermissions(permissions, 1000);
                                }
                                else{
                                    //permissions already granted.
                                    openCamera();
                                }
                            }
                            else{
                                openCamera();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Yoklama kağıdında olmayan haftalar belirlenemez.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Geçerli bir hafta giriniz.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Geçerli bir hafta giriniz.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        imageButtonConfirm = (ImageButton) this.findViewById(R.id.imageButtonConfirm);
        imageButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attendances.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Lütfen imza kağıdı ekleyin.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, ActivityChangeOrConfirmAttendance.class);
                    intent.putExtra("numberofweeks", numberOfWeeks);
                    intent.putExtra("firstweek", firstWeek);
                    intent.putExtra("course", course);
                    intent.putExtra("attendance", attendances);
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    bmMarked.compress(Bitmap.CompressFormat.JPEG,50,bs);
                    intent.putExtra("attendanceList",bs.toByteArray());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        numberOfWeeks = Integer.parseInt(text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1000 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            openCamera();
        }
        else{
            Toast.makeText(getApplicationContext(), "Permission denied.", Toast.LENGTH_SHORT).show();
        }
    }

    public void openCamera(){
        Toast.makeText(getApplicationContext(),"Yoklama taraması yaklaşık 90 saniye sürebilir.",Toast.LENGTH_LONG).show();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent intentUseCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentUseCamera.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(intentUseCamera, 2);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean ilkSayfa = true;
        if (bitmap != null) {
            bitmap.recycle();
            bmBlackAndWhite.recycle();
            if(bmMarked != null){    //If second sheet is scanned. Else is column error.
                bmMarked.recycle();
                ilkSayfa = false;
            }
            lineListX.removeAll(lineListX);
            lineListY.removeAll(lineListY);
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                startCrop(uri);
            } else if (requestCode == 2) {
                startCrop(image_uri);
            } else if (requestCode == UCrop.REQUEST_CROP) {
                Uri imageUriResultCrop = UCrop.getOutput(data);
                if (imageUriResultCrop != null) {
                    try {
                        InputStream stream = getContentResolver().openInputStream(imageUriResultCrop);
                        bitmap = BitmapFactory.decodeStream(stream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                doComputations(ilkSayfa);
            }
        }
    }

    private void startCrop(@NonNull Uri uri) {
        String destinationFileName = "SampleCropImg";
        destinationFileName = destinationFileName + ".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        uCrop.withAspectRatio(1, 1);
        //uCrop.withMaxResultSize(900,900);
        uCrop.withOptions(getCropOptions());
        uCrop.start(MainActivity.this);
    }

    private UCrop.Options getCropOptions() {
        UCrop.Options options = new UCrop.Options();
        //options.setCompressionQuality(70);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        options.setToolbarTitle("Crop image");
        return options;
    }

    public void doComputations(Boolean ilkSayfa) {
        turnImageToBlackAndWhite();
        boolean columns_recognized = lineRecognizer();
        if(columns_recognized == true){
            Bitmap images[][] = splitBitmap();
            String[][] textsFromImages = new String[lineListX.size()][lineListY.size()];
            for (int i = 0; i < lineListX.size() - 1; i++) {
                for (int j = 0; j < lineListY.size() - 1; j++) {
                    textsFromImages[i][j] = textRecognizer(images[i][j]);
                    if (j == 1) {
                        textsFromImages[i][j] = textsFromImages[i][j].replace(" ", "");
                    }
                }
            }
            int numberOfStudents = lineListX.size() - 1;
            String[][] attendance = new String[numberOfStudents][numberOfWeeks];
            int [][] x_coordinates_of_absentees = new int[100][2];
            int [][] y_coordinates_of_absentees = new int[100][2];
            int number_of_absentees = 0;
            int i;
            if (ilkSayfa == true) {
                i = 1;
            } else {
                i = 0;
            }
            while (i < numberOfStudents) {
                for (int j = 0; j < numberOfWeeks; j++) {
                    if (determineAbsentees(images[i][j + 4 + ((firstWeek - 1) % 4)]) == true) {
                        attendance[i][j] = "false";
                        x_coordinates_of_absentees[number_of_absentees][0] = lineListX.get(i);
                        y_coordinates_of_absentees[number_of_absentees][0] = lineListY.get(j + 4 + ((firstWeek - 1) % 4));
                        x_coordinates_of_absentees[number_of_absentees][1] = lineListX.get(i+1);
                        y_coordinates_of_absentees[number_of_absentees][1] = lineListY.get(j + 5 + ((firstWeek - 1) % 4));
                        number_of_absentees++;
                    } else {
                        attendance[i][j] = "true";
                    }
                }
                i++;
            }
            int [][] x_coordinates_of_fake_signatures = new int[100][2];
            int [][] y_coordinates_of_fake_signatures = new int[100][2];
            int number_of_fake_signatures = 0;
            if (ilkSayfa == true) {
                i = 1;
            } else {
                i = 0;
            }
            while (i < numberOfStudents) {
                if (numberOfWeeks == 1 && firstWeek > 1 && attendance[i][0].equals("true")) {
                    if (compareImages(images[i][3 + ((firstWeek - 1) % 4)], images[i][4 + ((firstWeek - 1) % 4)]) == false) {
                        attendance[i][0] = "fake";
                        x_coordinates_of_fake_signatures[number_of_fake_signatures][0] = lineListX.get(i);
                        y_coordinates_of_fake_signatures[number_of_fake_signatures][0] = lineListY.get(4 + ((firstWeek - 1) % 4));
                        x_coordinates_of_fake_signatures[number_of_fake_signatures][1] = lineListX.get(i+1);
                        y_coordinates_of_fake_signatures[number_of_fake_signatures][1] = lineListY.get(5 + ((firstWeek - 1) % 4));
                        number_of_fake_signatures++;
                    }
                }
                for (int j = 0; j < numberOfWeeks - 1; j++) {
                    if (attendance[i][j].equals("true") && attendance[i][j+1].equals("true")) {
                        if (compareImages(images[i][j + 4 + ((firstWeek - 1) % 4)], images[i][j + 5 + ((firstWeek - 1) % 4)]) == false) {
                            attendance[i][j + 1] = "fake";
                            x_coordinates_of_fake_signatures[number_of_fake_signatures][0] = lineListX.get(i);
                            y_coordinates_of_fake_signatures[number_of_fake_signatures][0] = lineListY.get(j + 5 + ((firstWeek - 1) % 4));
                            x_coordinates_of_fake_signatures[number_of_fake_signatures][1] = lineListX.get(i+1);
                            y_coordinates_of_fake_signatures[number_of_fake_signatures][1] = lineListY.get(j + 6 + ((firstWeek - 1) % 4));
                            number_of_fake_signatures++;
                        }
                    }
                    else if (j >= 1 && attendance[i][j - 1].equals("true") && attendance[i][j+1].equals("true")) {
                        if (compareImages(images[i][j + 4 + ((firstWeek - 1) % 4) - 1], images[i][j + 6 + ((firstWeek - 1) % 4) - 1]) == false) {
                            attendance[i][j + 1] = "fake";
                            x_coordinates_of_fake_signatures[number_of_fake_signatures][0] = lineListX.get(i);
                            y_coordinates_of_fake_signatures[number_of_fake_signatures][0] = lineListY.get(j + 6 + ((firstWeek - 1) % 4) - 1);
                            x_coordinates_of_fake_signatures[number_of_fake_signatures][1] = lineListX.get(i+1);
                            y_coordinates_of_fake_signatures[number_of_fake_signatures][1] = lineListY.get(j + 7 + ((firstWeek - 1) % 4) - 1);
                            number_of_fake_signatures++;
                        }
                    }
                }
                i++;
            }
            if (ilkSayfa == true) {
                i = 1;
            } else {
                i = 0;
            }
            while (i < numberOfStudents) {
                Student student = new Student(textsFromImages[i][1], textsFromImages[i][2], textsFromImages[i][3]);
                Attendance an_attendance = new Attendance(student, course, firstWeek, numberOfWeeks);
                for (int j = 0; j < numberOfWeeks; j++) {
                    if (attendance[i][j].equals("false")) {
                        int number_of_absenteeism = an_attendance.absenteeism(j + firstWeek - 1);
                    }
                    else if (attendance[i][j].equals("fake")) {
                        an_attendance.determine_fake_signatures(j + firstWeek - 1);
                    }
                }
                attendances.add(an_attendance);
                i++;
            }
            bmMarked = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bmMarked);
            c.drawBitmap(bitmap, 0, 0, new Paint());
            for(i=0;i<number_of_fake_signatures;i++){
                for(int k=x_coordinates_of_fake_signatures[i][0];k<x_coordinates_of_fake_signatures[i][1];k++){
                    for(int j=0;j<2;j++){
                        try{
                            bmMarked.setPixel(y_coordinates_of_fake_signatures[i][0]+j,k,Color.BLUE);
                        }
                        catch(Exception e){

                        }
                        try{
                            bmMarked.setPixel(y_coordinates_of_fake_signatures[i][1]+j,k,Color.BLUE);
                        }
                        catch(Exception e){

                        }
                    }
                }
                for(int k=y_coordinates_of_fake_signatures[i][0];k<y_coordinates_of_fake_signatures[i][1];k++){
                    for(int j=0;j<2;j++){
                        try{
                            bmMarked.setPixel(k,x_coordinates_of_fake_signatures[i][0]+j,Color.BLUE);
                        }
                        catch(Exception e){

                        }
                        try{
                            bmMarked.setPixel(k,x_coordinates_of_fake_signatures[i][1]+j,Color.BLUE);
                        }
                        catch(Exception e){

                        }
                    }
                }
            }
            for(i=0;i<number_of_absentees;i++){
                for(int k=x_coordinates_of_absentees[i][0];k<x_coordinates_of_absentees[i][1];k++){
                    for(int j=0;j<2;j++){
                        try{
                            bmMarked.setPixel(y_coordinates_of_absentees[i][0]+j,k,Color.RED);
                        }
                        catch(Exception e){

                        }
                        try{
                            bmMarked.setPixel(y_coordinates_of_absentees[i][1]+j,k,Color.RED);
                        }
                        catch(Exception e){

                        }
                    }
                }
                for(int k=y_coordinates_of_absentees[i][0];k<y_coordinates_of_absentees[i][1];k++){
                    for(int j=0;j<2;j++){
                        try{
                            bmMarked.setPixel(k,x_coordinates_of_absentees[i][0]+j,Color.RED);
                        }
                        catch(Exception e){

                        }
                        try{
                            bmMarked.setPixel(k,x_coordinates_of_absentees[i][1]+j,Color.RED);
                        }
                        catch(Exception e){

                        }
                    }
                }
            }
            Toast.makeText(getApplicationContext(), "Yoklama kağıdı taratıldı. Başka yoklama kağıdı varsa ekleyebilirsiniz.", Toast.LENGTH_LONG).show();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Hata");
            builder.setMessage("Sütunlar doğru algılanamadı."+"\n"+"Lütfen yeniden fotoğraf çekerek deneyiniz.");
            builder.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.show();
        }
    }

    public void turnImageToBlackAndWhite() {
        bmBlackAndWhite = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmBlackAndWhite);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    public boolean lineRecognizer() {
        int x, y, count;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        boolean[][] isBlack = new boolean[width][height];
        String red, green, blue;
        for (x = 0; x < width; x++) {
            for (y = 0; y < height; y++) {
                red = Integer.toHexString(bitmap.getPixel(x, y)).substring(2, 4);
                green = Integer.toHexString(bitmap.getPixel(x, y)).substring(4, 6);
                blue = Integer.toHexString(bitmap.getPixel(x, y)).substring(6, 8);
                if (red.compareTo("68") < 0 || green.compareTo("68") < 0 || blue.compareTo("68") < 0) {
                    isBlack[x][y] = true;
                } else {
                    isBlack[x][y] = false;
                }
            }
        }
        lineListX = new ArrayList<Integer>();
        lineListY = new ArrayList<Integer>();
        boolean[] markedX = new boolean[width];
        boolean[] markedY = new boolean[height];
        for (y = 0; y < height; y++) {
            markedY[y] = false;
        }
        //If column lines are not recognized well, we should change the percentage.
        int repeat = 0;
        while(lineListY.size() != 9 && repeat < 5){
            lineListY.removeAll(lineListY);
            int height_percentage;
            if(repeat == 0){
                height_percentage = 33;
            }
            else if(repeat == 1){
                height_percentage = 34;
            }
            else if(repeat == 2){
                height_percentage = 32;
            }
            else if(repeat == 3){
                height_percentage = 35;
            }
            else{
                height_percentage = 31;
            }
            for (x = 0; x < width; x++) {
                markedX[x] = false;
            }
            for (x = 0; x < width; x++) {
                y = 0;
                count = 0;
                while (y < height - 1) {
                    if (isBlack[x][y]) {
                        count++;
                    }
                    y++;
                }
                if (count > height * height_percentage / 100 && x >= 5 && !markedX[x] && !markedX[x - 1] && !markedX[x - 2] && !markedX[x - 3] && !markedX[x - 4] && !markedX[x - 5]) {
                    lineListY.add(x);
                    markedX[x] = true;
                }
            }
            repeat++;
        }
        boolean columns_recognized;
        if(lineListY.size() == 9){
            for (y = 0; y < height; y++) {
                x = 0;
                count = 0;
                while (x < width - 1) {
                    if (isBlack[x][y]) {
                        count++;
                    }
                    x++;
                }
                if (count > width * 32 / 100 && y >= 5 && !markedY[y] && !markedY[y - 1] && !markedY[y - 2] && !markedY[y - 3] && !markedY[y - 4] && !markedY[y - 5]) {
                    lineListX.add(y);
                    markedY[y] = true;
                }
            }
            int[] diff = new int[lineListX.size() - 1];
            for (int i = 0; i < lineListX.size() - 1; i++) {
                diff[i] = lineListX.get(i + 1) - lineListX.get(i);
            }
            int mode_of_diff = diff[0];
            int maxCount = 0;
            for (int i = 0; i < diff.length; i++) {
                int value = diff[i];
                count = 1;
                for (int j = 0; j < diff.length; j++) {
                    if (diff[j] == value) {
                        count++;
                    }
                    if (count > maxCount) {
                        mode_of_diff = value;
                        maxCount = count;
                    }
                }
            }
            for (int i = 0; i < lineListX.size() - 1; i++) {
                if (lineListX.get(i) + mode_of_diff + 10 < lineListX.get(i + 1)) {
                    lineListX.add(i + 1, lineListX.get(i) + mode_of_diff);
                }
                if (lineListX.get(0) - mode_of_diff - 10 > 0) {
                    lineListX.add(0, lineListX.get(0) - mode_of_diff);
                }
            }
            for (int i = 0; i < lineListX.size() - 1; i++) {      //Extra detected false rows are removed.
                if (lineListX.get(i) + mode_of_diff - 10 > lineListX.get(i + 1)) {
                    lineListX.remove(i+1);
                }
            }
            System.out.println(lineListX);
            System.out.println(lineListY);
            columns_recognized = true;
        }
        else{
            columns_recognized = false;
        }
        return columns_recognized;
    }

    public Bitmap[][] splitBitmap() {
        Bitmap picture = bmBlackAndWhite;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(picture, picture.getWidth(), picture.getHeight(), true);
        Bitmap[][] imgs;
        if (lineListX.size() > 0 && lineListY.size() > 0) {
            imgs = new Bitmap[lineListX.size() - 1][lineListY.size() - 1];
            for (int y = 1; y < lineListY.size(); y++) {
                for (int x = 1; x < lineListX.size(); x++) {
                    imgs[x - 1][y - 1] = Bitmap.createBitmap(scaledBitmap, lineListY.get(y - 1), lineListX.get(x - 1), lineListY.get(y) - lineListY.get(y - 1), lineListX.get(x) - lineListX.get(x - 1));
                }
            }
        } else {
            imgs = new Bitmap[lineListX.size()][lineListY.size()];
            System.out.println("dizi bos");
        }
        return imgs;
    }

    public String textRecognizer(Bitmap bitmapForText) {
        TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        Frame frame = new Frame.Builder().setBitmap(bitmapForText).build();
        SparseArray<TextBlock> items = recognizer.detect(frame);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            TextBlock myItem = items.valueAt(i);
            sb.append(myItem.getValue());
            //sb.append("\n");
        }
        String text = sb.toString().replace("\n", " ");
        return text;
    }

    public boolean determineAbsentees(Bitmap image) {
        String red, green, blue;
        int black_pixels = 0;
        int cross = 0;
        int number_of_pixels = 0;
        int width = image.getWidth();
        int height = image.getHeight();
        for (int x = 5; x < width; x++) {
            for (int y = 5; y < height; y++) {
                red = Integer.toHexString(image.getPixel(x, y)).substring(2, 4);
                green = Integer.toHexString(image.getPixel(x, y)).substring(4, 6);
                blue = Integer.toHexString(image.getPixel(x, y)).substring(6, 8);
                if (red.compareTo("80") < 0 && green.compareTo("80") < 0 && blue.compareTo("80") < 0) {
                    black_pixels++;
                    if ((y < ((x * height) / width) + 4 && y > ((x * height) / width) - 4) || (height - y < ((x * height) / width) + 4 && height - y > ((x * height) / width) - 4)) {
                        cross++;
                    }
                }
                number_of_pixels++;
            }
        }
        if ((float) cross / black_pixels > 0.57 || black_pixels < number_of_pixels * 1 / 20) {
            return true;
        } else {
            return false;
        }
    }

    public boolean compareImages(Bitmap bitmap1, Bitmap bitmap2){
        int y,x;
        int min_height, min_width;
        if(bitmap1.getHeight() < bitmap2.getHeight()){
            min_height = bitmap1.getHeight();
        }
        else{
            min_height = bitmap2.getHeight();
        }
        if(bitmap1.getWidth() < bitmap2.getWidth()){
            min_width = bitmap1.getWidth();
        }
        else{
            min_width = bitmap2.getWidth();
        }
        int piksel_sayisi = 0;
        int benzer_piksel_yukari_oteleme, benzer_piksel_asagi_oteleme, benzer_piksel_saga_oteleme, benzer_piksel_sola_oteleme;
        int farkli_piksel_yukari_oteleme, farkli_piksel_asagi_oteleme, farkli_piksel_saga_oteleme, farkli_piksel_sola_oteleme;
        int max_similar_pixels = 0;
        int min_different_pixels = 9999;
        int black_pixels_in_bitmap1 = 0;
        int black_pixels_in_bitmap2 = 0;
        String red_bitmap1, green_bitmap1, blue_bitmap1, red_bitmap2, green_bitmap2, blue_bitmap2;
        boolean [][] isBlack_bitmap1 = new boolean[min_height][min_width];
        boolean [][] isBlack_bitmap2 = new boolean[min_height][min_width];
        for (y = 0; y < min_height; y++) {
            for (x = 0; x < min_width; x++) {
                red_bitmap1 = Integer.toHexString(bitmap1.getPixel(x,y)).substring(2,4);
                green_bitmap1 = Integer.toHexString(bitmap1.getPixel(x,y)).substring(4,6);
                blue_bitmap1 = Integer.toHexString(bitmap1.getPixel(x,y)).substring(6,8);
                red_bitmap2 = Integer.toHexString(bitmap2.getPixel(x,y)).substring(2,4);
                green_bitmap2 = Integer.toHexString(bitmap2.getPixel(x,y)).substring(4,6);
                blue_bitmap2 = Integer.toHexString(bitmap2.getPixel(x,y)).substring(6,8);
                if(red_bitmap1.compareTo("80") < 0 && green_bitmap1.compareTo("80") < 0 && blue_bitmap1.compareTo("80") < 0){
                    black_pixels_in_bitmap1++;
                    isBlack_bitmap1[y][x] = true;
                }
                else{
                    isBlack_bitmap1[y][x] = false;
                }
                if(red_bitmap2.compareTo("80") < 0 && green_bitmap2.compareTo("80") < 0 && blue_bitmap2.compareTo("80") < 0){
                    black_pixels_in_bitmap2++;
                    isBlack_bitmap2[y][x] = true;
                }
                else{
                    isBlack_bitmap2[y][x] = false;
                }
                piksel_sayisi++;
            }
        }
        for(int ext = 0; ext < 3; ext++){
            benzer_piksel_yukari_oteleme = 0;
            benzer_piksel_asagi_oteleme = 0;
            benzer_piksel_saga_oteleme = 0;
            benzer_piksel_sola_oteleme = 0;
            farkli_piksel_yukari_oteleme = 0;
            farkli_piksel_asagi_oteleme = 0;
            farkli_piksel_saga_oteleme = 0;
            farkli_piksel_sola_oteleme = 0;
            for (y = 0; y < min_height; y++) {
                int common_black_pixels = 0;
                for (x = 0; x < min_width; x++) {
                    if(y-ext>0 && isBlack_bitmap1[y-ext][x] == true && isBlack_bitmap2[y][x] == true){
                        common_black_pixels++;
                    }
                }
                for (x = 0; x < min_width; x++) {
                    if(y-ext>0 && isBlack_bitmap1[y-ext][x] == true && isBlack_bitmap2[y][x] == true && common_black_pixels < min_width*8/10){
                        benzer_piksel_yukari_oteleme++;
                    }
                    if((y-ext>0 && isBlack_bitmap1[y-ext][x] == true && isBlack_bitmap2[y][x] == false) || (y-ext>0 && isBlack_bitmap1[y-ext][x] == false && isBlack_bitmap2[y][x] == true)){
                        if(common_black_pixels < min_width*8/10){
                            farkli_piksel_yukari_oteleme++;
                        }
                    }
                }
            }
            for (y = 0; y < min_height; y++) {
                int common_black_pixels = 0;
                for (x = 0; x < min_width; x++) {
                    if(y+ext<min_height && isBlack_bitmap1[y+ext][x] == true && isBlack_bitmap2[y][x] == true){
                        common_black_pixels++;
                    }
                }
                for (x = 0; x < min_width; x++) {
                    if(y+ext<min_height && isBlack_bitmap1[y+ext][x] == true && isBlack_bitmap2[y][x] == true && common_black_pixels < min_width*8/10){
                        benzer_piksel_asagi_oteleme++;
                    }
                    if((y+ext<min_height && isBlack_bitmap1[y+ext][x] == true && isBlack_bitmap2[y][x] == false) || (y+ext<min_height && isBlack_bitmap1[y+ext][x] == false && isBlack_bitmap2[y][x] == true)){
                        if(common_black_pixels < min_width*8/10){
                            farkli_piksel_asagi_oteleme++;
                        }
                    }
                }
            }
            for (y = 0; y < min_height; y++) {
                int common_black_pixels = 0;
                for (x = 0; x < min_width; x++) {
                    if(x-ext>0 && isBlack_bitmap1[y][x-ext] == true && isBlack_bitmap2[y][x] == true){
                        common_black_pixels++;
                    }
                }
                for (x = 0; x < min_width; x++) {
                    if(x-ext>0 && isBlack_bitmap1[y][x-ext] == true && isBlack_bitmap2[y][x] == true && common_black_pixels < min_width*8/10){
                        benzer_piksel_sola_oteleme++;
                    }
                    if((x-ext>0 && isBlack_bitmap1[y][x-ext] == true && isBlack_bitmap2[y][x] == false) || (x-ext>0 && isBlack_bitmap1[y][x-ext] == false && isBlack_bitmap2[y][x] == true)){
                        if(common_black_pixels < min_width*8/10){
                            farkli_piksel_sola_oteleme++;
                        }
                    }
                }
            }
            for (y = 0; y < min_height; y++) {
                int common_black_pixels = 0;
                for (x = 0; x < min_width; x++) {
                    if(x+ext<min_width && isBlack_bitmap1[y][x+ext] == true && isBlack_bitmap2[y][x] == true){
                        common_black_pixels++;
                    }
                }
                for (x = 0; x < min_width; x++) {
                    if(x+ext<min_width && isBlack_bitmap1[y][x+ext] == true && isBlack_bitmap2[y][x] == true && common_black_pixels < min_width*8/10){
                        benzer_piksel_saga_oteleme++;
                    }
                    if((x+ext<min_width && isBlack_bitmap1[y][x+ext] == true && isBlack_bitmap2[y][x] == false) || (x+ext<min_width && isBlack_bitmap1[y][x+ext] == false && isBlack_bitmap2[y][x] == true)){
                        if(common_black_pixels < min_width*8/10){
                            farkli_piksel_saga_oteleme++;
                        }
                    }
                }
            }
            if(benzer_piksel_asagi_oteleme > max_similar_pixels){
                max_similar_pixels = benzer_piksel_asagi_oteleme;
            }
            if(benzer_piksel_yukari_oteleme > max_similar_pixels){
                max_similar_pixels = benzer_piksel_yukari_oteleme;
            }
            if(benzer_piksel_saga_oteleme > max_similar_pixels){
                max_similar_pixels = benzer_piksel_saga_oteleme;
            }
            if(benzer_piksel_sola_oteleme > max_similar_pixels){
                max_similar_pixels = benzer_piksel_sola_oteleme;
            }
            if(farkli_piksel_asagi_oteleme < min_different_pixels){
                min_different_pixels = farkli_piksel_asagi_oteleme;
            }
            if(farkli_piksel_yukari_oteleme < min_different_pixels){
                min_different_pixels = farkli_piksel_yukari_oteleme;
            }
            if(farkli_piksel_saga_oteleme < min_different_pixels){
                min_different_pixels = farkli_piksel_saga_oteleme;
            }
            if(farkli_piksel_sola_oteleme < min_different_pixels){
                min_different_pixels = farkli_piksel_sola_oteleme;
            }
        }
        float similarity_rate = (float) max_similar_pixels / piksel_sayisi;
        float difference_rate = (float) min_different_pixels / piksel_sayisi;
        float black_pixel_rate = (float) (black_pixels_in_bitmap1+black_pixels_in_bitmap2) / (2 * piksel_sayisi);
        if(similarity_rate >= 0.15){
            return true;
        }
        else if(black_pixel_rate < 0.20){
            if(similarity_rate > 0.050 && difference_rate < 0.22){
                return true;
            }
            else{
                return false;
            }
        }
        else if(black_pixel_rate >= 0.20 && black_pixel_rate < 0.255){
            if(similarity_rate > 0.085 && difference_rate < 0.25){
                return true;
            }
            else if(similarity_rate > 0.073 && difference_rate < 0.23){
                return true;
            }
            else{
                return false;
            }
        }
        else if(black_pixel_rate >= 0.255 && black_pixel_rate < 0.30){
            if(similarity_rate > 0.096 && difference_rate < 0.34){
                return true;
            }
            else if(similarity_rate > 0.082 && difference_rate < 0.26){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            if(similarity_rate > 0.14 && difference_rate < 0.40){
                return true;
            }
            else if(similarity_rate > 0.12 && difference_rate < 0.35){
                return true;
            }
            else{
                return false;
            }
        }
    }

}