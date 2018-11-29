package com.hdvon.nmp.controller.system;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hdvon.nmp.controller.BaseController;
import io.swagger.annotations.Api;

@Api(value="/evaluateReport",tags="每月考核报表模块",description="每月考核报表模块")
@RestController
@RequestMapping("/evaluateReport")
public class EvaluateReportController extends BaseController {
	
}
