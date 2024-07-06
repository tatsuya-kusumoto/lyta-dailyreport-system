package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository, PasswordEncoder passwordEncoder) {
        this.reportRepository = reportRepository;
    }

    // 日報保存
    @Transactional
    public ErrorKinds save(Report report,UserDetail userDetail) {

     // 重複チェック
        if(report.getReportDate().isEqual(userDetail.getEmployee().getCreatedAt().toLocalDate())){
            return ErrorKinds.DATECHECK_ERROR;
        }
        report.setDeleteFlg(false);

        report.setEmployee(userDetail.getEmployee());
        LocalDateTime now = LocalDateTime.now();
        report.setCreatedAt(now);
        report.setUpdatedAt(now);

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }
    // 日報削除
    @Transactional
    public ErrorKinds delete(Integer id) {

        Report report = findByCode(id);
        report.setDeleteFlg(true);
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);
        report.setDeleteFlg(true);

        return ErrorKinds.SUCCESS;
    }

     // 日報一覧表示処理
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    public List<Report> findAllReport(UserDetail userDetail){

        List<Report> findReport = new ArrayList<>();
        List<Report> allReport = reportRepository.findAll();

        if(userDetail.getEmployee().getRole().getValue().equals("管理者")) {
            return allReport;
        }
        for(Report rep:allReport) {
            if(userDetail.getEmployee().getCode().equals(rep.getEmployee().getCode())) {
                findReport.add(rep);
            }
        }

        return findReport;
    }

    // 日報1件を検索
    public Report findByCode(Integer id) {
        // findByIdで検索
        Optional<Report> option = reportRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }

    // 日報を1件検索して返す
    public Report getReport(Integer id) {
        return reportRepository.findById(id).get();
    }

    // 日報の登録処理を行う
    @Transactional
    public ErrorKinds saveReport(Report report,UserDetail userDetail,Integer id) {

        // 更新対象レポートリスト
        List<Report> reportList = reportRepository.findByEmployee(userDetail.getEmployee());
        // 更新前レポート
        Report beforeReport = reportRepository.findById(id).get();

        //レポートリストがnullでないかつ同一ID作成されていてかつレポート日付が重複
        if(reportList != null && beforeReport.getEmployee().getCode().equals(userDetail.getEmployee().getCode()) && !beforeReport.getReportDate().equals(report.getReportDate())) {
            for(Report rep:reportList) {
                if(rep.getReportDate().equals(report.getReportDate())) {
                    return ErrorKinds.DATECHECK_ERROR;
                }
            }
        }

        report.setDeleteFlg(false);
        report.setEmployee(reportRepository.findById(id).get().getEmployee());
        LocalDateTime now = LocalDateTime.now();
        report.setReportDate(report.getReportDate());
        report.setCreatedAt(reportRepository.findById(id).get().getCreatedAt());
        report.setUpdatedAt(now);

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }

}
