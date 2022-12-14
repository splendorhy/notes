package com.splendor.notes.design.ddd.interfaces.facade;

import com.splendor.notes.design.ddd.application.service.LeaveApplicationService;
import com.splendor.notes.design.ddd.domain.leave.entity.Leave;
import com.splendor.notes.design.ddd.infrastructure.common.api.Response;
import com.splendor.notes.design.ddd.interfaces.assembler.LeaveAssembler;
import com.splendor.notes.design.ddd.interfaces.dto.LeaveDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author splendor.s
 * @create 2022/11/25 下午7:05
 * @description
 */
@RestController
@RequestMapping("/leave")
@Slf4j
public class LeaveApi {

    @Autowired
    LeaveApplicationService leaveApplicationService;

    @PostMapping
    public Response createLeaveInfo(LeaveDTO leaveDTO){
        Leave leave = LeaveAssembler.toDO(leaveDTO);
        leaveApplicationService.createLeaveInfo(leave);
        return Response.ok();
    }

    @PutMapping
    public Response updateLeaveInfo(LeaveDTO leaveDTO){
        Leave leave = LeaveAssembler.toDO(leaveDTO);
        leaveApplicationService.updateLeaveInfo(leave);
        return Response.ok();
    }

    @PostMapping("/submit")
    public Response submitApproval(LeaveDTO leaveDTO){
        Leave leave = LeaveAssembler.toDO(leaveDTO);
        leaveApplicationService.submitApproval(leave);
        return Response.ok();
    }

    @PostMapping("/{leaveId}")
    public Response findById(@PathVariable String leaveId){
        Leave leave = leaveApplicationService.getLeaveInfo(leaveId);
        return Response.ok(LeaveAssembler.toDTO(leave));
    }

    /**
     * 根据申请人查询所有请假单
     * @param applicantId
     * @return
     */
    @PostMapping("/query/applicant/{applicantId}")
    public Response queryByApplicant(@PathVariable String applicantId){
        List<Leave> leaveList = leaveApplicationService.queryLeaveInfosByApplicant(applicantId);
        List<LeaveDTO> leaveDTOList = leaveList.stream().map(leave -> LeaveAssembler.toDTO(leave)).collect(Collectors.toList());
        return Response.ok(leaveDTOList);
    }

    /**
     * 根据审批人id查询待审批请假单（待办任务）
     * @param approverId
     * @return
     */
    @PostMapping("/query/approver/{approverId}")
    public Response queryByApprover(@PathVariable String approverId){
        List<Leave> leaveList = leaveApplicationService.queryLeaveInfosByApprover(approverId);
        List<LeaveDTO> leaveDTOList = leaveList.stream().map(leave -> LeaveAssembler.toDTO(leave)).collect(Collectors.toList());
        return Response.ok(leaveDTOList);
    }
}