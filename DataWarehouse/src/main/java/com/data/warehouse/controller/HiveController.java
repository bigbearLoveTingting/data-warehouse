package com.data.warehouse.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 测试hive连接
 * @Title: HiveController
 * @Description:
 * @author:Administrator
 * @date 2018年8月4日
 */
@RestController
@RequestMapping("/hive")
public class HiveController {
	@Autowired
	@Qualifier("hiveJdbcTemplate")
	private JdbcTemplate hiveJdbcTemplate;

	@RequestMapping("/select")
	public List select() {
		List rows = hiveJdbcTemplate.queryForList("SELECT T_CSX_QRWB.name, count(T_HR_A010101.hdsd0001006) AS total_nums "
				+ "FROM T_HR_A010101"
				+ "LEFT JOIN T_CSX_QRWB ON (T_HR_A010101.exhdsd0001018 = T_CSX_QRWB.exhdsd0001018)"
				+ "GROUP BY T_CSX_QRWB.name");
		return rows;
	}
}
