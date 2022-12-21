package com.project.diary.Control.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.project.diary.Control.RootControl;
import com.project.diary.Model.Calendar.MyMaterialCalendarView;
import com.project.diary.Model.Diary.Diary;
import com.project.diary.Model.RichEditor.RichEditor;
import com.project.diary.Model.SQLite.SQLite;
import com.project.diary.R;
import com.project.diary.databinding.ActivityExportImportBinding;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;

import java.io.IOException;

public class ExportImportActivityControl extends RootControl {
    private CalendarDay fromDate, toDate;

    private Dialog mclvDialog;

    private StringBuilder richEdittextHtml;

    private RichEditor richEditor;

    private OnDateSelectedListener onDateSelectedListenerMCLV;

    private View.OnClickListener onClickTodayListenerMCLV;

    private ActivityExportImportBinding binding;

    private MyMaterialCalendarView calendarView;

    private int currentIdClick;

    private SQLite sqLite;

    /**
     * Set event for event when you click on day
     * @return
     */
    public OnDateSelectedListener getOnDateSelectedListenerMCLV() {
        return onDateSelectedListenerMCLV;
    }

    /**
     * Set event for event when you click today button <br>
     * Base: It will selected "today" when you click, you must write logic for it <br>
     * Must using {@link CalendarDay#today()} to get current day
     * @return
     */
    public View.OnClickListener getOnClickTodayListenerMCLV() {
        return onClickTodayListenerMCLV;
    }

    public Dialog getMclvDialog() {
        return mclvDialog;
    }

    public void setMclvDialog(Dialog mclvDialog) {
        this.mclvDialog = mclvDialog;
    }

    public CalendarDay getFromDate() {
        return fromDate;
    }

    public void setFromDate(CalendarDay fromDate) {
        this.fromDate = fromDate;
    }

    public CalendarDay getToDate() {
        return toDate;
    }

    public void setToDate(CalendarDay toDate) {
        this.toDate = toDate;
    }

    public ExportImportActivityControl(Context context, ActivityExportImportBinding binding) {
        super(context);
        this.binding = binding;
        currentIdClick = 0;
        sqLite = new SQLite(context);
    }

    public void showMyMaterialCalendarView(View v){
        currentIdClick = v.getId();
        mclvDialog.show();
    }

    private void setDateForFromToDate(CalendarDay calendarDay){
        String date = String.valueOf(calendarDay.getMonth()) +"-" + String.valueOf(calendarDay.getDay()) +"-" + String.valueOf(calendarDay.getYear());
        if(currentIdClick == R.id.cvFromDate){
            if(calendarDay.isBefore(toDate)){
                binding.txtFromDate.setText(date);
                fromDate = calendarDay;
            }else{
                calendarView.clearSelection();
                calendarView.setCurrentDate(fromDate);
                calendarView.setDateSelected(fromDate, true);
                Toast.makeText(getContext(), "From Date Must <= To Date", Toast.LENGTH_SHORT).show();
            }

        }else if(currentIdClick == R.id.cvToDate){
            if(calendarDay.isAfter(fromDate)){
                binding.txtToDate.setText(date);
                toDate = calendarDay;
            }else{
                calendarView.clearSelection();
                calendarView.setCurrentDate(toDate);
                calendarView.setDateSelected(toDate, true);
                Toast.makeText(getContext(), "From Date Must >= To Date", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void initMyMaterialCalendarView() {
        mclvDialog = new Dialog(getContext(), R.style.Dialog);
        mclvDialog.setContentView(R.layout.dialog_calandar);
        mclvDialog.setCanceledOnTouchOutside(true);
        calendarView = mclvDialog.findViewById(R.id.calendarView);
        LinearLayout Root = mclvDialog.findViewById(R.id.Root);
        TextView txtClose = mclvDialog.findViewById(R.id.txtClose);
        TextView txtToday = mclvDialog.findViewById(R.id.txtToday);
        String date = String.valueOf(CalendarDay.today().getMonth()) +"-" + String.valueOf(CalendarDay.today().getDay()) +"-" + String.valueOf(CalendarDay.today().getYear());
        fromDate = CalendarDay.today();
        toDate = CalendarDay.today();
        binding.txtFromDate.setText(date);
        binding.txtToDate.setText(date);
        onClickTodayListenerMCLV = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.clearSelection();
                calendarView.setCurrentDate(CalendarDay.today());
                calendarView.setDateSelected(CalendarDay.today(), true);
                setDateForFromToDate(CalendarDay.today());
            }
        };

        onDateSelectedListenerMCLV = new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                setDateForFromToDate(date);
            }
        };
        txtToday.setOnClickListener(onClickTodayListenerMCLV);
        calendarView.setOnDateChangedListener(onDateSelectedListenerMCLV);
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mclvDialog.dismiss();
            }
        });
        Root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mclvDialog.dismiss();
            }
        });
    }

    @WorkerThread
    public void exportToPdf() {
        ((Activity)getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.tiledProgressView.setLoadingColor(Color.parseColor("#03A9F4"));
                binding.tiledProgressView.setColor(Color.parseColor("#FFFFFFFF"));
                binding.tiledProgressView.setProgress(0f);
                binding.llProgessExportPdf.setVisibility(View.VISIBLE);
                binding.txtStateProgessExportPdf.setText("Reading ...");
            }
        });
        ArrayList<Diary> diaries = sqLite.getSqLiteControl().readData("Diary");
        ((Activity)getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.txtStateProgessExportPdf.setText("Maching Diary...");
            }
        });
        ArrayList<Diary> diariesOfficial = new ArrayList<>();
        for(Diary diary : diaries){
            if(diary.getDate().isInRange(fromDate, toDate)){
                diariesOfficial.add(diary);
            }
        }
        ((Activity)getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.txtStateProgessExportPdf.setText("Exporting Diary...");
                float index = 1f;
                richEdittextHtml = new StringBuilder();
                for(Diary diary : diariesOfficial){
                    float percent = (100f * index) / diariesOfficial.size();
                    index ++;
                    binding.tiledProgressView.setProgress(percent);
                    String date = String.valueOf(diary.getDate().getMonth()) +"-" + String.valueOf(diary.getDate().getDay()) +"-" + String.valueOf(diary.getDate().getYear());
                    binding.mEditor.insertHtml(date + "<BR>" + "<BR>" + "<BR>");
                    binding.mEditor.insertHtml(diary.getDiaryData().getData() + "<BR>" + "<hr>");
                }
                final String fileName= String.valueOf(System.currentTimeMillis()) + "_"+String.valueOf(CalendarDay.today().getDay())+"_"+
                        String.valueOf(CalendarDay.today().getMonth())+"_"+String.valueOf(CalendarDay.today().getYear())+".pdf";
                String filePath = getContext().getExternalCacheDir().getAbsolutePath() + "/PDFTest/" + fileName;
                //TODO TEST THIS
                exportToPdf( binding.mEditor ,filePath);
            }
        });
    }
    public void exportToPdf(RichEditor editor, String filePath) {
//        // First, create a Document object
//        Document document = new Document();
//
//        try {
//            // Next, create a PdfWriter and set the output file
//            PdfWriter.getInstance(document, new FileOutputStream(filePath));
//
//            // Open the document for writing
//            document.open();
//
//            // Get the HTML content from the RichEditor
//            String html = editor.getHtml();
//
//            // Use the HTMLWorker class to parse the HTML and add it to the document
//            HTMLWorker htmlWorker = new HTMLWorker(document);
//            htmlWorker.parse(new StringReader(html));
//
//            // Close the document
//            document.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}