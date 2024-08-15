package edu.stanford.fsi.reap.service;

import edu.stanford.fsi.reap.dto.CarerModifyRecordDTO;
import edu.stanford.fsi.reap.entity.Carer;
import edu.stanford.fsi.reap.entity.CarerModifyRecord;
import edu.stanford.fsi.reap.entity.User;
import edu.stanford.fsi.reap.repository.CarerModifyRecordRepository;
import edu.stanford.fsi.reap.repository.CarerRepository;
import edu.stanford.fsi.reap.repository.UserRepository;
import edu.stanford.fsi.reap.utils.FieldValueUtil;
import edu.stanford.fsi.reap.web.rest.errors.BadRequestAlertException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CarerModifyRecordService {

    @Autowired
    private CarerModifyRecordRepository repository;
    @Autowired
    private UserRepository userRepository;

    public List<CarerModifyRecordDTO> getCarerList(Long carerId) {
        List<CarerModifyRecord> carerModifyRecords = repository.findByCarerId(carerId);
        List<CarerModifyRecordDTO> res;

        res = carerModifyRecords.stream().map(carerModifyRecord -> {
                    CarerModifyRecordDTO dto = new CarerModifyRecordDTO();
                    if (userRepository.findById(carerModifyRecord.getUserId()).isPresent()) {
                        User user = userRepository.findById(carerModifyRecord.getUserId()).get();
                        String role;
                        if (user.getRole().equals("ROLE_ADMIN")) {
                            role = "管理员";
                        } else if (user.getRole().equals("ROLE_CHW")) {
                            role = "社区工作者";
                        } else {
                            role = "督导员";
                        }
                        dto.setRoleName(role);
                        dto.setUserName(user.getUsername());
                        dto.setLastModifiedAt(carerModifyRecord.getLastModifiedAt());
                    }

                    Carer oldCarer = carerModifyRecord.getOldCarerJson();
                    Carer newCarer = carerModifyRecord.getNewCarerJson();
                    ArrayList<String> oldValues = new ArrayList<>();
                    ArrayList<String> newValues = new ArrayList<>();
                    String[] split = new String[0];
                    try {
                        Map<String, Object> map = FieldValueUtil.getFieldValuePair(oldCarer, true);
                        Map<String, Object> newCarerMap = FieldValueUtil.getFieldValuePair(newCarer, true);
                        String changedColumn = carerModifyRecord.getChangedColumn();
                        split = changedColumn.split(",");
                        for (String s : split) {
                            String oldCarerValue = map.get(s) == null ? "未知" : map.get(s).toString();
                            oldValues.add(oldCarerValue);
                            String newCarerValue = newCarerMap.get(s) == null ? "未知" : newCarerMap.get(s).toString();
                            newValues.add(newCarerValue);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    dto.setColumnName(Arrays.asList(split));
                    dto.setOldValue(oldValues);
                    dto.setNewValue(newValues);
                    return dto;
                }
        ).collect(Collectors.toList());
        return res;
    }
}

