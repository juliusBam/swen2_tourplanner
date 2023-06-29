package at.fhtw.tourplanner.bl.service;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.dal.repository.ReportRepository;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public byte[] getReport(TourItem tourItem) {
        return reportRepository.getReport(tourItem.getId());
    }

    public void downloadSummaryReport(String absolutePath) {
        byte[] bytes = reportRepository.getSummaryReport();
        try (FileOutputStream stream = new FileOutputStream(absolutePath)) {
            stream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void downloadDetailReport(Long tourItemId, String absolutePath) {
        byte[] bytes = reportRepository.getReport(tourItemId);
        try (FileOutputStream stream = new FileOutputStream(absolutePath)) {
            stream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
