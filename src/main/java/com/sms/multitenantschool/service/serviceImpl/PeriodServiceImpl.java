package com.sms.multitenantschool.service.serviceImpl;

import com.sms.multitenantschool.Utils.CommonUtils;
import com.sms.multitenantschool.exceptions.BadRequestException;
import com.sms.multitenantschool.model.entity.Tenant;
import com.sms.multitenantschool.model.entity.timeTable.Period;
import com.sms.multitenantschool.model.entity.timeTable.PeriodDTO;
import com.sms.multitenantschool.repository.PeriodRepository;
import com.sms.multitenantschool.service.TenantService;
import org.springframework.stereotype.Service;

@Service
public class PeriodServiceImpl {

//    private final PeriodRepository repository;
//    private final TenantService tenantService;
//
//    public PeriodServiceImpl(PeriodRepository repository,TenantService tenantService){
//        this.repository = repository;
//        this.tenantService = tenantService;
//    }
//
//    public PeriodServiceImpl createPeriod(PeriodDTO req){
//      if(req == null){
//          throw new BadRequestException("Period", "Period request is null");
//      }
//
//      Tenant tenant = tenantService.getActiveTenant();
//        Period period = Period
//                .builder()
//                .periodId()
//                .tenantUuid(tenant.getTenantUuid())
//                .periodUuid(CommonUtils.generateNewUuid())
//                .endTime(req.getEndTime())
//                .startTime(req.getStartTime())
//                .dayOfWeek(req.getDayOfWeek())
//                .build();
//    }
}
