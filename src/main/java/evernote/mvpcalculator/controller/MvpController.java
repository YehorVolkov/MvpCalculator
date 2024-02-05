package evernote.mvpcalculator.controller;

import com.opencsv.exceptions.CsvException;
import evernote.mvpcalculator.service.MvpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/mvp")
public class MvpController {

    @Autowired
    private MvpService service;

    @PostMapping("/analyze")
    public String uploadGames(@RequestParam List<MultipartFile> csvFiles) throws IOException, CsvException {
        return service.calculateMvp(csvFiles);
    }
}
