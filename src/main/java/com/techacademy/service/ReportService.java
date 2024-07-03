package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
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
    public ErrorKinds saveReport(Report report,UserDetail userDetail) {

        // 重複チェック
        if(report.getReportDate().isEqual(userDetail.getEmployee().getCreatedAt().toLocalDate())){
            return ErrorKinds.DATECHECK_ERROR;
        }

        report.setDeleteFlg(false);

        LocalDateTime now = LocalDateTime.now();
        report.setCreatedAt(now);
        report.setUpdatedAt(now);

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;

    }

    public List<Employee> findByEmployee(Employee employee) {

        return ;
    }
}
