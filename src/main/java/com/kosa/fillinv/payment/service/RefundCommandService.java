package com.kosa.fillinv.payment.service;

import com.kosa.fillinv.payment.service.dto.RefundCreateCommand;
import com.kosa.fillinv.payment.service.dto.RefundDTO;
import com.kosa.fillinv.payment.service.dto.RefundStatusUpdateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundCommandService {

    public RefundDTO createRefund(RefundCreateCommand command) {
        return null;
    }

    public void updateStatus(RefundStatusUpdateCommand command) {

    }
}
